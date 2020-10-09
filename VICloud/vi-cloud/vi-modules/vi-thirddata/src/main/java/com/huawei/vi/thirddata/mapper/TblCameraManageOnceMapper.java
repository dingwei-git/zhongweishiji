
package com.huawei.vi.thirddata.mapper;


import com.huawei.vi.thirddata.service.binary.pojo.CaptureHourStatistics;
import com.huawei.vi.thirddata.service.binary.pojo.TblCameraDatailOriginalImageData;
import com.huawei.vi.thirddata.service.binary.pojo.TblCameraDetailOriginalData;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 摄像机详细信息Dao层
 *
 * @since 2019-08-01
 */
@Repository(value = "cameraManageOnceDao")
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface TblCameraManageOnceMapper extends IbaseDao<TblCameraDetailOriginalData, Integer> {
    /**
     * 每次采集开始先调用IVS接口将摄像机详细信息入库
     *
     * @param record 周期对象
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int insert(@Param("relists") List<TblCameraDetailOriginalData> record) throws DataAccessException;

    /**
     * 联合查询，取交集
     *
     * @param map 入参
     * @return list
     * @throws DataAccessException DataAccessException
     */
    List<TblCameraDetailOriginalData> getIntersection(Map<String, Object> map) throws DataAccessException;

    /**
     * 清表
     *
     * @param map 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int delTblOriginalData(Map<String, Object> map) throws DataAccessException;

    /**
     * 每个周期动态建摄像机详细信息表,与组网表取交集后
     *
     * @param map 入参
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int createTblOriginalData(Map<String, Object> map) throws DataAccessException;

    /**
     * 删除表
     *
     * @param map 入参
     * @return int 返回是否删除成功
     * @throws DataAccessException DataAccessException
     */
    int dropTable(Map<String, Object> map) throws DataAccessException;

    /**
     * 创建天维度统计表
     *
     * @param tableName 入参
     * @return int 返回是否创建表成功
     * @throws DataAccessException DataAccessException
     */
    int createCaptureTable(@Param("tableName") String tableName) throws DataAccessException;

    /**
     * 天维度统计表插入数据
     *
     * @param map 入参
     * @return int 返回插入数据是否成功
     * @throws DataAccessException DataAccessException
     */
    int insertCaptureTable(Map<String, Object> map) throws DataAccessException;

    /**
     * 创建小时维度统计表
     *
     * @param map 入参
     * @return int 返回建表是否成功
     * @throws DataAccessException DataAccessException
     */
    int createDimensionTable(Map<String, Object> map) throws DataAccessException;

    /**
     * 小时维度统计表插入数据
     *
     * @param map 入参
     * @return int 返回插入数据是否成功
     * @throws DataAccessException DataAccessException
     */
    int insertImagehourstatistics(Map<String, Object> map) throws DataAccessException;

    /**
     * 统计整点周期前一小时过车、过脸抓拍图片数
     *
     * @param map 入参
     * @return List
     * @throws DataAccessException DataAccessException
     */
    List<CaptureHourStatistics> selectImageCount(Map<String, Object> map) throws DataAccessException;

    List<TblCameraDetailOriginalData> getCameraInfoByState(Map<String, Object> map) throws DataAccessException;

    List<TblCameraDatailOriginalImageData> getCameraInfoAndImage(Map<String, Object> map) throws DataAccessException;
}