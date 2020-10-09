package com.huawei.vi.thirddata.controller;

import com.huawei.vi.thirddata.service.EquipmentService;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class EquipmentController {
    @Autowired
    EquipmentService equipmentService;

    @PostMapping("/equipmentStatistical.do")
    public RestResult equipmentStatistical(HttpServletRequest request){
        RestResult restResult = equipmentService.getEquipmentStatistical(request);
        return restResult;
    }

    @PostMapping("/queryEquipmentList.do")
    public RestResult queryEquipmentList(HttpServletRequest request){
        RestResult restResult = equipmentService.queryEquipmentList(request);
        return restResult;
    }

    @PostMapping("/queryChildEquipmentByVCN.do")
    public RestResult queryChildEquipmentByVCN(HttpServletRequest request){
        RestResult restResult = equipmentService.queryChildEquipmentByVCN(request);
        return restResult;
    }
    @PostMapping("/queryConditions.do")
    public RestResult queryConditions(HttpServletRequest request){
        RestResult restResult = equipmentService.queryConditions(request);
        return restResult;
    }

    @PostMapping("/exportEquipment.do")
    public RestResult exportEquipment(HttpServletRequest request, HttpServletResponse response){
        RestResult restResult = equipmentService.exportEquipment(request,response);
        return restResult;
    }

}
