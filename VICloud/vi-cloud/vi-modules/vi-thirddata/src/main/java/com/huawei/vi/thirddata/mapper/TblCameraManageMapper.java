/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.mapper;


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
@Repository(value = "cameraManageDao")
@DataSourceSelect(DataSourceEnum.VideoinsightCollecter)
public interface TblCameraManageMapper extends IbaseDao<TblCameraDetailOriginalData, Integer> {
    /**
     * 将组网关系表与调用IVS接口取交集的摄像机详细信息入库
     *
     * @param tableName 表名
     * @param record 周期对象
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int insert(@Param("tabName") String tableName, @Param("relists") List<TblCameraDetailOriginalData> record)
        throws DataAccessException;

    /**
     * 查询所有满足采集条件的摄像机详细信息
     *
     * @param map 入参
     * @return list
     * @throws DataAccessException DataAccessException
     */
    List<TblCameraDetailOriginalData> getValidateCameraList(Map<String, Object> map) throws DataAccessException;

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




}