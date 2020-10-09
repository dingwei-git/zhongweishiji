/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.jovision.jaws.common.constant;

/**
 * 日志类型 枚举类
 *
 * @since 2020-05-28
 */
public enum LogTypeEnum {
    /**
     * 工单日志
     */
    WORK_ORDER_LOG("workOrderLog"),
    /**
     * 档案日志
     */
    DOSSIER_LOG("dossierLog");

    private final String style;

    LogTypeEnum(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }
}
