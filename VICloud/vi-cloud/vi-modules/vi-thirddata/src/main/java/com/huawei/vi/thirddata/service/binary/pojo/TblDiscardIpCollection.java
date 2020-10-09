/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;

import java.io.Serializable;

/**
 * 无法获取网段配置的IP信息类
 *
 * @version 1.0, 2018年9月17日
 * @since 2018-09-17
 */
public class TblDiscardIpCollection implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 服务器IP;被叫ip:server_ip_addr;callee_ip
     */
    private String ip;

    /**
     * 服务器IP地理位置;被叫IP名称：server_ip_location;callee_ip_name
     */
    private String ipName;

    /**
     * 客户端IP;主叫IPclient_ip_addr;caller_ip
     */
    private String pip;

    /**
     * 主叫IP名称；客户端IP地理位置:caller_ip_name;client_ip_location
     */
    private String pipName;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpName() {
        return ipName;
    }

    public void setIpName(String ipName) {
        this.ipName = ipName;
    }

    public String getPip() {
        return pip;
    }

    public void setPip(String pip) {
        this.pip = pip;
    }

    public String getPipName() {
        return pipName;
    }

    public void setPipName(String pipName) {
        this.pipName = pipName;
    }
}
