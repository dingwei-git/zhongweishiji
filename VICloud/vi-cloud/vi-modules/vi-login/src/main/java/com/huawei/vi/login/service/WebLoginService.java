package com.huawei.vi.login.service;

import com.huawei.vi.login.po.WebLoginVo;
import com.huawei.vi.login.po.WebUserPO;
import com.jovision.jaws.common.util.RestResult;
import lombok.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface WebLoginService {

    public RestResult WebLogin(HttpServletRequest request);

    public boolean userIsLock(@NonNull String userName);

    public long remainTime(String userName);

    public RestResult loginIsSuccess(@NonNull String userName, @NonNull String userPwd);

    public void insertOrUpdateLoginRecord(@NonNull String userName);

    public Map<String, Object> addResponseData(@NonNull WebUserPO user);

    public RestResult webCleanSession(HttpServletRequest request);
}
