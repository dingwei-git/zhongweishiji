package com.jovision.jaws.common.dto;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


/**
 * 用户登录报文描述
 * @Author: ABug
 * @Date: 2020/1/4 21:44
 * @Version V1.0.0
 **/
@Data
public class LoginDto {
    /**
     * 登录账号 ; 邮箱或手机号
     * 长度校验
     */
    @NotBlank(message ="登录账号不能为空")
    @Length(max = 50 , min = 4)
    private String account;
    /**
     * 登录密码 (明文密码)
     */
    //    @CharLength(max = 30)
    //@NotBlank(message = "登录密码不能为空")
    private String password;

}
