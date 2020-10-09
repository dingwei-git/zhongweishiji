package com.jovision.jaws.common.po;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : fengyefeiluo
 * @ClassName CodeMsgPo
 * @Version 1.0
 * @Date : 2020/1/20 16:36
 * @UpdateDate : 2020/1/20 16:36
 * @Version V1.0.0
 **/
@Data
@AllArgsConstructor
public class CodeMsgPo {
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误消息
     */
    private String message;
}
