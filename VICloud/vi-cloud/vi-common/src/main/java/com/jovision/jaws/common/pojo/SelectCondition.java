/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.pojo;

import javax.xml.bind.annotation.*;

/**
 * automaticOrderConfig.xml中SelectCondition对应的实体类
 *
 * @since 2018-06-28
 */
@XmlRootElement
@XmlType(name = "selectConditon")
@XmlAccessorType(XmlAccessType.FIELD)
public class SelectCondition {
    /**
     * 设置逻辑条件
     */
    public static final String STATISTIC_TIME = "statisticTime";

    /**
     * 设置逻辑条件 出现次数 key
     */
    public static final String STATISTIC_NUMBER = "statisticNumber";

    @XmlAttribute(name = "statisticTime")
    private int statisticTime;

    @XmlAttribute(name = "statisticNumber")
    private int statisticNumber;

    public int getStatisticTime() {
        return statisticTime;
    }

    public void setStatisticTime(int statisticTime) {
        this.statisticTime = statisticTime;
    }

    public int getStatisticNumber() {
        return statisticNumber;
    }

    public void setStatisticNumber(int statisticNumber) {
        this.statisticNumber = statisticNumber;
    }
}
