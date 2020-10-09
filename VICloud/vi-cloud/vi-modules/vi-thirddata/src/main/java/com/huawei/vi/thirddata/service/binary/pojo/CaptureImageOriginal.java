/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * CaptureHourStatistics
 *
 * @since 2019-08-06
 */
@Getter
@Setter
public class CaptureImageOriginal implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /*
     * 摄像机编码
     */
    private String cameraSn;

    /*
     * 周期开始时间
     */
    private Date startTime;

    /*
     * 周期结束时间
     */
    private Date endTime;

    /*
     * 摄像机名称
     */
    private String cameraName;

    /*
     * 过车、过脸标志，0为过车，1为过脸
     */
    private int type;

    /*
     * 抓拍图片数量
     */
    private int pictureCount;

    /*
     * 是否有效摄像机标志
     */
    private int effectiveCamera;

    /**
     * 获取开始时间
     *
     * @return Date 开始时间
     */
    public Date getStartTime() {
        return startTime == null ? null : (Date) startTime.clone();
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = (startTime == null) ? null : (Date) startTime.clone();
    }

    /**
     * 获取结束时间
     *
     * @return Date 结束时间
     */
    public Date getEndTime() {
        return endTime == null ? null : (Date) endTime.clone();
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = (endTime == null) ? null : (Date) endTime.clone();
    }
}
