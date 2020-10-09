/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

import javax.xml.bind.annotation.*;

/**
 * 获取二进制码参数配置
 *
 * @version 1.0, 2018年6月20日
 * @since 2019-09-19
 */
@XmlRootElement(name = "protocolConfig")
@XmlType(name = "protocolConfig", propOrder = {"parameterConfig", "titleConfig"})
@XmlAccessorType(XmlAccessType.FIELD)
public class ProtocolConfigInfo {
    @XmlElement(name = "parameterConfig")
    private ParameterConfig parameterConfig;

    @XmlElement(name = "titleConfig")
    private TitleConfig titleConfig;

    public ParameterConfig getParameterConfig() {
        return parameterConfig;
    }

    public void setParameterConfig(ParameterConfig parameterConfig) {
        this.parameterConfig = parameterConfig;
    }

    public TitleConfig getTitleConfig() {
        return titleConfig;
    }

    public void setTitleConfig(TitleConfig titleConfig) {
        this.titleConfig = titleConfig;
    }
}
