package com.jovision.jaws.common.config.redis;


import com.jovision.jaws.common.constant.ErrorLockVo;
import com.jovision.jaws.common.constant.TokenConstant;
import com.jovision.jaws.common.constant.UmsVerificationCodeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ Description   :  redis操作工具类 <br/>
 * @ Author        :  ABug <br/>
 * @ CreateDate    :  2020/3/24 14:51 <br/>
 * @ UpdateUser    :  ABug <br/>
 * @ UpdateDate    :  2020/3/24 14:51 <br/>
 * @ UpdateRemark  :  修改内容 <br/>
 * @ Version       :  1.0 <br/>
 */
@Component
@Slf4j
public class RedisOperatingService {

    @Resource(name = "redisUmsTemplate")
    private RedisTemplate redisTemplate;

//    @Autowired
//    private UmsUserLogMapper umsUserLogMapper;
//    @Autowired
//    private PasswordEncryptionConfig passwordEncryptionConfig;
//    @Autowired
//    private AccountAESConfig accountAESConfig;



    /**
     * 获取用户登录ID
     *
     * @param userId 用户ID
     * @return
     */
    public String getLoginId(String userId,String tiken) {
        Object redisValue = redisTemplate.opsForValue().get(userId+TokenConstant.LOGINID + tiken);
        if (redisValue == null) {
            return null;
        }
        return redisValue.toString();
    }

    /**
     * 设置用户登录ID
     *
     * @param userId  用户ID
     * @param loginId 登录ID
     */
    public void setLoginId(String userId, String loginId,String tiken) {
        //Long tikenExpire = Long.valueOf(ApplicationConfig.getConfig("jwt.tiken.expire").toString());
        redisTemplate.opsForValue().set(userId+TokenConstant.LOGINID+tiken, loginId,  TokenConstant.TIKEN_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 获取该用户服务端存储token
     *
     * @param userId
     * @return
     */
    public String getToken(String userId) {
        Object redisValue = redisTemplate.opsForValue().get(TokenConstant.TOKEN + userId);
        if (redisValue == null) {
            return null;
        }
        return redisValue.toString();
    }
    /**
     * 获取该用户服务端存储token
     *
     * @param
     * @return
     */
    public String getTokenByToken(String token) {
        Object redisValue = redisTemplate.opsForValue().get(token);
        if (redisValue == null) {
            return null;
        }
        return redisValue.toString();
    }
    /**
     * 设置token
     *
     * @param userId
     * @param token
     */
    public void setToken(String userId, String token) {
        //Long tokenExpire = Long.valueOf(ApplicationConfig.getConfig("jwt.token.expire").toString());
        //Long tokenExpire = TokenConstant.TOKEN_EXPIRE;
        redisTemplate.opsForValue().set(userId+TokenConstant.TOKEN+token, "1", TokenConstant.TOKEN_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void setTokenByTime(String key, Object value,Integer expire,TimeUnit units) {
        if(expire==null||expire==0){
            redisTemplate.opsForValue().set(key, String.valueOf(value));
        }else{
            redisTemplate.opsForValue().set(key, String.valueOf(value), expire, units);
        }
    }

    /**
     *
     * @param key
     * @param value
     */
    public void setTokenByTime(String key, Object value) {
        redisTemplate.opsForValue().set(key, String.valueOf(value));
    }

    /**
     * 删除token
     *
     * @param userId
     */
    public void delToken(String userId) {
        redisTemplate.delete(TokenConstant.TOKEN + userId);
    }
    public void delTokenByKey(String key) {
        redisTemplate.delete(key);
    }
    public void delTikenByKey(String key) {
        redisTemplate.delete(key);
    }
    public void delByKey(String key){
        redisTemplate.delete(key);
    }
    /**
     * 设置tiken和iv
     *
     * @param userId
     * @param tiken
     * @param iv
     */
    public void setTikenAndTikenIv(String userId, String tiken, String iv,String token) {
        //Long tikenExpire = Long.valueOf(ApplicationConfig.getConfig("jwt.tiken.expire").toString());
        Long tikenExpire = TokenConstant.TIKEN_EXPIRE;
        redisTemplate.opsForValue().set(userId+TokenConstant.TIKEN + tiken, token, tikenExpire, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(userId+TokenConstant.TIKEN_IV + tiken, iv, tikenExpire, TimeUnit.SECONDS);
    }
    /**
     * 设置登录成功次数
     *
     */
    public void setLoginSuccessCount(String userId,int index){
        Object redisValue = redisTemplate.opsForValue().get(userId+ TokenConstant.LOGIN_COUNT);
        if(redisValue!=null){
            int count = Integer.parseInt(redisValue.toString())+index;
            redisTemplate.opsForValue().set(userId+ TokenConstant.LOGIN_COUNT, count+"", TokenConstant.LOCH_EXPIRE, TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(userId+ TokenConstant.LOGIN_COUNT, "1", TokenConstant.LOCH_EXPIRE, TimeUnit.SECONDS);
        }
    }
    /**
     * 获取用户登录成功次数
     * */
    public int getLoginSuccessCount(String userId){
        Object redisValue = redisTemplate.opsForValue().get(userId+ TokenConstant.LOGIN_COUNT);
        if (redisValue == null) {
            return 0;
        }
        return Integer.parseInt(redisValue.toString());
    }
    /**
     * 获取用户服务端存储的tiken
     *
     * @param userId
     * @return
     */
    public String getTiken(String userId,String tiken) {
        Object redisValue = redisTemplate.opsForValue().get(userId+TokenConstant.TIKEN + tiken);
        if (redisValue == null) {
            return null;
        }
        return redisValue.toString();
    }

    /**
     *
     * @param key
     * @return
     */
    public String getValueByKey(String key) {
        Object redisValue = redisTemplate.opsForValue().get(key);
        if (redisValue == null) {
            return null;
        }
        return redisValue.toString();
    }

    /**
     *  模糊查询
     * @param key
     * @return
     */
    public String getValueByFuzzyKey(String key) {
        Object redisValue = redisTemplate.keys(key);
        if (redisValue == null) {
            return null;
        }
        return redisValue.toString();
    }

    /**
     * 获取tikenIv
     *
     * @param tiken
     * @return
     */
    public String getTikenIv(String tiken) {
        Object redisValue = redisTemplate.opsForValue().get(tiken);
        if (redisValue == null) {
            return null;
        }
        return redisValue.toString();
    }

    /**
     * 删除tiken和tikenIv值
     *
     * @param userId
     */
//    public void delTikenAndTikenIv(String userId) {
//        String tiken = getTiken(userId);
//        if (tiken != null) {
//            redisTemplate.delete(TokenConstant.TIKEN + userId);
//
//            // TODO: 2020/3/24 不删除tiken_iv变量 , 因为删除了iv就会造成用户tiken解密失败
////            redisTemplate.delete(TIKEN_IV + tiken);
//        }
//    }

    /**
     * 判断获取验证码是否被锁定
     *
     * @param account 账号
     * @param type    验证码类型
     * @return true=被锁定 ; false=未锁定
     */
    public boolean isVerificationCodeLocking(String account, Integer type) {
        Object redisValue = redisTemplate.opsForValue().get(TokenConstant.VCODE_LOCKING + account + "_" + type);
        if (redisValue == null) {
            return false;
        }
        return true;
    }

    /**
     * 删除验证码
     *
     * @param account
     * @param enumObj
     */
    public void delVerificationCode(String account, UmsVerificationCodeTypeEnum enumObj) {
        redisTemplate.delete(TokenConstant.VCODE + account + "_" + enumObj.getValue());
    }


    /**
     * 获取验证码
     *
     * @param account 账号
     * @param enumObj 验证码类型
     * @return
     */
    private String getVerificationCode(String account, UmsVerificationCodeTypeEnum enumObj) {
        Object redisValue = redisTemplate.opsForValue().get(TokenConstant.VCODE + account + "_" + enumObj.getValue());
        if (redisValue == null) {
            return null;
        }
        return redisValue.toString();
    }

    /**
     * 设置验证码
     *
     * @param account 账号
     * @param type    验证码类型
     * @param code    验证码
     */
    public void setVerificationCodeAndVCodeLocking(String account, Integer type, String code) {
        Long verificationCodeExpire = Long.valueOf(ApplicationConfig.getConfig("verification.code.expire").toString());
        Long verificationCodeLockingExpire = Long.valueOf(ApplicationConfig.getConfig("verification.code.locking-expire").toString());

        redisTemplate.opsForValue().set(TokenConstant.VCODE + account + "_" + type, code, verificationCodeExpire, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(TokenConstant.VCODE_LOCKING + account + "_" + type, String.valueOf(type), verificationCodeLockingExpire, TimeUnit.SECONDS);
    }


    /**
     * 校验redis验证码
     *
     * @param code    待校验的验证码
     * @param account 账号 或 用户ID
     * @param enumObj 验证码类型
     * @return true=校验通过;false=校验失败
     */
    public boolean equalVerificationCode(String code, String account, UmsVerificationCodeTypeEnum enumObj) {
        String redisValue = getVerificationCode(account, enumObj);
        if (redisValue == null || !redisValue.toUpperCase().equals(code.toUpperCase())) {
            return false;
        }
        return true;
    }

    /**
     * 删除用户登录锁定次数
     * @param account
     */
    public void delUserLoginErrorNumber(String account) {
        redisTemplate.delete(TokenConstant.USER_LOGIN_ERROR_NUMBER + account);
    }

    /**
     * 获取用户登录错误次数
     * @param account
     * @return
     */
    public int getUserLoginErrorNumber(String account) {
        Object redisValue = redisTemplate.opsForValue().get(TokenConstant.USER_LOGIN_ERROR_NUMBER + account);
        if (redisValue == null) {
            return 0;
        }
        return Integer.valueOf(redisValue.toString()).intValue();
    }

    /**
     * 设置用户登录错误次数
     * @param account
     * @param number
     */
    public void setUserLoginErrorNumber(String account , int number,Long duration){
        if(number<0){
            return;
        }
        //Long duration = Long.valueOf(ApplicationConfig.getConfig("user.password.duration").toString());
        redisTemplate.opsForValue().set(TokenConstant.USER_LOGIN_ERROR_NUMBER + account , String.valueOf(number) , duration, TimeUnit.SECONDS);
    }

    /**
     * 设置用户登录错误次数
     */
    public Long getExpire(String key , TimeUnit duration){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 获取账号剩余锁定尝试次数和锁定时长
     *
     * @return
     */
    public ErrorLockVo getUserErrorNumber(String account) {
        ErrorLockVo errorLockVo = new ErrorLockVo();
        // 忘记密码接口调用锁定时长(复用验证码有效时长)
        Long lockDuration = Long.valueOf(ApplicationConfig.getConfig("user.password.duration").toString());
        // 忘记密码验证码错误次数达到多少次后锁定(复用密码错误次数)
        Integer codeErrorNum = Integer.valueOf(ApplicationConfig.getConfig("user.password.maxRetryCount").toString());

        Set<String> keyAccounts = redisTemplate.keys(TokenConstant.USER_ERROR + account + "*");

        errorLockVo.setSurplus_num(codeErrorNum - keyAccounts.size());
        errorLockVo.setLock_duration(lockDuration.intValue());
        return errorLockVo;
    }

    /**
     * 添加账号错误次数
     * 1分钟内用户验证码错误超过3次锁定账号所有操作
     *
     * @param account 账号
     * @return
     */
//    public void addUserErrorNumber(String account) {
//        // 验证码时间段错误次数超时时间(复用验证码再次获取锁定时长) 错误记录周期60秒
//        Long errorDuration = Long.valueOf(ApplicationConfig.getConfig("user.password.error-duration").toString());
//        // 忘记密码接口调用锁定时长(复用验证码有效时长)
//        Long lockDuration = Long.valueOf(ApplicationConfig.getConfig("user.password.duration").toString());
//        // 忘记密码验证码错误次数达到多少次后锁定(复用密码错误次数)
//        Integer codeErrorNum = Integer.valueOf(ApplicationConfig.getConfig("user.password.maxRetryCount").toString());
//
//        redisTemplate.opsForValue().set(USER_ERROR + account + System.currentTimeMillis(), "1", errorDuration, TimeUnit.SECONDS);
//        Set<String> keyAccounts = redisTemplate.keys(USER_ERROR + account + "*");
//        if (codeErrorNum <= (keyAccounts.size())) { // 错误次数超过3次将锁定该用户所有操作
//            redisTemplate.opsForValue().set(USER_LOCKING + account, account, lockDuration, TimeUnit.SECONDS);
//            redisTemplate.opsForValue().set(USER_LOCKING_CREATETIME + account, String.valueOf(System.currentTimeMillis()), lockDuration, TimeUnit.SECONDS);
//
//            //清除错误计数
//            redisTemplate.delete(keyAccounts);
//
//            // TODO: 2020/5/19 用户锁定日志入库
//            UmsUserLog umsUserLog = new UmsUserLog();
////            umsUserLog.setIp(ip);
//            umsUserLog.setOperate_time(new Date());
////            umsUserLog.setRequest_resource("");
//            MasterKeyVo masterKeyVo = EncAgentUtils.getLastestMK(EncAgentUtils.MasterKeyVoList);
//            String uname_iv = passwordEncryptionConfig.generateSalt();
//            umsUserLog.setUname_iv(uname_iv);
//            umsUserLog.setAccount(masterKeyVo.getMk_id() + "." + accountAESConfig.aesEncodeCustom(account, uname_iv, masterKeyVo.getMk_data().substring(0, 16)));
//            umsUserLog.setOperate_type(UmsUserLogTypeEnum.LOCK_USER.getValue());
//            umsUserLog.setEncrypted_uname(account.substring(0, 4) + SHA256Util.getSHA256String(account));
//            umsUserLogMapper.insertLog(umsUserLog);
//        }
//    }

    /**
     * 判断账号和用户ID 是否被锁定
     *
     * @param account
     */
    public boolean isUserLock(String account) {
        Object redisValue = redisTemplate.opsForValue().get(TokenConstant.USER_LOCKING + account);
        if (redisValue == null) {
            return false;
        }
        return true;
    }

    /**
     * 清除用户锁定信息
     *
     * @param account
     * @return
     */
    public void delUserLock(String account) {
        redisTemplate.delete(TokenConstant.USER_LOCKING + account);
        redisTemplate.delete(TokenConstant.USER_LOCKING_CREATETIME + account);
        Set<String> keyAccounts = redisTemplate.keys(TokenConstant.USER_ERROR + account + "*");
        redisTemplate.delete(keyAccounts);
    }

    /**
     * 根据Key清除Value
     *
     * @param key
     */
    public void delValueByKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据Key清除Value(模糊)
     *
     * @param key
     */
    public void delValueByFuzzyKey(String key) {
        Set<String> keys = redisTemplate.keys(key+"*");
        redisTemplate.delete(keys);
    }

    /**
     * 添加修改密码错误
     * n分钟内用户验证码错误超过3次锁定账号所有操作
     *
     * @param userId
     * @return
     */
    public void addModifyPwdErrorNumber(String userId) {
        // 5分钟内  锁定5分钟
        Long duration = Long.valueOf(ApplicationConfig.getConfig("user.password.duration").toString());
        redisTemplate.opsForValue().set(TokenConstant.MODIFY_PWD_ERROR + userId + System.currentTimeMillis(), "1", duration, TimeUnit.SECONDS);
    }

    /**
     * 判断账号 是否被锁定
     *
     * @param userId
     */
    public boolean isModifyPwdUserLock(String userId) {
        // 修改密码密码错误次数达到多少次后退出
        Integer codeErrorNum = 3;

        Set<String> keyAccounts = redisTemplate.keys(TokenConstant.MODIFY_PWD_ERROR + userId + "*");
        if (codeErrorNum <= (keyAccounts.size())) { // 错误次数超过3次,清除用户token和tiken
            return true;
        }
        return false;
    }

    /**
     * 清除修改密码锁定信息
     *
     * @param userId
     * @return
     */
    public void delModifyPwdUserLock(String userId) {
        Set<String> keyAccounts = redisTemplate.keys(TokenConstant.MODIFY_PWD_ERROR + userId + "*");
        redisTemplate.delete(keyAccounts);
    }


    /**
     * 获取当前redis中锁定的用户集合
     *
     * @return Map<账号, 创建时间>
     */
    public Map<String , Long> getNowUserLocks(){
        Map<String , Long> returnMap = new HashMap<>();

        Set<String> keyLockAccounts = redisTemplate.keys(TokenConstant.USER_LOCKING_CREATETIME + "*");

        keyLockAccounts.forEach(key -> {
            Object redisValue = redisTemplate.opsForValue().get(key);
            if (redisValue != null) {
                returnMap.put(key.substring(key.lastIndexOf(":") +1) , Long.valueOf(redisValue.toString()));
            }
        });

        return returnMap;
    }

    /**
     * 清除修改密码锁定信息
     *
     * @param userId
     * @return
     */
    public void delAddShare(String userId){
        redisTemplate.delete(TokenConstant.SHARE + userId);
    }
}