package com.huawei.vi.entity.po;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class UserPO{
    /**
     * 用户名
     */
    public static final String USER_NAME = "userTblName";

    /**
     * 数据库中的isDefault字段
     */
    public static final String IS_DEFAULT = "isDefault";

    /**
     * 数据库中的passUserWord字段
     */
    public static final String PASS_USER_WORD = "passUserWord";

    /**
     * is_inint_syspass 是否初始化密码的标识位
     */
    public static final String IS_ININT_SYSPASS = "isInintSyspass";

    /**
     * 默认用户admin的默认值
     */
    public static final int DEFAULT_USER = 1;

    private static final long serialVersionUID = 1L;
    // 用户ID
    private int userId = -1;

    // 用户名
    private String userTblName;

    private String userType;

    // 用户密码
    private String passUserWord;

    private String fatherName;

    // 是否修改密码
    private int isInintSyspass = -1;

    // 盐值
    private String userSalt;

    // 维护组织ID
    private int mainId = -1;

    // 报表等级
    private int reportLevel = -1;

    // 是否为默认用户
    private int isDefault = -1;

    /**
     * 用户基本信息构建
     *
     * @param userTblName 用户名称
     * @param passUserWord 用户密码
     * @param isInintSyspass 是否修改过密码
     * @param userSalt 密码盐值
     */
    public UserPO(String userTblName, String passUserWord, int isInintSyspass, String userSalt) {
        super();
        this.userTblName = userTblName;
        this.passUserWord = passUserWord;
        this.isInintSyspass = isInintSyspass;
        this.userSalt = userSalt;
    }

    /**
     * 构造函数
     */
    public UserPO() {

    }
}
