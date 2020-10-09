package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 设备是否在自定义报警时间内
 * @author JK
 * @date 2020-05-09
 * */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AlarmScheduleStatusEnum {

    /**
     * 不在自定义报警时间内
     */
    NOT_IN_ALARM_SCHEDULE(0, "不在自定义报警时间内"),
    /**
     * 在自定义报警时间内
     */
    IN_ALARM_SCHEDULE(1, "在自定义报警时间内");

    private Integer value;
    private String title;

}
