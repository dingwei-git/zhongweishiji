/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.importdataformcsv;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 原始CSV数据处理
 *
 * @version 1.0, 2018年7月17日
 * @since 2018-07-17
 */
public interface IoriginalDataService {
    /**
     * 遍历
     *
     * @return Map
     */
    Map<String, List<String>> sort();

    /**
     * 开始
     *
     * @param allPathlist 路径列表
     * @param tcppathlist tcp路径列表
     * @param udppathlist udp路径列表
     * @param periodStartDate 周期开始时间
     * @return Map
     */
    Map<String, Object> start(List<String> allPathlist, List<String> tcppathlist, List<String> udppathlist,
                              Date periodStartDate);

    /**
     * 开始
     */
    void start();

    /**
     * creareOriginalAfterDropTbl
     *
     * @param filePath 文件路径
     * @param periodStartDate 周期开始时间
     * @param preTableName 表名字
     */
    void creareOriginalAfterDropTbl(String filePath, Date periodStartDate, String preTableName);

    /**
     * 创建原始数据表
     *
     * @param filePath 文件路径
     * @param tabName 表名字
     * @return boolean
     */
    boolean createOriginalDataTable(String filePath, String tabName);
}
