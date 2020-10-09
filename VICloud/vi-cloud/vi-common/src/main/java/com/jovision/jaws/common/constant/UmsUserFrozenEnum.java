package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xianfeng
 * @version 1.0
 * @className UmsUserFrozenEnum
 * @Date 2020/5/7 11:30
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UmsUserFrozenEnum {
    /**
     * 已冻结
     */
    FROZEN(2,"已冻结") ,
    /**
     * 未冻结
     */
    NOT_FROZEN(1,"未冻结");

    private Integer value;
    private String title;
}
