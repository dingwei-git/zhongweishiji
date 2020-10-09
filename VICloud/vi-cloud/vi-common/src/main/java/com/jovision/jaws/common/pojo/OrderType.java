/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * automaticOrderConfig.xml中order节点对应的实体类
 *
 * @since 2018-06-28
 */
@XmlType(name = "orderType")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderType {
    @XmlAttribute(name = "key")
    private String key;

    @XmlAttribute(name = "isSelected")
    private String isSelected;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
