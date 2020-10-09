
package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.thirddata.service.binary.pojo.TblDiscardIpCollection;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * TblDiscardIpCollectionMapper dao层
 *
 * @since 2019-06-20
 */
@Repository(value = "tblDiscardIpCollectionDao")
@DataSourceSelect(DataSourceEnum.VideoinsightCollecter)
public interface TblDiscardIpCollectionMapper extends IbaseDao<TblDiscardIpCollection, String> {
    /**
     * 无法匹配网段配置的ip新建表保存
     *
     * @param map 入参
     * @return int 返回成功的条数
     * @throws DataAccessException DataAccessException
     */
    int insertDiscardIp(Map<String, Object> map) throws DataAccessException;

    /**
     * 存在即更新
     *
     * @param map 入参
     * @return int 返回成功的条数
     * @throws DataAccessException DataAccessException
     */
    int updateDiscardIp(Map<String, Object> map) throws DataAccessException;

    /**
     * 获取同时存在于tbl_discardip_collection、tbl_network_config表中的ip集合
     *
     * @return List 符合采集增量ip同时存在于网段配置中的条件的ip集合
     * @throws DataAccessException DataAccessException
     */
    List<String> getDiscardIpInNetworkconfig() throws DataAccessException;

    /**
     * 批量删除同时存在于tbl_discardip_collection、tbl_network_config表中的ip
     *
     * @param map 入参
     * @return int 返回反向删除成功的数据条数
     * @throws DataAccessException DataAccessException
     */
    int deleteIpList(Map<String, Object> map) throws DataAccessException;
}
