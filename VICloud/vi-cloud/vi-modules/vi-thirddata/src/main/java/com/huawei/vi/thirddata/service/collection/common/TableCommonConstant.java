/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.collection.common;

/**
 * 用于收录了数据表格中定义列名和实体之间的对应关系，便于替换，在代码中采用此常量形成键值对
 *
 * @since 2019-08-22
 */
public final class TableCommonConstant {
    /**
     * 数据库表名:摄像机详细信息表
     */
    public static final String TBL_CAMERA_MANAGER_ORIGINAL = "tbl_camera_manager_original";

    /**
     * 抓拍图片原始表名
     */
    public static final String IMAGE_TABLENAME = "tbl_image_day_statistics";

    /**
     * 小时维度统计表名
     */
    public static final String STATISTICS_TABLENAME = "tbl_image_hour_statistics";

    /**
     * 分页 pageNum 页码值
     */
    public static final String PAGEBEAN_PAGENUM = "pageNum";

    /**
     * 分页 rowsPerPage 每页行数
     */
    public static final String PAGEBEAN_ROWSPERPAGE = "rowsPerPage";

    /**
     * 构造函数
     */
    private TableCommonConstant() {
    }
}
