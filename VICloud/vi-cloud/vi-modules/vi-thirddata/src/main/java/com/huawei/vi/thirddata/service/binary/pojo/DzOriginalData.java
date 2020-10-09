/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 根据指定条件获取摄像机信令检测结果pojo
 *
 * @version 1.0, 2018年9月11日
 * @since 2018-09-11
 */
@Getter
@Setter
public class DzOriginalData implements Serializable {
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    private String cameraId;

    private String chName;

    private String rtpDelay;

    private String sipDelay;

    private String errorCode;

    private String ifRameDelay;

    private String dateTime;

    private String online;

    private double sceneChange;

    private double signalLost;

    private double colourCast;

    private double brightness;

    private double snow;

    private String pictureUrl;

    private double cover;

    private double stripe;

    private int id;

    private int detecitionStatus;

    private double ptzSpeed;

    private int connectedStaus;

    private double freeze;

    private double definition;

    /**
     * 画面异动
     */
    private double staticImage;

    /**
     * 抖动异常
     */
    private double imageJitter;

    /**
     * 镜头灰斑
     */
    private double dustyLens;

    private double indicia;

    private String latitude;

    private String longtitude;

    private String lwsx;

    private int deviceStatusOnline;

    private int recordIntegrity;

    private int cameraType;

    private String diagResultIndex;

    private String oldDiagResultIndex;

    private int diagnosisFlag;

    private String videoDateTime; // 摄像机论像巡检检测时间

    private int recordRule; // 录像规则

    private int recordSaveTime; // 录像保存时长；

    private String lostRecord; // 录像缺失时间数组；

    private String videoDiagResultIndex; // 录像巡检诊断批次号
}
