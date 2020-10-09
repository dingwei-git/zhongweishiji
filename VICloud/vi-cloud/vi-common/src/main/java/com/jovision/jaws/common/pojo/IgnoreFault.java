/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

/**
 * automaticOrderConfig.xml中ignoreFault对应的实体类
 *
 * @since 2018-06-28
 */
@XmlRootElement
@XmlType(name = "ignoreFault")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class IgnoreFault {
    /**
     * 其他参数设置 设置忽略故障时长 key
     */
    public static final String IGNORE_TIME = "ignoreTime";

    /**
     * 其他参数设置 设置忽略故障时长
     */
    public static final int MAX_HOUR_LIMIT = 720;

    @XmlAttribute(name = "ignoreTime")
    private int ignoreTime;
}
