package com.huawei.vi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LoginRecordPO {

    // 用户名
    private String userName;

    // 锁定状态
    private String lockFlag;

    // 失败次数
    private int failureNum;

    // 失败时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date loginDate;

    private String origin;
}
