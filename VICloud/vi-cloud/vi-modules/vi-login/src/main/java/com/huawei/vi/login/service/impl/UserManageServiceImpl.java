package com.huawei.vi.login.service.impl;

import com.huawei.vi.login.mapper.WebUserManagerMapper;
import com.huawei.vi.login.po.UserInfo;
import com.huawei.vi.login.po.WebUserPO;
import com.huawei.vi.login.service.UserManageService;
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
public class UserManageServiceImpl implements UserManageService {
    @Autowired
    WebUserManagerMapper webUserManagerMapper;

    private static final Logger LOG = LoggerFactory.getLogger(UserManageServiceImpl.class);
    @Override
    public List<WebUserPO> selectUserByName(@NonNull String userName) {
        Map<String, Object> userNameMap = new HashMap<String, Object>();
        userNameMap.put(WebUserPO.USER_NAME, userName);
        List<WebUserPO> users = userManagerMapperSelectByCondition(userNameMap);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users;
    }

    @Override
    public UserInfo selectUserInfo(String userName) {
        return webUserManagerMapper.selectUserInfo(userName);
    }

    /**
     * 根据条件查询信息
     *
     * @param map 查询条件
     * @return 用户列表
     */
    private List<WebUserPO> userManagerMapperSelectByCondition(Map<String, Object> map) {
        try {
            return webUserManagerMapper.selectByCondition(map);
        } catch (DataAccessException dataAccessException) {
            LOG.error("userManagerMapperSelectByCondition DataAccessException {} ", dataAccessException.getMessage());
        }
        return Collections.emptyList();
    }
}
