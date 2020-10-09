/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;

import java.io.Serializable;

/**
 * TCP原始数据pojo类
 *
 * @since 2019-06-05
 */
public class TcpOriginalData implements Serializable {
    /**
     * Serializable
     */
    private static final long serialVersionUID = 1L;

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

    /**
     * 返回 clientIpLocation
     *
     * @return clientIpLocation值
     */
    public String getClientIpLocation() {
        return clientIpLocation;
    }

    /**
     * 对clientIpLocation进行赋值
     *
     * @param clientIpLocation clientIpLocation值
     */
    public void setClientIpLocation(String clientIpLocation) {
        this.clientIpLocation = clientIpLocation;
    }

    /**
     * 返回 serverIpLocation
     *
     * @return serverIpLocation值
     */
    public String getServerIpLocation() {
        return serverIpLocation;
    }

    /**
     * 对serverIpLocation进行赋值
     *
     * @param serverIpLocation serverIpLocation值
     */
    public void setServerIpLocation(String serverIpLocation) {
        this.serverIpLocation = serverIpLocation;
    }

    /**
     * 返回 clientIpAddr
     *
     * @return clientIpAddr值
     */
    public String getClientIpAddr() {
        return clientIpAddr;
    }

    /**
     * 对clientIpAddr进行赋值
     *
     * @param clientIpAddr clientIpAddr值
     */
    public void setClientIpAddr(String clientIpAddr) {
        this.clientIpAddr = clientIpAddr;
    }

    /**
     * 返回 clientPort
     *
     * @return clientPort值
     */
    public String getClientPort() {
        return clientPort;
    }

    /**
     * 对clientPort进行赋值
     *
     * @param clientPort clientPort值
     */
    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }

    /**
     * 返回 serverIpAddr
     *
     * @return serverIpAddr值
     */
    public String getServerIpAddr() {
        return serverIpAddr;
    }

    /**
     * 对serverIpAddr进行赋值
     *
     * @param serverIpAddr serverIpAddr值
     */
    public void setServerIpAddr(String serverIpAddr) {
        this.serverIpAddr = serverIpAddr;
    }

    /**
     * 返回 serverPort
     *
     * @return serverPort值
     */
    public String getServerPort() {
        return serverPort;
    }

    /**
     * 对serverPort进行赋值
     *
     * @param serverPort serverPort值
     */
    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * 返回 protocol
     *
     * @return protocol值
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * 对protocol进行赋值
     *
     * @param protocol protocol值
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * 返回 applicationId
     *
     * @return applicationId值
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * 对applicationId进行赋值
     *
     * @param applicationId applicationId值
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * 返回 tcpStatus
     *
     * @return tcpStatus值
     */
    public String getTcpStatus() {
        return tcpStatus;
    }

    /**
     * 对tcpStatus进行赋值
     *
     * @param tcpStatus tcpStatus值
     */
    public void setTcpStatus(String tcpStatus) {
        this.tcpStatus = tcpStatus;
    }

    /**
     * 返回 totalPacket
     *
     * @return totalPacket值
     */
    public String getTotalPacket() {
        return totalPacket;
    }

    /**
     * 对totalPacket进行赋值
     *
     * @param totalPacket totalPacket值
     */
    public void setTotalPacket(String totalPacket) {
        this.totalPacket = totalPacket;
    }

    /**
     * 返回 serverTotalPacket
     *
     * @return serverTotalPacket值
     */
    public String getServerTotalPacket() {
        return serverTotalPacket;
    }

    /**
     * 对serverTotalPacket进行赋值
     *
     * @param serverTotalPacket serverTotalPacket值
     */
    public void setServerTotalPacket(String serverTotalPacket) {
        this.serverTotalPacket = serverTotalPacket;
    }

    /**
     * 返回 clientTotalPacket
     *
     * @return clientTotalPacket值
     */
    public String getClientTotalPacket() {
        return clientTotalPacket;
    }

    /**
     * 对clientTotalPacket进行赋值
     *
     * @param clientTotalPacket clientTotalPacket值
     */
    public void setClientTotalPacket(String clientTotalPacket) {
        this.clientTotalPacket = clientTotalPacket;
    }

    /**
     * 返回 flowStartTime
     *
     * @return flowStartTime值
     */
    public String getFlowStartTime() {
        return flowStartTime;
    }

    /**
     * 对flowStartTime进行赋值
     *
     * @param flowStartTime flowStartTime值
     */
    public void setFlowStartTime(String flowStartTime) {
        this.flowStartTime = flowStartTime;
    }

    /**
     * 返回 flowEndTime
     *
     * @return flowEndTime值
     */
    public String getFlowEndTime() {
        return flowEndTime;
    }

    /**
     * 对flowEndTime进行赋值
     *
     * @param flowEndTime flowEndTime值
     */
    public void setFlowEndTime(String flowEndTime) {
        this.flowEndTime = flowEndTime;
    }

    /**
     * 返回 flowDuration
     *
     * @return flowDuration值
     */
    public String getFlowDuration() {
        return flowDuration;
    }

    /**
     * 对flowDuration进行赋值
     *
     * @param flowDuration flowDuration值
     */
    public void setFlowDuration(String flowDuration) {
        this.flowDuration = flowDuration;
    }

    /**
     * 返回 totalBitps
     *
     * @return totalBitps值
     */
    public String getTotalBitps() {
        return totalBitps;
    }

    /**
     * 对totalBitps进行赋值
     *
     * @param totalBitps totalBitps值
     */
    public void setTotalBitps(String totalBitps) {
        this.totalBitps = totalBitps;
    }

    /**
     * 返回 clientBitps
     *
     * @return clientBitps值
     */
    public String getClientBitps() {
        return clientBitps;
    }

    /**
     * 对clientBitps进行赋值
     *
     * @param clientBitps clientBitps值
     */
    public void setClientBitps(String clientBitps) {
        this.clientBitps = clientBitps;
    }

    /**
     * 返回 serverBitps
     *
     * @return serverBitps值
     */
    public String getServerBitps() {
        return serverBitps;
    }

    /**
     * 对serverBitps进行赋值
     *
     * @param serverBitps serverBitps值
     */
    public void setServerBitps(String serverBitps) {
        this.serverBitps = serverBitps;
    }

    /**
     * 返回 clientTcpRetransmissionRate
     *
     * @return clientTcpRetransmissionRate值
     */
    public String getClientTcpRetransmissionRate() {
        return clientTcpRetransmissionRate;
    }

    /**
     * 对clientTcpRetransmissionRate进行赋值
     *
     * @param clientTcpRetransmissionRate clientTcpRetransmissionRate值
     */
    public void setClientTcpRetransmissionRate(String clientTcpRetransmissionRate) {
        this.clientTcpRetransmissionRate = clientTcpRetransmissionRate;
    }

    /**
     * 返回 serverTcpRetransmissionRate
     *
     * @return serverTcpRetransmissionRate值
     */
    public String getServerTcpRetransmissionRate() {
        return serverTcpRetransmissionRate;
    }

    /**
     * 对serverTcpRetransmissionRate进行赋值
     *
     * @param serverTcpRetransmissionRate serverTcpRetransmissionRate值
     */
    public void setServerTcpRetransmissionRate(String serverTcpRetransmissionRate) {
        this.serverTcpRetransmissionRate = serverTcpRetransmissionRate;
    }

    /**
     * 返回 clientTcpSegmentLostPacket
     *
     * @return clientTcpSegmentLostPacket值
     */
    public String getClientTcpSegmentLostPacket() {
        return clientTcpSegmentLostPacket;
    }

    /**
     * 对clientTcpSegmentLostPacket进行赋值
     *
     * @param clientTcpSegmentLostPacket clientTcpSegmentLostPacket值
     */
    public void setClientTcpSegmentLostPacket(String clientTcpSegmentLostPacket) {
        this.clientTcpSegmentLostPacket = clientTcpSegmentLostPacket;
    }

    /**
     * 返回 serverTcpSegmentLostPacket
     *
     * @return serverTcpSegmentLostPacket值
     */
    public String getServerTcpSegmentLostPacket() {
        return serverTcpSegmentLostPacket;
    }

    /**
     * 对serverTcpSegmentLostPacket进行赋值
     *
     * @param serverTcpSegmentLostPacket serverTcpSegmentLostPacket值
     */
    public void setServerTcpSegmentLostPacket(String serverTcpSegmentLostPacket) {
        this.serverTcpSegmentLostPacket = serverTcpSegmentLostPacket;
    }

    /**
     * 返回 linkId
     *
     * @return linkId值
     */
    public String getLinkId() {
        return linkId;
    }

    /**
     * 对linkId进行赋值
     *
     * @param linkId linkId值
     */
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }
}
