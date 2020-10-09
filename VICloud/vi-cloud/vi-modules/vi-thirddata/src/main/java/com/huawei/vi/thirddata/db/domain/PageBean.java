/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.db.domain;

import com.huawei.utils.NumConstant;

import java.io.Serializable;

/**
 * 分页实体
 *
 * @since 2019-06-03
 */
public class PageBean implements Serializable {
    private static final long serialVersionUID = -3057863921814025117L;

    /**
     * 默认每页行数
     */
    private static final int DEFAULT_ROWSPERPAGE = NumConstant.NUM_15;

    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页行数
     */
    private int rowsPerPage = DEFAULT_ROWSPERPAGE;

    /**
     * 最大页码
     */
    private int maxPage;

    /**
     * 总行数
     */
    private int rowsNum;

    /**
     * 无参构造
     */
    public PageBean() {
    }

    /**
     * 有参构造
     *
     * @param pageNum 页数
     * @param rowsPerPage 每页行数
     */
    public PageBean(int pageNum, int rowsPerPage) {
        this.pageNum = pageNum;
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * 获取上一页页码值
     *
     * @return 上一页页码值
     */
    public int getLastPage() {
        if (pageNum > 1) {
            return pageNum - 1;
        }
        return 0;
    }

    /**
     * 获取下一页页码值
     *
     * @return 下一页页码值
     */
    public int getNextPage() {
        if (pageNum < maxPage) {
            return pageNum + 1;
        }
        return 0;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getRowsNum() {
        return rowsNum;
    }

    public void setRowsNum(int rowsNum) {
        this.rowsNum = rowsNum;
    }
}
