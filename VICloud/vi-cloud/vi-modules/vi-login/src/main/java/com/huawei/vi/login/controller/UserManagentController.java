package com.huawei.vi.login.controller;

import com.huawei.vi.login.service.UserPermissionService;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserManagentController {
    @Autowired
    UserPermissionService userPermissionService;
    /**
     * 查看用户当前页面权限
     *
     * @param request 网络请求
     * @return 用户页面权限列表
     */
    @RequestMapping(value = "/selectUserOpenResource.do",method = RequestMethod.POST)
    public RestResult selectUserOpenResource(HttpServletRequest request){
        RestResult restResult = userPermissionService.selectUserOpenResource(request);
        return restResult;
    }
}
