/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * 解析XML TO Beans
 *
 * @version 1.0, 2018年6月28日
 * @since 2018-06-28
 */
@Setter
@Getter
@XmlType(name = "threshold")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThresholdInfo {
    /**
     * 摄像机在线率 green key
     */
    public static final String GREEN_CAMERA_ONLINE_RATE = "greenCameraOnlineRate";

    /**
     * 摄像机在线率 yellow key
     */
    public static final String YELLOW_CAMERA_ONLINE_RATE = "yellowCameraOnlineRate";

    /**
     * 视频记录完整率 green key
     */
    public static final String GREEN_VIDEO_ONLINE_RATE = "greenVideoOnlineRate";

    /**
     * 视频记录完整率 yellow key
     */
    public static final String YELLOW_VIDEO_ONLINE_RATE = "yellowVideoOnlineRate";

    /**
     * 视频图像完好率 green key
     */
    public static final String GREEN_OK_RATE = "greenOkRate";

    /**
     * 视频图像完好率 yellow key
     */
    public static final String YELLOW_OK_RATE = "yellowOkRate";

    @XmlAttribute(name = "key")
    private String cite;

    @XmlAttribute(name = "cameraOnlineRate")
    private int cameraOnlineRate;

    @XmlAttribute(name = "videoOnlineRate")
    private int videoOnlineRate;

    @XmlAttribute(name = "videoOkRate")
    private int videoOkRate;

    /**
     * 获取不同阀值对应的颜色
     *
     * @return String
     */
    public String getKey() {
        return cite;
    }

    /**
     * 设置不同阀值对应的颜色
     *
     * @param key 颜色字符串
     */
    public void setKey(String key) {
        cite = key;
    }
}
