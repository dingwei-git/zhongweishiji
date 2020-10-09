/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;

import com.huawei.utils.CommonUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 周期管理vo层
 *
 * @version 1.0, 2018年7月2日
 * @since 2018-07-02
 */
public class TblPeriodManage implements Serializable {
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5855495714667508355L;

    /**
     * 周期时间
     */
    private Date periodStartTime;

    /**
     * 是否分析完成
     */
    private String isAnalyseOver;

    /**
     * 获取周期get方法
     *
     * @return Date
     */
    public Date getPeriodStartTime() {
        if (periodStartTime != null) {
            return (Date) periodStartTime.clone();
        }
        return null;
    }

    /**
     * 获取周期set方法
     *
     * @param periodStartTime 周期时间
     */
    public void setPeriodStartTime(Date periodStartTime) {
        if (CommonUtil.isNull(periodStartTime)) {
            this.periodStartTime = null;
        } else {
            this.periodStartTime = (Date) periodStartTime.clone();
        }
    }

    public String getIsAnalyseOver() {
        return isAnalyseOver;
    }

    public void setIsAnalyseOver(String isAnalyseOver) {
        this.isAnalyseOver = isAnalyseOver;
    }
}