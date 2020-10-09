/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.mapper;


import com.huawei.vi.thirddata.service.binary.pojo.DzOriginalData;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * TblDzOriginalDataDao
 *
 * @version 1.0, 2018年9月12日
 * @since 2018-09-12
 */
@Repository(value = "TblDzOriginalDataDao")
@DataSourceSelect(DataSourceEnum.VideoinsightCollecter)
public interface TblDzOriginalDataMapper extends IbaseDao<DzOriginalData, String> {
    /**
     * 创建东智服务器原始数据入库表
     *
     * @param map 入参
     * @return isCreateDzOriginalDataTableFlag 返回是否成功
     * @throws DataAccessException DataAccessException
     */
    int createDzOriginalDataTable(Map<String, Object> map) throws DataAccessException;

    /**
     * 删除表
     *
     * @param map 入参
     * @return isDropTableFlag 返回是否删除成功
     * @throws DataAccessException DataAccessException
     */
    boolean dropTable(Map<String, Object> map) throws DataAccessException;

    /**
     * 东智服务器摄像机最新图像质量诊断结果入库
     *
     * @param map 数据入库参数设置
     * @return isInsertDzOriginalDataFlag 返回是否插入成功
     * @throws DataAccessException DataAccessException
     */
    int insertDzOriginalData(Map<String, Object> map) throws DataAccessException;

    /**
     * 更新Kpi指标数据
     *
     * @param map 入参
     * @return int 返回成功的条数
     * @throws DataAccessException DataAccessException
     */
    int updateDlkDefaultData(Map<String, Object> map) throws DataAccessException;

    /**
     * 获取重复cameraId信息
     *
     * @param map 入参
     * @return List 重复cameraId对应的数据信息
     * @throws DataAccessException DataAccessException
     */
    List<String> getRepeatCameraId(Map<String, Object> map) throws DataAccessException;

    /**
     * 获取DLK故障最新值信息
     *
     * @param map 入参
     * @return DzOriginalData 重复cameraId对应的数据信息
     * @throws DataAccessException DataAccessException
     */
    DzOriginalData getMinData(Map<String, Object> map) throws DataAccessException;

    /**
     * 获取最小值对应的图片路径
     *
     * @param map 入参
     * @return DzOriginalData 返回符合条件的原始数据对象
     * @throws DataAccessException DataAccessException
     */
    DzOriginalData getPictureUrl(Map<String, Object> map) throws DataAccessException;

    /**
     * 删除重复的信息
     *
     * @param map 入参
     * @return int.
     * @throws DataAccessException DataAccessException
     */
    int deleteRepeatInfo(Map<String, Object> map) throws DataAccessException;

    /**
     * 更新上一个中的诊断结果至这个周期
     *
     * @param map 入参
     * @return int 返回成功的条数
     * @throws DataAccessException DataAccessException
     */
    int updateOldDiagResultIndex(Map<String, Object> map) throws DataAccessException;

    /**
     * 更新这个周期表中是否为最新诊断结果标识
     *
     * @param map 入参
     * @return int 返回成功的条数
     * @throws DataAccessException DataAccessException
     */
    int updateDiagResultFlag(Map<String, Object> map) throws DataAccessException;
}
