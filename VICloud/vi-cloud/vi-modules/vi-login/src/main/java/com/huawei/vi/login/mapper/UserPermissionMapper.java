package com.huawei.vi.login.mapper;

import com.huawei.vi.login.po.RoleRelationInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserPermissionMapper {

    /**
     * 查询角色开启权限
     *
     * @param roleId 角色Id
     * @return 开启标志
     */
    List<RoleRelationInfo> selectUserOpenResource(int roleId);

}
