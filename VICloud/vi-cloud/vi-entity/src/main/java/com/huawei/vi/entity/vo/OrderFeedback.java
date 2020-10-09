package com.huawei.vi.entity.vo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 工单反馈实体
 *
 * @since 2020-06-03
 */
@Setter
@Getter
@ToString
public class OrderFeedback {
    /**
     * 常用字段orderStatus工单状态集合
     */
    public static final String ORDER_STATUS = "orderStatus";

    /**
     * 常用字段orderCodes工单编码集合
     */
    public static final String ORDER_CODES = "orderCodes";

    /**
     * 常用字段4维修工roleId
     */
    public static final String ROLE_ID = "4";

    // 工单编码
    private String orderCode;

    // 反馈类型
    private String feedbackType;

    // 反馈内容
    private String feedbackContent;
}