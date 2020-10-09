/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.entity.po;

import javax.xml.bind.annotation.*;

/**
 * xml配置类
 *
 * @version 1.0, 2018年7月30日
 * @since 2018-07-30
 */
@XmlRootElement(name = "baseCofig")
@XmlType(name = "baseCofig", propOrder = {"udpFilterConfig", "udpFilterConfigEn"})
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseCofigInfo {
    @XmlElement(name = "udpFilterConfig")
    private String udpFilterConfig;

    @XmlElement(name = "udpFilterConfigEn")
    private String udpFilterConfigEn;

    public String getUdpFilterConfig() {
        return udpFilterConfig;
    }

    public void setUdpFilterConfig(String udpFilterConfig) {
        this.udpFilterConfig = udpFilterConfig;
    }

    public String getUdpFilterConfigEn() {
        return udpFilterConfigEn;
    }

    public void setUdpFilterConfigEn(String udpFilterConfigEn) {
        this.udpFilterConfigEn = udpFilterConfigEn;
    }
}