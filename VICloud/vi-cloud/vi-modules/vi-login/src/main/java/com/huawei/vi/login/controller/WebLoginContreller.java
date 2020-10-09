package com.huawei.vi.login.controller;

import com.huawei.vi.login.service.WebLoginService;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WebLoginContreller {

    @Autowired
    WebLoginService webLoginService;

    /**
     * web端登陆接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/webLogin.do", method = RequestMethod.POST)
    public RestResult WebLogin(HttpServletRequest request){
        RestResult restResult = webLoginService.WebLogin(request);
        return restResult;
    }
    /**
     * web端登陆注销接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/webCleanSession.do", method = RequestMethod.POST)
    public RestResult webCleanSession(HttpServletRequest request){
        RestResult restResult = webLoginService.webCleanSession(request);
        return restResult;
    }
}
