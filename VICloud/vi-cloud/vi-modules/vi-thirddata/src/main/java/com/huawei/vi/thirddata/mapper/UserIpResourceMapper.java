package com.huawei.vi.thirddata.mapper;

import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository(value = "userIpResourceMapper")
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface UserIpResourceMapper {

    /**
     * 根据用户查询用户权限下的ipc
     *
     * @param map 需要查询的条件
     * @return list 查询出的ipc集合
     * @throws DataAccessException DataAccessException
     */
    List<Map<String, Object>> selectIpcsByUser(Map<String, Object> map) throws DataAccessException;

    /**
     * 通过用户拥有的ip查询对应的组织层级数
     *
     * @param map 入参map（ip集合与层级id集合）
     * @return List 返回值map集合
     * @throws DataAccessException 数据操作异常
     */
    List<Map<String, String>> getOrganizationsByUser(Map<String, Object> map) throws DataAccessException;
}
