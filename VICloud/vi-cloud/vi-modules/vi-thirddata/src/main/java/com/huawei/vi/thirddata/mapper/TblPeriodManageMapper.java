/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.mapper;


import com.huawei.vi.thirddata.service.binary.pojo.TblPeriodManage;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 周期管理表mapper接口
 *
 * @version 1.0, 2018年6月24日
 * @since 2018-06-24
 */
@Repository(value = "periodManageDao")
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface TblPeriodManageMapper extends IbaseDao<TblPeriodManage, Integer> {
    /**
     * 获取指定周期信息条数
     *
     * @param periodStartTime 周期
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int getPeriodStartTimeCount(String periodStartTime) throws DataAccessException;

    /**
     * 查询周期总数，用于判断是否是第一次进行数据采集
     *
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int getDateCount() throws DataAccessException;

    /**
     * 每次采集完成之后将采集开始时间以及是否分析完成入库
     *
     * @param record 周期对象
     * @return int
     * @throws DataAccessException DataAccessException
     */
    int insert(TblPeriodManage record) throws DataAccessException;

    /**
     * 方法名:getBeforePeriod 描述:获取小于某个周期的时间集合
     *
     * @param map 参数对
     * @return List 创建日期:2018年3月21日
     * @throws DataAccessException DataAccessException
     */
    List<Date> getBeforePeriod(Map<String, Object> map) throws DataAccessException;

    /**
     * 获取最新的周期时间
     *
     * @return String
     * @throws DataAccessException DataAccessException
     */
    String getNewDate() throws DataAccessException;

    /**
     * 获取离当前周期最近的一次周期时间
     *
     * @param periodStartTime 入参
     * @return 返回周期时间
     * @throws DataAccessException DataAccessException
     */
    String getLastNewDate(String periodStartTime) throws DataAccessException;
}