/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.ivsserver.common;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PlateDatas
 *
 * @since 2019-08-06
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"plateData"})
@XmlRootElement(name = "plateDatas")
public class PlateDatas {
    /**
     * plateData
     */
    @XmlElement(name = "plateData")
    protected List<PlateData> plateData;

    /**
     * 获取Plate集合
     *
     * @return List
     */
    public List<PlateData> getPlateData() {
        if (plateData == null) {
            plateData = new ArrayList<PlateData>();
        }
        return plateData;
    }
}
