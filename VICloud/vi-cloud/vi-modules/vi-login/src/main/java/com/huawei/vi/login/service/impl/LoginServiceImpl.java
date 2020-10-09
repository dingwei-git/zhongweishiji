package com.huawei.vi.login.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.huawei.cn.components.cryption.Pbkdf2Util;
import com.huawei.vi.entity.po.LoginRecordPO;
import com.huawei.vi.entity.po.UserPO;
import com.huawei.vi.login.mapper.UserMapper;
import com.huawei.vi.login.service.LoginRecordService;
import com.huawei.vi.login.service.LoginService;
import com.huawei.vi.login.service.UserService;
import com.jovision.jaws.common.config.jwt.JwtTokenUtil;
import com.jovision.jaws.common.config.jwt.TikenPayload;
import com.jovision.jaws.common.config.jwt.TikenUtil;
import com.jovision.jaws.common.config.redis.RedisOperatingService;
import com.jovision.jaws.common.constant.*;
import com.jovision.jaws.common.dto.LoginDto;
import com.jovision.jaws.common.dto.ModifyPwdDto;
import com.jovision.jaws.common.dto.RenewalTokenDto;
import com.jovision.jaws.common.exception.BusinessErrorEnum;
import com.jovision.jaws.common.exception.BusinessException;
import com.jovision.jaws.common.util.CommonSymbolicConstant;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.SecureRandomUtil;
import com.jovision.jaws.common.util.StringUtils;
import com.jovision.jaws.common.vo.ExecuteResult;
import com.jovision.jaws.common.vo.LoginVo;
import com.jovision.jaws.common.vo.RenewalTokenVo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperatingService redisOperatingService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TikenUtil tikenUtil;

    @Value("${login.lock.duration}")
    private Long duration;

    @Override
    public RestResult login(LoginDto body) {

        // 检查输入的用户名和输入的密码；
        //String loginUserName = Optional.ofNullable(body.getAccount()).map(String::trim).orElse("");
        String loginUserName = Optional.ofNullable(body.getAccount()).get();
        String loginPass = Optional.ofNullable(body.getPassword()).get();
        if (StringUtil.isEmpty(loginUserName) || StringUtil.isEmpty(loginPass)) {
            return RestResult.generateRestResult(AppResultEnum.USER_PASSWORD_FAIL.getCode(),AppResultEnum.USER_PASSWORD_FAIL.getMessage(), null);
        }
        //判断用户角色,只有维修员可以登录
        Map roleMap = userService.getRole(loginUserName);
        if(roleMap==null){
            return RestResult.generateRestResult(AppResultEnum.USER_PASSWORD_FAIL.getCode(),AppResultEnum.USER_PASSWORD_FAIL.getMessage(), null);
        }else{
            log.info("roleMap:"+roleMap.toString());
            int roleId= Integer.parseInt(roleMap.get("role_id").toString());
            if(roleId!=4){
                //4.维修人员
                return RestResult.generateRestResult(AppResultEnum.LOGIN_FAIL.getCode(),AppResultEnum.LOGIN_FAIL.getMessage(), null);
            }
        }
        //判断当前用户在线数量
        String userId = roleMap.get("user_id")==null?"":roleMap.get("user_id").toString();
        int loginSuccessCount = redisOperatingService.getLoginSuccessCount(userId);
        if(loginSuccessCount>=10){
            return RestResult.generateRestResult(AppResultEnum.LOGIN_COUNT_OUT.getCode(),AppResultEnum.LOGIN_COUNT_OUT.getMessage(), null);
        }
        
        // 校验用户名和密码是否匹配
        // --确定用户是否被锁定
        LoginRecordPO loginRecordPO = loginRecordService.selectByUser(loginUserName,"APP");
        if(loginRecordPO!=null&&loginRecordPO.getLockFlag().equals("1")){
            // 当用户锁定时，判断是否被解锁；
            // 算法：当前时间 减去 最后一次登录时间 是否小于 锁定时间
            // isLock:true(被锁定)；false:(被解锁)
            boolean isLock = new Date().getTime() - loginRecordPO.getLoginDate().getTime() < duration*1000;
            // 用户被解锁时，需要删除该用户的登录记录
            if (!isLock) {
                loginRecordPO.setLockFlag("0");
                loginRecordPO.setFailureNum(0);
                loginRecordService.deleteUserName(loginUserName,"APP");
            }else{
                //获取剩余时间
                Long l = redisOperatingService.getExpire(TokenConstant.USER_LOGIN_ERROR_NUMBER+loginUserName, TimeUnit.SECONDS);
                return RestResult.generateRestResult(AppResultEnum.USER_LOCKED.getCode(), MessageFormat.format(AppResultEnum.USER_LOCKED.getMessage(),l), null);
            }
        }
        // --更新用户盐值 && 对输入的登录密码进行加密
        String userSalt = getUserSalt(loginUserName);
        if (StringUtil.isEmpty(userSalt)) {
            LoginRecordPO loginRecordPO2 = loginRecordService.selectByUser(loginUserName,"APP");
            int lockFlag = failLogin(loginRecordPO2,loginUserName,userId,duration);
            if(lockFlag==1){
                Long l = redisOperatingService.getExpire(TokenConstant.USER_LOGIN_ERROR_NUMBER+loginUserName, TimeUnit.SECONDS);
                return RestResult.generateRestResult(AppResultEnum.USER_LOCKED.getCode(), MessageFormat.format(AppResultEnum.USER_LOCKED.getMessage(),l), null);
            }else{
                return RestResult.generateRestResult(AppResultEnum.USER_PASSWORD_FAIL.getCode(), AppResultEnum.USER_PASSWORD_FAIL.getMessage(),null);
            }
        }
        loginPass = Pbkdf2Util.encrypt(loginPass, userSalt, CommonConst.ITERATIONS);
        RestResult restResult = loginIsSuccess(loginUserName, loginPass);
        // 判断用户是否登录成功
        if (restResult.getCode() != AppResultEnum.SUCCESS.getCode()) {
            LoginRecordPO loginRecordPO2 = loginRecordService.selectByUser(loginUserName,"APP");
            int lockFlag = failLogin(loginRecordPO2,loginUserName,userId,duration);
            if(lockFlag==1){
                Long l = redisOperatingService.getExpire(TokenConstant.USER_LOGIN_ERROR_NUMBER+loginUserName, TimeUnit.SECONDS);
                return RestResult.generateRestResult(AppResultEnum.USER_LOCKED.getCode(), MessageFormat.format(AppResultEnum.USER_LOCKED.getMessage(),l), null);
            }else{
                return RestResult.generateRestResult(AppResultEnum.USER_PASSWORD_FAIL.getCode(), AppResultEnum.USER_PASSWORD_FAIL.getMessage(),null);
            }
        }
        UserPO successUser = (UserPO) restResult.getData();
        //登录成功，生产token、tiken
        //tiken的iv偏移量
        String tikenIv = null;
        //登录标识
        String loginId = null;
        try {
            tikenIv = SecureRandomUtil.randomIV16bit();
            loginId = SecureRandomUtil.randomIV16bit();
        } catch (NoSuchAlgorithmException e) {
            ErrorLockVo errorLockVo = redisOperatingService.getUserErrorNumber(body.getAccount());
            return RestResult.generateRestResult(AppResultEnum.ERROE.getCode(),null,null);
        }

        Long tokenExpire = TokenConstant.TOKEN_EXPIRE;
        Long tikenExpire = TokenConstant.TIKEN_EXPIRE;
        //生成用户token 和 tiken 计入redis中 设置生命周期分别为5分钟和3天
        String token = jwtTokenUtil.createJWTToken(String.valueOf(successUser.getUserId()), loginUserName , tokenExpire);
        Long nowDate = System.currentTimeMillis() / 1000;
        TikenPayload tikenPayload = new TikenPayload(String.valueOf(successUser.getUserId()), successUser.getUserTblName(),loginId , nowDate , nowDate + tikenExpire);
        String tiken = tikenUtil.aesEncode(JSONObject.toJSONString(tikenPayload), tikenIv);

        // 删除之前存储的tiken信息
        //redisOperatingService.delTikenAndTikenIv(String.valueOf(successUser.getUserId()));
        redisOperatingService.setToken(String.valueOf(successUser.getUserId()), token);
        redisOperatingService.setTikenAndTikenIv(String.valueOf(successUser.getUserId()), tiken , tikenIv,userId+TokenConstant.TOKEN+token);
        redisOperatingService.setLoginId(String.valueOf(successUser.getUserId()) , loginId,tiken);
        //登录成功数量+1
        redisOperatingService.setLoginSuccessCount(String.valueOf(successUser.getUserId()),1);
        //登录成功清理锁定状态及锁定次数
        if(loginRecordPO!=null){
            //1。清除登录失败记录
            loginRecordService.deleteUserName(loginUserName,"APP");
        }
        redisOperatingService.delUserLock(body.getAccount());
        LoginVo loginVo = new LoginVo();
        loginVo.setTiken(userId+TokenConstant.TIKEN + tiken);
        loginVo.setTiken_expires_in(tikenExpire);
        loginVo.setToken(userId+TokenConstant.TOKEN+token);
        loginVo.setToken_expires_in(tokenExpire);
        loginVo.setIsInintSyspass(successUser.getIsInintSyspass());
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),loginVo);
    }

    private int failLogin(LoginRecordPO loginRecordPO,String loginUserName,String userId,Long duration){
        int lockFlag;
        if(loginRecordPO==null){
            //新增登录记录
            LoginRecordPO loginRecord = new LoginRecordPO();
            loginRecord.setUserName(loginUserName);
            loginRecord.setFailureNum(1);
            lockFlag=0;
            loginRecord.setLockFlag(lockFlag+"");//0未锁定，1已锁定
            loginRecord.setLoginDate(new Date());
            loginRecord.setOrigin("APP");
            loginRecordService.insert(loginRecord);
            redisOperatingService.setUserLoginErrorNumber(loginUserName, NumConstant.NUM_1,duration);
        }else{
            int failureNum = loginRecordPO.getFailureNum();
            LoginRecordPO loginRecord = new LoginRecordPO();
            if(failureNum>=4){
                lockFlag=1;
                loginRecord.setLockFlag(lockFlag+"");
            }else{
                lockFlag=0;
                loginRecord.setLockFlag(lockFlag+"");
            }
            failureNum=failureNum+1;
            loginRecord.setUserName(loginUserName);
            loginRecord.setFailureNum(failureNum);
            loginRecord.setLoginDate(new Date());
            loginRecordService.update(loginRecord);
            redisOperatingService.setUserLoginErrorNumber(loginUserName, failureNum,duration);
            if(lockFlag==1){
                //清空token
                redisOperatingService.delByKey(userId+TokenConstant.TOKEN+"*");
                //清空tiken
                redisOperatingService.delByKey(userId+TokenConstant.TIKEN+"*");
            }
        }
        return lockFlag;
    }
    public RestResult loginIsSuccess(@NonNull String userName, @NonNull String userPwd) {
        Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put(UserPO.USER_NAME, userName);
        conditionMap.put(UserPO.PASS_USER_WORD, userPwd);
        // 根据用户名密码查询用户并校验
        return Optional.ofNullable(userService.selectUser(conditionMap)).map(user -> {
            // 判断用户权限是否有效；true:有效；false:权限失效 (权限失效原因：分配的IPC在最新的组网关系表不存在)
            boolean isAuthorityValid = isAuthorityValid(user);
            if (isAuthorityValid) {
                // 查询成功，权限未失效 把User放入RestResult 的data属性中，便于后面代码获取
                return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(), AppResultEnum.SUCCESS.getMessage(), user);
            }
            // 查询成功，但权限失效
            return RestResult.generateRestResult(AppResultEnum.USER_AUTHORITY_FAIL.getCode(), AppResultEnum.USER_AUTHORITY_FAIL.getMessage(), null);
        }).orElse(
                // 查询失败，用户名和密码不匹配
            RestResult.generateRestResult(AppResultEnum.USER_PASSWORD_FAIL.getCode(), AppResultEnum.USER_PASSWORD_FAIL.getMessage(), null));
    }
    /**
     * 判断用户权限是否有效
     *
     * @param user 用户
     * @return 权限是否有效
     */
    public boolean isAuthorityValid(UserPO user) {
//        if (user.getIsDefault() != UserPO.DEFAULT_USER) {
//            // 非管理员，查看是否在最新的组网关系表中有ipc
//            List<String> ipcIpByUserOrId =
//                    userIpResourceService.getIpcIpByUserOrId(user.getUserTblName(), null, CommonConst.TBL_IPC_IP_TMP);
//            if (CollectionUtil.isEmpty(ipcIpByUserOrId)) {
//                return false;
//            }
//
//            // 非管理员查询是否具备页面权限
//            List<Integer> userRoleIds = roleInfoMapper.selectUserRoleId(user.getUserId());
//            Optional<Integer> findFirst = userRoleIds.stream().filter(intger -> intger > 0).findFirst();
//            if (!findFirst.isPresent()) {
//                return false;
//            }
//        }
        return true;
    }

    /**
     * 根据用户名获取对应的盐值
     *
     * @param loginUserName 登录用户名
     * @return 用户私有盐值
     */
    private String getUserSalt(String loginUserName) {
        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put(UserPO.USER_NAME, loginUserName);
        UserPO user = userService.selectUser(selectMap);
        if (user == null) {
            return CommonSymbolicConstant.SYMBOL_BLANK;
        }
        return Optional.ofNullable(user.getUserSalt()).map(String::trim).orElse("");
    }
    /**
     * 查询账号是否被锁定
     * */
 //   public boolean userIsLock(@NonNull String userName) {
//        boolean isUserLock = Optional.ofNullable(loginRecordDao.selectByUserName(userName)).map(record -> {
//            // 当用户没有被锁定时，返回false
//            if (!ServiceCommonConst.FLAG_USER_LOCK.equals(record.getLockFlag())) {
//                return false;
//            }
//
//            // 当用户锁定时，判断是否被解锁；
//            // 算法：当前时间 减去 最后一次登录时间 是否小于 锁定时间
//            // isLock:true(被锁定)；false:(被解锁)
//            boolean isLock = new Date().getTime() - record.getLoginDate().getTime() < Long
//                    .parseLong(GlobalConfigProperty.getGlobalMap().get(PropertiesConfigCommonConst.INIT_LOCKTIME))
//                    * NumConstant.NUM_1000;
//
//            // 用户被解锁时，需要删除该用户的登录记录
//            if (!isLock) {
//                loginRecordDao.deleteUserName(userName);
//            }
//            return isLock;
//        }).orElse(false);
//        return isUserLock;
//    }


    /**
     * token续约
     *
     * @param body
     * @return
     */
    @Override
    public RestResult renewalToken(RenewalTokenDto body,HttpServletResponse response) {

        String paramTiken = body.getTiken();
        String newSubTiken = paramTiken.substring(paramTiken.indexOf(TokenConstant.TIKEN)+11);
        String userId = paramTiken.substring(0,paramTiken.indexOf(TokenConstant.TIKEN));
        Long tokenExpire =TokenConstant.TOKEN_EXPIRE;
        TikenPayload tikenPayload = null;
        try {
            String tikenIv = redisOperatingService.getTikenIv(userId+TokenConstant.TIKEN_IV + newSubTiken);
            if (tikenIv == null) {
                response.setStatus(302);
                return RestResult.generateRestResult(AppResultEnum.TIKEN_TIME_OUT.getCode(),AppResultEnum.TIKEN_TIME_OUT.getMessage(),null);
            }
            tikenPayload = JSONObject.parseObject(tikenUtil.aesDecode(newSubTiken, tikenIv), TikenPayload.class);
        } catch (Exception ex) {
            response.setStatus(302);
            return RestResult.generateRestResult(AppResultEnum.TIKEN_TIME_OUT.getCode(),AppResultEnum.TIKEN_TIME_OUT.getMessage(),null);
            //throw new BusinessException(BusinessErrorEnum.UMS_TIKEN_OVERDUE);
        }
        //checkUserFrozenInside(tikenPayload.getUser_id());
        //验证loginId是否是当前用户的

        String redisLoginId = redisOperatingService.getLoginId(tikenPayload.getUser_id(),newSubTiken);
        if(redisLoginId!=null && (!redisLoginId.equals(tikenPayload.getLogin_id()))){
            throw new BusinessException(BusinessErrorEnum.UMS_ACCOUNT_KICKED_OUT);
        }
        String oldToken = redisOperatingService.getTiken(tikenPayload.getUser_id(),newSubTiken);
        if (StringUtils.isEmpty(oldToken)) {
            response.setStatus(302);
            return RestResult.generateRestResult(AppResultEnum.TIKEN_TIME_OUT.getCode(),AppResultEnum.TIKEN_TIME_OUT.getMessage(),null);
        }
        Long total = userService.getTotalByUserId(tikenPayload.getUser_id());
        if (total == null || total == 0) {
            throw new BusinessException(BusinessErrorEnum.UMS_ACCOUNT_NOT_EXIST);
        }
        //Long tokenExpire = Long.valueOf(ApplicationConfig.getConfig("jwt.token.expire").toString());
        String newToken = jwtTokenUtil.createJWTToken(tikenPayload.getUser_id(), tikenPayload.getUser_name() , tokenExpire);
        redisOperatingService.setToken(tikenPayload.getUser_id(), newToken);
        //删除之前的token
        redisOperatingService.delByKey(oldToken);
       // ExecuteResult result = new ExecuteResult(new RenewalTokenVo(token, tokenExpire));
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),new RenewalTokenVo(tikenPayload.getUser_id()+"_app_token_"+newToken, tokenExpire));
    }

    /**
     * 内部方法校验用户是否已冻结
     * @param user_id
     */
    private void checkUserFrozenInside(String user_id) {
        Integer frozen = userService.getFrozenByUserId(user_id);
        if(frozen == null){
            throw new BusinessException(BusinessErrorEnum.UMS_ACCOUNT_NOT_EXIST);
        }
        if (frozen.equals(UmsUserFrozenEnum.FROZEN.getValue())){
            redisOperatingService.delToken(user_id);
            //redisOperatingService.delTikenAndTikenIv(user_id);
            throw new BusinessException(BusinessErrorEnum.UMS_ACCOUNT_FROZEN);
        }
    }
    /**
     * 修改密码
     * 1.旧密码错误三次,直接退出
     * @param body
     * @return
     */
    @Override
    @Transactional
    public RestResult modifyPwd(ModifyPwdDto body,String userId) {
        ExecuteResult executeResult = new ExecuteResult();
        if(body==null){
            return RestResult.generateRestResult(AppResultEnum.USER_DATA_EMPTY.getCode(),AppResultEnum.USER_DATA_EMPTY.getMessage(),null);
        }
        if(StringUtils.isEmpty(body.getPassword())){
            return RestResult.generateRestResult(AppResultEnum.USER_DATA_EMPTY.getCode(),AppResultEnum.USER_DATA_EMPTY.getMessage(),null);
        }
        if(StringUtils.isEmpty(body.getNew_password())){
            return RestResult.generateRestResult(AppResultEnum.USER_DATA_EMPTY.getCode(),AppResultEnum.USER_DATA_EMPTY.getMessage(),null);
        }
        if(StringUtils.isEmpty(body.getCheck_password())){
            return RestResult.generateRestResult(AppResultEnum.USER_DATA_EMPTY.getCode(),AppResultEnum.USER_DATA_EMPTY.getMessage(),null);
        }
        if(!body.getCheck_password().equals(body.getNew_password())){
            return RestResult.generateRestResult(AppResultEnum.PASSWORD_INCONFORMITY.getCode(),AppResultEnum.PASSWORD_INCONFORMITY.getMessage(),null);
        }
        if(body.getPassword().equals(body.getNew_password())){
            return RestResult.generateRestResult(AppResultEnum.PASSWORD_IS_EQUAL.getCode(),AppResultEnum.PASSWORD_IS_EQUAL.getMessage(),null);
        }
        if(body.getPassword()!=null){
            UserPO userPO = userService.getInfo(userId);
            if (userPO!=null){
                String userpassword = userPO.getPassUserWord();
                String loginPass = Pbkdf2Util.encrypt(body.getPassword(), userPO.getUserSalt(), CommonConst.ITERATIONS);
                if(!userpassword.equals(loginPass)){
                    return RestResult.generateRestResult(AppResultEnum.OLD_PASSWORD_ERROR.getCode(),AppResultEnum.OLD_PASSWORD_ERROR.getMessage(),null);
                }
            }else{
                return RestResult.generateRestResult(AppResultEnum.NOT_FIND_USER.getCode(),AppResultEnum.NOT_FIND_USER.getMessage(),null);
            }
        }

        //校验账号忘记密码被锁定
//        if(redisOperatingService.isModifyPwdUserLock(userId)){
//            redisOperatingService.delToken(userId);
//            redisOperatingService.delTikenAndTikenIv(userId);
//            redisOperatingService.delModifyPwdUserLock(userId);
//            throw new BusinessException(BusinessErrorEnum.UMS_ACCOUNT_IS_FORCED_OFFLINE);
//        }
        //校验用户是否存在
        UserPO userPO = userMapper.getUser(userId);
        if (userPO == null) {
            throw new BusinessException(BusinessErrorEnum.UMS_ACCOUNT_NOT_EXIST);
        }
        //检验原密码是否正确
        //用户输入密码 加密
        String loginPass = Pbkdf2Util.encrypt(body.getPassword(), userPO.getUserSalt(), CommonConst.ITERATIONS);
        String userName = userPO.getUserTblName();
        Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put(UserPO.USER_NAME, userName);
        conditionMap.put(UserPO.PASS_USER_WORD, loginPass);
        // 根据用户名密码查询用户并校验
        UserPO userPO1 = userService.selectUser(conditionMap);
        if(userPO1==null){
            return RestResult.generateRestResult(AppResultEnum.OLD_PASSWORD_ERROR.getCode(),AppResultEnum.OLD_PASSWORD_ERROR.getMessage(),null);
        }
        //String newPasswordToEncryption = passwordEncryptionConfig.getEncryptedPassword(body.getNew_password(), salt);
        String newPasswordToEncryption = Pbkdf2Util.encrypt(body.getNew_password(), userPO.getUserSalt(), CommonConst.ITERATIONS);
        //更新密码
        userService.updatePassword(userId,newPasswordToEncryption);
        //清空token
        redisOperatingService.delByKey(userId+TokenConstant.TOKEN+"*");
        //清空tiken
        redisOperatingService.delByKey(userId+TokenConstant.TIKEN+"*");
        redisOperatingService.delByKey(userId+TokenConstant.LOGIN_COUNT);
        //登录成功清理锁定次数
        redisOperatingService.delModifyPwdUserLock(userId);
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),null);
    }


    /**
     * 登出
     *
     * @param
     * @return
     */
    @Override
    public RestResult logout(String token, String tiken) {
        redisOperatingService.delByKey(token);
        redisOperatingService.delByKey(tiken);
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),null);
    }

    /**
     * 校验密码
     * */
    @Override
    public RestResult pwdrulecheck(String password){
        if(org.springframework.util.StringUtils.isEmpty(password)){
            return RestResult.generateRestResult(AppResultEnum.USER_DATA_EMPTY.getCode(),AppResultEnum.USER_DATA_EMPTY.getMessage(),null);
        }
        if (password.length()<8){
            return RestResult.generateRestResult(AppResultEnum.PWD_LENGTH_SMALL.getCode(),AppResultEnum.PWD_LENGTH_SMALL.getMessage(),null);
        }
        if (password.length()>32){
            return RestResult.generateRestResult(AppResultEnum.PWD_LENGTH_BIG.getCode(),AppResultEnum.PWD_LENGTH_BIG.getMessage(),null);
        }
        if(!password.matches("^.*[a-zA-Z]+.*$")){
            return RestResult.generateRestResult(AppResultEnum.PWD_CONTAIN_ALPHABET.getCode(),AppResultEnum.PWD_CONTAIN_ALPHABET.getMessage(),null);
        }
        if(!password.matches("^.*[0-9]+.*$")){
            return RestResult.generateRestResult(AppResultEnum.PWD_CONTAIN_NUM.getCode(),AppResultEnum.PWD_CONTAIN_NUM.getMessage(),null);
        }
        if(!password.matches("^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$")){
            return RestResult.generateRestResult(AppResultEnum.PWD_CONTAIN_SYMBOL.getCode(),AppResultEnum.PWD_CONTAIN_SYMBOL.getMessage(),null);
        }
        if(password.contains("password")||password.contains("PASSWORD")){
            return RestResult.generateRestResult(AppResultEnum.PWD_CONTAIN_PASSWORD.getCode(),AppResultEnum.PWD_CONTAIN_PASSWORD.getMessage(),null);
        }
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),null);
    }

}
