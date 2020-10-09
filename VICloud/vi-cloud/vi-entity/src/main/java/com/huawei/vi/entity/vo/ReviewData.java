/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.vi.entity.vo;
import lombok.Getter;
import lombok.Setter;

/**
 * 复检数据对象
 *
 * @since 2020-10-25
 */
@Getter
@Setter
public class ReviewData {
    /**
     * 摄像机编码
     */
    String cameraId;

    /**
     * 故障类型
     */
    String faultType;

    /**
     * 监控类型
     */
    int monitorType;

    /**
     * 任务id
     */
    String taskId;

    /**
     * vi任务id
     */
    int viTaskId;

    /**
     * 接口名称
     */
    String interfaceName;

    /**
     * 接口类型
     */
    String templateId;

    /**
     * 复检来源标识: review来自告警中心 orderReview来自工单
     */
    String source;

    /**
     * 工单编码
     */
    String orderCode;

    /**
     * 根据模版id和告警编码双子段分组key获取
     *
     * @return 分组key
     */
    public String getGroupKey() {
        return faultType + viTaskId;
    }
}
