package com.jovision.jaws.common.constant;

public class TokenConstant {

    /**token密钥*/
    public static final String TOKEN_SECRET = "tikentikentikentikentikentikentikentiken";
    /**tiken密钥*/
    public static final String TIKEN_SECRET = "tikentikentikentikentikentikentikentikentikentikentikentiken";
    /**token过期时间*/
    public static final Long TOKEN_EXPIRE = 600L;
    //public static final Long TOKEN_EXPIRE = 60L;
    /**Webtoken过期时间*/
    public static final Long WEB_TOKEN_EXPIRE = 1800L;
    /**tiken过期时间*/
    public static final Long TIKEN_EXPIRE = 259200L;
    /**账号锁定时间*/
    public static final Long LOCH_EXPIRE = 300L;
    /**web验证码过期时间*/
    public static final int DRAW_IMAGE_EXPIRE = 600;
    /**
     * 用户登录token前缀 + userid
     */
    public static final String TOKEN = "_app_token_";
    /**
     * 用户登录tiken前缀 + userid
     */
    public final static String TIKEN = "_app_tiken_";
    /**
     * 登录成功次数
     * */
    public final static String LOGIN_COUNT = "_app_count";
    /**
     * 用户登录tikenIv前缀 + tiken
     */
    public final static String TIKEN_IV = "_app_iv_";
    /**
     * 用户登录唯一标识 + userid
     */
    public final static String LOGINID = "_app_loginId_";
    /**
     * 用户验证码前缀 + account_type
     */
    public final static String VCODE = "APP:VCODE:VALUE:";
    /**
     * 用户验证码获取锁定前缀 + account_type
     */
    public final static String VCODE_LOCKING = "APP:VCODE:LOCKING:";
    /**
     * 用户登录错误次数前缀 + account
     */
    public final static String USER_LOGIN_ERROR_NUMBER = "APP:LOGIN:ERROR_NUMBER:";
    /**
     * 忘记密码锁定 + 账号
     */
    public final static String USER_LOCKING = "app_user_locking_";
    /**
     * 忘记密码锁定创建时间 + 账号
     */
    public final static String USER_LOCKING_CREATETIME = "app_user_locking_createTime_";
    /**
     * 忘记密码错误次数 + 用户账号
     */
    public final static String USER_ERROR = "app_user_error_";

    /**
     * 修改密码锁定 + 账号
     */
    public final static String MODIFY_PWD_LOCKING = "APP:MODIFYPWD:LOCKING:";
    /**
     * 修改密码错误次数 + 用户账号
     */
    public final static String MODIFY_PWD_ERROR = "APP:MODIFYPWD:ERROR:";
    /**
     * 用户添加分享唯一标识 + userid
     */
    public final static String SHARE = "APP:SHARE:";


}
