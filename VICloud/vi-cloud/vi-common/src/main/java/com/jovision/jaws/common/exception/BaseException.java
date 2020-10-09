package com.jovision.jaws.common.exception;

import lombok.Data;

/**
 * 自定义异常父类
 * @Author:         ABug
 * @CreateDate:     2019/9/18 14:51
 * @Version:        1.0
 */
@Data
public class BaseException extends RuntimeException {
    /**
     * 异常代码
     */
    private Integer code;
    /**
     * 异常描述: 可设置自定义信息
     */
    private String message;

    public BaseException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
