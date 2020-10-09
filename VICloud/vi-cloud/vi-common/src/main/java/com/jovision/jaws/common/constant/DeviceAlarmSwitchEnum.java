package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum DeviceAlarmSwitchEnum {

    /**
     * 开启
     */
    OPEN (1, "开启"),

    /**
     * 关闭
     * */
    CLOSE(0, "关闭");

    private Integer value;
    private String title;

}
