package com.huawei.vi.login.service;

import com.jovision.jaws.common.util.RestResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserPermissionService {

    public RestResult selectUserOpenResource(HttpServletRequest request);

}
