package com.huawei.vi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class LogManagerPO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date datetime;

    /**
     * 操作人
     * */
    private String operateName;

    /**
     * 指派给谁
     * */
    private String assign;

    /**
     * 操作类型
     * */
    private String operateType;

    /**
     * 操作详情
     * */
    private String details;

    // 工单编码,用于唯一标示工单，一机一档为null
    private String code;

    // 日志类型,区分是哪个模块的日志
    private String logType;
}