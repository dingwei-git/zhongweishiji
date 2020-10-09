/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.config.vi;

import com.jovision.jaws.common.pojo.*;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * AutomaticOrderConfig.xml对应的实体类
 *
 * @since 2018-12-10
 */
@Data
@XmlRootElement(name = "automaticOrder")
@XmlType(name = "",
    propOrder = {"faultNames", "selectCondition", "thresholdValue", "ignoreFault", "confirmTypes", "orderTypes"})
@XmlAccessorType(XmlAccessType.FIELD)
public class AutomaticOrderConfig {
    /**
     * 故障自动处理参数配置 故障确认方式 key
     */
    public static final String CONFIRM_TYPES = "confirmTypes";

    /**
     * 故障自动处理参数配置 故障派单方式 key
     */
    public static final String ORDER_TYPES = "orderTypes";

    /**
     * 是否修改服务器参数配置中的派单参数（show文件）
     */
    private static boolean isUpdateShowAutoParam;

    /**
     * 派单参数是否修改(analyse文件)
     */
    private static boolean isUpateAnalyseAutoOrderParam;

    @XmlElementWrapper(name = "faultNames")
    @XmlElement(name = "faultName")
    private List<FaultName> faultNames;

    @XmlElement(name = "selectCondition")
    private SelectCondition selectCondition;

    @XmlElement(name = "thresholdValue")
    private ThresholdValue thresholdValue;

    @XmlElement(name = "ignoreFault")
    private IgnoreFault ignoreFault;

    @XmlElementWrapper(name = "confirmTypes")
    @XmlElement(name = "orderType")
    private List<OrderType> confirmTypes;

    @XmlElementWrapper(name = "orderTypes")
    @XmlElement(name = "orderType")
    private List<OrderType> orderTypes;

    /**
     * 返回 isUpdateShowAutoParam
     *
     * @return isUpdateShowAutoParam值
     */
    public static boolean isUpdateShowAutoParam() {
        return isUpdateShowAutoParam;
    }

    /**
     * 对isUpdateShowAutoParam进行赋值
     *
     * @param isUpdateShow isUpdateShowAutoParam值
     */
    public static void setUpdateShowAutoParam(boolean isUpdateShow) {
        AutomaticOrderConfig.isUpdateShowAutoParam = isUpdateShow;
    }

    /**
     * 返回 isUpateAnalyseAutoOrderParam
     *
     * @return isUpateAnalyseAutoOrderParam值
     */
    public static boolean isUpateAnalyseAutoOrderParam() {
        return isUpateAnalyseAutoOrderParam;
    }

    /**
     * 对isUpateAnalyseAutoOrderParam进行赋值
     *
     * @param isUpdateOrder isUpateAnalyseAutoOrderParam值
     */
    public static void setUpateAnalyseAutoOrderParam(boolean isUpdateOrder) {
        AutomaticOrderConfig.isUpateAnalyseAutoOrderParam = isUpdateOrder;
    }
}
