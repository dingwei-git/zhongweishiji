package com.huawei.vi.entity.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class MaintenanceInfoEntity {
    private String orderCode;
    private int departmentId;
    private int personId;
    private Timestamp feedbackTime;
    private Timestamp demandTime;
    private Timestamp doneTime;
    private boolean isTimeout;
    private String reviewStatus;
    private String reviewResult;
    private int delay;
    private int dzFaultNum;
    private int receivedNum;
}
