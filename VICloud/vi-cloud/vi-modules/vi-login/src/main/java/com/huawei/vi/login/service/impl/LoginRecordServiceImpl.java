package com.huawei.vi.login.service.impl;

import com.huawei.vi.entity.po.LoginRecordPO;
import com.huawei.vi.login.mapper.LoginRecordMapper;
import com.huawei.vi.login.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginRecordServiceImpl implements LoginRecordService {

    @Autowired
    private LoginRecordMapper loginRecordMapper;
    @Override
    public LoginRecordPO selectByUser(String userName,String origin) {
        return loginRecordMapper.selectByUser(userName,origin);
    }

    @Override
    public void deleteUserName(String userName,String origin) {
        loginRecordMapper.deleteUserName(userName,origin);
    }

    @Override
    public int insert(LoginRecordPO loginRecordPO) {
        return loginRecordMapper.insert(loginRecordPO);
    }

    @Override
    public int update(LoginRecordPO loginRecordPO) {
        return loginRecordMapper.update(loginRecordPO);
    }


}
