package com.huawei.vi.thirddata.service.ivsserver;

import com.huawei.vi.entity.vo.ImageDayStatisticsVcnVO;
import com.huawei.vi.thirddata.service.binary.pojo.CaptureImageOriginal;
import com.huawei.vi.thirddata.service.binary.pojo.TblCameraDatailOriginalImageData;
import com.huawei.vi.thirddata.service.binary.pojo.TblCameraDetailOriginalData;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;


import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * IvsService
 *
 * @since 2019-08-06
 */
public interface IvsService {
    /**
     * IVS服务器数据采集
     *
     * @param beginTime 开始采集的时间
     * @param tblServerParamConfig IVS服务器数据采集接口
     */
    void ivsCollect(List<TblServerParamConfig> tblServerParamConfig, Date beginTime);

    /*
    * IVS服务器数据采集
    * */
    void ivsCollects(List<TblServerParamConfig> tblServerParamConfig,Date beginTime);

    /**
     * 分批次处理过车过脸抓拍图片数据
     *
     * @param originalList 抓拍图片数据
     */
    void captureImageCollect(List<CaptureImageOriginal> originalList);

    /**
     * 开始采集
     *
     * @param beginTime 开始时间
     * @param url 地址
     * @param tblServerParamConfig 配置
     */
    void ivsCollectStart(Date beginTime, String url, TblServerParamConfig tblServerParamConfig);

    /**
     * 查询是否配置IVS服务器
     *
     * @return int
     */
    int queryIvsServerParam();

    /**
     * 获取摄像机详细信息
     * @return 摄像机详细信息列表
     */
    List<TblCameraDetailOriginalData> selectCameraInfoByState(HttpServletResponse response);

    /**
     * 获取摄像机以及图片详细信息
     *
     * @param stats 摄像机状态
     * @return 摄像机详细信息列表
     */
    List<TblCameraDatailOriginalImageData> selectCameraInfoAndImageByState(String stats);
}
