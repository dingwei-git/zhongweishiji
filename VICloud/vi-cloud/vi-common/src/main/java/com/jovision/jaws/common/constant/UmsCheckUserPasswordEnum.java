package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xianfeng
 * @version 1.0
 * @className UmsCheckUserPasswordEnum
 * @Date 2020/6/23 9:58
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum UmsCheckUserPasswordEnum {
    /**
     * 错误
     */
    ERROR(0,"错误") ,
    /**
     * 正确
     */
    CORRECT(1,"正确");

    private Integer value;
    private String title;
}
