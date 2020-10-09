package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用户角色
 * 0-普通用户,1-管理员
 * @author xianfeng
 * @version 1.0
 * @date 2020/1/4 20:13
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UmsUserRoleEnum {
    /**
     * 普通用户
     */
    GENERAL_USER(0,"普通用户") ,
    /**
     * 管理员
     */
    ADMIN_USER(1,"管理员");

    private int value;
    private String title;
}
