package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 验证码模板
 * 1=注册 2=忘记密码 3=登录 4=修改密码
 * @author ABug
 * @version 1.0
 * @date 2020/1/4 20:13
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UmsVerificationCodeTypeEnum {
    /**
     * 注册
     */
    REGISTER(1,"注册") ,
    /**
     * 忘记密码
     */
    FORGET_PWD(2,"忘记密码"),
    /**
     * 登录
     */
    LOGIN(3,"登录"),
    /**
     * 修改密码
     */
    MODIFY_PWD(4,"修改密码"),
    /**
     * 注销用户
     */
    CANCEL_USER(5,"注销用户");

    private int value;
    private String title;
}
