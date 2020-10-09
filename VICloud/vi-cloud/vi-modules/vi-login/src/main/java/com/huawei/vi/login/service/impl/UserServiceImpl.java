package com.huawei.vi.login.service.impl;

import com.huawei.vi.entity.po.UserPO;
import com.huawei.vi.login.mapper.UserMapper;
import com.huawei.vi.login.service.UserService;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.ResultCode;
import com.jovision.jaws.common.vo.ExecuteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserPO selectUser(Map selectMap) {
        return userMapper.selectUser(selectMap);
    }

    @Override
    public Long getTotalByUserId(String userId) {
        return userMapper.getTotalByUserId(userId);
    }

    @Override
    public Integer getFrozenByUserId(String user_id) {
        return userMapper.getFrozenByUserId(user_id);
    }

    @Override
    public RestResult getUser(String userId) {
        return RestResult.generateRestResult(ResultCode.SUCCESS.code(),ResultCode.SUCCESS.message(),userMapper.getUser(userId));
    }

    @Override
    public UserPO getInfo(String userId) {
        return userMapper.getInfo(userId);
    }

    @Override
    public int updatePassword(String userId, String password) {
        Map map = new HashMap();
        map.put("userId",userId);
        map.put("password",password);
        return userMapper.updatePassword(map);
    }
    /**查询用户角色*/
    @Override
    public Map getRole(String userName){
        return userMapper.getRole(userName);
    }

}
