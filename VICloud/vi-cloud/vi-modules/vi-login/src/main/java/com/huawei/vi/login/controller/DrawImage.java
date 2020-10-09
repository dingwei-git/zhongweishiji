package com.huawei.vi.login.controller;

import com.huawei.vi.login.service.DrawImageService;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 生成图片验证码
 *
 * @version 1.0, 2020-08-17
 * @dingwei 2020-08-17
 */
@RestController
public class DrawImage {
    @Autowired
    DrawImageService drawImageService;

    /**
     * 生成随机验证码
     *
     * @param request request
     * @param response response
     * @return String 图片路径
     */
    @RequestMapping(value = "/image.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResult image(HttpServletRequest request, HttpServletResponse response) {

        RestResult restResult = drawImageService.getImage(request,response);

        return restResult;
    }

}
