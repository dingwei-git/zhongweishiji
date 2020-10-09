package com.huawei.vi.login.service;

import com.huawei.vi.entity.po.LoginRecordPO;

public interface LoginRecordService {
    /**
     * 根据用户名查询
     * */
    LoginRecordPO selectByUser(String userName,String origin);

    void deleteUserName(String userName,String origin);


    int insert(LoginRecordPO loginRecordPO);

    int update(LoginRecordPO loginRecordPO);

}
