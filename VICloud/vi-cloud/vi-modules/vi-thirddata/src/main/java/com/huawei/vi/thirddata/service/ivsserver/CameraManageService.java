package com.huawei.vi.thirddata.service.ivsserver;

import com.huawei.vi.thirddata.service.baseserv.IbaseServ;
import com.huawei.vi.thirddata.service.binary.pojo.TblCameraDetailOriginalData;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;

import java.util.List;

/**
 * CameraManageService
 */
public interface CameraManageService extends IbaseServ<TblCameraDetailOriginalData, Integer> {
    /**
     * 清空原始数据临时表
     *
     * @param tableName 表名
     */
    void truncateCameraOriginal(String tableName);

    /**
     * IVS分页获取数据后，原始数据入临时表方法
     *
     * @param ipAddress IP地址
     * @param account 账号
     * @param secretCode 密码
     * @return 插入行数
     */
    int saveCameraOriginal(String ipAddress, String account, String secretCode);

    /**
     * 取交集后的入库方法
     *
     * @param tableName 表名
     * @return 插入行数
     */
    int saveCameraIntersection(String tableName);

    /**
     * 获取某一周期的摄像机详细信息
     *
     * @param tabName 表名
     * @return 参与分析的摄像机详细信息列表
     */
    List<TblCameraDetailOriginalData> getCameraIntersection(String tabName);

    /**
     * 是否登录IVS
     *
     * @param ipAddress IP地址
     * @param account 账号
     * @param secretCode 密码
     * @return 是否登录成功
     */
    boolean isLoginIvs(String ipAddress, String account, String secretCode);


    /**
     * 登出操作
     *
     * @param ipAddress IP地址
     */
    void logOutIvs(String ipAddress);

    /**
     * 更新服务参数表状态
     *
     * @param isLogin 是否登录成功
     * @param tspc 服务配置表的实体
     */
    void updateServerConnectStatus(boolean isLogin, TblServerParamConfig tspc);




}
