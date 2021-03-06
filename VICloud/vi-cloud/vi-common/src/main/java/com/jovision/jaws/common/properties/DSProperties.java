package com.jovision.jaws.common.properties;

import lombok.Data;

/**
 * @program: jaws
 * @description: 数据库配置类
 * @author: pxh
 * @create: 2020-07-06 17:33
 */
@Data
public class DSProperties {
    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private Integer initialSize;
    private Integer minIdle;
    private Integer maxActive;
    private Integer maxWait;
    private Integer timeBetweenEvictionRunsMillis;
    private Integer minEvictableIdleTimeMillis;
    //数据库服务宕机自动重连机制 需要重连设置为false
    private Boolean breakAfterAcquireFailure;
    //连接出错后再尝试连接次数
    private Integer connectionErrorRetryAttempts;
    private String validationQuery;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private Boolean poolPreparedStatements;
    private Integer maxOpenPreparedStatements;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String filters;
    private String publicKey;
    private String connectionProperties;

}
