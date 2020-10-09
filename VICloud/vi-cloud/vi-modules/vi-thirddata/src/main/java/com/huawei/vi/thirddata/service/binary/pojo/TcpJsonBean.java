/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;

import com.huawei.vi.entity.vo.ProgressInfo;
import com.jovision.jaws.common.util.ProtocolConfig;
import com.jovision.jaws.common.util.UtilMethod;

import java.math.BigDecimal;

/**
 * TcpJsonBean
 *
 * @version 1.0, 2018年6月22日
 * @since 2019-09-19
 */
public class TcpJsonBean {
    /**
     * 语言中文
     */
    private static String languType = "zh-CN";

    /**
     * 时间单位转换
     */
    private final Long timeNum = 1000000000L;

    private String timeKey;

    private String clientIpLocation;

    private String serverIpLocation;

    private String clientIpAddr;

    private String clientPort;

    private String serverIpAddr;

    private String serverPort;

    private String protocol;

    private String applicationId;

    private String tcpStatus;

    private String totalPacket;

    private String serverTotalPacket;

    private String clientTotalPacket;

    private String flowStartTime;

    private String flowEndTime;

    private String flowDuration;

    private String totalBitps;

    private String clientBitps;

    private String serverBitps;

    private String clientTcpRetransmissionRate;

    private String serverTcpRetransmissionRate;

    private String clientTcpSegmentLostPacket;

    private String serverTcpSegmentLostPacket;

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

    public String getTotalPacket() {
        return totalPacket;
    }

    /**
     * 修改源数据中的科学计数法
     *
     * @param totalPacket 总丢包
     */
    public void setTotalPacket(String totalPacket) {
        BigDecimal bd = new BigDecimal(totalPacket);
        this.totalPacket = bd.toPlainString();
    }

    public String getServerTotalPacket() {
        return serverTotalPacket;
    }

    /**
     * 源数据科学技术法转换
     *
     * @param serverTotalPacket 目标IP地址数据包数
     */
    public void setServerTotalPacket(String serverTotalPacket) {
        BigDecimal bd = new BigDecimal(serverTotalPacket);
        this.serverTotalPacket = bd.toPlainString();
    }

    public String getClientTotalPacket() {
        return clientTotalPacket;
    }

    /**
     * 源数据科学技术法转换
     *
     * @param clientTotalPacket 源IP地址数据包数
     */
    public void setClientTotalPacket(String clientTotalPacket) {
        BigDecimal bd = new BigDecimal(clientTotalPacket);
        this.clientTotalPacket = bd.toPlainString();
    }

    public String getClientIpLocation() {
        return clientIpLocation;
    }

    public void setClientIpLocation(String clientIpLocation) {
        this.clientIpLocation = clientIpLocation;
    }

    public String getServerIpLocation() {
        return serverIpLocation;
    }

    public void setServerIpLocation(String serverIpLocation) {
        this.serverIpLocation = serverIpLocation;
    }

    public String getClientIpAddr() {
        return clientIpAddr;
    }

    public void setClientIpAddr(String clientIpAddr) {
        this.clientIpAddr = clientIpAddr;
    }

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }

    public String getServerIpAddr() {
        return serverIpAddr;
    }

    public void setServerIpAddr(String serverIpAddr) {
        this.serverIpAddr = serverIpAddr;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getApplicationId() {
        return applicationId;
    }

    /**
     * 将应用id转化为Multimedia
     *
     * @param applicationId 应用id
     */
    public void setApplicationId(String applicationId) {
        String applicationIdTemp = "";
        if ("1958".equals(applicationId)) {
            applicationIdTemp = "Multimedia";
        } else {
            applicationIdTemp = languType.equals(ProgressInfo.getLanguageTyle())
                ? ProtocolConfig.getGlobalMap().get("unknownApplicationCn")
                : ProtocolConfig.getGlobalMap().get("unknownApplicationEn");
        }
        this.applicationId = applicationIdTemp;
    }

    public String getTcpStatus() {
        return tcpStatus;
    }

    /**
     * 将连接状态对应的编码改为相应的中文描述
     *
     * @param tcpStatus 连接状态
     */
    public void setTcpStatus(String tcpStatus) {
        String tcpStatusTemp = "";
        switch (tcpStatus) {
            case "0":
                tcpStatusTemp = languType.equals(ProgressInfo.getLanguageTyle())
                    ? ProtocolConfig.getGlobalMap().get("unknownCn") : ProtocolConfig.getGlobalMap().get("unknownEn");
                break;
            case "1":
                tcpStatusTemp = languType.equals(ProgressInfo.getLanguageTyle())
                    ? ProtocolConfig.getGlobalMap().get("synchronizationPackageSentCn")
                    : ProtocolConfig.getGlobalMap().get("synchronizationPackageSentEn");
                break;
            case "2":
                tcpStatusTemp = languType.equals(ProgressInfo.getLanguageTyle())
                    ? ProtocolConfig.getGlobalMap().get("synchronizationPackageReceivedCn")
                    : ProtocolConfig.getGlobalMap().get("synchronizationPackageReceivedEn");
                break;
            case "3":
                tcpStatusTemp = languType.equals(ProgressInfo.getLanguageTyle())
                    ? ProtocolConfig.getGlobalMap().get("connectionEstablishedCn")
                    : ProtocolConfig.getGlobalMap().get("connectionEstablishedEn");
                break;
            case "4":
                tcpStatusTemp = languType.equals(ProgressInfo.getLanguageTyle())
                    ? ProtocolConfig.getGlobalMap().get("connectionResetCn")
                    : ProtocolConfig.getGlobalMap().get("connectionResetEn");
                break;
            case "5":
                tcpStatusTemp = languType.equals(ProgressInfo.getLanguageTyle())
                    ? ProtocolConfig.getGlobalMap().get("closingCn") : ProtocolConfig.getGlobalMap().get("closingEn");
                break;
            case "6":
                tcpStatusTemp = languType.equals(ProgressInfo.getLanguageTyle())
                    ? ProtocolConfig.getGlobalMap().get("timerTimeoutCn")
                    : ProtocolConfig.getGlobalMap().get("timerTimeoutEn");
                break;
            case "7":
                tcpStatusTemp = languType.equals(ProgressInfo.getLanguageTyle())
                    ? ProtocolConfig.getGlobalMap().get("closedCn") : ProtocolConfig.getGlobalMap().get("closedEn");
                break;
            default:
                break;
        }
        this.tcpStatus = tcpStatusTemp;
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
        String fet = flowStartTime + "000"; // 结束时间
        String floet = com.jovision.jaws.common.util.UtilMethod.parseStringNumberToStringDate(fet);
        this.flowStartTime = floet;
    }

    public String getFlowEndTime() {
        return flowEndTime;
    }

    /**
     * 将结束时间转化为String格式
     *
     * @param flowEndTime 结束时间
     */
    public void setFlowEndTime(String flowEndTime) {
        String fet = flowEndTime + "000"; // 结束时间
        String floet = UtilMethod.parseStringNumberToStringDate(fet);
        this.flowEndTime = floet;
    }

    public String getFlowDuration() {
        return flowDuration;
    }

    /**
     * 时间单位转化
     *
     * @param flowDuration 持续时长
     */
    public void setFlowDuration(String flowDuration) {
        BigDecimal bd = new BigDecimal(flowDuration);
        Long fd = Long.parseLong(bd.toPlainString()) / timeNum;
        this.flowDuration = fd.toString();
    }

    public String getTotalBitps() {
        return totalBitps;
    }

    /**
     * 科学计数法转化
     *
     * @param totalBitps 总bitps
     */
    public void setTotalBitps(String totalBitps) {
        BigDecimal bd = new BigDecimal(totalBitps);
        this.totalBitps = bd.toPlainString();
    }

    public String getClientBitps() {
        return clientBitps;
    }

    /**
     * 科学计数法转化
     *
     * @param clientBitps 客户端bitps
     */
    public void setClientBitps(String clientBitps) {
        BigDecimal bd = new BigDecimal(clientBitps);
        this.clientBitps = bd.toPlainString();
    }

    public String getServerBitps() {
        return serverBitps;
    }

    /**
     * 科学计数法转化
     *
     * @param serverBitps 服务器端bitps
     */
    public void setServerBitps(String serverBitps) {
        BigDecimal bd = new BigDecimal(serverBitps);
        this.serverBitps = bd.toPlainString();
    }

    public String getClientTcpRetransmissionRate() {
        return clientTcpRetransmissionRate;
    }

    /**
     * 小数转化为%
     *
     * @param clientTcpRetransmissionRate 客户端tcp重传率
     */
    public void setClientTcpRetransmissionRate(String clientTcpRetransmissionRate) {
        BigDecimal bd = new BigDecimal(clientTcpRetransmissionRate);
        this.clientTcpRetransmissionRate = bd.toPlainString();
    }

    public String getServerTcpRetransmissionRate() {
        return serverTcpRetransmissionRate;
    }

    /**
     * 科学计数法转化
     *
     * @param serverTcpRetransmissionRate 服务器TCP重传率
     */
    public void setServerTcpRetransmissionRate(String serverTcpRetransmissionRate) {
        BigDecimal bd = new BigDecimal(serverTcpRetransmissionRate);
        this.serverTcpRetransmissionRate = bd.toPlainString();
    }

    public String getClientTcpSegmentLostPacket() {
        return clientTcpSegmentLostPacket;
    }

    /**
     * 科学计数法转化
     *
     * @param clientTcpSegmentLostPacket 客户端TCP重传率
     */
    public void setClientTcpSegmentLostPacket(String clientTcpSegmentLostPacket) {
        BigDecimal bd = new BigDecimal(clientTcpSegmentLostPacket);
        this.clientTcpSegmentLostPacket = bd.toPlainString();
    }

    public String getServerTcpSegmentLostPacket() {
        return serverTcpSegmentLostPacket;
    }

    /**
     * 科学计数法转化
     *
     * @param serverTcpSegmentLostPacket 客户端TCP丢失包率
     */
    public void setServerTcpSegmentLostPacket(String serverTcpSegmentLostPacket) {
        BigDecimal bd = new BigDecimal(serverTcpSegmentLostPacket);
        this.serverTcpSegmentLostPacket = bd.toPlainString();
    }
}
