/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.ivsserver.common;

import javax.xml.bind.annotation.*;

/**
 * PlateResponse
 *
 * @since 2019-08-06
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"result", "plateDatas", "totalCount"})
@XmlRootElement(name = "response")
public class PlateResponse {
    /**
     * result
     */
    @XmlElement(name = "result")
    protected Result result;

    /**
     * plateDatas
     */
    @XmlElement(name = "plateDatas")
    protected PlateDatas plateDatas;

    /**
     * totalCount
     */
    @XmlElement(name = "totalCount")
    protected int totalCount;

    public Result getResult() {
        return result;
    }

    public void setResult(Result value) {
        result = value;
    }

    public PlateDatas getPlateDatas() {
        return plateDatas;
    }

    public void setPlateDatas(PlateDatas value) {
        plateDatas = value;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int value) {
        totalCount = value;
    }
}
