package com.huawei.vi.login.controller;


import com.huawei.vi.entity.po.ForbackTempletePO;
import com.jovision.jaws.common.constant.AppResultEnum;
import com.jovision.jaws.common.constant.TokenConstant;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/connect/")
public class DemoController {
    @GetMapping("test")
    public RestResult test(){
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),null);
    }
}

