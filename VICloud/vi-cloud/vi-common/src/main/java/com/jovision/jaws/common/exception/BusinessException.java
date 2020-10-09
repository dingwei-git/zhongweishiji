package com.jovision.jaws.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 业务异常处理
 * @Author: ABug
 * @Date: 2019/12/24 11:33
 * @Version V1.0.0
 **/
@NoArgsConstructor
@Getter
public class BusinessException extends RuntimeException {
    private int code;
    private String msg;

    /**
     * 使用枚举传参
     * @param errorEnum 异常
     */
    public BusinessException(BusinessErrorEnum errorEnum){
        super(errorEnum.getMsg());
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
    }

    /**
     * 自定义消息异常
     * @param code 异常码
     * @param msg 异常描述信息
     */
    public BusinessException(int code , String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
