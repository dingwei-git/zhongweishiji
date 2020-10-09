package com.huawei.vi.workorder.service;

import com.huawei.vi.entity.po.AlarmPO;
import com.jovision.jaws.common.util.RestResult;

import java.util.List;

public interface AlarmService {

    RestResult queryAlarmInfo(String alarmCode,String orderCode) ;

    List<String> queryAlarmCode(String orderCode);
}
