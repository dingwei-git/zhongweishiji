/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * automaticOrderConfig.xml中faultTypes节点对应的实体类
 *
 * @since 2019-08-27
 */
@XmlType(name = "faultName")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class FaultName {
    @XmlAttribute(name = "name")
    private String name; // 故障名称.

    @XmlAttribute(name = "isState")
    private String isState; // 故障状态̬

    @XmlAttribute(name = "interfaceType")
    private String interfaceType; // 故障来源

    @XmlAttribute(name = "key")
    private String key;

    @XmlAttribute(name = "isServiceMode")
    private String isServiceMode;
}
