
package com.huawei.vi.thirddata.service.binary.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * UDP原始数据pojo类
 *
 * @since 2019-06-05
 */
@Setter
@Getter
public class UdpOriginalData implements Serializable {
    private static final long serialVersionUID = 2773645641069714380L;

    private String callerIpName;

    private String calleeIpName;

    private String callerPort;

    private String calleeIp;

    private String calleePort;

    private String callerIp;

    private String callId;

    private String flowType;

    private String durationTime;

    private String avgLostPacketRate;

    private String totalCodeBitps;

    private String videoMosAvg;

    private String flowStartTime;

    private String flowEndTime;

    private String protocol;

    private String ssrc;

    private String jitterAvg;

    private String linkId;
}
