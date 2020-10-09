/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.vi.login.po;

import com.huawei.utils.NumConstant;
import lombok.Data;

/**
 * 用户管理界面用户返回实体
 *
 * @since 2020-05-13
 */
@Data
public class UserInfo {
    // 用户编号
    private int userId = NumConstant.NUM_1_NEGATIVE;

    // 用户名
    private String userTblName;

    // 组织Id
    private int mainId = NumConstant.NUM_1_NEGATIVE;

    // 组织名
    private String maintenanceOrganization;

    // 角色Id
    private int roleId = NumConstant.NUM_1_NEGATIVE;

    // 角色名称
    private String roleName;

    // 是否默认
    private int isDefault = NumConstant.NUM_1_NEGATIVE;

    // 用户密码
    private String passUserWord;

    // 报表层级
    private int reportLevel = NumConstant.NUM_1_NEGATIVE;

    // 报表层级名称
    private String reportLevelName;

    // 用户拥有ipc列表
    private String[] ipList;

    // 该用户是否为本人
    private boolean isMySelf;

    // 是否初始化密码
    private int isInintSyspass;
}
