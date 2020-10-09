package com.huawei.vi.workorder.controller;

import com.huawei.vi.workorder.service.ForbackTempleteService;
import com.jovision.jaws.common.constant.AppResultEnum;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forback/")
public class ForbackTempleteController {

    @Autowired
    private ForbackTempleteService forbackTempleteService;

    @GetMapping("templete")
    public RestResult gettemplete(String orderCode){
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),forbackTempleteService.getTemplete(orderCode));
    }
}
