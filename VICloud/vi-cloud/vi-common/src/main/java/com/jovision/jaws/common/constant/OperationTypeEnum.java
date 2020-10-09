package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xianfeng
 * @version 1.0
 * @className OperationTypeEnum
 * @Date 2020/3/18 17:59
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum OperationTypeEnum {

        /**
         * 拒绝
         */
        OPERATION_TYPE_NO(0,"拒绝") ,
        /**
         * 确认
         */
        OPERATION_TYPE_YES(1,"确认");

        private int value;
        private String title;

}
