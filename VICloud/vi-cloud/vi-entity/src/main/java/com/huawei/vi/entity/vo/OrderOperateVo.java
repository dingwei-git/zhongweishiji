package com.huawei.vi.entity.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderOperateVo {

    /**
     * 工单编码
     * */
    private String orderCode;
    /**
     * 操作类型：如申请完成
     * */
    private String operateType;
    /**
     * 操作内容
     * */
    private String operateContent;

    /**
     * 延期时间
     * */
    private String delayTime;
}
