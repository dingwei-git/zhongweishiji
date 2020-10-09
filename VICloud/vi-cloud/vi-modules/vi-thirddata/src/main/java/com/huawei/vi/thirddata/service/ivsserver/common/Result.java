/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.ivsserver.common;

import javax.xml.bind.annotation.*;

/**
 * Result
 *
 * @since 2019-08-06
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"errmsg", "code"})
@XmlRootElement(name = "result")
public class Result {
    /**
     * errmsg
     */
    @XmlSchemaType(name = "errmsg")
    protected String errmsg;

    /**
     * code
     */
    @XmlElement(name = "code")
    protected String code;

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
