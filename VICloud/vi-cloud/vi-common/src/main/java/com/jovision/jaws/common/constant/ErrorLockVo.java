package com.jovision.jaws.common.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLockVo {
    /**
     * 剩余尝试次数
     */
    private Integer surplus_num;
    /**
     * 锁定时长(秒)
     */
    private Integer lock_duration;


}
