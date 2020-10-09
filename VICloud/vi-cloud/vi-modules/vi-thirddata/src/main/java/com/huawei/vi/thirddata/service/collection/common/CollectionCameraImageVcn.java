package com.huawei.vi.thirddata.service.collection.common;

import com.huawei.utils.CommonUtil;
import com.huawei.utils.SpringUtil;
import com.huawei.vi.entity.vo.ProgressInfo;
import com.huawei.vi.entity.model.ConfigBean;
import com.huawei.vi.entity.vo.ImageDayStatisticsVcnVO;
import com.huawei.vi.thirddata.service.binary.BinaryConversionService;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;
import com.huawei.vi.thirddata.service.dzserver.DzCollectService;
import com.huawei.vi.thirddata.service.ivsserver.IvsService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CollectionCameraImageVcn implements Runnable{
    /**
     * 公共日志打印
     */
    private static final Logger LOG = LoggerFactory.getLogger(CollectionTimer.class);

    /**
     * 注释内容
     */
    private static CollectionTimer collectionTimer = null;

    @Getter
    private static AtomicBoolean isCollect = new AtomicBoolean(false);

    /**
     * 服务器集合
     */
    List<TblServerParamConfig> serverParamInfos = null;

    /**
     * 开始时间
     */
    private Date starTime;

    private BinaryConversionService binaryConversionService;

    private DzCollectService dzCollectService;

    private IvsService ivsServ;

    /**
     * 数据采集定时器
     *
     * @param periodStarTime 周期时间
     * @param serverParam 服务器参数列表
     * @param binaryConversionService 科来二进制转换调用接口
     * @param dzCollectService 东智服务器数据采集接口
     * @param ivsServ ivs服务器数据采集接口
     */
    public CollectionCameraImageVcn(Date periodStarTime, List<TblServerParamConfig> serverParam,
                                    BinaryConversionService binaryConversionService, DzCollectService dzCollectService, IvsService ivsServ) {
        super();
        if (CommonUtil.isNull(periodStarTime)) {
            starTime = null;
        } else {
            starTime = (Date) periodStarTime.clone();
        }
        serverParamInfos = serverParam;
        this.binaryConversionService = binaryConversionService;
        this.dzCollectService = dzCollectService;
        this.ivsServ = ivsServ;
    }

    /**
     * 获取分析定时器
     *
     * @param periodStarTime 周期时间
     * @param serverParam 服务器列表
     * @param binaryConversionService 二进制转换调用接口
     * @param dzCollectService 东智服务器数据采集接口
     * @param ivsServ IVS服务器接口
     * @return CollectionTimer [返回类型说明]
     */
    public static CollectionTimer getTimer(Date periodStarTime, List<TblServerParamConfig> serverParam,
                                           BinaryConversionService binaryConversionService, DzCollectService dzCollectService, IvsService ivsServ) {
        synchronized (CollectionTimer.class) {
            if (CommonUtil.isNull(collectionTimer)) {
                collectionTimer = new CollectionTimer(periodStarTime, serverParam, binaryConversionService,
                        dzCollectService, ivsServ);
            }
        }
        return collectionTimer;
    }

    /**
     * run
     */
    @Override
    public void run() {
        if (!isCollect.get()) {
            return;
        }
        Date date = new Date();
        long periodTime = SpringUtil.getBean("configBean", ConfigBean.class).getPeriodTime();
        LOG.info("start collection ====");
        long nowTime = date.getTime();

        // 当前时间距离上次采集时间不足一个周期时间，跳出 run 方法
        if (nowTime - starTime.getTime() >= periodTime) {
            ProgressInfo.getInstance().setIsOnceCollectionOver(false);
            LOG.info("run uploadNetWorkConfig");
            binaryConversionService.uploadNetWorkConfig();
            LOG.info("run dataProcessingStart");
            binaryConversionService.dataProcessingStart(serverParamInfos, starTime);
            LOG.info("run dzServerCollectStart");
            if (ProgressInfo.isHaveImageDiagnosisService()) {
                dzCollectService.dzServerInternalCollect(serverParamInfos, starTime);
            } else {
                dzCollectService.dzServerCollectStart(serverParamInfos, starTime);
            }
            LOG.info("run insertPeriodStartTime");
            if (ivsServ.queryIvsServerParam() > 0) {
                ImageDayStatisticsVcnVO imageDayStatisticsVcnVO = null;
                ivsServ.ivsCollects(serverParamInfos, starTime);
                LOG.info("run ivsServerCollectStart");
            }
            binaryConversionService.insertPeriodStartTime(starTime);
            ProgressInfo.getInstance().setIsOnceCollectionOver(true);

            // 进行周期累加
            starTime = new Date(starTime.getTime() + periodTime);
        }
        LOG.info("end collection=====");
    }





}
