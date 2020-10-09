package com.huawei.vi.entity.po;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class IPCInfo {
    private String ParentID;
    private String Status;
    private String LoginName;
    private String Owner;
    private String CivilCode;
    private String Address;
    private String DeviceID;
    private String Port;
    // 其中有key PTZType
    private Map<String, Object> Info;
    private String RegisterWay;
    private String Name;
    private String SafetyWay;
    private String Parental;
    private String ChannelNO;
    private String Manufacturer;
    private String Model;
    private String IPAddress;
    private String Secrecy;
    private String Password;
    private List<IPCInfo> children;
}
