package com.huawei.vi.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CameraInformationVo {

    String id;// 主键
    String cameraType;// 对应字段
    String cameraValue;// 对应字段值
    String isUse;// 是否采用(1采用0不采用)
    String cameraSn;// 设备id
    Date createTime;// 创建时间
    Date updateTime;// 修改时间

}
