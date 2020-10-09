package com.huawei.vi.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class OrderDetailVO {

    private int id;
    /**
     * 工单编码
     */
    private String orderCode;
    /**
     * 故障类型
     */
    private String faultTypes;
    /**
     * 故障类型名称
     */
    private String faultTypesName;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 创建时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date creatTime;
    /**
     * 摄像机编码
     */
    private String cameraId;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 工单描述
     * */
    private String orderDescription;
    /**
     * 要求完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date demandTime;

    /**
     * 故障图片地址
     */
    private String faultPictureUrl;
    private List<String> faultPictureUrlList;

    /**
     * /** 现场图片地址
     */
    private String onsitePictureUrl;
    private List<String> onsitePictureUrlList;

    /**
     * 复检图像地址
     */
    private String recheckPictureUrl;
    private List<String> recheckPictureUrlList;
    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 工单标题
     */
    private String orderTitle;
    /**
     * 报警id
     * */
    private List<String> alarmList;

    /**反馈*/
    private String forbackDetails;
    /**延期*/
    private int delay;

    private String  creationType;

    private String reviewStatus;

}
