/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.entity.po;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 告警等级实体
 *
 * @since 2019-12-26
 */
@Data
@XmlType(name = "alarm")
@XmlAccessorType(XmlAccessType.FIELD)
public class TblAlarmLevel implements Serializable {
    private static final long serialVersionUID = 5473604235479802580L;

    @XmlAttribute(name = "id")
    private int id; // 告警id PK

    @XmlAttribute(name = "name")
    private String name; // 告警等级名称

    @XmlAttribute(name = "color")
    private String color; // 告警等级名称

    /**
     * 是否立即上报 默认立即上报
     */
    private boolean isNow = true;

    /**
     * M小时重复告警不上报
     */
    private int howLong;
}