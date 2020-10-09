/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.info;

import com.jovision.jaws.common.config.vi.AutomaticOrderConfig;
import com.jovision.jaws.common.config.vi.FileMoreConfig;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * xml配置类
 *
 * @version 1.0, 2018年7月30日
 * @since 2018-07-30
 */
@XmlRootElement(name = "baseCofig")
@XmlType(name = "baseCofig",
        propOrder = {"fileMoreConfig", "filterOrganization",
                "filterplatform", "udpFilterConfig", "connectionTypeConfig", "netWorkFieldConfig", "automaticOrderConfig"})
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseCofigInfo {
    /**
     * 端口过滤规则 key
     */
    public static final String RUL_ETEXT = "ruleText";

    /**
     * 所属机构平台筛选关键字 分局
     */
    public static final String ORGANIZATION = "organization";

    /**
     * 所属机构平台筛选关键字 派出所
     */
    public static final String PLATFORM = "platform";

    @XmlElement(name = "fileMoreConfig")
    private FileMoreConfig fileMoreConfig;

    @XmlElement(name = "udpFilterConfig")
    private String udpFilterConfig;

    @XmlElement(name = "connectionTypeConfig")
    private String connectionTypeConfig;

    @XmlElement(name = "netWorkFieldConfig")
    private String netWorkFieldConfig;

    @XmlElement(name = "filterOrganization")
    private String filterOrganization;

    @XmlElement(name = "filterplatform")
    private String filterplatform;

    @Getter
    @Setter
    @XmlElement(name = "automaticOrder")
    private AutomaticOrderConfig automaticOrderConfig;

    public String getFilterOrganization() {
        return filterOrganization;
    }

    public void setFilterOrganization(String filterOrganization) {
        this.filterOrganization = filterOrganization;
    }

    public String getFilterplatform() {
        return filterplatform;
    }

    public void setFilterplatform(String filterplatform) {
        this.filterplatform = filterplatform;
    }

    public FileMoreConfig getFileMoreConfig() {
        return fileMoreConfig;
    }

    public void setFileMoreConfig(FileMoreConfig fileMoreConfig) {
        this.fileMoreConfig = fileMoreConfig;
    }

    public String getUdpFilterConfig() {
        return udpFilterConfig;
    }

    public void setUdpFilterConfig(String udpFilterConfig) {
        this.udpFilterConfig = udpFilterConfig;
    }

    public String getConnectionTypeConfig() {
        return connectionTypeConfig;
    }

    public void setConnectionTypeConfig(String connectionTypeConfig) {
        this.connectionTypeConfig = connectionTypeConfig;
    }

    public String getNetWorkFieldConfig() {
        return netWorkFieldConfig;
    }

    public void setNetWorkFieldConfig(String netWorkFieldConfig) {
        this.netWorkFieldConfig = netWorkFieldConfig;
    }

}