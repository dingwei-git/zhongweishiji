
package com.huawei.vi.thirddata.service.collection;


import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;

import java.util.List;

/**
 * 数据采集service接口
 *
 * @version 1.0, 2018年7月2日
 * @since 2018-07-02
 */
public interface CollectionService {
    /**
     * 获取服务器参数信息
     *
     * @return 服务器集合
     */
    List<TblServerParamConfig> getServerParamInfo();

    /**
     * 获取开始采集周期的周期时间
     *
     * @return String
     */
    String getStartTime();

    /**
     * 开始采集
     *
     * @param choose 开始采集入参
     */
    void startCollection(String choose);

    /**
     * 停止采集
     */
    void stopCollection();

    /**
     * 判断是否有数据
     *
     * @return int
     */
    int isHasData();
}
