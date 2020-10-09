package com.huawei.vi.entity.po;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkOrderPicturePO {
    /**
     * 工单号
     */
    private String orderCode;

    /**
     * 故障图片url
     */
    private String faultPictureUrl;

    /**
     * /** 现场图片地址
     */
    private String onsitePictureUrl;

    /**
     * 复检图像地址
     */
    private String recheckPictureUrl;
}
