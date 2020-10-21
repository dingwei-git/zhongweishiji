package com.huawei.vi.thirddata.mapper;


import com.huawei.vi.entity.po.TblIpcIp;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * TblIpcIpMapper
 *
 * @since 2019-08-01
 */
@Repository
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface TblIpcIpMapper extends IbaseDao<TblIpcIp, String> {



    Integer queryMaxIp();

    /**
     * 从tbl_ipc_ip表中查询
     *
     * @param map 查询IPC条件
     * @return List 组网信息集合
     * @throws DataAccessException sql异常
     */
    List<TblIpcIp> queryIp(Map<String, Object> map) throws DataAccessException;


    /**
     * 从tbl_ipc_ip表中查询
     *
     * @param map 查询IPC条件
     * @return List 组网信息集合
     * @throws DataAccessException sql异常
     */
    List<Map<String,Object>> queryIpcByParams(Map<String, Object> map);

    /**
     * 从tbl_ipc_ip表中查询
     *
     * @param map 查询IPC条件
     * @return List 组网信息集合
     * @throws DataAccessException sql异常
     */
    Integer queryIpcByParamsCount(Map<String, Object> map);

    /**
     * 得到摄像机总数
     *
     * @return int
     * @throws DataAccessException sql异常
     */
    int queryAllCameras() throws DataAccessException;

    /**
     * 获取所有的ipc信息
     *
     */
    List<Map<String,Object>> queryAllIPC();

    /**
     * 查询免考核的摄像机ip
     *
     * @param map map
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int queryIsCheckCameras(Map<String, Object> map) throws DataAccessException;


    /**
     * 功能描述: <br>更新表数据
     * @Param: [param]
     * @Return: void
     * @Author: pangxh
     * @Date: 2020/8/14 19:37
     */
    int updateIpcByCameraNum(Map<String,Object> param);

    /**
     * 功能描述: 根据传入的camera集合查询出已经在表中的ipc
     * @Param: [list]
     * @Return: java.util.List<java.lang.String>
     * @Author: pangxh
     * @Date: 2020/8/14 19:48
     */
    List<String> queryIpcExist(List<String> list);

    /*
    * 获取摄像机编码
    * */
    List<TblIpcIp> getCameraSn(Map map)throws DataAccessException;


    /**
     * 功能描述: 获取最大的共杆编码
     * @Param: []
     * @Return: int
     * @Author: pangxh
     * @Date: 2020/8/20 11:29
     */
    int maxCopoleCode();

    int insertBatchIpcTable(List<Map<String,Object>> networkList);

    int insertBatchIpcSelective(Map<String,Object> map);

    /**
     * 通过查询基表，获取最新的组网关系表
     *
     * @return 最新的组网关系表
     * @throws DataAccessException 数据访问异常
     */
    String getNewestNetworkTable() throws DataAccessException;

    List<Map> getOrganization(Map<String,Object>map) throws DataAccessException;

    List<Map> getCameraIds(Map<String,Object>map) throws DataAccessException;

    List<Map> getCameraSnById(Map<String,Object>map) throws DataAccessException;

    String getFiled(Map map) throws DataAccessException;

    String getFileds(Map map)throws DataAccessException;

    /*
    * 图片溯源新
    * */
    List<Map<String,Object>> getPicturesCount(Map<String,Object>map) throws DataAccessException;

    List<Map<String,Object>> getPicturesCountHour(Map<String,Object>map) throws DataAccessException;

    List<String> qryCameraManagerOriList(Map<String,Object>map) throws DataAccessException;

    String getRelationShip() throws DataAccessException;

    List<Map<String,Object>> getlevelNames(Map<String,Object>map) throws DataAccessException;

    List<String> getTime(Map<String,Object>map)throws DataAccessException;

    List<String> getTimes(Map<String,Object>map)throws DataAccessException;

    List<String> getPeriodTime(Map<String,Object>map)throws DataAccessException;

    List<String> getPeriodTimes(Map<String,Object>map)throws DataAccessException;

    List<Map<String,Object>> getPicturesCountDay(Map<String,Object>map) throws DataAccessException;

    List<Map<String,Object>> getPicturesCountWD(Map<String,Object>map) throws DataAccessException;
    /*
    * 按照摄像机查询（新）
    * */
    List<Map<String,Object>> getPicturesCountCamera(Map<String,Object>map) throws DataAccessException;

    List<Map<String,Object>> getCamernSns(Map<String,Object>map) throws DataAccessException;

    List<Map<String,Object>> getPicturesCountCameraDay(Map<String,Object>map) throws DataAccessException;

    String getMaxTime()throws DataAccessException;

    List<Map<String,Object>> getcamSns(Map<String,Object>map)throws DataAccessException;
}