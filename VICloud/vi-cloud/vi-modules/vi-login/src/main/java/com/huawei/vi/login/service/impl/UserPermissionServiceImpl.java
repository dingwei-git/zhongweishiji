package com.huawei.vi.login.service.impl;

import com.huawei.vi.login.mapper.UserPermissionMapper;
import com.huawei.vi.login.po.UserInfo;
import com.huawei.vi.login.service.UserManageService;
import com.huawei.vi.login.service.UserPermissionService;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.ServiceCommonConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserPermissionServiceImpl implements UserPermissionService {
    @Autowired
    UserManageService userManageService;
    @Autowired
    UserPermissionMapper userPermissionMapper;

    @Override
    public RestResult selectUserOpenResource(HttpServletRequest request) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"", null);
        String loginUserName = request.getParameter(CommonConst.LOGIN_USER);
        UserInfo userInfo = userManageService.selectUserInfo(loginUserName);
        Map<String, Object> userRelationMap = new HashMap<String, Object>();
        if (userInfo != null) {
            userRelationMap.put("userRelation", userPermissionMapper.selectUserOpenResource(userInfo.getRoleId()));
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(userRelationMap);
        }
        return restResult;
    }
}
