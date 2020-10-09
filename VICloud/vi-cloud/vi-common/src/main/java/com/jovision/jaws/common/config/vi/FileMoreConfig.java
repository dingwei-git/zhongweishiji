/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.config.vi;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

/**
 * baseconfig.xml中fileMoreConfig节点对应的实体类
 *
 * @version 1.0, 2018年6月28日
 * @since 2018-06-28
 */
@XmlRootElement
@XmlType(name = "fileMoreConfig")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class FileMoreConfig {
    /**
     * 摄像机大面积离线告警阈值 key
     */
    public static final String GATE_THRESHOLD = "gateThreshold";

    @XmlElement(name = "topMore")
    private String topMore;

    @XmlElement(name = "barMore")
    private String barMore;

    @XmlElement(name = "bottomMore")
    private String bottomMore;

    @XmlElement(name = "addedIpcDown")
    private String addedIpcDown;

    @XmlElement(name = "firstaryInstitutions")
    private String firstaryInstitutions;

    @XmlElement(name = "secondaryInstitutions")
    private String secondaryInstitutions;

    @XmlElement(name = "gateThreshold")
    private String gateThreshold;

    @XmlElement(name = "warningOrganization")
    private String warningOrganization;

    @XmlElement(name = "warningGroup")
    private String warningGroup;

    @XmlElement(name = "warningCamera")
    private String warningCamera;

    @XmlElement(name = "realtimeAlarm")
    private String realtimeAlarm;

    @XmlElement(name = "comparativeAnalysisCamera")
    private String comparativeAnalysisCamera;

    @XmlElement(name = "comparativeAnalysisGroup")
    private String comparativeAnalysisGroup;

    @XmlElement(name = "trendAnalysisCamera")
    private String trendAnalysisCamera;

    @XmlElement(name = "trendAnalysisGroup")
    private String trendAnalysisGroup;

    @XmlElement(name = "comparativeAnalysisTip")
    private String comparativeAnalysisTip;

    @XmlElement(name = "xingHuoHead")
    private String xingHuoHead;

    @XmlElement(name = "exportReportHead")
    private String exportReportHead;

    @XmlElement(name = "tableHeadData")
    private String tableHeadData;

    @XmlElement(name = "tableHeadDataDongzhi")
    private String tableHeadDataDongzhi;

    @XmlElement(name = "tableHeadFaultDetails")
    private String tableHeadFaultDetails;

    @XmlElement(name = "dossiersCamera")
    private String dossiersCamera;

    @XmlElement(name = "dossiersInfrastructure")
    private String dossiersInfrastructure;

    @XmlElement(name = "dossiersNo")
    private String dossiersNo;

    @XmlElement(name = "dossiersAttribute")
    private String dossiersAttribute;

    @XmlElement(name = "dossiersDataName")
    private String dossiersDataName;

    @XmlElement(name = "dossiersValueRequirement")
    private String dossiersValueRequirement;

    @XmlElement(name = "dossiersExclName")
    private String dossiersExclName;
}
