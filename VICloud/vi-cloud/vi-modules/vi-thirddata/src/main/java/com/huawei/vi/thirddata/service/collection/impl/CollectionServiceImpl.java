
package com.huawei.vi.thirddata.service.collection.impl;


import com.huawei.vi.entity.vo.ProgressInfo;
import com.huawei.vi.thirddata.mapper.TblPeriodManageMapper;
import com.huawei.vi.thirddata.mapper.TblServerParamConfigMapper;
import com.huawei.vi.thirddata.service.binary.BinaryConversionService;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;
import com.huawei.vi.thirddata.service.collection.CollecterStartDateService;
import com.huawei.vi.thirddata.service.collection.CollectionService;
import com.huawei.vi.thirddata.service.collection.common.CollectionTimer;
import com.huawei.vi.thirddata.service.dzserver.DzCollectService;
import com.huawei.vi.thirddata.service.ivsserver.IvsService;
import com.jovision.jaws.common.util.ScheduledThreadPoolUtil;
import com.jovision.jaws.common.util.UtilMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 采集业务层实现类
 *
 */
@Service(value = "collectionServiceImpl")
public class CollectionServiceImpl implements CollectionService {
    /**
     * 日志log
     */
    private static final Logger LOG = LoggerFactory.getLogger(CollectionServiceImpl.class);

    /**
     * 一个月秒数
     */
    private static final long SECONDOFONCEMONTH = 2678400L;

    /**
     * 定时器的间隔时间，一分钟
     */
    private static final long TIMERPERIODTIME = 60000L;

    /**
     * 用于秒和毫秒之前的计算
     */
    private static final long ONETHOUSAND = 1000L;

    private Future future;

    @Autowired
    private TblPeriodManageMapper periodManageDao;

    @Autowired
    private TblServerParamConfigMapper serverParamConfig;

    @Resource(name = "binaryConversionServ")
    private BinaryConversionService binaryConversionService;

    @Autowired
    private DzCollectService dzCollectService;

    @Resource(name = "collecterStartDateServ")
    private CollecterStartDateService collecterStartDate;

    @Resource(name = "ivsServ")
    private IvsService ivsServ;

    /**
     * 采集数据的间隔周期时间十分钟,需要在配置文件中获取
     */
    @Value("${configbean.periodtime}")
    private long periodTime;

    /**
     * 查询到的所有的服务器配置信息
     *
     * @return List
     */
    @Override
    public List<TblServerParamConfig> getServerParamInfo() {
        try {
            return serverParamConfig.selectAll();
        } catch (DataAccessException e) {
            LOG.error("selectAll DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    /**
     * 查询周期管理表中总共有多少条数据，用于判断是否是第一次采集数据
     *
     * @return int
     */
    @Override
    public int isHasData() {
        try {
            return periodManageDao.getDateCount();
        } catch (DataAccessException e) {
            LOG.error("getDateCount DataAccessException {} ", e.getMessage());
        }
        return 0;
    }

    /**
     * 采集周期开始时间
     *
     * @return String
     */
    @Override
    public String getStartTime() {
        try {
            return periodManageDao.getNewDate();
        } catch (DataAccessException e) {
            LOG.error("getNewDate DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    private Date getStartDate() {
        // 是否开启配置文件中的：SnapMode 为标识
        boolean isImage = ProgressInfo.isHaveIvs();
        if (isImage) {
            int modelNum = (int) (periodTime / TIMERPERIODTIME);
            LOG.info("modelNum {}", modelNum);
            return collecterStartDate.getCollecterStartDate(new Date(), modelNum);
        }
        return collecterStartDate.getCollecterStartDate(new Date(), 0);
    }

    /**
     * 开始采集数据
     *
     * @param choose choose
     */
    @Override
    public void startCollection(String choose) {
        CollectionTimer.getIsCollect().set(true);
        List<TblServerParamConfig> serverParamInfos = getServerParamInfo();

        // 获取数据库中记录的最后一次采集的开始时间
        String maxCollectionTime = getStartTime();
        Date lastCollectionStartTime = UtilMethod.stringToDate(maxCollectionTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        // 获取当前时间:存在整10分钟时间和自然时间两种情况
        Date nowTime = getStartDate();
        Timer timer = new Timer();

        // 周期管理表中是否有数据 没有：第一次采集；
        if (isHasData() < 1) {
            // 计算出采集开始时间
            String collectStart = sdf.format(nowTime.getTime() - periodTime);

            // 开始时间转换成Date类型
            Date time = UtilMethod.formatStringToDate(collectStart);

            future = ScheduledThreadPoolUtil.getInstance()
                .getCollectionScheduledThreadPool()
                .scheduleWithFixedDelay(CollectionTimer.getTimer(time, serverParamInfos, binaryConversionService,
                    dzCollectService, ivsServ), 0, TIMERPERIODTIME, TimeUnit.MILLISECONDS);
        } else {
            // 非第一次采集数据
            // ROUNDDOWN((当前时间-数据库中记录的采集到的数据的结束时间)除以周期时间)ｘ周期时间
            int rundown = (int) ((nowTime.getTime() - (lastCollectionStartTime.getTime() + periodTime)) / periodTime);
            if (rundown == 0) {
                // 开始时间
                String time = sdf.format(lastCollectionStartTime.getTime() + periodTime);
                Date lastEndTime = UtilMethod.formatStringToDate(time);

                // 计算延迟时间
                long delaytime = periodTime - (nowTime.getTime() - (lastCollectionStartTime.getTime() + periodTime));

                // 定时器
                future = ScheduledThreadPoolUtil.getInstance()
                    .getCollectionScheduledThreadPool()
                    .scheduleWithFixedDelay(CollectionTimer.getTimer(lastEndTime, serverParamInfos,
                        binaryConversionService, dzCollectService, ivsServ), delaytime, TIMERPERIODTIME,
                        TimeUnit.MILLISECONDS);
            } else {
                if ("Y".equals(choose)) {
                    // 从当前最近一个周期开始采集数据
                    // 数据库中记录的采集到的数据的结束时间
                    // ROUNDDOWN((当前时间-数据库中记录的采集到的数据的结束时间)除以周期时间)ｘ周期时间
                    Date date = getStartDate();
                    long starTime = (lastCollectionStartTime.getTime() + periodTime) + rundown * periodTime;
                    long delayTime = periodTime - (date.getTime() - starTime);
                    String start = sdf.format(starTime);
                    Date startDate = UtilMethod.formatStringToDate(start);

                    // 定时器
                    future = ScheduledThreadPoolUtil.getInstance()
                        .getCollectionScheduledThreadPool()
                        .scheduleWithFixedDelay(CollectionTimer.getTimer(startDate, serverParamInfos,
                            binaryConversionService, dzCollectService, ivsServ), delayTime, TIMERPERIODTIME,
                            TimeUnit.MILLISECONDS);
                } else if ("N".equals(choose)) {
                    startCollectorFormLastDate(lastCollectionStartTime, sdf, serverParamInfos, timer);
                } else {
                    LOG.error("Acquired parameter error!");
                }
            }
        }
    }

    @Override
    public void stopCollection() {
        future.cancel(true);
        CollectionTimer.getIsCollect().set(false);
        ProgressInfo.getInstance().setIscollecting("no");
        CollectionTimer.setCollectionTimer(null);
    }

    /**
     * 快速采集：从上次采集结束开始连续采集
     *
     * @param lastCollectionStartTime 上次采集时间
     * @param sdf 格式
     * @param serverParamInfo 服务信息
     * @param timer 定时器
     */
    private void startCollectorFormLastDate(Date lastCollectionStartTime, SimpleDateFormat sdf,
        List<TblServerParamConfig> serverParamInfo, Timer timer) {
        ProgressInfo.getInstance();
        ProgressInfo.setIsOnceCollectionOver(false);

        // 从上一次停止采集的周期继续采集
        long time = lastCollectionStartTime.getTime() + periodTime;
        Date startTime = null;

        // 快速采集
        long delayTime = 0L;
        long intervalTime = 0L;

        // 补充采集不采集一个月之前的数据
        // 当前时间和数据库中保存最后一次采集的间隔时间
        Date date = new Date();

        // 获取当前时间到最近一次采集时间加一个周期的总时长，单位为：秒S
        long blackTime = (date.getTime() - time) / ONETHOUSAND;
        if (blackTime >= SECONDOFONCEMONTH) {
            time = date.getTime() - SECONDOFONCEMONTH * ONETHOUSAND;
        }
        while (true) {
            ProgressInfo.getInstance();
            ProgressInfo.setIscollecting("yes");
            Date curTime = getStartDate();

            // 当前时间和上一次采集的结束时间的间隔时间
            intervalTime = curTime.getTime() - time;
            if (intervalTime > periodTime) {
                String nextStartTime = sdf.format(time);
                startTime = UtilMethod.formatStringToDate(nextStartTime);

                // 此处调用采集方法
                binaryConversionService.uploadNetWorkConfig();
                binaryConversionService.dataProcessingStart(serverParamInfo, startTime);
                if (ProgressInfo.isHaveImageDiagnosisService()) {
                    dzCollectService.dzServerInternalCollect(serverParamInfo, startTime);
                } else {
                    dzCollectService.dzServerCollectStart(serverParamInfo, startTime);
                }
                binaryConversionService.insertPeriodStartTime(startTime);

                // 每次采集的开始时间为上一次采集的结束时间
                time += periodTime;
            } else {
                // 延迟时间
                delayTime = periodTime - intervalTime;
                break;
            }
        }
        startTime = UtilMethod.formatStringToDate(sdf.format(time));
        ProgressInfo.getInstance();
        ProgressInfo.setIsOnceCollectionOver(true);
        future = ScheduledThreadPoolUtil.getInstance()
            .getCollectionScheduledThreadPool()
            .scheduleWithFixedDelay(CollectionTimer.getTimer(startTime, serverParamInfo, binaryConversionService,
                dzCollectService, ivsServ), delayTime, TIMERPERIODTIME, TimeUnit.MILLISECONDS);
    }
}
