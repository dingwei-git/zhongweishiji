package com.huawei.vi.entity.vo;

import lombok.Data;

@Data
public class CameraBriefInfoExes {
    String code;// 设备编码
    String name;// 摄像机名称
    String deviceGroupCode;// 所属设备组编码
    String parentCode;// 父设备编码
    String domainCode;// 设备归属域的域编码
    String deviceModelType;// 主设备型号
    String parentConnectCode;// 主设备互联编码
    String vendorType;// 设备提供商类型
    String deviceFormType;// 主设备类型
    String type;// 摄像机类型
    String cameraLocation;// 摄像机安装位置描述
    String cameraStatus;// 摄像机扩展状态
    String status;// 设备状态
    String netType;// 网络类型
    String isSupportIntelligent;// 是否支持智能分析
    String enableVoice;// 是否启用随路音频
    String nvrCode;// 设备所属NVR编码
    String deviceCreateTime;// 设备创建时间
    String isExDomain;// 是否为外域
    String longitude;// 经度
    String latitude;// 纬度
    String height;// 摄像机安装高度
    String isShadowDev;// 是否为影子摄像机
    String origDevCode;// 原始设备编码
    String origParentDevCode;// 原始父设备编码
    String oriDevDomainCode;// 原始域编码
    String deleteStatus;// 删除状态
    String connectCode;// 互联编码
    String supportGA1400;// •0：不支持1400协议，即作为视频子设备 •1：仅支持1400协议，即作为视图子设备 •2：同时支持1400协议和视频能力
    String reserve;// 保留字段

}
