package com.jovision.jaws.common.controller;

import com.jovision.jaws.common.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @program: VideoInsight
 * @description: controller基类用于管理请求
 * @author: pxh
 * @create: 2020-08-05 09:40
 */

public class BaseController {

    public static final ThreadLocal<HttpServletRequest>  requestLocal = new ThreadLocal<>();

    public static final ThreadLocal<HttpServletResponse>  responseLocal = new ThreadLocal<>();

    @ModelAttribute
    public void setLang(HttpServletRequest request, HttpServletResponse response){
        requestLocal.set(request);
        responseLocal.set(response);
    }

    public HttpServletRequest getRequest(){
        return requestLocal.get();
    }
    public HttpServletResponse getResponse(){
        return responseLocal.get();
    }
    public String getToken(){return getRequest().getHeader("token");}
    public String getUserId(){ return getRequest().getHeader("userId"); }
    public String getUserName(){ return getRequest().getHeader("userName"); }
    public String getUserIdFromAttribute(){ return Optional.ofNullable(getRequest().getAttribute("userId")).get().toString(); }
    public String getUserNameFromAttribute(){ return Optional.ofNullable(getRequest().getAttribute("userName")).get().toString(); }
}
