package com.huawei.vi.login.mapper;

import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface RoleInfoMapper {
    /**
     * 查询用户角色id
     *
     * @param userId 用户id
     * @return 用户角色id集
     * @throws DataAccessException 数据库异常
     */
    List<Integer> selectUserRoleId(int userId) throws DataAccessException;
}
