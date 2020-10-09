package com.huawei.vi.workorder.controller;

import com.huawei.vi.workorder.service.IpcIpService;
import com.jovision.jaws.common.constant.AppResultEnum;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**设备相关接口*/
@RestController
@RequestMapping("/ipc/")
public class IpcIpController {


    @Autowired
    private IpcIpService ipcIpService;
    @GetMapping("info/{cameraId}")
    public RestResult queryList(@PathVariable String cameraId){
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),ipcIpService.getIpcInfo(cameraId));
    }


}
