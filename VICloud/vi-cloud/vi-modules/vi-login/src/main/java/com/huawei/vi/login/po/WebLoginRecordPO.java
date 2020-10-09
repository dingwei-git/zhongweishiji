package com.huawei.vi.login.po;

import com.huawei.utils.CommonUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WebLoginRecordPO implements Serializable {

    /**
     * 在 tbl_login_record 表中定义的列名 userName 注：当前使用的是 userName 待规范之后，替换为userLoginName
     */
    public static final String USERLOGINNAME = "userName";

    /**
     * 在 tbl_login_record 表中定义的列名 lockFlag
     */
    public static final String LOCKFLAG = "lockFlag";

    /**
     * 在 tbl_login_record 表中定义的列名 failureNum
     */
    public static final String FAILURENUM = "failureNum";

    /**
     * 在 tbl_login_record 表中定义的列名 loginDate
     */
    public static final String LOGINDATE = "loginDate";

    /**
     * 用户被锁定的标识
     */
    public static final String LOCK_FLAG = "1";

    /**
     * UID
     */
    private static final long serialVersionUID = -1496785744053891644L;

    // 用户名
    private String userLoginName;

    // 锁定状态
    private String lockFlag;

    // 失败次数
    private int failureNum;

    // 失败时间
    private Date loginDate;

    // 盐值
    private String userSalt;

    public WebLoginRecordPO(){
        super();
    }

    /**
     * 登录
     *
     * @param usertmpName userName
     * @param lockFlag lockFlag
     * @param failureNum failureNum
     * @param loginDate loginDate
     */
    public WebLoginRecordPO(String usertmpName, String lockFlag, int failureNum, Date loginDate) {
        userLoginName = usertmpName;
        this.lockFlag = lockFlag;
        this.failureNum = failureNum;
        if (CommonUtil.isNotNull(loginDate)) {
            this.loginDate = (Date) loginDate.clone();
        } else {
            this.loginDate = null;
        }
    }

    /**
     * 获得登录时间
     *
     * @return Date 返回类型
     */
    public Date getLoginDate() {
        if (CommonUtil.isNotNull(loginDate)) {
            return (Date) loginDate.clone();
        }
        return null;
    }

    /**
     * 设置登录时间
     *
     * @param loginDate loginDate
     */
    public void setLoginDate(Date loginDate) {
        if (CommonUtil.isNotNull(loginDate)) {
            this.loginDate = (Date) loginDate.clone();
        } else {
            this.loginDate = null;
        }
    }

}
