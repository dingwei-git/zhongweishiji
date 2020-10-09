package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * apollo可变配置
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ChangeConfigEnum {
    /**
     * 新用户端jwt秘钥
     */
    JWT_TOKEN_SECRET_CURRENT("jwt.token.secret.current") ,
    /**
     * 新接口端jwt秘钥
     */
    JWT_SAAS_SECRET_CURRENT("jwt.saas.secret.current"),
    /**
     * 新tiken jwt秘钥
     */
    JWT_TIKEN_SECRET_CURRENT("jwt.tiken.secret.current"),
    /**
     * 旧用户端jwt秘钥
     */
    JWT_TOKEN_SECRET_OLD("jwt.token.secret.old") ,
    /**
     * 旧接口端jwt秘钥
     */
    JWT_SAAS_SECRET_OLD("jwt.saas.secret.old"),
    /**
     * 旧tiken jwt秘钥
     */
    JWT_TIKEN_SECRET_OLD("jwt.tiken.secret.old");

    private String title;
}
