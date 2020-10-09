/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.dzserver;



import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;

import java.util.Date;
import java.util.List;

/**
 * 东智服务器数据采集service接口
 *
 * @version 1.0, 2018年9月10日
 * @since 2018-09-10
 */
public interface DzCollectService {
    /**
     * 东智服务器数据采集
     *
     * @param beginTime 开始采集的时间
     * @param tblServerParamConfig 东智服务器数据采集接口
     */
    void dzServerCollectStart(List<TblServerParamConfig> tblServerParamConfig, Date beginTime);

    /**
     * 查询东智服务器配置
     *
     * @return String 返回东智服务器参数配置的ip+：+port
     */
    String getFirstDzServerParamConfig();

    /**
     * 批量删除同时存在于tbl_discardip_collection、tbl_network_config表中的ip
     */
    void deleteDiscardIpInNetworkConfig();

    /**
     * dz轮循诊断服务化改造数据采集
     *
     * @param beginTime 开始采集的时间
     * @param tblServerParamConfig 东智服务器数据采集接口
     */
    void dzServerInternalCollect(List<TblServerParamConfig> tblServerParamConfig, Date beginTime);
}
