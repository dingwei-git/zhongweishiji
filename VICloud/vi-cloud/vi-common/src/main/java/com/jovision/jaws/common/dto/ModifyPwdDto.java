package com.jovision.jaws.common.dto;

import lombok.Data;

/**
 * 修改密码
 *
 * @author xianfeng
 * @version 1.0
 * @date 2020/1/4 13:49
 */
@Data
public class ModifyPwdDto {
    /**
     * 原密码
     */
    private String password;
    /**
     * 新密码
     */
    private String new_password;

    private String check_password;
}
