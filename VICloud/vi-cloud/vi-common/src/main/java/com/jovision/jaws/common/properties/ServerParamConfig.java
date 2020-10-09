
package com.jovision.jaws.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * 图像诊断服务器参数
 *
 */

@Getter
@Setter
public class ServerParamConfig {
    /**
     * dz总控程序端口
     */
    private int dzPort;

    /**
     * 消息服务器Id
     */
    private int mqId;

    /**
     * 消息服务器端口
     */
    private int mqPort;

    /**
     * mq连接的用户名
     */
    private String mqUser;

    /**
     * mq连接的密码
     */
    private String mqPwd;

    /**
     * 文件服务器1Id
     */
    private int fileServerIp1Id;

    /**
     * 文件服务器1端口
     */
    private int fileServerIp1Port;

    /**
     * 文件服务器1类型
     */
    private String fileServerIp1Type;

    /**
     * 诊断服务器Id
     */
    private int diagId;

    /**
     * 诊断服务器端口
     */
    private int diagPort;

    /**
     * 诊断服务器用户名
     */
    private String diagUser;

    /**
     * 诊断服务器密码
     */
    private String diagPwd;

    /**
     * 采集服务器Id
     */
    private int collectId;

    /**
     * 采集服务器端口
     */
    private int collectPort;

    /**
     * 采集服务器用户名
     */
    private String collectUser;

    /**
     * 采集服务器密码
     */
    private String collectPwd;

    /**
     * 中间件1Id
     */
    private int midIp1Id;

    /**
     * 中间件1端口
     */
    private int midIp1Port;

    /**
     * 中间件1Http端口
     */
    private int midIp1HttpPort;

    /**
     * 中间件1Tcp端口
     */
    private int midIp1TcpPort;

    /**
     * 视频监控平台Id
     */
    private int platformId;
}
