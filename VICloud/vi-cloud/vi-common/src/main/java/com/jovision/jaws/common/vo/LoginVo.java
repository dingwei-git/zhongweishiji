package com.jovision.jaws.common.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应报文描述
 * @Author: ABug
 * @Date: 2020/1/4 21:48
 * @Version V1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    /**
     * 用token令牌
     */
    private String token;
    /**
     * token有效时长
     */
    private Long token_expires_in;
    /**
     * 用户tiken令牌
     */
    private String tiken;
    /**
     * tiken有效时长
     */
    private Long tiken_expires_in;
    /**
     * 自研推送elb地址
     */
    private String selfPushElbUrl;
    /**
     * 新自研推送elb地址
     */
    private String newSelfPushElbUrl;
    /**
     * 重定向后的url
     */
    private String redirectAddress;

    /**
     * 剩余尝试次数
     */
    private Integer surplus_num;
    /**
     * 锁定时长(秒)
     */
    private Integer lock_duration;
    private Integer isInintSyspass;

    /**
     * 创建错误的登录响应报文
     * @param surplus_num
     * @param lock_duration
     * @return
     */
    public LoginVo(Integer surplus_num , Integer lock_duration){
        this.lock_duration = lock_duration;
        this.selfPushElbUrl = "";
        this.newSelfPushElbUrl = "";
        this.surplus_num = surplus_num;
        this.tiken = "";
        this.tiken_expires_in = 0L;
        this.token = "";
        this.token_expires_in = 0L;
    }


}
