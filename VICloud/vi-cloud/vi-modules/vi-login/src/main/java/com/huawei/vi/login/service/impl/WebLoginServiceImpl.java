package com.huawei.vi.login.service.impl;

import cn.hutool.core.collection.CollectionUtil;

import com.alibaba.fastjson.JSONObject;
import com.huawei.cn.components.cryption.Pbkdf2Util;
import com.huawei.utils.CommonSymbolicConstant;
import com.huawei.utils.NumConstant;
import com.huawei.vi.login.mapper.RoleInfoMapper;
import com.huawei.vi.login.mapper.TblServerParamConfigMapper;
import com.huawei.vi.login.mapper.WebLoginMapper;
import com.huawei.vi.login.mapper.WebUserIpResourceMapper;
import com.huawei.vi.login.po.WebLoginRecordPO;
import com.huawei.vi.login.po.WebUserPO;
import com.huawei.vi.login.service.UserIpResourceService;
import com.huawei.vi.login.service.WebLoginService;
import com.jovision.jaws.common.config.i18n.MessageSourceUtil;
import com.jovision.jaws.common.config.jwt.JwtTokenUtil;
import com.jovision.jaws.common.config.redis.RedisOperatingService;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.constant.TokenConstant;
import com.jovision.jaws.common.properties.PropertiesConfigCommonConst;

import com.jovision.jaws.common.util.*;
import com.jovision.jaws.common.vo.Host;
import com.jovision.jaws.common.vo.SsoUser;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class WebLoginServiceImpl implements WebLoginService {

    private static final Logger LOG = LoggerFactory.getLogger(WebLoginServiceImpl.class);

    private static final String INIT_RESULT_MESSAGE = "initPassMessage";

    @Value("${WEB.CHECKCERTIFICATEURLURL}")
    private String checkCertificateUrl;
    @Value("${WEB.webTokenExpire}")
    private int webTokenExpire = 30;
    @Value("${WEB.webLoginLimitExpire}")
    private int webLoginLimitExpire = 10;

    /**
     * 供SessionListener控制用
     */
    public static final ConcurrentHashMap<String, SsoUser> SESSION_AND_USER_MAP = new ConcurrentHashMap<String, SsoUser>();

    @Autowired
    private RedisOperatingService redisOperatingService;

    @Autowired
    UserIpResourceService userIpResourceService;

    @Autowired
    WebLoginMapper webLoginMapper;

    @Autowired
    RoleInfoMapper roleInfoMapper;

    @Autowired
    WebUserIpResourceMapper webUserIpResourceMapper;

    @Autowired
    TblServerParamConfigMapper serverParamConfigDao;
    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @Override
    public RestResult WebLogin(HttpServletRequest request) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"", null);
        // 检查输入的用户名和输入的密码；

        String loginUserName = Optional.ofNullable(request.getParameter(CommonConst.LOGIN_USER)).map(String::trim).orElse("");
        String loginPass = Optional.ofNullable(request.getParameter("loginPass")).map(String::trim).orElse("");
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(loginPass)) {
            // 用户名或密码为空,登录失败
            return RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"LoginController.Both.The.User.Name.And.Password.Cannot.Be.Empty", null);
        }
        // 检查验证码是否正确
        String validateCode = Optional.ofNullable(request.getParameter("DrawImage")).orElse("");
        String drawImageId = Optional.ofNullable(request.getParameter("DrawImageId")).orElse("");
        String drawImageCode = redisOperatingService.getValueByKey(drawImageId);
        boolean isCheckValidateCodeSuccess = StringUtils.isNotEmpty(validateCode) && validateCode.equalsIgnoreCase(drawImageCode);
        if (!isCheckValidateCodeSuccess) {
            restResult.setMessage(messageSourceUtil.getMessage("LoginService.Verification"));
            // 验证码错误
            return restResult;
        }
        // 清除验证码缓存
        redisOperatingService.delValueByKey(drawImageId);
//         校验用户名和密码是否匹配
//         --确定用户是否被锁定
        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put(WebUserPO.USER_NAME, loginUserName);
        WebUserPO webUserPO = webLoginMapper.selectUser(selectMap);
        if(webUserPO==null){
            return RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,
                    messageSourceUtil.getMessage("LoginService.Incorrect.User.Name.Or.Password"), null);
        }
        String countKey = webUserPO.getUserId()+"_web_count";
        String wand = null;
        String countValue = null;
        synchronized(this){
            if (userIsLock(loginUserName)) {
                restResult.setMessage(MessageFormat.format(messageSourceUtil.getMessage("LoginService.user.locked"),remainTime(loginUserName)));
                redisOperatingService.delValueByFuzzyKey(webUserPO.getUserId()+"_web_");
                redisOperatingService.delValueByKey(countKey);
                wand =  Utils.getWand("addAll","",webLoginLimitExpire);
                redisOperatingService.setTokenByTime(countKey,wand);
                return restResult;
            }
            countValue = redisOperatingService.getValueByKey(countKey);
            if(StringUtils.isNotEmpty(countValue)){
                wand = countValue;
                if(!countValue.contains("0")){
                    restResult.setMessage(messageSourceUtil.getMessage("LoginService.user.locked.login"));
                    return restResult;
                }
            }else{
                wand = Utils.getWand("addAll","",webLoginLimitExpire);
            }
        }
        // --更新用户盐值 && 对输入的登录密码进行加密
        String userSalt = getUserSalt(loginUserName);
        if (StringUtils.isEmpty(userSalt)) {
            restResult.setMessage(messageSourceUtil.getMessage("LoginService.Incorrect.User.Name.Or.Password"));
            return restResult;
        }
        loginPass = Pbkdf2Util.encrypt(loginPass, userSalt, CommonConst.ITERATIONS);
        RestResult result = loginIsSuccess(loginUserName, loginPass);

        // 判断用户是否登录成功
        if (result.getCode() == ServiceCommonConst.CODE_FAILURE) {
            insertOrUpdateLoginRecord(loginUserName);
            return result;
        }
        WebUserPO successUser = (WebUserPO) result.getData();
        // 登录成功时，需构造返回的data数据
        Map<String, Object> dataMap = addResponseData(successUser);
        if(result.getCode()==ServiceCommonConst.CODE_SUCCESS){
            Long tokenExpire = TokenConstant.WEB_TOKEN_EXPIRE;
            String token = successUser.getUserId()+"_web_"+
                    new JwtTokenUtil().createJWTToken(successUser.getUserId()+"",successUser.getUserTblName(),tokenExpire);
            redisOperatingService.setTokenByTime(token,"1",webTokenExpire, TimeUnit.MINUTES);
            String url = checkCertificateUrl+"/ign/checkIsImportLicense.do?isDefault="+successUser.getIsDefault()+"&token="+token;
            LOG.info("url========================="+url);
            JSONObject param = new JSONObject();
            String str = HttpUtils.sendPost(url,param);
            LOG.info("str========================="+str);
            if(StringUtils.isEmpty(str)||"isLoginFlag".equals(str)){
                // 清除token，登陆失败
                redisOperatingService.delValueByKey(token);
                LOG.info("license========================="+messageSourceUtil.getMessage("license"));
                restResult.setMessage(messageSourceUtil.getMessage("license"));
                return restResult;
            }
            JSONObject json = JSONObject.parseObject(str);
            if(!"0".equals(json.getString("code"))){
                redisOperatingService.delValueByKey(token);
                restResult.setMessage(json.getString("message"));
                return restResult;
            }
            dataMap.put("page",json.getJSONObject("data").getString("page"));
//            dataMap.put("page","performanceMonitoring");
            wand = Utils.getWand("add",wand,webLoginLimitExpire);
            redisOperatingService.setTokenByTime(countKey,wand);
            dataMap.put("token",token);
            restResult.setMessage("login success");
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(dataMap);
            return restResult;
        }else {
            return restResult;
        }
    }

    /**
     * 登录成功之后的后置处理
     *
     * @param request 请求
     * @param user 登录成功的用户
     */
    private void afterLoginSuccess(@NonNull HttpServletRequest request, @NonNull WebUserPO user) {
        // 登录成功之后
        // 对过滤标识 loginFlag置为true
        request.getSession().setAttribute("loginFlag", true);

        // 向session中存储登录用户信息
        request.getSession().setAttribute(CommonConst.LOGIN_USER, user.getUserTblName());

        // 注销原session,生成新的session
        UtilMethod.changeSessionId(request);

        // 记录登录的相关信息
        SsoUser ssoUser = new SsoUser();
        ssoUser.setStartTime(System.currentTimeMillis());
        ssoUser.setIp(UtilMethod.getIpAddr(request));
        ssoUser.setSessionId(request.getSession().getId());
        SESSION_AND_USER_MAP.put(request.getSession().getId(), ssoUser);

        Host.getInstance().setScheme(request.getScheme());
        Host.getInstance().setLocalPort(request.getServerPort());
        Host.getInstance().setIp(request.getLocalAddr());
    }

    @Override
    public boolean userIsLock(@NonNull String userName) {
        boolean isUserLock = Optional.ofNullable(webLoginMapper.selectByUserName(userName)).map(record -> {
            // 当用户没有被锁定时，返回false
            if (!ServiceCommonConst.FLAG_USER_LOCK.equals(record.getLockFlag())) {
                return false;
            }
            // 当用户锁定时，判断是否被解锁；
            // 算法：当前时间 减去 最后一次登录时间 是否小于 锁定时间
            // isLock:true(被锁定)；false:(被解锁)
            boolean isLock = new Date().getTime() - record.getLoginDate().getTime() < Long
                    .parseLong(messageSourceUtil.getMessage(PropertiesConfigCommonConst.INIT_LOCKTIME))
                    * NumConstant.NUM_1000;
            // 用户被解锁时，需要删除该用户的登录记录
            if (!isLock) {
                webLoginMapper.deleteUserName(userName);
            }
            return isLock;
        }).orElse(false);
        return isUserLock;
    }

    @Override
    public long remainTime(String userName) {
        WebLoginRecordPO loginRecord = webLoginMapper.selectByUserName(userName);
        if (loginRecord == null) {
            return 0L;
        }
        int lockingTime = 0;
//        Conditions.requireArgument(loginRecord.getLoginDate() != null, "user db lock data is null");
        if(messageSourceUtil.getMessage(PropertiesConfigCommonConst.INIT_LOCKTIME)!=null){
            lockingTime = Integer.parseInt(messageSourceUtil.getMessage(PropertiesConfigCommonConst.INIT_LOCKTIME));
        }

        // 锁定时间-(当前时间-开始锁定时间) 单位 秒
        return lockingTime - (new Date().getTime() - loginRecord.getLoginDate().getTime()) / NumConstant.NUM_1000;
    }

    /**
     * 根据用户名获取对应的盐值
     *
     * @param loginUserName 登录用户名
     * @return 用户私有盐值
     */
    private String getUserSalt(String loginUserName) {
        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put(WebUserPO.USER_NAME, loginUserName);
        WebUserPO user = webLoginMapper.selectUser(selectMap);
        if (user == null) {
            return CommonSymbolicConstant.SYMBOL_BLANK;
        }
        return Optional.ofNullable(user.getUserSalt()).map(String::trim).orElse("");
    }

    @Override
    public RestResult loginIsSuccess(@NonNull String userName, @NonNull String userPwd) {
        Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put(WebUserPO.USER_NAME, userName);
        conditionMap.put(WebUserPO.PASS_USER_WORD, userPwd);

        // 根据用户名密码查询用户并校验
        return Optional.ofNullable(webLoginMapper.selectUser(conditionMap)).map(user -> {
            // 判断用户权限是否有效；true:有效；false:权限失效 (权限失效原因：分配的IPC在最新的组网关系表不存在)
            boolean isAuthorityValid = isAuthorityValid(user);
            if (isAuthorityValid) {
                // 查询成功，权限未失效 把User放入RestResult 的data属性中，便于后面代码获取
                return RestResult.generateRestResult(ServiceCommonConst.CODE_SUCCESS, "", user);
            }

            // 查询成功，但权限失效
            return RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,
                    messageSourceUtil.getMessage("LoginService.Permission.Expired"), null);
        })
                .orElse(
                        // 查询失败，用户名和密码不匹配
                        RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,
                                messageSourceUtil.getMessage("LoginService.Incorrect.User.Name.Or.Password"), null));
    }

    /**
     * 判断用户权限是否有效
     *
     * @param user 用户
     * @return 权限是否有效
     */
    public boolean isAuthorityValid(WebUserPO user) {
        if (user.getIsDefault() != WebUserPO.DEFAULT_USER) {
            // 非管理员，查看是否在最新的组网关系表中有ipc
            List<String> ipcIpByUserOrId =
                    userIpResourceService.getIpcIpByUserOrId(user.getUserTblName(), null, CommonConst.TBL_IPC_IP_TMP);
            if (CollectionUtil.isEmpty(ipcIpByUserOrId)) {
                LOG.error("{} ipcList is empty", user.getUserTblName());
                return false;
            }

            // 非管理员查询是否具备页面权限
            List<Integer> userRoleIds = roleInfoMapper.selectUserRoleId(user.getUserId());
            Optional<Integer> findFirst = userRoleIds.stream().filter(intger -> intger > 0).findFirst();
            if (!findFirst.isPresent()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void insertOrUpdateLoginRecord(@NonNull String userName) {
        // 根据用户名在tbl_login_record中进行查询
        WebLoginRecordPO loginRecord = webUserIpResourceMapper.selectByUserName(userName);
        if (loginRecord == null) {
            // 查无此人时，插入新数据
            webUserIpResourceMapper.insertRecord(new WebLoginRecordPO(userName, "0", 1, new Date()));
            return;
        }

        // 更新登录次数或锁定状态
        Map<String, Object> updateMap = new HashMap<String, Object>();

        // 获取之前登录失败的次数
        int failNum = loginRecord.getFailureNum();

        // 获取最多失败的次数
        int maxFail = Integer.parseInt(messageSourceUtil.getMessage(PropertiesConfigCommonConst.INIT_LOGIN_FAILURES));
        if (failNum + 1 > maxFail) {
            updateMap.put(WebLoginRecordPO.LOCKFLAG, ServiceCommonConst.FLAG_USER_LOCK);
        }

        // 更新失败次数
        updateMap.put(WebLoginRecordPO.FAILURENUM, failNum + 1);

        // 更新登录时间
        updateMap.put(WebLoginRecordPO.LOGINDATE, new Date());

        // 更新时的查询条件：userName
        updateMap.put(WebLoginRecordPO.USERLOGINNAME, userName);
        webUserIpResourceMapper.updateByName(updateMap);
    }

    @Override
    public Map<String, Object> addResponseData(@NonNull WebUserPO user) {
//        Map<String, Object> dataMap = new HashMap<String, Object>(CommonConst.INIT_CAPACITY, CommonConst.LOAD_FACTOR);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        // 判断用户是否进行了初始化密码
        if (user.getIsInintSyspass() == ServiceCommonConst.FLAG_USER_INIT_PAS) {
            // 需要初始化密码
            dataMap.put(WebUserPO.IS_ININT_SYSPASS, false);
            dataMap.put(INIT_RESULT_MESSAGE, messageSourceUtil.getMessage("LoginController.Init.Sys.pass"));
        } else {
            dataMap.put(WebUserPO.IS_ININT_SYSPASS, true);
        }

        // 先写死兼容旧的权限，待新权限模块适配完后删掉。
        dataMap.put(WebUserPO.IS_DEFAULT, user.getIsDefault());
        dataMap.put(PropertiesConfigCommonConst.CONFIG_PERIOD_TIME,
                messageSourceUtil.getMessage(PropertiesConfigCommonConst.CONFIG_PERIOD_TIME)); // 获取周期时间间隔，单位为：s
        return dataMap;
    }

    @Override
    public RestResult webCleanSession(HttpServletRequest request) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        String loginUserName = Optional.ofNullable(request.getParameter(CommonConst.LOGIN_USER)).map(String::trim).orElse("");
        String token = Optional.ofNullable(request.getParameter("token")).map(String::trim).orElse("");
        if(StringUtils.isEmpty(loginUserName)){
            restResult.setMessage(messageSourceUtil.getMessage(PropertiesConfigCommonConst.VI_PARAM_NOT_BE_EMPTY));
            return restResult;
        }
        Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put(WebUserPO.USER_NAME, loginUserName);
        WebUserPO webUserPO = webLoginMapper.selectUser(conditionMap);
        if(webUserPO==null){
            restResult.setMessage(messageSourceUtil.getMessage("closeUser"));
            return restResult;
        }
        String kokenValue = redisOperatingService.getValueByKey(token);
        if(StringUtils.isEmpty(kokenValue)){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            return restResult;
        }
//        String tokenKey = webUserPO.getUserId()+"_web_"+token;
        redisOperatingService.delValueByKey(token);
        String countKey = webUserPO.getUserId()+"_web_count";
        String countValue = redisOperatingService.getValueByKey(countKey);
        if(StringUtils.isNotEmpty(countValue)){
            String wand = Utils.getWand("delete",countValue,webLoginLimitExpire);
            redisOperatingService.setTokenByTime(countKey,wand);
        }
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        return restResult;

    }

}
