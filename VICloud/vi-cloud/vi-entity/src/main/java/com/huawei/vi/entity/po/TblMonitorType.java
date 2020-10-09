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
 * 监控类型实体
 *
 * @since 2020-01-21
 */
@Getter
@Setter
@Data
@XmlType(name = "monitorType")
@XmlAccessorType(XmlAccessType.FIELD)
public class TblMonitorType implements Serializable {
    private static final long serialVersionUID = 8725735831241262244L;

    @XmlAttribute(name = "id")
    private int id; // 监控类型id

    @XmlAttribute(name = "type")
    private String type; // 监控类型名称
}
