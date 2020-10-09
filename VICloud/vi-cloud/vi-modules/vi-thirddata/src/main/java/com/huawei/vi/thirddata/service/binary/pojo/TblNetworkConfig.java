/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;

import java.io.Serializable;

/**
 * 网段配置实体类
 *
 * @version 1.0, 2018年7月2日
 * @since 2018-07-02
 */
public class TblNetworkConfig implements Serializable {
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1293381611114012945L;

    private String name;

    private String location;

    private String type;

    private String networkRules;

    private String networkTotalBroadband;

    private String networkInBroadband;

    private String networkOutBroadband;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNetworkRules() {
        return networkRules;
    }

    public void setNetworkRules(String networkRules) {
        this.networkRules = networkRules;
    }

    public String getNetworkTotalBroadband() {
        return networkTotalBroadband;
    }

    public void setNetworkTotalBroadband(String networkTotalBroadband) {
        this.networkTotalBroadband = networkTotalBroadband;
    }

    public String getNetworkInBroadband() {
        return networkInBroadband;
    }

    public void setNetworkInBroadband(String networkInBroadband) {
        this.networkInBroadband = networkInBroadband;
    }

    public String getNetworkOutBroadband() {
        return networkOutBroadband;
    }

    public void setNetworkOutBroadband(String networkOutBroadband) {
        this.networkOutBroadband = networkOutBroadband;
    }
}