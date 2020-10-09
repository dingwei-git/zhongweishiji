package com.jovision.jaws.common.config.jwt;


import lombok.Data;

/**
 * token负载体
 * @Author: ABug
 * @Date: 2020/1/4 19:26
 * @Version V1.0.0
 **/
@Data
public class TokenPayload {
    /**
     * 用户ID
     */
    private String user_id;
}
