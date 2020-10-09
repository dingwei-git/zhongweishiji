package com.jovision.jaws.common.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 未添加类说明
 * @Author: ABug
 * @Date: 2020/1/6 09:09
 * @Version V1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenewalTokenVo {
    /**
     * 用户令牌
     */
    private String token;
    /**
     * token有效时长
     */
    private Long token_expires_in;
//    /**
//     * 重定向url
//     */
//    private String redirectAddress;
}
