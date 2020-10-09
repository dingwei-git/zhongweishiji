/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.entity.po;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 解析xml to Bean
 *
 * @since 2019-08-20
 */
@XmlRootElement(name = "sessionFiled")
@XmlType(name = "sessionFiled")
@XmlAccessorType(XmlAccessType.FIELD)
public class SessionFiled {
    @XmlElementWrapper(name = "tcpFiled")
    @XmlElement(name = "filed")
    private List<FiledInfo> tcpFiledInfoList;

    @XmlElementWrapper(name = "udpFiled")
    @XmlElement(name = "filed")
    private List<FiledInfo> udpFiledInfoList;

    public List<FiledInfo> getTcpFiledInfoList() {
        return tcpFiledInfoList;
    }

    public void setTcpFiledInfoList(List<FiledInfo> tcpFiledInfoList) {
        this.tcpFiledInfoList = tcpFiledInfoList;
    }

    public List<FiledInfo> getUdpFiledInfoList() {
        return udpFiledInfoList;
    }

    public void setUdpFiledInfoList(List<FiledInfo> udpFiledInfoList) {
        this.udpFiledInfoList = udpFiledInfoList;
    }
}
