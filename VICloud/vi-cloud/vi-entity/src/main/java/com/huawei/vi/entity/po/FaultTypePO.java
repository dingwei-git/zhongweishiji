package com.huawei.vi.entity.po;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaultTypePO {

    private String id;
    private String name;
    private int alarmId;
    private String repairSuggest;
    private String faultExplain;

}