/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.vi.login.po;

import lombok.Data;

/**
 * 前台资源控制实体
 *
 * @since 2020-05-25
 */
@Data
public class RoleRelationInfo {
    // 是否开启
    private int isOpen;

    // 权限id
    private int permissionId;

    // 权限名称
    private String permissionName;
}
