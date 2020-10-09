/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;


import com.huawei.utils.StringUtil;
import com.huawei.vi.entity.vo.ProgressInfo;
import com.jovision.jaws.common.util.ProtocolConfig;
import com.jovision.jaws.common.util.UtilMethod;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * UdpJsonBean
 *
 * @version 1.0, 2018年6月22日
 * @since 2019-09-19
 */
public class UdpJsonBean {
    /**
     * 语言
     */
    private static String languType = "zh-CN";

    /**
     * 时间单位转换
     */
    private final Long timeNum = 1000000000L;

    /**
     * 16进制
     */
    private final Integer sixteen = 16;

    private String timeKey;

    private String callerIpName;

    private String calleeIpName;

    private String callerPort;

    private String calleeIp;

    private String calleePort;

    private String callerIp;

    private String callId;

    private String flowType;

    private String durationTime;

    private String avgLostPacketRate;

    private String totalCodeBitps;

    private String videoMosAvg;

    private String flowStartTime;

    private String flowEndTime;

    private String protocol;

    private String ssrc;

    private String jitterAvg;

    private String linkId;

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getTimeKey() {
        return timeKey;
    }

    public void setTimeKey(String timeKey) {
        this.timeKey = timeKey;
    }

    public String getJitterAvg() {
        return jitterAvg;
    }

    public void setJitterAvg(String jitterAvg) {
        this.jitterAvg = jitterAvg;
    }

    public String getSsrc() {
        return ssrc;
    }

    /**
     * null值改为--
     *
     * @param ssrc 标志
     */
    public void setSsrc(String ssrc) {
        if (!StringUtil.isNull(ssrc)) {
            BigInteger bigInteger = new BigInteger(ssrc);
            this.ssrc = "0x" + bigInteger.toString(sixteen);
        } else {
            this.ssrc = "--";
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCallerIpName() {
        return callerIpName;
    }

    public void setCallerIpName(String callerIpName) {
        this.callerIpName = callerIpName;
    }

    public String getCalleeIpName() {
        return calleeIpName;
    }

    public void setCalleeIpName(String calleeIpName) {
        this.calleeIpName = calleeIpName;
    }

    public String getCalleeIp() {
        return calleeIp;
    }

    public void setCalleeIp(String calleeIp) {
        this.calleeIp = calleeIp;
    }

    public String getCalleePort() {
        return calleePort;
    }

    public void setCalleePort(String calleePort) {
        this.calleePort = calleePort;
    }

    public String getCallerIp() {
        return callerIp;
    }

    public void setCallerIp(String callerIp) {
        this.callerIp = callerIp;
    }

    public String getCallId() {
        return callId;
    }

    /**
     * 科学计数法转化
     *
     * @param callId 主叫id
     */
    public void setCallId(String callId) {
        BigDecimal bd = new BigDecimal(callId);
        this.callId = bd.toPlainString();
    }

    public String getFlowType() {
        return flowType;
    }

    /**
     * 相应的数字编码对应的流类型进行转化
     *
     * @param flowType 流类型
     */
    public void setFlowType(String flowType) {
        String flowTypeTemp = "";
        switch (flowType) {
            case "1":
                flowTypeTemp =
                    languType.equals(ProgressInfo.getLanguageTyle()) ? ProtocolConfig.getGlobalMap().get("stpStreamCn")
                        : ProtocolConfig.getGlobalMap().get("stpStreamEn");
                break;
            case "2":
            case "4":
                flowTypeTemp = languType.equals(ProgressInfo.getLanguageTyle())
                    ? ProtocolConfig.getGlobalMap().get("videoStreamCn")
                    : ProtocolConfig.getGlobalMap().get("videoStreamEn");
                break;
            case "3":
            case "5":
                flowTypeTemp = languType.equals(ProgressInfo.getLanguageTyle())
                    ? ProtocolConfig.getGlobalMap().get("audioStreamCn")
                    : ProtocolConfig.getGlobalMap().get("audioStreamEn");
                break;
            case "6":
                flowTypeTemp =
                    languType.equals(ProgressInfo.getLanguageTyle()) ? ProtocolConfig.getGlobalMap().get("h245StreamCn")
                        : ProtocolConfig.getGlobalMap().get("h245StreamEn");
                break;
            case "7":
                flowTypeTemp = "H225Q931";
                break;
            case "8":
                flowTypeTemp = " H225RAS";
                break;
            case "9":
                flowTypeTemp = "RTCP";
                break;
            default:
                break;
        }
        this.flowType = flowTypeTemp;
    }

    public String getDurationTime() {
        return durationTime;
    }

    /**
     * 时间单位转化
     *
     * @param durationTime 持续时长
     */
    public void setDurationTime(String durationTime) {
        BigDecimal bd = new BigDecimal(durationTime);
        Long dt = Long.parseLong(bd.toPlainString()) / timeNum;
        this.durationTime = dt.toString();
    }

    public String getAvgLostPacketRate() {
        return avgLostPacketRate;
    }

    /**
     * 小数转化为百分比
     *
     * @param avgLostPacketRate 平均丢失包
     */
    public void setAvgLostPacketRate(String avgLostPacketRate) {
        BigDecimal bd = new BigDecimal(avgLostPacketRate);
        this.avgLostPacketRate = bd.toPlainString();
    }

    public String getTotalCodeBitps() {
        return totalCodeBitps;
    }

    /**
     * 科学计数法转化
     *
     * @param totalCodeBitps 码率
     */
    public void setTotalCodeBitps(String totalCodeBitps) {
        BigDecimal bd = new BigDecimal(totalCodeBitps);
        this.totalCodeBitps = bd.toString();
    }

    public String getVideoMosAvg() {
        return videoMosAvg;
    }

    public void setVideoMosAvg(String videoMosAvg) {
        this.videoMosAvg = videoMosAvg;
    }

    public String getFlowStartTime() {
        return flowStartTime;
    }

    /**
     * 开始时间
     *
     * @param flowStartTime 开始时间
     */
    public void setFlowStartTime(String flowStartTime) {
        String fendTime = flowStartTime + "000"; // 结束时间
        String flet = com.jovision.jaws.common.util.UtilMethod.parseStringNumberToStringDate(fendTime);
        this.flowStartTime = flet;
    }

    public String getFlowEndTime() {
        return flowEndTime;
    }

    /**
     * 时间转化为字符串
     *
     * @param flowEndTime 结束时间
     */
    public void setFlowEndTime(String flowEndTime) {
        String fendTime = flowEndTime + "000"; // 结束时间
        String flet = UtilMethod.parseStringNumberToStringDate(fendTime);
        this.flowEndTime = flet;
    }

    public String getCallerPort() {
        return callerPort;
    }

    public void setCallerPort(String callerPort) {
        this.callerPort = callerPort;
    }
}
