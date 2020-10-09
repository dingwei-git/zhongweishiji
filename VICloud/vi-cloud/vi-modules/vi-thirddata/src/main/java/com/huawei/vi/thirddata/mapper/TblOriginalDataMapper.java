
package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.thirddata.service.binary.pojo.TcpOriginalData;
import com.huawei.vi.thirddata.service.binary.pojo.UdpOriginalData;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据采集元数据持久化操作mapper接口
 *
 * @version 1.0, 2018年6月24日
 * @since 2018-06-24
 */
@Repository(value = "originalDataDao")
@DataSourceSelect(DataSourceEnum.VideoinsightCollecter)
public interface TblOriginalDataMapper extends IbaseDao<TcpOriginalData, String> {
    /**
     * 向原始数据表中插入csv格式的数据
     *
     * @param condition 查询条件入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int insertOriginalData(Map<String, Object> condition) throws DataAccessException;

    /**
     * 向原始数据表中插入csv格式的数据
     *
     * @param condition 查询条件入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int insertOriginalDataInLinux(Map<String, Object> condition) throws DataAccessException;

    /**
     * 删除表
     *
     * @param map 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int dropTable(Map<String, Object> map) throws DataAccessException;

    /**
     * 向原始数据UDP表中插入csv格式的数据
     *
     * @param condition 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int insertOriginalUdpData(Map<String, Object> condition) throws DataAccessException;

    /**
     * 向原始数据TCP表中插入csv格式的数据
     *
     * @param condition 入参
     * @return boolean
     * @throws DataAccessException DataAccessException
     */
    int insertOriginalTcpData(Map<String, Object> condition) throws DataAccessException;

    /**
     * 根据周期的时间动态建原始数据表
     *
     * @param condition 入参
     * @return boolean
     * @throws DataAccessException DataAccessException
     */
    int createOriginalData(Map<String, Object> condition) throws DataAccessException;

    /**
     * 根据周期的时间原始数据表创建索引
     *
     * @param condition 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int createOrigIndex(Map<String, Object> condition) throws DataAccessException;

    /**
     * 获取所有的udp会话
     *
     * @param map 查询条件入参
     * @return List
     * @throws DataAccessException DataAccessException
     */
    List<UdpOriginalData> getAllUdpData(Map<String, Object> map) throws DataAccessException;

    /**
     * 获取所有的tcp会话
     *
     * @param map 查询条件入参
     * @return List
     * @throws DataAccessException DataAccessException
     */
    List<TcpOriginalData> getAllTcpData(Map<String, Object> map) throws DataAccessException;

    /**
     * changeFiled
     *
     * @param map 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int changeFiled(Map<String, Object> map) throws DataAccessException;

    /**
     * 删除列
     *
     * @param map 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int dropColumn(Map<String, Object> map) throws DataAccessException;

    /**
     * 通过replace 替换时间格式中的斜杠为中杠
     *
     * @param map 修改条件
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int formatDateType(Map<String, Object> map) throws DataAccessException;

    /**
     * 通过名字展示表，判断表是否存在
     *
     * @param map 入参
     * @return String
     * @throws DataAccessException DataAccessException
     */
    String showTableByname(Map<String, Object> map) throws DataAccessException;

    /**
     * 创建抓拍图片数天表
     *
     * @param tableName 表名
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int createCaptureTable(@Param("tableName") String tableName) throws DataAccessException;

    /**
     * 向原始过车、过脸记录表插入数据
     *
     * @param map 查询条件
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int insertCaptureTable(Map<String, Object> map) throws DataAccessException;

    /**
     * 从摄像机原始信息表获取sn号
     *
     * @param map 查询条件
     * @return List
     * @throws DataAccessException DataAccessException
     */
    List<String> selectCameraSn(Map<String, Object> map) throws DataAccessException;

    /**
     * 向抓拍图片数小时维度统计表插入数据
     *
     * @param map map
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int insertImagehourstatistics(Map<String, Object> map) throws DataAccessException;

    /**
     * 查询摄像机当前周期往前推24h的抓拍图片总数
     *
     * @param map map
     * @return long
     * @throws DataAccessException DataAccessException
     */
    long select24H(Map<String, Object> map) throws DataAccessException;

    /**
     * 查询是否配置IVS服务器参数
     *
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int queryIvsServerParam() throws DataAccessException;
}
