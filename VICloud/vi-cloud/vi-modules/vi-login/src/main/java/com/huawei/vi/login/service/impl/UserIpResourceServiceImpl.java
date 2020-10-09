package com.huawei.vi.login.service.impl;

import com.huawei.utils.NumConstant;
import com.huawei.vi.login.mapper.WebUserIpResourceMapper;
import com.huawei.vi.login.po.WebUserPO;
import com.huawei.vi.login.service.UserIpResourceService;
import com.huawei.vi.login.service.UserManageService;
import com.jovision.jaws.common.config.i18n.MessageSourceUtil;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.properties.PropertiesConfigCommonConst;
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
import java.util.stream.Collectors;

@Service
public class UserIpResourceServiceImpl implements UserIpResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(UserIpResourceServiceImpl.class);
    @Autowired
    UserManageService userManageService;

    @Autowired
    WebUserIpResourceMapper webUserIpResourceMapper;

    @Autowired
    private MessageSourceUtil messageSourceUtil;


    @Override
    public List<String> getIpcIpByUserOrId(@NonNull String loginUserName, List<String> id, @NonNull String tableName) {
        List<Map<String, Object>> ipcs = getIpcsByUser(loginUserName, id, tableName);
        return ipcs.stream().map(item -> String.valueOf(item.get(CommonConst.IP))).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getIpcsByUser(@NonNull String loginUserName, List<String> id,
                                                   @NonNull String tableName) {
        List<WebUserPO> users = userManageService.selectUserByName(loginUserName);
        if (CollectionUtils.isEmpty(users)) {
            LOG.error("users is empty.");
            throw new IllegalAccessError("用户不存在");
        }
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put(CommonConst.TABLE, tableName);
        conditions.put(WebUserPO.IS_DEFAULT, users.get(0).getIsDefault());
        conditions.put(WebUserPO.USER_NAME, loginUserName);
        if (CollectionUtils.isNotEmpty(id)) {
            // 获取权限id是组网关系表的第几层组织
            int length = id.get(0).length() / messageSourceUtil.getMessage(CommonConst.STATION_STARTENCODING).length();
            conditions.put(CommonConst.ID, CommonConst.ID + (length - NumConstant.NUM_1));
            conditions.put(CommonConst.ID_LIST, id);
        }
        return userIpResourceMapperSelectIpcsByUser(conditions);
    }

    private List<Map<String, Object>> userIpResourceMapperSelectIpcsByUser(Map<String, Object> map) {
        try {
            return webUserIpResourceMapper.selectIpcsByUser(map);
        } catch (DataAccessException e) {
            LOG.error("userIpResourceMapperSelectIpcsByUser DataAccessException {}", e.getMessage());
        }
        return Collections.emptyList();
    }

}
