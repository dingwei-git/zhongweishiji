
package com.huawei.vi.thirddata.controller;


import com.huawei.utils.CollectionUtil;
import com.huawei.utils.CommonUtil;
import com.huawei.vi.entity.vo.ProgressInfo;
import com.huawei.vi.thirddata.service.binary.BinaryConversionService;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;
import com.huawei.vi.thirddata.service.collection.CollectionService;
import com.huawei.vi.thirddata.service.importdataformcsv.IoriginalDataService;
import com.huawei.vi.thirddata.service.serverparamconfig.IserverParamConfigService;
import com.jovision.jaws.common.util.ScheduledThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 数据采集控制层
 *
 * @version 1.0, 2018年7月2日
 * @since 2018-07-02
 */
@Controller
@RequestMapping(value = "collection")
public class CollectionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionController.class);

    private static final String ISDLK = "isDlk";

    // 记录定时器开始还是结束
    private static boolean isEndTimer = true;

    private Future future;

    @Resource(name = "collectionServiceImpl")
    private CollectionService collectionService;

    @Resource(name = "binaryConversionServ")
    private BinaryConversionService binaryConversionService;

    @Resource(name = "originalDataService")
    private IoriginalDataService originalDataService;

    @Value("${configbean.periodtime}")
    private long periodTime;

    /**
     * 是否是离线数据 true:离线 false:在线
     */
    @Value("${configbean.isOfflineAnalys}")
    private Boolean isOfflineAnalys;

    @Resource(name = "serverParamConfigServ")
    private IserverParamConfigService serverParamConfigService;

    private String dzServiceFlag = "2";

    /**
     * 合一部署是否以东智为主
     *
     * @param request 网络请求
     * @return 返回是否需要配置DLk服务器 true:需要配置；false:不需要配置
     */
    @ResponseBody
    @RequestMapping(value = "isHaveDlk.do", method = {RequestMethod.POST})
    public boolean isHaveDlk(HttpServletRequest request) {
        binaryConversionService.getLanguage();
        String dzServiceStr = request.getParameter("dzServiceFlag");
        if (ISDLK.equals(dzServiceStr)) {
            TblServerParamConfig tblServerParam = new TblServerParamConfig();
            tblServerParam.setServiceFlag(dzServiceFlag);
            List<TblServerParamConfig> lists = serverParamConfigService.getAllServerParamConfig(tblServerParam);
            return CollectionUtil.isEmpty(lists);
        } else {
            return false;
        }
    }

    /**
     * 开始采集
     *
     * @param request 网络请求
     */
    @ResponseBody
    @RequestMapping(value = "/start.do", method = {RequestMethod.POST})
    public void startCollection(HttpServletRequest request) {
        binaryConversionService.getLanguage();
        String status = request.getParameter("status");
        String choose = request.getParameter("choose");

        // 非离线数据--用 false 表示
        if (!isOfflineAnalys) {
            if (CommonUtil.isNull(status)) {
                LOGGER.error("status is null .");
            } else if ("start".equals(status)) {
                ProgressInfo.getInstance().setIscollecting("yes");
                collectionService.startCollection(choose);
            } else if ("end".equals(status)) {
                collectionService.stopCollection();
            } else {
                LOGGER.error("acquired parameter error! parameter not in (start end)");
            }
        } else { // 离线数据处理--用yes表示
            if (CommonUtil.isNotNull(status) && "start".equals(status)) {
                ProgressInfo.getInstance();
                ProgressInfo.setIscollecting("yes");
                if (isEndTimer) {
                    setEndTimer(false);
                    AnalyseTimer.getTimer().setOriginalDataService(originalDataService);
                    future = ScheduledThreadPoolUtil.getInstance()
                        .getOfflineScheduledThreadPool()
                        .scheduleWithFixedDelay(AnalyseTimer.getTimer(), 0, periodTime, TimeUnit.MILLISECONDS);
                }
            } else {
                // 结束分析,停止定时器
                future.cancel(true);
                setEndTimer(true);
                ProgressInfo.getInstance();
                ProgressInfo.setIscollecting("no");
            }
        }
    }

    /**
     * 是否一次采集结束
     *
     * @param request 网络请求
     * @return Map
     */
    @ResponseBody
    @RequestMapping(value = "onePeriod.do", method = RequestMethod.POST)
    public Map<String, String> isEndOnePeriod(HttpServletRequest request) {
        HashMap<String, String> maps = new HashMap<String, String>();

        // 判断是否正在采集
        String isCollecting = ProgressInfo.getInstance().getIscollecting();

        // 判断是否完成一次采集周期
        boolean isOnceCollOver = ProgressInfo.getInstance().getIsOnceCollectionOver();
        maps.put("isEnd", isOnceCollOver ? "1" : "0"); // 一次采集周期是否结束0：没有结束，1：结束
        maps.put("isCollecting", isCollecting);
        return maps;
    }

    /**
     * 用于判断是否是第一次采集，第一次为true，否则为false
     *
     * @return boolean
     */
    @ResponseBody
    @RequestMapping(value = "isFirstCollection.do", method = RequestMethod.POST)
    public boolean isFirstCollection() {
        // 判断是否存在数据 没有数据?第一次采集(true):false
        return collectionService.isHasData() == 0;
    }

    public static void setEndTimer(boolean isEndTimerFlag) {
        CollectionController.isEndTimer = isEndTimerFlag;
    }

    /**
     * 离线定时器
     *
     * @version 1.0, 2018年7月23日
     * @since 2018-07-23
     */
    public static class AnalyseTimer implements Runnable {
        private static AnalyseTimer timer = null;

        private IoriginalDataService originalDataService;

        /**
         * 获取定时器
         *
         * @return AnalyseTimer
         */
        public static synchronized AnalyseTimer getTimer() {
            if (timer == null) {
                timer = new AnalyseTimer();
            }
            return timer;
        }

        /**
         * start the task
         */
        @Override
        public void run() {
            LOGGER.info("start the task.");
            if (originalDataService != null) {
                ProgressInfo.getInstance().setIsOnceCollectionOver(false);
                originalDataService.start();
                ProgressInfo.getInstance().setIsOnceCollectionOver(true);
            } else {
                LOGGER.error("input file is null");
            }
            LOGGER.info("analyse task end------");
        }

        public void setOriginalDataService(IoriginalDataService originalDataService) {
            this.originalDataService = originalDataService;
        }
    }
}
