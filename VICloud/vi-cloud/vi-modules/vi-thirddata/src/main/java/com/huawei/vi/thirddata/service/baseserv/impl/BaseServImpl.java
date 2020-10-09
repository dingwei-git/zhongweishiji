/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.baseserv.impl;

import com.huawei.utils.Bean2Map;
import com.huawei.vi.thirddata.db.domain.PageBean;
import com.huawei.vi.thirddata.mapper.IbaseDao;
import com.huawei.vi.thirddata.service.baseserv.IbaseServ;
import com.jovision.jaws.common.util.TableCommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BaseServ 的实现类
 *
 * @param <TT>
 * @param <ID>
 * @since 2019-06-03
 */

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public abstract class BaseServImpl<TT extends Serializable, ID extends Serializable> implements IbaseServ<TT, ID> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IbaseServ.class);

    private static final int NEGATIVE_ONE = -1;

    private IbaseDao<TT, ID> baseDao;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int create(TT user) {
        try {
            return baseDao.insertSelective(user);
        } catch (DataAccessException e) {
            LOGGER.error("insert falied:{}", e.getMessage());
        }
        return NEGATIVE_ONE;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int createBatch(List<TT> ts) {
        try {
            return baseDao.insertBatch(ts);
        } catch (DataAccessException e) {
            LOGGER.error("insertBatch DataAccessException {}", e.getMessage());
        }
        return NEGATIVE_ONE;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void delete(ID id) {
        TT bean = load(id);
        if (bean != null) {
            try {
                baseDao.deleteByPrimaryKey(id);
            } catch (DataAccessException e) {
                LOGGER.error("deleteByPrimaryKey DataAccessException {} ", e.getMessage());
            }
        }
    }

    @Override
    public TT load(ID id) {
        try {
            return baseDao.selectByPrimaryKey(id);
        } catch (DataAccessException e) {
            LOGGER.error("selectByPrimaryKey DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int modify(TT recod) {
        try {
            return baseDao.updateByPrimaryKeySelective(recod);
        } catch (DataAccessException e) {
            LOGGER.error("updateByPrimaryKeySelective DataAccessException {} ", e.getMessage());
        }
        return NEGATIVE_ONE;
    }

    @Override
    public List<TT> selectByPage(TT tt, Map<String, Object> condition, PageBean pages) {
        Map<String, Object> maps = getMap(tt, condition);
        if (pages != null && pages.getRowsNum() > 0) {
            if (pages.getPageNum() < 1) {
                pages.setPageNum(1);
            }
            if (pages.getMaxPage() < 1) {
                try {
                    int rowsNum = baseDao.selectRowsNumByCondition(maps);
                    if (rowsNum < 1) {
                        return null;
                    }
                    int maxPage = (rowsNum - 1 + pages.getRowsPerPage()) / pages.getRowsPerPage();
                    pages.setMaxPage(maxPage);
                    pages.setRowsNum(rowsNum);
                } catch (DataAccessException e) {
                    LOGGER.error("selectRowsNumByCondition DataAccessException {} ", e.getMessage());
                    return null;
                }
            }
            if (pages.getPageNum() > pages.getMaxPage()) {
                pages.setPageNum(pages.getMaxPage());
            }
            maps.put(TableCommonConstant.PAGEBEAN_PAGENUM, pages.getPageNum());
            maps.put(TableCommonConstant.PAGEBEAN_ROWSPERPAGE, pages.getRowsPerPage());
        }
        try {
            return baseDao.selectByCondition(maps);
        } catch (DataAccessException e) {
            LOGGER.error("selectByCondition DataAccessException {} ", e.getMessage());
            return null;
        }
    }

    /**
     * 根据条件查询结果
     *
     * @param tt 对象
     * @param condition 条件
     * @return 结果map
     */
    protected abstract Map<String, Object> getMap(TT tt, Map<String, Object> condition);



    protected void setBaseDao(IbaseDao<TT, ID> baseDao) {
        this.baseDao = baseDao;
    }
}
