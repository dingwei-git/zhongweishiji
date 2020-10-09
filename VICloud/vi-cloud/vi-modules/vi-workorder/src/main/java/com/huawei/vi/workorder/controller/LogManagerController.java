package com.huawei.vi.workorder.controller;

import com.huawei.vi.workorder.service.LogManagerService;
import com.jovision.jaws.common.constant.AppResultEnum;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log/")
public class LogManagerController {
    @Autowired
    private LogManagerService logManagerService;

    @GetMapping("list/{orderCode}")
    public RestResult queryLog(@PathVariable String orderCode){
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),logManagerService.queryLog(orderCode));
    }


}
