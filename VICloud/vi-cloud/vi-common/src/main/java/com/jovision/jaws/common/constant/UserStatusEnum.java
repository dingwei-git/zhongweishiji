package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 日志类型
 * @author ABug
 * @version 1.0
 * @date 2020/1/4 20:13
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserStatusEnum {
    /**
     * 登录
     */
    LOGIN(1,"登录") ,
    /**
     * 登出
     */
    LOGOUT(0,"登出");

    private Integer value;
    private String title;
}
