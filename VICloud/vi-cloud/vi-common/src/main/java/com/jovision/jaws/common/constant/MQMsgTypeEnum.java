package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MQMsgTypeEnum {
    /**
     * 设备分享消息
     */
    SHARE_TYPE("device share", "设备分享消息");

    private String value;
    private String title;
}
