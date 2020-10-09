/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

import javax.xml.bind.annotation.*;

/**
 * 采集参数配置 protocolConfig.xml
 *
 * @version 1.0, 2018年6月20日
 * @since 2019-09-19
 */
@XmlRootElement
@XmlType(name = "parameterConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterConfig {
    @XmlElement(name = "loginApi")
    private String loginApi;

    @XmlElement(name = "logoutApi")
    private String logoutApi;

    @XmlElement(name = "callBack")
    private String callBack;

    @XmlElement(name = "someUrl")
    private String someUrl;

    @XmlElement(name = "tcpTable")
    private String tcpTable;

    @XmlElement(name = "tcpKeys")
    private String tcpKeys;

    @XmlElement(name = "tcpFields")
    private String tcpFields;

    @XmlElement(name = "udpTable")
    private String udpTable;

    @XmlElement(name = "udpKeys")
    private String udpKeys;

    @XmlElement(name = "udpFields")
    private String udpFields;

    @XmlElement(name = "sortType")
    private String sortType;

    @XmlElement(name = "tcpSortField")
    private String tcpSortField;

    @XmlElement(name = "udpSortField")
    private String udpSortField;

    @XmlElement(name = "timeUnit")
    private String timeUnit;

    @XmlElement(name = "filter")
    private String filter;

    @XmlElement(name = "topCount")
    private String topCount;

    @XmlElement(name = "dataApi")
    private String dataApi;

    @XmlElement(name = "videoinsightUrl")
    private String videoinsightUrl;

    @XmlElement(name = "dzCollectUrl")
    private String dzCollectUrl;

    @XmlElement(name = "dzViewPort")
    private String dzViewPort;

    @XmlElement(name = "ivsCollectUrl")
    private String ivsCollectUrl;

    @XmlElement(name = "imageDiagnosisUrl")
    private String imageDiagnosisUrl;

    public String getIvsCollectUrl() {
        return ivsCollectUrl;
    }

    public void setIvsCollectUrl(String ivsCollectUrl) {
        this.ivsCollectUrl = ivsCollectUrl;
    }

    public String getDzViewPort() {
        return dzViewPort;
    }

    public void setDzViewPort(String dzViewPort) {
        this.dzViewPort = dzViewPort;
    }

    public String getDataApi() {
        return dataApi;
    }

    public void setDataApi(String dataApi) {
        this.dataApi = dataApi;
    }

    public String getLoginApi() {
        return loginApi;
    }

    public void setLoginApi(String loginApi) {
        this.loginApi = loginApi;
    }

    public String getLogoutApi() {
        return logoutApi;
    }

    public void setLogoutApi(String logoutApi) {
        this.logoutApi = logoutApi;
    }

    public String getTcpTable() {
        return tcpTable;
    }

    public void setTcpTable(String tcpTable) {
        this.tcpTable = tcpTable;
    }

    public String getTcpKeys() {
        return tcpKeys;
    }

    public void setTcpKeys(String tcpKeys) {
        this.tcpKeys = tcpKeys;
    }

    public String getTcpFields() {
        return tcpFields;
    }

    public void setTcpFields(String tcpFields) {
        this.tcpFields = tcpFields;
    }

    public String getUdpTable() {
        return udpTable;
    }

    public void setUdpTable(String udpTable) {
        this.udpTable = udpTable;
    }

    public String getUdpKeys() {
        return udpKeys;
    }

    public void setUdpKeys(String udpKeys) {
        this.udpKeys = udpKeys;
    }

    public String getUdpFields() {
        return udpFields;
    }

    public void setUdpFields(String udpFields) {
        this.udpFields = udpFields;
    }

    public String getCallBack() {
        return callBack;
    }

    public void setCallBack(String callBack) {
        this.callBack = callBack;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getTopCount() {
        return topCount;
    }

    public void setTopCount(String topCount) {
        this.topCount = topCount;
    }

    public String getSomeUrl() {
        return someUrl;
    }

    public void setSomeUrl(String someUrl) {
        this.someUrl = someUrl;
    }

    public String getTcpSortField() {
        return tcpSortField;
    }

    public void setTcpSortField(String tcpSortField) {
        this.tcpSortField = tcpSortField;
    }

    public String getUdpSortField() {
        return udpSortField;
    }

    public void setUdpSortField(String udpSortField) {
        this.udpSortField = udpSortField;
    }

    public String getVideoinsightUrl() {
        return videoinsightUrl;
    }

    public void setVideoinsightUrl(String videoinsightUrl) {
        this.videoinsightUrl = videoinsightUrl;
    }

    public String getDzCollectUrl() {
        return dzCollectUrl;
    }

    public void setDzCollectUrl(String dzCollectUrl) {
        this.dzCollectUrl = dzCollectUrl;
    }

    public String getImageDiagnosisUrl() {
        return imageDiagnosisUrl;
    }

    public void setImageDiagnosisUrl(String imageDiagnosisUrl) {
        this.imageDiagnosisUrl = imageDiagnosisUrl;
    }
}
