package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 访问agent的方法
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BusinessAgentTypeEnum {
    /**
     * 获取主密钥列表
     */
    GET_MASTER_KEY_LIST(1,"获取主密钥列表"),
    /**
     * 获取配置文件的明文
     */
    DECPRYT_CONFIGURATION_FIELDS(2,"获取配置文件的明文")
    ;

    /**
     * 异常码
     */
    private int code;
    /**
     * 异常描述 , 返回调用方
     */
    private String msg;
}
