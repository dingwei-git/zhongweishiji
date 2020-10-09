/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.entity.model;

import java.io.Serializable;

/**
 * 作为一个用于设置一些配置信息的实体类，通过注入的方式设置具体的置
 */
public class ConfigBean implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 表示视频工具的主机名-ip
     */
    private String videoInsightHost;

    /**
     * 表示视频工具的端口
     */
    private String videoInsightPort;

    /**
     * fieldConfig.xml TCP CSV文件的总列数
     */
    private Integer allFieldNum4Tcp;

    /**
     * fieldConfig.xml UDP CSV文件的总列数
     */
    private Integer allFieldNum4Udp;

    /**
     * 采集周期时间
     */
    private Long periodTime;

    /**
     * CSV 文件路径
     */
    private String inputPath;

    /**
     * 表示是否是在线数据
     */
    private Boolean isOfflineAnalys;

    /**
     * 是否演示
     */
    private boolean isShow;

    /**
     * 无参构造
     */
    public ConfigBean() {
        inputPath = "";
    }

    public Integer getAllFieldNum4Tcp() {
        return allFieldNum4Tcp;
    }

    public void setAllFieldNum4Tcp(Integer allFieldNum4Tcp) {
        this.allFieldNum4Tcp = allFieldNum4Tcp;
    }

    public Integer getAllFieldNum4Udp() {
        return allFieldNum4Udp;
    }

    public void setAllFieldNum4Udp(Integer allFieldNum4Udp) {
        this.allFieldNum4Udp = allFieldNum4Udp;
    }

    public String getVideoInsightHost() {
        return videoInsightHost;
    }

    public void setVideoInsightHost(String videoInsightHost) {
        this.videoInsightHost = videoInsightHost;
    }

    public String getVideoInsightPort() {
        return videoInsightPort;
    }

    public void setVideoInsightPort(String videoInsightPort) {
        this.videoInsightPort = videoInsightPort;
    }

    public Long getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(Long periodTime) {
        this.periodTime = periodTime;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public Boolean getIsOfflineAnalys() {
        return isOfflineAnalys;
    }

    public void setIsOfflineAnalys(Boolean isOfflineAnalys) {
        this.isOfflineAnalys = isOfflineAnalys;
    }

    /**
     * 返回 isShow
     *
     * @return isShow值
     */
    public boolean getIsShow() {
        return isShow;
    }

    /**
     * 对isShow进行赋值
     *
     * @param isShow isShow值
     */
    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }
}
