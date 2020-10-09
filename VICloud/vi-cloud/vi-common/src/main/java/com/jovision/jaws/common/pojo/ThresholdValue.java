/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 解析xml TO Beans
 *
 * @version 1.0, 2018年6月28日
 * @since 2018-06-28
 */
@Getter
@Setter
@XmlRootElement
@XmlType(name = "thresholdValue")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThresholdValue {
    /**
     * 故障时间占比门阀 key
     */
    public static final String FAULT_RATE = "faultRate";

    @XmlElement(name = "threshold")
    private List<ThresholdInfo> thresholdInfoList;

    @XmlAttribute(name = "faultRate")
    private int faultRate;
}
