package com.huawei.vi.thirddata.service.usermanage.impl;

import com.huawei.vi.thirddata.mapper.UserManagerMapper;
import com.huawei.vi.thirddata.service.baseserv.impl.BaseServImpl;
import com.huawei.vi.thirddata.service.usermanage.UserManageService;
import com.jovision.jaws.common.pojo.User;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserManageServiceImpl extends BaseServImpl<User, Integer> implements UserManageService {

    private static final Logger LOG = LoggerFactory.getLogger(UserManageServiceImpl.class);
    @Autowired
    private UserManagerMapper userManagerMapper;

    /**
     * 使用用户名查询用户
     *
     * @param userName 要查询的用户名
     * @return 用户列表(只包含一个用户)
     */
    @Override
    public List<User> selectUserByName(@NonNull String userName) {
        Map<String, Object> userNameMap = new HashMap<String, Object>();
        userNameMap.put(User.USER_NAME, userName);
        List<User> users = userManagerMapperSelectByCondition(userNameMap);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users;
    }


    /**
     * 根据条件查询信息
     *
     * @param map 查询条件
     * @return 用户列表
     */
    private List<User> userManagerMapperSelectByCondition(Map<String, Object> map) {
        try {
            return userManagerMapper.selectByCondition(map);
        } catch (DataAccessException dataAccessException) {
            LOG.error("userManagerMapperSelectByCondition DataAccessException {} ", dataAccessException.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    protected Map<String, Object> getMap(User user, Map<String, Object> condition) {
        return null;
    }
}
