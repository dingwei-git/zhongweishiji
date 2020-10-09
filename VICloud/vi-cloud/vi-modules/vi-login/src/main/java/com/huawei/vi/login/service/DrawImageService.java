package com.huawei.vi.login.service;

import com.jovision.jaws.common.util.RestResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DrawImageService {

    public RestResult getImage(HttpServletRequest request, HttpServletResponse response);
}
