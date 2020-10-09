package com.jovision.jaws.common.constant;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单状态枚举类（英文国际化待完善）
 *
 * @since 2020-06-04
 */
@Getter
public enum OrderStatusEnum {

    /**工单管理：待维修*/
    WAITTING_REPAIR("待维修"),
    /**工单管理：进行中*/
    IN_PROGRESS("进行中"),
    /**工单管理：已完成*/
    ORDER_FINISHED("已完成"),


    TO_BE_SUBMITTED("待提交"),
    PENDING_CREATION_APPROVAL("带建单审核"),
    TO_BE_DISPATCHED("待派发"),
    TO_BE_ACCEPTED("待接收"),
    TO_BE_DISTRIBUTED("待分发"),
    APPLY_FOR_DELAY("申请延期"),
    APPLY_FOR_SUSPENSION("申请挂起"),
    APPLY_FOR_CLOSED("申请关闭"),
    APPLY_FOR_COMPLETED("申请完成"),
    REJECTED_LEVEL_ONE("已驳回（一级）"),
    DELAYED_LEVEL_ONE("已延期（一级）"),
    SUSPENDED_LEVEL_ONE("已挂起（一级）"),
    TO_BE_MAINTAINED("待维修"),
    DELAYED_LEVEL_TWO("已延期（二级）"),
    SUSPENDED_LEVEL_TWO("已挂起（二级）"),
    REJECTED_LEVEL_TWO("已驳回（二级）"),
    PENDING_DELAY_APPROVAL("申请延期待审核"),
    PENDING_SUSPENSION_APPROVAL("申请挂起待审核"),
    PENDING_CLOSURE_APPROVAL("申请关闭待审核"),
    CLOSED("已关闭"),
    COMPLETED("已完成"),
    PENDING_FORCIBLE_CLOSURE_APPROVAL("待关闭审核");

    private String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }

    /**
     * 获取枚举类的所有值
     *
     * @return list 枚举集合
     */
    public static List<String> toList() {
        List<String> list = new ArrayList<>();
        for (OrderStatusEnum enumm : OrderStatusEnum.values()) {
            list.add(enumm.getValue());
        }
        return list;
    }
}