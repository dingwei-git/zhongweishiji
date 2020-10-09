
package com.huawei.vi.thirddata.mapper;


import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 服务器参数配置Mapper接口
 *
 * @version 1.0, 2018年7月2日
 * @since 2018-07-02
 */
@Repository(value = "serverParamConfigDao")
@DataSourceSelect(DataSourceEnum.VideoinsightCollecter)
public interface TblServerParamConfigMapper extends IbaseDao<TblServerParamConfig, Integer> {
    /**
     * 修改服务器配置
     *
     * @param record 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int updateByPrimaryKeyAndIpAddressSelective(TblServerParamConfig record) throws DataAccessException;

    /**
     * 修改服务器配置通过主键
     *
     * @param record 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int updateByPrimaryKey(TblServerParamConfig record) throws DataAccessException;

    /**
     * 查询所有服务器配置
     *
     * @return List
     * @throws DataAccessException DataAccessException
     */
    List<TblServerParamConfig> selectAll() throws DataAccessException;

    /**
     * 查询东智服务器配置
     *
     * @return String 返回东智服务器参数配置的ip+：+port
     * @throws DataAccessException DataAccessException
     */
    String selectFirstDzServerParamConfig() throws DataAccessException;

    /**
     * 根据IP查询已包含子类型个数
     *
     * @param tblServerParam 查询条件
     * @return 包含子类型个数
     */
    int getServerSubTypeCountByIp(TblServerParamConfig tblServerParam);

    /**
     * 修改服务器配置IP
     *
     * @param serverParamConfig 实体类
     * @return 修改记录数
     */
    int updateByPrimaryKeySelectiveIp(TblServerParamConfig serverParamConfig);

    /*
    * 查询摄像机在线
    * */
    List<TblServerParamConfig> selectOnline() throws DataAccessException;

    /*
    * 获取服务相关信息
    * */
    List<TblServerParamConfig> selectIpAndRemark(Map map) throws DataAccessException;


    /*
     * 获取同步DZ的ip或platformid
     * */
    List<Map<String,Object>> selectIpOrPlatformid(Map<String,Object> param);


}