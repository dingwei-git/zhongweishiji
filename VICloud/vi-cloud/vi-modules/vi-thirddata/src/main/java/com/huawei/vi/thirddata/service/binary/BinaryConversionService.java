package com.huawei.vi.thirddata.service.binary;

import com.alibaba.fastjson.JSONObject;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;


import java.util.Date;
import java.util.List;

/**
 * 二进制码解析入库数据接口 连接服务器；请求数据；返回二进制码；解析二进制码；获取周期存入周期管理表；解析后的数据入库；生成.scv文件；多个服务器请求处理的数据进行合并...
 *
 * @version 1.0, 2018年6月19日
 * @since 2018-06-19
 */
public interface BinaryConversionService {
    /**
     * 登录连接服务器 执行登录，获取session，如果登录失败，打印返回信息
     *
     * @param url 登录服务器链接信息
     * @param usertmpName 用户名
     * @param secretCode 密码
     * @return String 登录失败返回相应的错误码；成功则返回
     */
    JSONObject login(String url, String usertmpName, String secretCode);

    /**
     * 登出服务器
     *
     * @param url 登出服务器链接信息
     * @param session 登录后返回的sessionId
     */
    void logout(String url, String session);

    /**
     * 修改服务器链接状态
     *
     * @param obj （登录成功之后返回的obj对象）
     * @param tspc （服务器管理列表对象）
     */
    void updateServerConnectStatus(JSONObject obj, TblServerParamConfig tspc);

    /**
     * 数据处理开始 登录科来服务器，获取二进制数据并进行转换为需要的数据，入库、生成csv文件等
     *
     * @param tblServerParamConfig 服务器参数配置
     * @param beginTime 开始采集数据的时间
     */
    void dataProcessingStart(List<TblServerParamConfig> tblServerParamConfig, Date beginTime);

    /**
     * 从videoinsight获取语言
     */
    void getLanguage();

    /**
     * 数据采集完毕之后周期表入库 当DLK数据或者探针数据任何一个采集入库完成即在周期管理表中插入当前周期数据
     *
     * @param beginTime 数据采集时间
     */
    void insertPeriodStartTime(Date beginTime);

    /**
     * 获取解析入库csv文件的do
     */
    void uploadNetWorkConfig();
}
