package com.jovision.jaws.common.constant;

import lombok.Getter;

/**
 * 工单操作处理环节枚举
 *
 * @since 2020-06-09
 */
@Getter
public enum HandlePhaseEnum {
    WORKORDER_CREATED("工单创建完成"),
    CREATED_REVIEWED_APPROVED("建单审核通过"),
    CREATED_REVIEWED_REJECTED("建单审核驳回"),
    WORKORDER_DISPATCHED("工单已派发"),
    WORKORDER_RECEIVED("工单已接收"),
    WORKORDER_DISTRIBUTED("工单已分发"),
    EXTENSION_INITIAL_REVIEW_APPROVED("延期申请初审通过"),
    EXTENSION_INITIAL_REVIEW_REJECT("延期申请初审驳回"),
    HANGUP_INITIAL_REVIEW_APPROVED("挂起申请初审通过"),
    HANGUP_INITIAL_REVIEW_REJECT("挂起申请初审驳回"),
    CLOSE_INITIAL_REVIEW_APPROVED("关闭申请初审通过"),
    CLOSE_INITIAL_REVIEW_REJECT("关闭申请初审驳回"),
    REPAIR_COMPLETED_INITIAL_REVIEW_APPROVED("维修完成申请初审通过"),
    REPAIR_COMPLETED_INITIAL_REVIEW_REJECT("维修完成申请初审驳回"),
    EXTENSION_SUBMITTED("延期申请已提交"),
    HANGUP_SUBMITTED("挂起申请已提交"),
    CLOSE_SUBMITTED("关闭申请已提交"),
    REPAIR_COMPLETED_SUBMITTED("维修完成申请已提交"),
    EXTENSION_FINAL_REVIEW_APPROVE("延期申请终审通过"),
    EXTENSION_FINAL_REVIEW_REJECT("延期申请终审驳回"),
    HANGUP_FINAL_REVIEW_APPROVE("挂起申请终审通过"),
    HANGUP_FINAL_REVIEW_REJECT("挂起申请终审驳回"),
    CLOSE_FINAL_REVIEW_APPROVE("关闭申请终审通过"),
    CLOSE_FINAL_REVIEW_REJECT("关闭申请终审驳回"),
    REPAIR_COMPLETED_FINAL_REVIEW_APPROVED("维修完成终审通过"),
    REPAIR_COMPLETED_FINAL_REVIEW_REJECT("维修完成终审驳回");
    private String value;

    HandlePhaseEnum(String value) {
        this.value = value;
    }

    /**
     * 根据key值，获取当前枚举值
     *
     * @param key 值
     * @return 枚举值
     */
    public static HandlePhaseEnum getEnumByKey(String key) {
        for (HandlePhaseEnum handlePhase : HandlePhaseEnum.values()) {
            if (key.equals(handlePhase.getValue())) {
                return handlePhase;
            }
        }
        throw new IllegalArgumentException("get enumByKey error,key is:" + key);
    }
}