/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.mapper;

import org.springframework.dao.DataAccessException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * baseDao接口
 *
 * @param <TT> 泛型Bean
 * @param <ID> 主键ID
 * @since 2019-08-24
 */
public interface IbaseDao<TT extends Serializable, ID extends Serializable> {
    /**
     * 根据主键删除
     *
     * @param id 主键ID
     * @return 受影响行数
     * @throws DataAccessException 数据操作异常
     */
    int deleteByPrimaryKey(ID id) throws DataAccessException;

    /**
     * 动态插入实体
     *
     * @param bean 实体
     * @return 受影响行数
     * @throws DataAccessException 数据操作异常
     */
    int insertSelective(TT bean) throws DataAccessException;

    /**
     * 批量插入
     *
     * @param bean 实体集合
     * @return 受影响行数
     * @throws DataAccessException 数据操作异常
     */
    int insertBatch(List<TT> bean) throws DataAccessException;

    /**
     * 根据主键ID动态更新实体(update set ... where id=xx)
     *
     * @param bean 实体
     * @return 受影响行数
     * @throws DataAccessException 数据操作异常
     */
    int updateByPrimaryKeySelective(TT bean) throws DataAccessException;

    /**
     * 根据主键ID进行查询
     *
     * @param id 主键ID
     * @return 查询结果
     * @throws DataAccessException 数据操作异常
     */
    TT selectByPrimaryKey(ID id) throws DataAccessException;

    /**
     * 根据条件进行查询
     *
     * @param condition 查询条件
     * @return 查询结果集
     * @throws DataAccessException 数据操作异常
     */
    List<TT> selectByCondition(Map<String, Object> condition) throws DataAccessException;

    /**
     * 根据条件查询总行数
     *
     * @param condition 查询条件
     * @return 总行数
     * @throws DataAccessException 数据操作异常
     */
    int selectRowsNumByCondition(Map<String, Object> condition) throws DataAccessException;
}
