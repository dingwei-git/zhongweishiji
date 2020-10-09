/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.ivsserver.common;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * PlateData
 *
 * @since 2019-08-06
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"cameraName", "cameraSn", "count"})
@XmlRootElement(name = "plateData")
public class PlateData {
    /**
     * cameraName
     */
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "cameraName")
    protected String cameraName;

    /**
     * cameraSn
     */
    @XmlElement(name = "cameraSn")
    protected String cameraSn;

    /**
     * count
     */
    @XmlElement(name = "count")
    protected int count;

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String value) {
        cameraName = value;
    }

    public String getCameraSn() {
        return cameraSn;
    }

    public void setCameraSn(String value) {
        cameraSn = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int value) {
        count = value;
    }
}
