package com.huawei.vi.thirddata.service.userIpresorce.impl;

import com.huawei.utils.CollectionUtil;
import com.huawei.utils.NumConstant;
import com.huawei.vi.thirddata.mapper.UserIpResourceMapper;
import com.huawei.vi.thirddata.service.baseserv.impl.BaseServImpl;
import com.huawei.vi.thirddata.service.userIpresorce.UserIpResourceService;
import com.huawei.vi.thirddata.service.usermanage.UserManageService;
import com.jovision.jaws.common.config.redis.RedisOperatingService;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.pojo.User;
import com.jovision.jaws.common.pojo.UserIpResource;
import com.jovision.jaws.common.util.BaseConfig;
import com.jovision.jaws.common.util.RedisUtil;
import com.jovision.jaws.common.util.ReportConstant;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

@Service(value = "userIpResourceService")
public class UserIpResourceServiceImpl extends BaseServImpl<UserIpResource, String> implements UserIpResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(UserIpResourceServiceImpl.class);

    private static final String LEVEL_NUM = "levelNum";

    @Autowired
    private UserManageService userManageService;
    @Autowired
    private UserIpResourceMapper userIpResourceMapper;
    @Autowired
    private RedisUtil redis;

    /**
     * 通过用户名和层级数(层级数若不传则默认查所有层级)查询用户所拥有的组织
     *
     * @param userName 用户名
     * @param levelNum 层级数
     * @param tableName 要进行关联的表名（tbl_ipc_ip或tbl_ipc_ip_tmp）
     * @return Map 组织层级map
     */
    @Override
    public Map<String, List<Map<String, String>>> getOrganizationsByUser(String userName, Integer levelNum,
                                                                         String tableName) {
        List<User> users = userManageService.selectUserByName(userName);
        Map<String, List<Map<String, String>>> returnMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(CommonConst.TABLE, tableName);

        // 非admin用户
        if (users!=null&&users.size()>0&&users.get(NumConstant.NUM_0).getIsDefault() != User.DEFAULT_USER) {
            List<String> ipList = getIpcIpByUserOrId(userName, null, tableName);
            if (CollectionUtil.isEmpty(ipList)) {
                LOG.error("ipList is empty or null");
                return returnMap;
            }
            paramMap.put("ipList", ipList);
        }
        if (levelNum == null) {
            // 根据ip查询用户所拥有的全部组织层级
//            int level = ProgressInfo.getInstance().getTmpLevel();
            String ipclLevel = redis.getValue("ipclLevel");
            int level = 3;
            if(StringUtils.isNotEmpty(ipclLevel)){
                level = Integer.parseInt(ipclLevel);
            }
            for (int ii = 0; ii < level; ii++) {
                paramMap.put(LEVEL_NUM, ii);
                returnMap.put(ReportConstant.IPCLEVEL + (ii + NumConstant.NUM_1),
                        userIpResourceMapper.getOrganizationsByUser(paramMap));
            }
        } else {
            // 根据所选层级进行查询
            paramMap.put(LEVEL_NUM, levelNum - NumConstant.NUM_1);
            returnMap.put(ReportConstant.IPCLEVEL + levelNum, userIpResourceMapper.getOrganizationsByUser(paramMap));
        }
        return returnMap;
    }

    /**
     * 根据用户名查询权限下的ipc集合
     */
    @Override
    public List<String> getIpcIpByUserOrId(@NonNull String loginUserName, List<String> id, @NonNull String tableName) {
        List<Map<String, Object>> ipcs = getIpcsByUser(loginUserName, id, tableName);
        return ipcs.stream().map(item -> String.valueOf(item.get(CommonConst.IP))).collect(Collectors.toList());
    }

    /**
     * 根据用户名查询权限下的ipc详细信息(可用于ipc的ztree结果)
     */
    @Override
    public List<Map<String, Object>> getIpcsByUser(@NonNull String loginUserName, List<String> id,
                                                   @NonNull String tableName) {
        List<User> users = userManageService.selectUserByName(loginUserName);
        if (CollectionUtils.isEmpty(users)) {
            LOG.error("users is empty.");
            throw new IllegalAccessError("users is empty.");
        }
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put(CommonConst.TABLE, tableName);
        conditions.put(User.IS_DEFAULT, users.get(0).getIsDefault());
        conditions.put(User.USER_NAME, loginUserName);
        if (CollectionUtils.isNotEmpty(id)) {
            // 获取权限id是组网关系表的第几层组织
            int length = id.get(0).length() / BaseConfig.getGlobalMap().get(CommonConst.STATION_STARTENCODING).length();
            conditions.put(CommonConst.ID, CommonConst.ID + (length - NumConstant.NUM_1));
            conditions.put(CommonConst.ID_LIST, id);
        }
        return userIpResourceMapperSelectIpcsByUser(conditions);
    }

    private List<Map<String, Object>> userIpResourceMapperSelectIpcsByUser(Map<String, Object> map) {
        try {
            return userIpResourceMapper.selectIpcsByUser(map);
        } catch (DataAccessException e) {
            LOG.error("userIpResourceMapperSelectIpcsByUser DataAccessException {}", e.getMessage());
        }
        return Collections.emptyList();
    }


    @Override
    protected Map<String, Object> getMap(UserIpResource userIpResource, Map<String, Object> condition) {
        return null;
    }
}
