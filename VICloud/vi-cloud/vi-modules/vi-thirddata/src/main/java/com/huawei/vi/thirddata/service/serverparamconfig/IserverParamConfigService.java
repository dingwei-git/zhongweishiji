/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.serverparamconfig;

import com.alibaba.fastjson.JSONObject;
import com.huawei.vi.thirddata.service.baseserv.IbaseServ;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;


import java.util.List;

/**
 * 服务器参数配置Service实现类
 *
 * @version 1.0, 2018年7月2日
 * @since 2018-07-02
 */
public interface IserverParamConfigService extends IbaseServ<TblServerParamConfig, Integer> {
    /**
     * 修改 服务器参数配置
     *
     * @param serverParamConfig 入参
     * @return int
     */
    int updateServerParamConfig(TblServerParamConfig serverParamConfig);

    /**
     * 修改 服务器参数配置
     *
     * @param serverParamConfig 入参
     * @return int
     */
    int updateServerParamConfigByIdAndIpAddress(TblServerParamConfig serverParamConfig);

    /**
     * 删除 服务器参数配置
     *
     * @param id 服务器id
     * @return int
     */
    int deleteServerParamConfig(Integer id);

    /**
     * 獲取所有符合條件的服务器参数配置
     *
     * @param serverParamConfig 入参
     * @return List<TblServerParamConfig/>
     */
    List<TblServerParamConfig> getAllServerParamConfig(TblServerParamConfig serverParamConfig);

    /**
     * 图像诊断服务化改造后的参数add操作
     *
     * @param tblServerParamConfig 实体类
     * @param jsonObject 返回体
     */
    void processTxzdParamConfig(TblServerParamConfig tblServerParamConfig, JSONObject jsonObject);

    /**
     * 图像诊断服务化改造后的服务器状态刷新
     *
     * @param lists 所刷新的数据集
     * @param serverParamConfig 实体类
     */
    void refreshServerStatus(List<TblServerParamConfig> lists, TblServerParamConfig serverParamConfig);

    /**
     * 通过正则表达式匹配出ping的返回结果中的丢失百分比 NULL:表示失败，true:成功，false:超时 判断标准：丢失率为0时，表示成功，丢失率非0为超时，其余为失败
     *
     * @param ipAddress ipAddress
     * @return 0：连接成功；1:连接超时；-1：连接失败
     */
    int judgmentPingResoult(String ipAddress);

    /**
     * 通过状态值更新服务器参数属性
     *
     * @param status 状态值
     * @param serverParamConfig 实体类
     */
    void transMessageByStatus(Integer status, TblServerParamConfig serverParamConfig);

    /**
     * 服务器参数配置内容校验
     *
     * @param serverParamConfig 对象实体
     * @return 是否存在服务器参数配置的异常
     */
    boolean isImageDiagnosisFail(TblServerParamConfig serverParamConfig);

    /**
     * 修改 服务器参数配置
     *
     * @param tblServerParamConfig 实体类
     * @return int
     */
    int updateServerParamConfigIp(TblServerParamConfig tblServerParamConfig);
}
