package com.huawei.vi.thirddata.service;

import com.jovision.jaws.common.util.RestResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface EquipmentService {

    public RestResult getEquipmentStatistical(HttpServletRequest request);

    public RestResult queryEquipmentList(HttpServletRequest request);

    public RestResult queryChildEquipmentByVCN(HttpServletRequest request);

    public RestResult queryConditions(HttpServletRequest request);

    public RestResult exportEquipment(HttpServletRequest request,HttpServletResponse response);
}
