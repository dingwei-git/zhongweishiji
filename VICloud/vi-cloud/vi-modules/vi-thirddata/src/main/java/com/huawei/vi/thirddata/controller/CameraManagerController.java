package com.huawei.vi.thirddata.controller;

import com.huawei.vi.entity.vo.CameraPageVo;
import com.huawei.vi.entity.vo.CameraVo;
import com.huawei.vi.thirddata.service.cameramanage.CameraMangeService;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CameraManagerController {

    @Autowired
    CameraMangeService cameraMangeService;

    @RequestMapping(value = "/cameraPage.do",method = {RequestMethod.POST})
    public RestResult getCameraPage(@RequestBody CameraVo cameraVo){
        RestResult restResult = cameraMangeService.getCameraPage(cameraVo);
        return restResult;
    }


    /*
    * 模糊查询
    * */

    @RequestMapping(value = "manyCameraPage.do",method = {RequestMethod.POST})
    public RestResult getManyCameraPage(@RequestBody CameraPageVo cameraPageVo){
        RestResult restResult = cameraMangeService.getManyCameraPage(cameraPageVo);
        return restResult;
    }

}
