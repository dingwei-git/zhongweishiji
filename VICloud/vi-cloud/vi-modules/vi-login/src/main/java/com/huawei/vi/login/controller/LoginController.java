package com.huawei.vi.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.huawei.vi.login.service.LoginService;
import com.jovision.jaws.common.constant.AppResultEnum;
import com.jovision.jaws.common.constant.HeaderParamEnum;
import com.jovision.jaws.common.controller.BaseController;
import com.jovision.jaws.common.dto.LoginDto;
import com.jovision.jaws.common.dto.ModifyPwdDto;
import com.jovision.jaws.common.dto.RenewalTokenDto;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     *
     * @param body
     * @return
     */
    //@CalStatistics(description = "登录")
    @PostMapping("/login")
    public RestResult login(@RequestBody LoginDto body) {
        return loginService.login(body);
    }
    /**
     * token续约接口
     *
     * @param body
     * @return
     */
    @PostMapping("/renewal_token")
    public RestResult renewalToken(@RequestBody RenewalTokenDto body, HttpServletResponse response) {
        return loginService.renewalToken(body,response);
    }


    /**
     * 修改密码
     *
     * @param body
     * @return
     */
    @PostMapping("/modify_pwd")
    public RestResult modifyPwd(@RequestBody ModifyPwdDto body) {
        if(StringUtils.isEmpty(getUserIdFromAttribute())){
            return RestResult.generateRestResult(AppResultEnum.NOT_FIND_USER.getCode(),AppResultEnum.NOT_FIND_USER.getMessage(),null);
        }
        return loginService.modifyPwd(body,getUserIdFromAttribute());
    }
    @GetMapping("/pwdrulecheck")
    public RestResult pwdrulecheck(String passwordrulecheck) {
        return loginService.pwdrulecheck(passwordrulecheck);
    }

    /**
     * 退出登录
     *
     * @param
     * @return
     */
    @PostMapping("/logout")
    public RestResult logout(HttpServletRequest request, @RequestBody JSONObject data) {
        String token = request.getHeader(HeaderParamEnum.AUTHORIZATION.getTitle()).substring(7);
        String tiken = data.getString("tiken");

        if(org.apache.commons.lang3.StringUtils.isNotEmpty(token)&&org.apache.commons.lang3.StringUtils.isNotEmpty(tiken)){
            return loginService.logout(token,tiken);
        }else{
            return RestResult.generateRestResult(AppResultEnum.ERROE.getCode(),AppResultEnum.ERROE.getMessage(),null);
        }
    }

}
