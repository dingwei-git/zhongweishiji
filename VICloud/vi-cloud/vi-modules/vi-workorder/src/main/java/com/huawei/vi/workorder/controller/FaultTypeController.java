package com.huawei.vi.workorder.controller;


import com.huawei.vi.workorder.service.FaultTypeService;
import com.jovision.jaws.common.constant.AppResultEnum;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fault/")
public class FaultTypeController {


    @Autowired
    private FaultTypeService faultTypeService;

    /**
     * 故障类型信息
     * */
    @GetMapping("info")
    public RestResult getAllFaultInfo(String orderCode){
        //return RestResult.failure(ResultCode.SUCCESS,faultTypeService.queryAll());
        List list = faultTypeService.queryFaultInfo(orderCode);
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),list);
    }

    /**
     * 获取维修反馈模板
     * @param
     * */


}
