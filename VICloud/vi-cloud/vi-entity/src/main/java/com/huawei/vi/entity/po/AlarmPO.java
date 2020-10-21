/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.huawei.vi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警数据
 *
 * @since 2020-01-21
 */
@Getter
@Setter
@NoArgsConstructor
public class AlarmPO implements Serializable {

    /**
     * 告警数据表名前缀
     */
    public static final String TBL_ALARM = "tbl_alarm_";

    /**
     * 告警day表过滤前缀
     */
    public static final String TBL_ALARM_2 = "tbl_alarm_2";

    /**
     * 告警详情day表前缀
     */
    public static final String TBL_ALARM_MESSAGE = "tbl_alarm_message_";

    /**
     * 摄像机编码
     */
    public static final String CAMERA_ID = "cameraId";

    /**
     * 故障类型
     */
    public static final String FAULT_TYPE_ID = "faultTypeId";

    /**
     * 告警Id
     */
    public static final String ALARM_ID = "alarmId";

    /**
     * 图片url
     */
    public static final String PICTURE_URL = "pictureUrl";

    /**
     * 未恢复的告警状态id
     */
    public static final int NOT_RECOVERY_ID = 2;

    /**
     * 自动恢复的告警状态id
     */
    public static final int AUTO_RECOVERY_ID = 1;

    /**
     * 手动恢复的告警状态id
     */
    public static final int HAND_RECOVERY_ID = 3;

    /**
     * 排序
     */
    public static final String ORDER_BY = "orderBy";

    /**
     * 排序规则
     */
    public static final String SORT = "sort";

    /**
     * 倒序
     */
    public static final String DESC = "desc";

    private static final long serialVersionUID = -1579705183352986250L;

    private int id;

    /**
     * 摄像机编码
     */
    private String cameraId;
    private String cameraName;

    /**
     * 监控类型 id
     */
    private int monitorTypeId;
    private String monitorTypeName;
    /**
     * 监控类型实体
     */
    private TblMonitorType monitorType;

    /**
     * 故障类型 id
     */
    private String faultTypeId;
    private String  faultTypeName;
    /**
     * 故障类型实体
     */
    private FaultTypePO faultType;

    /**
     * 告警等级 id
     */
    private int alarmLevelId;
    private String alarmLevelName;
    /**
     * 告警等级实体
     */
    private TblAlarmLevel alarmLevel;

    /**
     * 告警开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    /**
     * 告警结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 诊断次数
     */
    private int diagnosisNum;

    /**
     * 告警状态 id
     */
    private int alarmStatusId;
    private String alarmStatusName;
    /**
     * 告警状态实体
     */
    private TblAlarmStatus alarmStatus;

    /**
     * 是否附件
     */
    private boolean reCheckStatus;

    /**
     * 诊断图片路径
     */
    private String pictureUrl;

    /**
     * 复检图片路径
     */
    private String rePictureUrl;

    /**
     * 任务编号
     */
    private String taskId;

    private String ip;

    private long timeLong;



    /**
     * 告警与故障关联关系 id
     */
    private int faultRelationId;

    private String alarmCode;

    public AlarmPO(String cameraId, int monitorTypeId, String faultTypeId, int alarmLevelId, int alarmStatusId) {
        this.cameraId = cameraId;
        this.monitorTypeId = monitorTypeId;
        this.faultTypeId = faultTypeId;
        this.alarmLevelId = alarmLevelId;
        this.alarmStatusId = alarmStatusId;
    }
}