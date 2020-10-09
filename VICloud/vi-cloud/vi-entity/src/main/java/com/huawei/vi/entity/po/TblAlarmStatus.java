/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.vi.entity.po;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * 告警状态实体
 *
 * @since 2020-01-21
 */
@Getter
@Setter
@Data
@XmlType(name = "alarms")
@XmlAccessorType(XmlAccessType.FIELD)
public class TblAlarmStatus implements Serializable {
    /**
     * 自动恢复
     */
    public static final int AUTO_RECOVERY = 1;

    /**
     * 未恢复
     */
    public static final int NOT_RECOVERY = 2;

    /**
     * 手动恢复
     */
    public static final int HAND_RECOVERY = 3;

    private static final long serialVersionUID = -6726739816754574938L;

    @XmlAttribute(name = "id")
    private int id; // 告警状态id

    @XmlAttribute(name = "status")
    private String status; // 告警状态名称
}