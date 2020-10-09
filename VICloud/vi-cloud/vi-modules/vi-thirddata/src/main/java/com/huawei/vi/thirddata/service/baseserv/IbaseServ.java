/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.baseserv;



import com.huawei.vi.thirddata.db.domain.PageBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Service层的IBaseServ
 *
 * @param <TT> Bean
 * @param <ID> 主键
 * @since 2019-06-03
 */
public interface IbaseServ<TT extends Serializable, ID extends Serializable> {
    /**
     * 创建实体
     *
     * @param tt 实体
     * @return 受影响行数
     */
    int create(TT tt);

    /**
     * 批量创建实体
     *
     * @param ts 实体集合
     * @return 受影响行数
     */
    int createBatch(List<TT> ts);

    /**
     * 根据主键ID删除
     *
     * @param id 主键
     */
    void delete(ID id);

    /**
     * 根据主键ID查询实体
     *
     * @param id 主键
     * @return 查询结果
     */
    TT load(ID id);

    /**
     * 根据主键ID动态更新实体(update set ... where id=xx)
     *
     * @param tt 实体
     * @return 受影响行数
     */
    int modify(TT tt);

    /**
     * 根据条件查询，有分页参数pageBean
     *
     * @param tt 实体
     * @param condition 查询条件
     * @param pages 分页参数
     * @return 查询结果集
     */
    List<TT> selectByPage(TT tt, Map<String, Object> condition, PageBean pages);
}
