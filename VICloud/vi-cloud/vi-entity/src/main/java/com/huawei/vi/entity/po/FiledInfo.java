/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.entity.po;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * xml配置类
 *
 * @version 1.0, 2018年7月30日
 * @since 2018-07-30
 */
@Setter
@Getter
@XmlType(name = "filed")
@XmlAccessorType(XmlAccessType.FIELD)
public class FiledInfo {
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "chineseName")
    private String chineseName;

    @XmlAttribute(name = "dataName")
    private String dataName;

    @XmlAttribute(name = "paramType")
    private String paramType;

    @XmlAttribute(name = "dbtype")
    private String dbtype;

    @XmlAttribute(name = "ref")
    private String ref;

    @XmlAttribute(name = "handler")
    private String handler;

    @XmlAttribute(name = "rule")
    private String rule;
}
