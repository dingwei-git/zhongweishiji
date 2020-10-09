/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * CaptureHourStatistics
 *
 * @since 2019-08-06
 */
public class CaptureHourStatistics implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /*
    * 域编码
    * */
    private String domainCode;

    /*
     * 摄像机编码
     */
    private String cameraSn;

    /*
     * 开始小时点
     */
    private Date startHour;

    /*
     * 结束小时点
     */
    private Date endHour;

    /*
     * 摄像机名称
     */
    private String cameraName;

    /*
     * 过车、过脸标志，0为过车，1为过脸
     */
    private int type;

    /*
    * 大小图片类型，1小图2大图
    *
    * */
    private int picType;

    /*
    * 图片传输统计类型，1存储2转发
    * */
    private int tranType;

    /*
    * 来源1：vcm 2:vcn
    * */
    private int source;

    /*
     * 小时内抓拍图片数统计
     */
    private long pictureCount;

    /*
     * 是否完整小时标志
     */
    private int completeHour;

    /**
     * 摄像机分组
     */
    private String groupName;

    /**
     * 有效摄像机标志
     */
    private int effectiveCamera;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public int getPicType() {
        return picType;
    }

    public void setPicType(int picType) {
        this.picType = picType;
    }

    public int getTranType() {
        return tranType;
    }

    public void setTranType(int tranType) {
        this.tranType = tranType;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getEffectiveCamera() {
        return effectiveCamera;
    }

    public void setEffectiveCamera(int effectiveCamera) {
        this.effectiveCamera = effectiveCamera;
    }

    public String getCameraSn() {
        return cameraSn;
    }

    public void setCameraSn(String cameraSn) {
        this.cameraSn = cameraSn;
    }

    /**
     * 获取开始时间
     *
     * @return Date
     */
    public Date getStartHour() {
        if (startHour == null) {
            return null;
        }
        return (Date) startHour.clone();
    }

    /**
     * 对开始时间进行赋值
     *
     * @param startHour 开始时间
     */
    public void setStartHour(Date startHour) {
        if (startHour == null) {
            this.startHour = null;
        } else {
            this.startHour = (Date) startHour.clone();
        }
    }

    /**
     * 获取结束时间
     *
     * @return Date 结束时间
     */
    public Date getEndHour() {
        if (endHour == null) {
            return null;
        }
        return (Date) endHour.clone();
    }

    /**
     * 对结束时间进行赋值
     *
     * @param endHour 结束时间
     */
    public void setEndHour(Date endHour) {
        if (endHour == null) {
            this.endHour = null;
        } else {
            this.endHour = (Date) endHour.clone();
        }
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPictureCount() {
        return pictureCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setPictureCount(long pictureCount) {
        this.pictureCount = pictureCount;
    }

    public int getCompleteHour() {
        return completeHour;
    }

    public void setCompleteHour(int completeHour) {
        this.completeHour = completeHour;
    }
}
