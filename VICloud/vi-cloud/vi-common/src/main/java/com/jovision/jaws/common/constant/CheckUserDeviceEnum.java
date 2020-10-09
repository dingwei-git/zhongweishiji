package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum  CheckUserDeviceEnum {

    RELATION_IS_EXIST(1,"存在关联"),

    RELATION_IS_NOT_EXIST(0,"不存在关联");

    private Integer value;

    private String title;
}
