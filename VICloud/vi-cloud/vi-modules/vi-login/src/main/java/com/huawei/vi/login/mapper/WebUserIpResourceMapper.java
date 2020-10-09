package com.huawei.vi.login.mapper;

import com.huawei.vi.login.po.WebLoginRecordPO;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@Mapper
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface WebUserIpResourceMapper {
    /**
     * 根据用户查询用户权限下的ipc
     *
     * @param map 需要查询的条件
     * @return list 查询出的ipc集合
     * @throws DataAccessException DataAccessException
     */
    List<Map<String, Object>> selectIpcsByUser(Map<String, Object> map) throws DataAccessException;

    WebLoginRecordPO selectByUserName(@Param("userName") String usertmpName) throws DataAccessException;

    /**
     * 插入数据记录
     *
     * @param loginRecord 参数
     * @throws DataAccessException sql异常
     */
    void insertRecord(WebLoginRecordPO loginRecord) throws DataAccessException;

    /**
     * 修改记录表中的数据
     *
     * @param map 参数
     * @throws DataAccessException sql异常
     */
    void updateByName(Map<String, Object> map) throws DataAccessException;
}
