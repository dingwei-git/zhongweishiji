package com.huawei.vi.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class OrderListVO {
    private int id;
    /**
     * 工单编码
     */
    private String orderCode;
    /**
     * 工单标题
     */
    private String orderTitle;
    /**
     *创建时间
     * */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String creatTime;
    /**
     * 设备名称
     */
    private String cameraName;
    /**
     * 故障类型，多个以逗号分割
     */
    private String faultTypes;

    private String orderType;
    private String orderStatus;
    private String delay;
}
