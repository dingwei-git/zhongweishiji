/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

import javax.xml.bind.annotation.*;

/**
 * CSV生成配置文件
 *
 * @version 1.0, 2018年6月22日
 * @since 2019-09-19
 */
@XmlRootElement
@XmlType(name = "udpConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class TitleConfig {
    @XmlElement(name = "udpTitle")
    private String udpTitle;

    @XmlElement(name = "tcpTitle")
    private String tcpTitle;

    @XmlElement(name = "udpCsvCN")
    private String udpCsvCn;

    @XmlElement(name = "tcpCsvCN")
    private String tcpCsvCn;

    @XmlElement(name = "udpCsvUS")
    private String udpCsvUs;

    @XmlElement(name = "tcpCsvUS")
    private String tcpCsvUs;

    @XmlElement(name = "originalTableName")
    private String originalTableName;

    public String getOriginalTableName() {
        return originalTableName;
    }

    public void setOriginalTableName(String originalTableName) {
        this.originalTableName = originalTableName;
    }

    public String getUdpTitle() {
        return udpTitle;
    }

    public void setUdpTitle(String udpTitle) {
        this.udpTitle = udpTitle;
    }

    public String getTcpTitle() {
        return tcpTitle;
    }

    public void setTcpTitle(String tcpTitle) {
        this.tcpTitle = tcpTitle;
    }

    public String getUdpCsvCn() {
        return udpCsvCn;
    }

    public void setUdpCsvCn(String udpCsvCn) {
        this.udpCsvCn = udpCsvCn;
    }

    public String getTcpCsvCn() {
        return tcpCsvCn;
    }

    public void setTcpCsvCn(String tcpCsvCn) {
        this.tcpCsvCn = tcpCsvCn;
    }

    public String getUdpCsvUs() {
        return udpCsvUs;
    }

    public void setUdpCsvUs(String udpCsvUs) {
        this.udpCsvUs = udpCsvUs;
    }

    public String getTcpCsvUs() {
        return tcpCsvUs;
    }

    public void setTcpCsvUs(String tcpCsvUs) {
        this.tcpCsvUs = tcpCsvUs;
    }
}
