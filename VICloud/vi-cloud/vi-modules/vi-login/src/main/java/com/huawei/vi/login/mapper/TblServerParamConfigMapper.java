package com.huawei.vi.login.mapper;

import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface TblServerParamConfigMapper {
    /**
     * 查询服务器参数配置表中是否配置过参数
     *
     * @param condition 条件
     * @return int 查询到的记录个数
     * @throws DataAccessException DataAccessException
     */
    int selectServerParamConfig(Map<String, Object> condition) throws DataAccessException;
}
