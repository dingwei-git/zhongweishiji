package com.huawei.vi.thirddata.controller;

import com.alibaba.fastjson.JSONObject;
import com.huawei.utils.CommonUtil;

import com.huawei.vi.entity.vo.ImageDayStatisticsVcnVO;
import com.huawei.vi.entity.vo.ProgressInfo;
import com.huawei.vi.thirddata.service.MonitorService;
import com.huawei.vi.thirddata.service.binary.BinaryConversionService;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;
import com.huawei.vi.thirddata.service.collection.CollectionService;
import com.huawei.vi.thirddata.service.importdataformcsv.IoriginalDataService;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.ScheduledThreadPoolUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.huawei.vi.thirddata.controller.CollectionController.setEndTimer;

@Controller
@RequestMapping("imageDataStream")
public class ImageDataStreamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDataStreamController.class);

    private HttpSession httpSession = null;
    private String sessionId = null;
    private Cookie cookie = null;

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

    private String dzServiceFlag = "2";

    @Autowired
    MonitorService monitorService;

    /*
    * 获得cookie
    *
    * */
    @ResponseBody
    @RequestMapping(value = "/login.do",method = {RequestMethod.POST})
    public void login(Map<String,String> map,HttpServletResponse response,HttpServletRequest request){
        monitorService.getCookie(map,response,request);
    }


    /*
    * 摄像机维度
    * */
    @ResponseBody
    @RequestMapping(value = "/countPicByCamera.do", method = {RequestMethod.POST})
    public RestResult startCollectionByCamera(HttpServletRequest request, HttpServletResponse response, @RequestBody ImageDayStatisticsVcnVO imageDayStatisticsVcn) {
        Map<String,String> conMap = new HashedMap();
        conMap.put("userName",imageDayStatisticsVcn.getUserName());
        conMap.put("password",imageDayStatisticsVcn.getPassword());
        //获取ip
        String addrerssIp = "115.236.55.211";
//        List<TblServerParamConfig> tblServerParamConfigList = monitorService.getAddressIp(imageDayStatisticsVcn);
//        if(tblServerParamConfigList.size()>0){
//            for(int i = 0;i<tblServerParamConfigList.size();i++){
//                if(tblServerParamConfigList.get(i).getRemark().toLowerCase().equals("vcm1")){
//                    addrerssIp = tblServerParamConfigList.get(i).getServiceIpAddress();
//                    jsonObject.put("serverIp",addrerssIp);
//                }
//            }
//        }
        conMap.put("serverIp",addrerssIp);
        login(conMap,response,request);
//        binaryConversionService.getLanguage();
//        String status = request.getParameter("status");
//        String choose = request.getParameter("choose");
//        // 非离线数据--用 false 表示
//        if (!isOfflineAnalys) {
//            if (CommonUtil.isNull(status)) {
//                LOGGER.error("status is null .");
//            } else if ("start".equals(status)) {
//                ProgressInfo.getInstance().setIscollecting("yes");
//                collectionService.startCollection(choose);
//            } else if ("end".equals(status)) {
//                collectionService.stopCollection();
//            } else {
//                LOGGER.error("acquired parameter error! parameter not in (start end)");
//            }
//        } else { // 离线数据处理--用yes表示
//            if (CommonUtil.isNotNull(status) && "start".equals(status)) {
//                ProgressInfo.getInstance();
//                ProgressInfo.setIscollecting("yes");
//                if (isEndTimer) {
//                    setEndTimer(false);
//                    CollectionController.AnalyseTimer.getTimer().setOriginalDataService(originalDataService);
//                    future = ScheduledThreadPoolUtil.getInstance()
//                            .getOfflineScheduledThreadPool()
//                            .scheduleWithFixedDelay(CollectionController.AnalyseTimer.getTimer(), 0, periodTime, TimeUnit.MILLISECONDS);
//                }
//            } else {
//                // 结束分析,停止定时器
//                future.cancel(true);
//                setEndTimer(true);
//                ProgressInfo.getInstance();
//                ProgressInfo.setIscollecting("no");
//            }
//        }
        JSONObject obj = monitorService.getDomiancode(response);
        imageDayStatisticsVcn.setDomainCode(obj.getString("domainCode"));
        RestResult restResult = monitorService.getCountPicture(imageDayStatisticsVcn,request,response);
        return restResult;
    }


    /*
    * 按照整域查询统计
    * */
    @ResponseBody
    @RequestMapping(value = "/countPicByCameraRegion.do", method = {RequestMethod.POST})
    public RestResult startCollectionByRegion(HttpServletRequest request,HttpServletResponse response,@RequestBody ImageDayStatisticsVcnVO imageDayStatisticsVcn) {
        Map<String,String> map = new HashedMap();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName",imageDayStatisticsVcn.getUserName());
        jsonObject.put("password",imageDayStatisticsVcn.getPassword());
        //获取ip
        String addrerssIp = "";
        List<TblServerParamConfig> tblServerParamConfigList = monitorService.getAddressIp(imageDayStatisticsVcn);
        if(tblServerParamConfigList.size()>0){
            for(int i = 0;i<tblServerParamConfigList.size();i++){
                if(tblServerParamConfigList.get(i).getRemark().toLowerCase().equals("vcn")){
                    addrerssIp = tblServerParamConfigList.get(i).getServiceIpAddress();
                    jsonObject.put("serverIp",addrerssIp);
                }
            }
        }
        jsonObject.put("serverPort","9900");
        login(map,response,request);
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
                    CollectionController.AnalyseTimer.getTimer().setOriginalDataService(originalDataService);
                    future = ScheduledThreadPoolUtil.getInstance()
                            .getOfflineScheduledThreadPool()
                            .scheduleWithFixedDelay(CollectionController.AnalyseTimer.getTimer(), 0, periodTime, TimeUnit.MILLISECONDS);
                }
            } else {
                // 结束分析,停止定时器
                future.cancel(true);
                setEndTimer(true);
                ProgressInfo.getInstance();
                ProgressInfo.setIscollecting("no");
            }
        }
        JSONObject obj = monitorService.getDomiancode(response);
        imageDayStatisticsVcn.setDomainCode(obj.getString("domainCode"));
        RestResult restResult = monitorService.getCountPictureByRegion(imageDayStatisticsVcn,request,response);
        return  restResult;
    }
}


