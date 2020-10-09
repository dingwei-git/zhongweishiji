package com.jovision.jaws.common.config.jwt;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * tiken负载体
 * @Author: ABug
 * @Date: 2020/1/4 19:26
 * @Version V1.0.0
 **/
@Data
@AllArgsConstructor
public class TikenPayload {
    /**
     * 用户ID
     */
    private String user_id;
    /**
     * 用户名
     */
    private String user_name;

    /**
     * 用户登录标识
     */
    private String login_id;
    /**
     * 创建时间
     */
    private Long time;
    /**
     * 有效期
     */
    private Long validity_time;
}
