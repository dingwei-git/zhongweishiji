package com.huawei.vi.thirddata.controller;


import com.huawei.vi.thirddata.service.ivsserver.IvsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class OnlineExportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineExportController.class);

    @Autowired
    private IvsService ivsService;


    /*
    * 摄像机在线导出Excel 摄像机在线：0 摄像机离线：1 有效抓拍机：2
    * */
    @ResponseBody
    @RequestMapping(value = "exportExcel.do",method = {RequestMethod.GET})
    public void onlineExportExcel(HttpServletRequest request, HttpServletResponse response){
        ivsService.selectCameraInfoByState(response);
    }


}
