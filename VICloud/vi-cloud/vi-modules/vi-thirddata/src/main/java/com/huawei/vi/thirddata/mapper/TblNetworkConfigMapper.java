
package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.thirddata.service.binary.pojo.TblNetworkConfig;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 网段配置Mapper接口
 *
 * @version 1.0, 2018年7月2日
 * @since 2018-07-02
 */
@Repository(value = "networkConfigDao")
@DataSourceSelect(DataSourceEnum.VideoinsightCollecter)
public interface TblNetworkConfigMapper extends IbaseDao<TblNetworkConfig, String> {
    /**
     * 网段配置删除
     *
     * @param map 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int delTblOriginalData(Map<String, Object> map) throws DataAccessException;

    /**
     * 通过ip获取网段规则
     *
     * @param map 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    String getDataByIp(Map<String, Object> map) throws DataAccessException;

    /**
     * 删除表
     *
     * @param map map
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int dropTable(Map<String, Object> map) throws DataAccessException;
}