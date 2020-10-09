package com.huawei.vi.login.service;

import com.huawei.vi.login.po.UserInfo;
import com.huawei.vi.login.po.WebUserPO;
import lombok.NonNull;

import java.util.List;

public interface UserManageService {

    List<WebUserPO> selectUserByName(@NonNull String userName);

    /**
     * 用户详细信息展示
     *
     * @param userName 用户名
     * @return 用户详细信息
     */
    UserInfo selectUserInfo(String userName);
}
