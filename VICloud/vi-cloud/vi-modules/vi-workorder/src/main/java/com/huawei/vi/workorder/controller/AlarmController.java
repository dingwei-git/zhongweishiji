package com.huawei.vi.workorder.controller;


import com.huawei.vi.workorder.service.AlarmService;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alarm/")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @GetMapping("info/{alarmCode}")
    public RestResult getAlarmInfo(@PathVariable String alarmCode,@RequestParam  String orderCode){
        return alarmService.queryAlarmInfo(alarmCode,orderCode);
    }
}
