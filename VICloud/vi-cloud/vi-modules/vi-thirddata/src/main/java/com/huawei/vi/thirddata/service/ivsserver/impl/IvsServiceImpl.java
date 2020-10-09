
package com.huawei.vi.thirddata.service.ivsserver.impl;

import com.alibaba.fastjson.JSONObject;
import com.huawei.utils.NumConstant;
import com.huawei.utils.*;
import com.huawei.vi.entity.vo.ImageDayStatisticsVcnVO;
import com.huawei.vi.thirddata.mapper.TblCameraManageOnceMapper;
import com.huawei.vi.thirddata.mapper.TblOriginalDataMapper;
import com.huawei.vi.thirddata.service.baseserv.impl.BaseServImpl;
import com.huawei.vi.thirddata.service.binary.pojo.*;
import com.huawei.vi.thirddata.service.ivsserver.CameraManageService;
import com.huawei.vi.thirddata.service.ivsserver.IvsService;
import com.huawei.vi.thirddata.service.ivsserver.ServiceCommonConst;
import com.huawei.vi.thirddata.service.ivsserver.common.PlateData;
import com.huawei.vi.thirddata.service.ivsserver.common.PlateResponse;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.util.SslClient;
import com.jovision.jaws.common.util.StringNumConstant;
import com.jovision.jaws.common.util.UtilMethod;
import com.jovision.jaws.common.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jovision.jaws.common.constant.CommonConst.IMAGE_TABLENAME;
import static com.jovision.jaws.common.util.TableCommonConstant.TBL_CAMERA_MANAGER_ORIGINAL;


/**
 * IvsServiceImpl
 */

@Service("ivsServ")
public class IvsServiceImpl extends BaseServImpl<CaptureImageOriginal, String> implements IvsService {
    /**
     * 公共日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IvsServiceImpl.class);

    private static final String TMP_PATH = CommonConst.DIR_TMP + "respose.xml";

    /**
     * 获取过脸统计接口url
     */
    private static final String FACE_URL = "statisticanalysis/face/today";

    /**
     * 获取过车统计接口url
     */
    private static final String CAR_URL = "statisticanalysis/plate/today";

    /*
     * sql传入表名
     */
    private static final String TABLE_NAME = "tableName";

    /*
     * sql传入周期结束时间
     */
    private static final String END_TIME = "endTime";

    /*
     * 整小时匹配(匹配样例20190829110000)
     */
    private static final Pattern HOUR_PATTERN = Pattern.compile("[0-9]{10}[0]{4}");

    /**
     * 零点匹配
     */
    private static final Pattern DAY_PATTERN = Pattern.compile("[0-9]{8}[0]{6}");

    @Autowired
    private TblCameraManageOnceMapper tblCameraManageOnceMapper;

    /*
     * 采集数据处理线程池
     */
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CommonConst.COREPOOLSIZE,
        CommonConst.MAXIMUMPOOLSIZE, CommonConst.KEEPALIVETIME, TimeUnit.SECONDS,
        new LinkedBlockingQueue<Runnable>(CommonConst.WORKQUEUESIZE), new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 默认的端口為8080，支持在配置文件中進行配置
     */
    @Value("${configbean.videoinsightport}")
    private String videoInsightPort;

    /**
     * 视频工具的部署ip
     */
    @Value("${configbean.videoinsighthost}")
    private String videoInsightHost;

    /*
     * 周期开始时间
     */
    private Date periodStartTime;

    /*
     * 周期结束时间
     */
    private Date periodEndTime;

    /*
     * 天维度统计表名
     */
    private String dayTableName;

    /*
     * 有效摄像机统计时长
     */
    private String effectiveCameraMinute;

    /*
     * 采集周期
     */
    @Value("${configbean.periodtime}")
    private int periodTime;

    @Resource(name = "originalDataDao")
    private TblOriginalDataMapper originalDataDao;

    @Resource
    private CameraManageService cameraMangageService;

    @Resource(name = "cameraManageOnceDao")
    private TblCameraManageOnceMapper cameraManageOnceDao;

    /**
     * IVS服务器数据采集开始
     */
    @Override
    public void ivsCollect(List<TblServerParamConfig> tblServerParamConfig, Date beginTime) {
        if (CollectionUtil.isNull(tblServerParamConfig) || CollectionUtil.isEmpty(tblServerParamConfig)) {
            LOGGER.info("server parameter config is empty ...");
            return;
        }
        // 获取有效摄像机统计时长
        effectiveCameraMinute = getEffectiveCameraMinute();
        boolean isFirst = true;

        // 循环所有IVS系统，开始执行数据采集
        for (TblServerParamConfig tblServerParam : tblServerParamConfig) {
            // 过滤IVS服务器参数
            if (StringNumConstant.STRING_NUM_3.equals(tblServerParam.getServiceFlag())
                && StringNumConstant.STRING_NUM_0.equals(tblServerParam.getServiceStatus())) {
                String account = tblServerParam.getServiceName();
                String secretCode = tblServerParam.getServicePassword();
                boolean isLogin = cameraMangageService.isLoginIvs(tblServerParam.getServiceIpAddress(), account,
                    Rsa.decryptString(secretCode));

                // 更新服务器配置参数的登录状态
                cameraMangageService.updateServerConnectStatus(isLogin, tblServerParam);
                if (!isLogin) {
                    LOGGER.info("login_errcode");
                    return;
                }
                String tableName = UtilMethod.getTableName(TBL_CAMERA_MANAGER_ORIGINAL, beginTime);
                if (isFirst) {
                    cameraMangageService.truncateCameraOriginal(tableName);
                    isFirst = false;
                }

                // 当多套IVS系统时，预先将摄像机详细信息原始数据入表tbl_camera_manager_original
                cameraMangageService.saveCameraOriginal(tblServerParam.getServiceIpAddress(), account,
                    Rsa.decryptString(secretCode));

                String url = ProtocolConfig.getParameterConfig()
                    .getIvsCollectUrl()
                    .replaceAll(ServiceCommonConst.HOST, tblServerParam.getServiceIpAddress());

                // 第一步：保存摄像机详细信息交集到动态表tbl_camera_manager_original_***
                int camerCount = cameraMangageService.saveCameraIntersection(tableName);
                LOGGER.info("saveCameraIntersection camerCount size is {}", camerCount);
                if (camerCount > 0) {
                    // 第二步：开始正式数据采集
                    ivsCollectStart(beginTime, url, tblServerParam);
                    while (threadPool.getActiveCount() >= NumConstant.NUM_1) {
                        try {
                            Thread.sleep(CommonConst.KEEPSLEEPTIME);
                        } catch (InterruptedException e) {
                            LOGGER.error("Thread.sleep: {}", e.getMessage());
                        }
                    }

                    // 小时维度统计表入库
                    insertHourStatistics();
                } else {
                    LOGGER.error("This IVS get camera is null!");
                    continue;
                }

                // 登出ivs
                cameraMangageService.logOutIvs(tblServerParam.getServiceIpAddress());
            }
        }
    }


    /**
     * IVS服务器数据采集开始
     */
    @Override
    public void ivsCollects(List<TblServerParamConfig> tblServerParamConfig, Date beginTime) {
        if (CollectionUtil.isNull(tblServerParamConfig) || CollectionUtil.isEmpty(tblServerParamConfig)) {
            LOGGER.info("server parameter config is empty ...");
            return;
        }
        // 获取有效摄像机统计时长
        effectiveCameraMinute = getEffectiveCameraMinute();
        boolean isFirst = true;

        // 循环所有IVS系统，开始执行数据采集
        for (TblServerParamConfig tblServerParam : tblServerParamConfig) {
            // 过滤IVS服务器参数
            if (StringNumConstant.STRING_NUM_3.equals(tblServerParam.getServiceFlag())
                    && StringNumConstant.STRING_NUM_0.equals(tblServerParam.getServiceStatus())) {
                String account = tblServerParam.getServiceName();
                String secretCode = tblServerParam.getServicePassword();
                boolean isLogin = cameraMangageService.isLoginIvs(tblServerParam.getServiceIpAddress(), account,
                        Rsa.decryptString(secretCode));

                // 更新服务器配置参数的登录状态
                cameraMangageService.updateServerConnectStatus(isLogin, tblServerParam);
                if (!isLogin) {
                    LOGGER.info("login_errcode");
                    return;
                }
                String tableName = UtilMethod.getTableName(TBL_CAMERA_MANAGER_ORIGINAL, beginTime);
                if (isFirst) {
                    cameraMangageService.truncateCameraOriginal(tableName);
                    isFirst = false;
                }

                // 当多套IVS系统时，预先将摄像机详细信息原始数据入表tbl_camera_manager_original
                cameraMangageService.saveCameraOriginal(tblServerParam.getServiceIpAddress(), account,
                        Rsa.decryptString(secretCode));

                String url = ProtocolConfig.getParameterConfig()
                        .getIvsCollectUrl()
                        .replaceAll(ServiceCommonConst.HOST, tblServerParam.getServiceIpAddress());

                // 第一步：保存摄像机详细信息交集到动态表tbl_camera_manager_original_***
                int camerCount = cameraMangageService.saveCameraIntersection(tableName);
                LOGGER.info("saveCameraIntersection camerCount size is {}", camerCount);
                if (camerCount > 0) {
                    // 第二步：开始正式数据采集
                    ivsCollectStart(beginTime, url, tblServerParam);
                    while (threadPool.getActiveCount() >= NumConstant.NUM_1) {
                        try {
                            Thread.sleep(CommonConst.KEEPSLEEPTIME);
                        } catch (InterruptedException e) {
                            LOGGER.error("Thread.sleep: {}", e.getMessage());
                        }
                    }

                    // 小时维度统计表入库
                    insertHourStatistics();
                } else {
                    LOGGER.error("This IVS get camera is null!");
                    continue;
                }

                // 登出ivs
                cameraMangageService.logOutIvs(tblServerParam.getServiceIpAddress());
            }
        }
    }




    @Override
    public void ivsCollectStart(Date beginTime, String url, TblServerParamConfig tblServerParamConfig) {
        LOGGER.info("ivsCollectStart start.");
        if (CommonUtil.isExistsNull(beginTime, url, tblServerParamConfig)) {
            LOGGER.error("beginTime, url, tblServerParamConfig exists null. So ivsCollectStart end.");
            return;
        }
        String tableName = UtilMethod.getTableName(TBL_CAMERA_MANAGER_ORIGINAL, beginTime);
        Map<String, String> globalMap = GlobalConfigProperty.getGlobalMap();
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put(TABLE_NAME, tableName);
        maps.put("cameraUse", globalMap.get("plateFlag"));
        maps.put("ivsIp", tblServerParamConfig.getServiceIpAddress());
        List<String> plateCameraSnList = originalDataDaoSelectCameraSn(maps);
        List<List<String>> plateGroupList = groupCameraSn(plateCameraSnList);
        List<CaptureImageOriginal> plateList;
        if (CollectionUtil.isNotEmpty(plateGroupList)) {
            for (int num = NumConstant.NUM_0; num < plateGroupList.size(); num++) {
                plateList = getCollectList(beginTime, url, plateGroupList.get(num), NumConstant.NUM_0);
                threadPool.execute(new IvsRunnable(plateList));
            }
        }
        maps.put("cameraUse", globalMap.get("faceFlag"));
        List<String> faceCameraSnList = originalDataDaoSelectCameraSn(maps);
        List<List<String>> faceGroupList = groupCameraSn(faceCameraSnList);
        List<CaptureImageOriginal> faceList;
        if (CollectionUtil.isNotEmpty(faceGroupList)) {
            for (int num = NumConstant.NUM_0; num < faceGroupList.size(); num++) {
                faceList = getCollectList(beginTime, url, faceGroupList.get(num), NumConstant.NUM_1);
                threadPool.execute(new IvsRunnable(faceList));
            }
        }
        LOGGER.info("ivsCollectStart end. ");
    }

    private List<String> originalDataDaoSelectCameraSn(Map<String, Object> map) {
        try {
            return originalDataDao.selectCameraSn(map);
        } catch (DataAccessException e) {
            LOGGER.error("selectCameraSn DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    /*
     * 对接IVS接口获取返回数据
     */
    private List<CaptureImageOriginal> getCollectList(Date beginTime, String url, List<String> cameraSnList, int type) {
        periodEndTime = getTime(beginTime);
        periodStartTime = beginTime;
        String captureUrl = type == NumConstant.NUM_1 ? url + FACE_URL : url + CAR_URL;
        List<CaptureImageOriginal> originalList = getCaptureList(cameraSnList, type, captureUrl);
        return originalList;
    }

    /**
     * IVS数据采集开始
     *
     * @param originalList 采集原始数据
     */
    @Override
    public void captureImageCollect(List<CaptureImageOriginal> originalList) {
        dayTableName = IMAGE_TABLENAME + CommonConst.SYMBOL_UNDERLINE
            + DateUtil.dateToString(periodEndTime, DateUtil.TimeFormat.DATE_FORMAT_WITH_NO_SYMBOL);
        if (!isTableExist(dayTableName)) {
            createOriginalTable(dayTableName);
        }

        if (CommonUtil.isNotNull(originalList)) {
            insertCaptureData(originalList, dayTableName); // 采集原始抓拍图片数入库原始统计表
        }
    }

    /**
     * 小时维度统计表入库
     *
     * @return boolean
     */
    private boolean insertHourStatistics() {
        Map<String, Object> maps = new HashMap<String, Object>();
        String cameraInfoTableName = UtilMethod.getTableName("tbl_camera_manager_original", periodStartTime);
        if (isTableExist(cameraInfoTableName)) {
            maps.put("cameraInfoTableName", cameraInfoTableName);
        }
        maps.put(END_TIME, periodEndTime);
        maps.put(TABLE_NAME, dayTableName);
        String endTimeStr = DateUtil.dateToString(periodEndTime, DateUtil.TimeFormat.DATETIME_FORMAT_WITH_NO_SYMBOL);
        Matcher matcher = HOUR_PATTERN.matcher(endTimeStr);
        if (matcher.matches()) {
            String hourDimensionTableName = getTableName(TableCommonConstant.STATISTICS_TABLENAME, periodEndTime);
            Map<String, Object> hourStatictisMap = new HashMap<String, Object>();
            hourStatictisMap.put(TABLE_NAME, hourDimensionTableName);

            // 判断当天小时维度统计表是否存在
            if (!isTableExist(hourDimensionTableName)) {
                // 创建抓拍图片数小时维度统计表
                cameraManageOnceDaoCreateDimensionTable(hourStatictisMap);
            }

            // 统计前一个小时过脸、过车抓拍图片数
            List<CaptureHourStatistics> imageCounts = cameraManageOnceDaoSelectImageCount(maps);
            if (CollectionUtil.isEmpty(imageCounts)) {
                LOGGER.error("insertHourStatistics imageCounts is empty");
                return false;
            }

            // 判断是否是完整小时，1为完整小时，0为非完整小时
            for (CaptureHourStatistics cap : imageCounts) {
                cap.setEndHour(periodEndTime);
                if (cap.getCompleteHour() <= NumConstant.HOURMS / periodTime) {
                    cap.setCompleteHour(NumConstant.NUM_0);
                } else {
                    cap.setCompleteHour(NumConstant.NUM_1);
                }
            }

            // 向抓拍图片数小时维度统计表插入数据
            Map<String, Object> hourStatisticsMaps = new HashMap<String, Object>();
            hourStatisticsMaps.put(TABLE_NAME, hourDimensionTableName);
            hourStatisticsMaps.put("dataList", imageCounts);
            return insertImagehourstatistics(hourStatisticsMaps) > NumConstant.NUM_0;
        }
        return false;
    }

    /**
     * 小时维度统计表入库
     *
     * @return boolean
     */
    private boolean insertHourStatistic(ImageDayStatisticsVcnVO imageDayStatisticsVcnVO,String domainCode) {
        Map<String, Object> maps = new HashMap<String, Object>();
        String cameraInfoTableName = UtilMethod.getTableName("tbl_camera_manager_original", periodStartTime);
        if (isTableExist(cameraInfoTableName)) {
            maps.put("cameraInfoTableName", cameraInfoTableName);
        }
        maps.put("startTime",CommonConst.START_TIME);
        maps.put(END_TIME, periodEndTime);
        maps.put(TABLE_NAME, dayTableName);
        maps.put("domainCode",domainCode);
        maps.put("type",imageDayStatisticsVcnVO.getType());
        maps.put("picType",imageDayStatisticsVcnVO.getPicType());
        maps.put("tranType",imageDayStatisticsVcnVO.getTranType());
        maps.put("cameraList",imageDayStatisticsVcnVO.getCameraSn());

        String endTimeStr = DateUtil.dateToString(periodEndTime, DateUtil.TimeFormat.DATETIME_FORMAT_WITH_NO_SYMBOL);
        Matcher matcher = HOUR_PATTERN.matcher(endTimeStr);
        if (matcher.matches()) {
            String hourDimensionTableName = getTableName(TableCommonConstant.STATISTICS_TABLENAME, periodEndTime);
            Map<String, Object> hourStatictisMap = new HashMap<String, Object>();
            hourStatictisMap.put(TABLE_NAME, hourDimensionTableName);

            // 判断当天小时维度统计表是否存在
            if (!isTableExist(hourDimensionTableName)) {
                // 创建抓拍图片数小时维度统计表
                cameraManageOnceDaoCreateDimensionTable(hourStatictisMap);
            }

            // 统计前一个小时过脸、过车抓拍图片数
            List<CaptureHourStatistics> imageCounts = cameraManageOnceDaoSelectImageCount(maps);
            if (CollectionUtil.isEmpty(imageCounts)) {
                LOGGER.error("insertHourStatistics imageCounts is empty");
                return false;
            }

            // 判断是否是完整小时，1为完整小时，0为非完整小时
            for (CaptureHourStatistics cap : imageCounts) {
                cap.setEndHour(periodEndTime);
                if (cap.getCompleteHour() <= NumConstant.HOURMS / periodTime) {
                    cap.setCompleteHour(NumConstant.NUM_0);
                } else {
                    cap.setCompleteHour(NumConstant.NUM_1);
                }
            }

            // 向抓拍图片数小时维度统计表插入数据
            Map<String, Object> hourStatisticsMaps = new HashMap<String, Object>();
            hourStatisticsMaps.put(TABLE_NAME, hourDimensionTableName);
            hourStatisticsMaps.put("dataList", imageCounts);
            return insertImagehourstatistics(hourStatisticsMaps) > NumConstant.NUM_0;
        }
        return false;
    }


    /**
     * 根据摄像机编码与检测时间查询指定时间段最新一条检测结果,如果无结果返回失败
     *
     * @param cameraSnList 摄像机sn号列表
     * @param type 过车过脸标志
     * @return List
     */
    private synchronized List<CaptureImageOriginal> getCaptureList(List<String> cameraSnList, int type,
        String captureUrl) {
        HttpPost httpPost = new HttpPost(captureUrl);
        String requestParam = paramSplice(cameraSnList, periodStartTime, periodEndTime);
        StringEntity requestEntity = new StringEntity(requestParam, CommonConstant.ENCODE);
        requestEntity.setContentEncoding(CommonConstant.ENCODE);
        httpPost.setEntity(requestEntity);
        httpPost.addHeader("Content-Type", "text/plain");
        HttpResponse response = null;
        String result = null;
        SslClient httpClient = new HttpClientUtil().getHttpClient();
        try {
            response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (CommonUtil.isNotNull(resEntity)) {
                result = EntityUtils.toString(resEntity, CommonConstant.ENCODE);
            }
        } catch (IOException e) {
            LOGGER.error("imageCapture collect exception {}", e.getMessage());
        }
        if (null == result) {
            return Collections.emptyList();
        }
        result = result.replaceAll("faceData", "plateData");
        File file = FileUtils.getFile(TMP_PATH);
        try (OutputStream os = FileUtils.openOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write(result);
            bw.flush();
        } catch (IOException e) {
            LOGGER.error("IoException {}", e.getMessage());
        }

        PlateResponse fr = XmlUtil.xmlToObject(TMP_PATH, PlateResponse.class);
        boolean isDeleteInfo = file.delete();
        if (!isDeleteInfo) {
            LOGGER.error("delte tmpFile exception");
        }

        List<PlateData> dataList = fr.getPlateDatas().getPlateData();

        List<CaptureImageOriginal> originalList = getCaptureImageOriginalData(dataList, type);

        return originalList;
    }

    private List<CaptureImageOriginal> getCaptureImageOriginalData(List<PlateData> dataList, int type) {
        List<CaptureImageOriginal> originalList = new ArrayList<CaptureImageOriginal>();
        for (PlateData data : dataList) {
            CaptureImageOriginal capture = new CaptureImageOriginal();
            capture.setCameraSn(data.getCameraSn());
            capture.setCameraName(data.getCameraName());
            capture.setPictureCount(data.getCount());
            capture.setStartTime(periodStartTime);
            capture.setEndTime(periodEndTime);
            capture.setType(type);
            originalList.add(capture);
        }
        return originalList;
    }

    /*
     * 拼接请求参数
     */
    private String paramSplice(List<String> cameraSnList, Date startTime, Date endTime) {
        StringBuilder sb = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        sb.append("<request><cameraSns>");
        for (String cameraSn : cameraSnList) {
            sb.append("<cameraSn>" + cameraSn + "</cameraSn>");
        }
        sb.append("</cameraSns><searchType>1</searchType>");
        String startDayStr = DateUtil.dateToString(startTime, DateUtil.TimeFormat.DATE_FORMAT_WITH_NO_SYMBOL);
        Date nowDate = new Date();
        String nowZeroStr = DateUtil.dateToString(nowDate, DateUtil.TimeFormat.DATE_FORMAT_WITH_NO_SYMBOL);
        if (!startDayStr.equals(nowZeroStr)) {
            try {
                Long startTimeTmp = nowDate.getTime()
                    - DateUtil.stringToDate(nowZeroStr, DateUtil.TimeFormat.DATE_FORMAT_WITH_NO_SYMBOL)
                        .getTime() > NumConstant.NUM_10000 * NumConstant.NUM_60
                            ? nowDate.getTime() - NumConstant.NUM_10000 * NumConstant.NUM_60
                            : nowDate.getTime() - NumConstant.NUM_6 * NumConstant.NUM_1000;
                sb.append("<startTime>" + startTimeTmp + "</startTime>");
                sb.append("<endTime>" + +nowDate.getTime() + "</endTime></request>");
            } catch (ParseException e) {
                LOGGER.error("nowZeroStr parse exception {}", e.getMessage());
            }
        } else {
            sb.append("<startTime>" + startTime.getTime() + "</startTime>");
            sb.append("<endTime>" + endTime.getTime() + "</endTime></request>");
        }
        return sb.toString();
    }

    /*
     * 抓拍图片数原始数据入库
     */
    private void insertCaptureData(List<CaptureImageOriginal> dataList, String tableName) {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put(TABLE_NAME, tableName);
        maps.put("dataList", dataList);

        // 采集库天维度表插入数据
        originalDataDao.insertCaptureTable(maps);
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put(TABLE_NAME, tableName);
        conditions.put(END_TIME, periodEndTime);
        conditions.put("effectiveCameraMinute", effectiveCameraMinute);

        // 获取当前时间与有效摄像机统计时间差值时间戳
        long difference =
            periodEndTime.getTime() - Long.parseLong(effectiveCameraMinute) * (NumConstant.HOURMS / NumConstant.NUM_60);
        String timeStr = DateUtil.dateToString(periodEndTime, DateUtil.TimeFormat.DATEZERO_FORMAT);
        if (difference < com.jovision.jaws.common.util.UtilMethod.stringToDate(timeStr).getTime()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(periodEndTime);
            cal.add(Calendar.SECOND, -NumConstant.NUM_24 * NumConstant.HOURMS / NumConstant.NUM_1000);
            String previousTableName = getTableName(IMAGE_TABLENAME, cal.getTime());
            if (isTableExist(previousTableName)) {
                conditions.put("previousTableName", previousTableName);
            }
        }
        for (CaptureImageOriginal cap : dataList) {
            conditions.put("cameraSn", cap.getCameraSn());
            conditions.put("type", cap.getType());

            if (cap.getPictureCount() != 0) {
                cap.setEffectiveCamera(1);
            } else {
                long pictureCount24h = originalDataDao.select24H(conditions);
                cap.setEffectiveCamera(pictureCount24h > 0 ? 1 : 0);
            }
        }
        maps.put("analysisList", dataList);

        // 分析库天维度表插入数据
        try {
            cameraManageOnceDao.insertCaptureTable(maps);
        } catch (DataAccessException e) {
            LOGGER.error("insertCaptureTable DataAccessException {} ", e.getMessage());
        }
    }

    private String getEffectiveCameraMinute() {
        String result = "";
        Map<String, String> parmMap = new HashMap<String, String>();
        String url = ProtocolConfig.getParameterConfig()
            .getVideoinsightUrl()
            .replaceFirst(ServiceCommonConst.HOST, videoInsightHost)
            .replaceFirst(ServiceCommonConst.PORT, videoInsightPort);
        String effectiveCameraUrl = url + "getParameterConfig.do?param=VideoInsightLanguageConfig";
        Object resultObj =
            new HttpClientUtil().doVedioPost(effectiveCameraUrl, parmMap, CommonConstant.CHARSET_UTF8, true);
        if (resultObj == null) {
            // 如果为空，则默认24小时
            result = "1440";
            LOGGER.error("Get EffectiveCameraHour is null.");
        } else {
            JSONObject jsonObject = JSONObject.parseObject(resultObj.toString());
            JSONObject jsonData = jsonObject.getJSONObject("data");
            if (CommonUtil.isNotNull(jsonData)) {
                result = jsonData.getString("effectiveCameraHour");
            }
        }
        return result;
    }

    private void cameraManageOnceDaoCreateDimensionTable(Map<String, Object> map) {
        try {
            cameraManageOnceDao.createDimensionTable(map);
        } catch (DataAccessException e) {
            LOGGER.error("createDimensionTable DataAccessException {} ", e.getMessage());
        }
    }

    /*
     * 创建抓拍图片数天维度原始表
     */
    private void createOriginalTable(String tableName) {
        try {
            originalDataDao.createCaptureTable(tableName); // 采集库建表
            cameraManageOnceDao.createCaptureTable(tableName); // 分析库建表
        } catch (DataAccessException e) {
            LOGGER.error("createCaptureTable DataAccessException {} ", e.getMessage());
        }
    }

    private int insertImagehourstatistics(Map<String, Object> map) {
        try {
            return cameraManageOnceDao.insertImagehourstatistics(map);
        } catch (DataAccessException e) {
            LOGGER.error("insertImagehourstatistics DataAccessException {} ", e.getMessage());
        }
        return 0;
    }

    /**
     * 判断表是否存在
     *
     * @param stableName 表名
     * @return 判断表是否存在
     */
    public boolean isTableExist(String stableName) {
        if (StringUtil.isNull(stableName)) {
            return false;
        }
        String dataBaseType = GlobalConfigProperty.getGlobalMap().get("dataBaseType");
        String tableName = stableName;
        if ("gauss".equals(dataBaseType)) {
            tableName = stableName.toUpperCase(Locale.ENGLISH);
        }
        if (StringUtil.isNotEmpty(tableName)) {
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put(TABLE_NAME, tableName);
            String tableNameByShow = cameraManageOnceDaoShowTableByname(maps);
            return StringUtil.isNotEmpty(tableNameByShow);
        }
        return false;
    }

    private String cameraManageOnceDaoShowTableByname(Map<String, Object> map) {
        try {
            return originalDataDao.showTableByname(map);
        } catch (DataAccessException e) {
            LOGGER.error("showTableByname DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    private List<CaptureHourStatistics> cameraManageOnceDaoSelectImageCount(Map<String, Object> map) {
        try {
            return cameraManageOnceDao.selectImageCount(map);
        } catch (DataAccessException e) {
            LOGGER.error("selectImageCount DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    /*
     * 获取以天维度记录的表名
     */
    private String getTableName(String preTableName, Date time) {
        return preTableName + CommonConst.SYMBOL_UNDERLINE
            + DateUtil.dateToString(time, DateUtil.TimeFormat.DATE_FORMAT_WITH_NO_SYMBOL);
    }

    private List<List<String>> groupCameraSn(List<String> cameraSnList) {
        if (CollectionUtil.isEmpty(cameraSnList)) {
            return Collections.emptyList();
        }
        List<List<String>> resultList = new ArrayList<List<String>>();
        int index = cameraSnList.size();
        List<String> tmpList;

        do {
            List<String> middleList = new ArrayList<String>();
            if (index <= NumConstant.NUM_100) {
                tmpList = cameraSnList.subList(0, index);
            } else {
                tmpList = cameraSnList.subList(0, NumConstant.NUM_100);
            }
            middleList.addAll(tmpList);
            resultList.add(middleList);
            cameraSnList.removeAll(tmpList);
            index = cameraSnList.size();
        } while (index > NumConstant.NUM_100);
        if (cameraSnList.size() > 0) {
            resultList.add(cameraSnList);
        }
        return resultList;
    }

    private Date getTime(Date beginTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginTime);
        cal.add(Calendar.SECOND, periodTime / NumConstant.NUM_1000);
        return cal.getTime();
    }

    @Override
    public int queryIvsServerParam() {
        try {
            return originalDataDao.queryIvsServerParam();
        } catch (DataAccessException e) {
            LOGGER.error("queryIvsServerParam DataAccessException {} ", e.getMessage());
        }
        return 0;
    }

    @Override
    protected Map<String, Object> getMap(CaptureImageOriginal captureImageOriginal, Map<String, Object> condition) {
        Map<String, Object> maps = new HashMap<String, Object>();
        if (condition != null && !condition.isEmpty()) {
            maps.putAll(condition);
        }
        maps.putAll(Bean2Map.bean2Map(captureImageOriginal));
        return maps;
    }

    /**
     * IvsRunnable
     *
     * @since 2019-08-06
     */
    public class IvsRunnable implements Runnable {
        private List<CaptureImageOriginal> dataList;

        /**
         * IVS线程
         *
         * @param dataList 数据集合
         */
        public IvsRunnable(List<CaptureImageOriginal> dataList) {
            this.dataList = dataList;
        }

        @Override
        public void run() {
            captureImageCollect(dataList);
        }
    }


    @Override
    public List<TblCameraDetailOriginalData> selectCameraInfoByState(HttpServletResponse response){
        Map<String,Object> maps= new HashMap<>();
        Map<String,Object> strMaps = new HashedMap();
        maps.put("tabName",TBL_CAMERA_MANAGER_ORIGINAL);
        strMaps.put("tabName",TBL_CAMERA_MANAGER_ORIGINAL);
        strMaps.put("tableName",IMAGE_TABLENAME);
        strMaps.put("effectiveCamera", NumConstant.NUM_1);
        try{
            List<TblCameraDetailOriginalData> list = tblCameraManageOnceMapper.getCameraInfoByState(maps);
            List<TblCameraDetailOriginalData> cameraOnline = new ArrayList<>();
            List<TblCameraDetailOriginalData> cameraOffLine = new ArrayList<>();
            List<String> list1 = getTableTitle();
            if(null != list && list.size()>0){
                for(int x=0;x<list.size();x++){
                    if(list.get(x).getCameraState()==0){
                        cameraOnline.add(list.get(x));
                    }
                    if(list.get(x).getCameraState()==1){
                        cameraOffLine.add(list.get(x));
                    }
                }
            }
            List<TblCameraDatailOriginalImageData> list2 = tblCameraManageOnceMapper.getCameraInfoAndImage(strMaps);
            List<String> titleLists = new ArrayList<>();
            if(list2!=null && list2.size()>0){
                for(int i = 0;i<list2.size();i++){
                    String strTime = list2.get(i).getStartTime();
                    String strEndTime = list2.get(i).getEndTime();
                    list2.get(i).setStartTime(strTime);
                    list2.get(i).setEndTime(strEndTime);
                }
                titleLists = getTableTitles();
            }
            creatTblCameraDetailOriginalCell(list1.toArray(),cameraOnline,cameraOffLine,titleLists.toArray(),list2,response);
        }catch (DataAccessException e){
            e.printStackTrace();
            LOGGER.error("getValidateCameraList DataAccessException {} ", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<TblCameraDatailOriginalImageData> selectCameraInfoAndImageByState(String state){
        Map<String,Object> maps= new HashMap<>();
        maps.put("tabName",TBL_CAMERA_MANAGER_ORIGINAL);
        maps.put("tableName",IMAGE_TABLENAME);
        maps.put("effectiveCamera", NumConstant.NUM_1);
        try{
            List<TblCameraDatailOriginalImageData> list = tblCameraManageOnceMapper.getCameraInfoAndImage(maps);
            if(list!=null && list.size()>0){
                for(int i = 0;i<list.size();i++){
                    String strTime = list.get(i).getStartTime();
                    String strEndTime = list.get(i).getEndTime();
                    list.get(i).setStartTime(strTime);
                    list.get(i).setEndTime(strEndTime);
                }
                List<String> list1 = getTableTitles();
                if(null != list && list.size()>0) {
                    creatTblCameraDetailOriginalImageCell(list1.toArray(), list);
                }
            }
        }catch (DataAccessException e){
            e.printStackTrace();
            LOGGER.error("getCameraInfoAndImage DataAccessException {} ", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getTableTitle(){
        List<String> list = new LinkedList<>();
        list.add("摄像机编码");
        list.add("摄像机名称");
        list.add("安装高度");
        list.add("摄像机经度");
        list.add("摄像机纬度");
        list.add("摄像机地址");
        list.add("摄像机IP,取流url");
        list.add("摄像机方位");
        list.add("车道");
        list.add("摄像机在线状态");
        list.add("摄像机类型");
        list.add("摄像机用途");
        list.add("摄像机功能");
        list.add("平台名称");
        list.add("分辨率");
        list.add("域编码");
        list.add("罗盘");
        list.add("摄像机nvr");
        list.add("组织id");
        list.add("摄像机id");
        list.add("0:未订阅 1:已订阅");
        list.add("0：普通相机 1：人脸卡口物理相机 2：车物理卡口相机 3：未知的其他相机 4：机非人相机");
        list.add("智能分析列表");
        list.add("0001，车牌识别；0010，人脸识别；0100，行为分析；1000，人体分析；1001，人车混合结构化");
        list.add("智能任务列表");
        list.add("0: 行为分析 1: 车牌分析 2: 人脸分析 4: 人体分析 9：人车混合结构化");
        list.add("IVS平台IP地址");
        return list;
    }

    private List<String> getTableTitles(){
        List<String> list = getTableTitle();
        list.add("开始时间");
        list.add("结束时间");
        list.add("0为过车，1为过脸");
        list.add("图片数量");
        list.add("0：无效 1：有效");
        return list;
    }

    public String creatTblCameraDetailOriginalCell(Object []titles, List<TblCameraDetailOriginalData> list, List<TblCameraDetailOriginalData> list1, Object []titlesList, List<TblCameraDatailOriginalImageData> list2,HttpServletResponse response) throws Exception {
        //定义表头
        String str[]=Arrays.copyOf(titles, titles.length, String[].class);
        //创建excel工作簿
        HSSFWorkbook workbook=new HSSFWorkbook();
        //创建工作表sheet
        HSSFSheet sheet=workbook.createSheet("摄像机在线信息");
        HSSFSheet sheet1=workbook.createSheet("摄像机离线信息");
        HSSFSheet sheet2=workbook.createSheet("有效抓拍机");
        //创建工作表sheet2
        HSSFSheet sheet3=workbook.createSheet("字段说明");
        //创建第一行
        HSSFRow row=sheet.createRow(0);
        HSSFRow row1=sheet1.createRow(0);
        HSSFRow row2=sheet2.createRow(0);
        HSSFRow row3=sheet3.createRow(0);
        HSSFCell hssfCell=null;
        //插入第一行数据的表头
        for(int i=0;i<str.length;i++){
            if(i==20){
                hssfCell=row.createCell(i);
                hssfCell.setCellValue("订阅状态");
            } else if(i==21){
                hssfCell=row.createCell(i);
                hssfCell.setCellValue("相机属性");
            } else if(i==23){
                hssfCell=row.createCell(i);
                hssfCell.setCellValue("分析类型");
            } else if(i==25){
                hssfCell=row.createCell(i);
                hssfCell.setCellValue("智能任务类型");
            }else {
                hssfCell = row.createCell(i);
                hssfCell.setCellValue(str[i]);
            }
        }
        //插入第一行数据的表头
        for(int i=0;i<str.length;i++){
            if(i==20){
                hssfCell=row1.createCell(i);
                hssfCell.setCellValue("订阅状态");
            } else if(i==21){
                hssfCell=row1.createCell(i);
                hssfCell.setCellValue("相机属性");
            } else if(i==23){
                hssfCell=row1.createCell(i);
                hssfCell.setCellValue("分析类型");
            } else if(i==25){
                hssfCell=row1.createCell(i);
                hssfCell.setCellValue("智能任务类型");
            }else {
                hssfCell = row1.createCell(i);
                hssfCell.setCellValue(str[i]);
            }
        }

        //第二行写入数据
        for (int i=0;i<list.size();i++){
            //list.size()代表有多少行
            HSSFRow nrow=sheet.createRow(i+1);
            for(int j=0;j<=titles.length;j++){
                //写入第一列
                HSSFCell ncell=nrow.createCell(j);
                getCon(ncell,list.get(i),j);
            }
        }
        //第二行写入数据
        for(int x=0;x<list1.size();x++){
            HSSFRow nrow1=sheet1.createRow(x+1);
            for(int z=0;z<=titles.length;z++){
                //写入第一列
                HSSFCell ncells=nrow1.createCell(z);
                getCones(ncells,list1.get(x),z);
            }
        }

        //有效抓拍机
        String strs[]=Arrays.copyOf(titlesList, titlesList.length, String[].class);
        for(int i=0;i<strs.length;i++){
            if(i==20){
                hssfCell=row2.createCell(i);
                hssfCell.setCellValue("订阅状态");
            } else if(i==21){
                hssfCell=row2.createCell(i);
                hssfCell.setCellValue("相机属性");
            } else if(i==23){
                hssfCell=row2.createCell(i);
                hssfCell.setCellValue("分析类型");
            } else if(i==25){
                hssfCell=row2.createCell(i);
                hssfCell.setCellValue("智能任务类型");
            }else if(i==29){
                hssfCell=row2.createCell(i);
                hssfCell.setCellValue("类型");
            }else if(i==31){
                hssfCell=row2.createCell(i);
                hssfCell.setCellValue("有效摄像机标志");
            }else {
                hssfCell = row2.createCell(i);
                hssfCell.setCellValue(strs[i]);
            }
        }

        //写入数据
        for (int i=0;i<list2.size();i++){
            //list.size()代表有多少行
            HSSFRow nrow=sheet2.createRow(i+1);
            for(int j=0;j<=titles.length;j++){
                //写入第一列
                HSSFCell ncell=nrow.createCell(j);
                getCons(ncell,list2.get(i),j);
            }
        }

        /*
        * 字段说明
        * */
        HSSFCell hssfCell2=null;
        hssfCell2=row3.createCell(0);
        hssfCell2.setCellValue("摄像机在线离线字段说明");
        for(int i=0;i<titles.length;i++){
            //第二行
            for(int j=0;j<1;j++){
                if(i==20){
                    HSSFRow nrow=sheet3.createRow(1);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("订阅状态");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
                if(i==21){
                    HSSFRow nrow=sheet3.createRow(2);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("相机属性");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
                if(i==23){
                    HSSFRow nrow=sheet3.createRow(3);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("分析类型");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
                if(i==25){
                    HSSFRow nrow=sheet3.createRow(4);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("智能任务类型");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
                if(i==29){
                    HSSFRow nrow=sheet3.createRow(5);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("类型");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
                if(i==31){
                    HSSFRow nrow=sheet3.createRow(6);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("有效摄像机标志");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
            }
        }

        HSSFRow nrow1=sheet3.createRow(8);
        HSSFCell ncells=nrow1.createCell(0);
        ncells.setCellValue("有效抓拍机字段说明");
        HSSFRow nrow=sheet3.createRow(9);
        HSSFCell ncell=nrow.createCell(0);
        ncell.setCellValue("摄像机在线状态");
        HSSFCell ncell1=nrow.createCell(1);
        ncell1.setCellValue("0：离线，1：在线");
        //创建excel文件
        File file=new File("/tmp/poi.xls");
        String path = file.getPath();
        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= FileUtils.openOutputStream(file);
            workbook.write(stream);
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode("poi.xls", "utf-8"));

            // 2.下载
            OutputStream out = null;
            out = response.getOutputStream();
            // inputStream：读文件，前提是这个文件必须存在，要不就会报错
            InputStream is = new FileInputStream(path);
            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            stream.close();
            out.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(file.exists()){
                file.delete();
            }
        }
        return path;
    }






    public String creatTblCameraDetailOriginalImageCell(Object []titles,List<TblCameraDatailOriginalImageData> lists) throws Exception {
        //定义表头
        String str[]=Arrays.copyOf(titles, titles.length, String[].class);
        //创建excel工作簿
        HSSFWorkbook workbook=new HSSFWorkbook();
        //创建工作表sheet
        HSSFSheet sheet=workbook.createSheet("字段说明");
        //创建工作表sheet2
        HSSFSheet sheet1=workbook.createSheet("数据信息");
        //创建第一行
        HSSFRow row=sheet.createRow(0);
        HSSFCell hssfCell=null;
        //插入第一行数据的表头
        for(int i=0;i<str.length;i++){
            if(i==20){
                hssfCell=row.createCell(i);
                hssfCell.setCellValue("订阅状态");
            } else if(i==21){
                hssfCell=row.createCell(i);
                hssfCell.setCellValue("相机属性");
            } else if(i==23){
                hssfCell=row.createCell(i);
                hssfCell.setCellValue("分析类型");
            } else if(i==25){
                hssfCell=row.createCell(i);
                hssfCell.setCellValue("智能任务类型");
            }else if(i==29){
                hssfCell=row.createCell(i);
                hssfCell.setCellValue("类型");
            }else if(i==31){
                hssfCell=row.createCell(i);
                hssfCell.setCellValue("有效摄像机标志");
            }else {
                hssfCell = row.createCell(i);
                hssfCell.setCellValue(str[i]);
            }
        }
        //写入数据
        for (int i=0;i<lists.size();i++){
            //list.size()代表有多少行
            HSSFRow nrow=sheet.createRow(i+1);
            for(int j=0;j<=titles.length;j++){
                //写入第一列
                HSSFCell ncell=nrow.createCell(j);
                getCons(ncell,lists.get(i),j);

            }
        }
        HSSFRow row1=sheet1.createRow(0);
        HSSFCell hssfCell1=null;
        String path = null;
        hssfCell1=row1.createCell(0);
        hssfCell1.setCellValue("字段说明");
        for(int i=0;i<titles.length;i++){
            //第二行
            HSSFRow nrow1=sheet1.createRow(i+1);
            for(int j=0;j<1;j++){
                if(i==20){
                    HSSFRow nrow=sheet1.createRow(1);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("订阅状态");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
                if(i==21){
                    HSSFRow nrow=sheet1.createRow(2);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("相机属性");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
                if(i==23){
                    HSSFRow nrow=sheet1.createRow(3);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("分析类型");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
                if(i==25){
                    HSSFRow nrow=sheet1.createRow(4);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("智能任务类型");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
                if(i==29){
                    HSSFRow nrow=sheet1.createRow(5);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("类型");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
                if(i==31){
                    HSSFRow nrow=sheet1.createRow(6);
                    HSSFCell ncell=nrow.createCell(j);
                    ncell.setCellValue("有效摄像机标志");
                    HSSFCell ncell1=nrow.createCell(j+1);
                    ncell1.setCellValue(titles[i].toString());
                }
            }
        }
        //创建excel文件
        File file=new File("D://poi.xlsx");
        path = file.getPath();
        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= FileUtils.openOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    private void getCon(HSSFCell ncell, TblCameraDetailOriginalData tblCameraDetailOriginalData, int j){

        if(j==0){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraSn()== null ? "":tblCameraDetailOriginalData.getCameraSn());
        }
        if(j==1){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraName()== null ? "":tblCameraDetailOriginalData.getCameraName());
        }
        if(j==2){
            ncell.setCellValue(tblCameraDetailOriginalData.getMountHeight() ==null ? "":tblCameraDetailOriginalData.getMountHeight());
        }
        if(j==3){
            ncell.setCellValue(tblCameraDetailOriginalData.getLongitude() ==null ? "":tblCameraDetailOriginalData.getLongitude());
        }
        if(j==4){
            ncell.setCellValue(tblCameraDetailOriginalData.getLatitude() ==null ? "":tblCameraDetailOriginalData.getLatitude());
        }
        if(j==5){
            ncell.setCellValue(tblCameraDetailOriginalData.getAddress() ==null ? "":tblCameraDetailOriginalData.getAddress());
        }
        if(j==6){
            ncell.setCellValue(tblCameraDetailOriginalData.getStreamUrl() ==null ? "":tblCameraDetailOriginalData.getStreamUrl());
        }
        if(j==7){
            ncell.setCellValue(tblCameraDetailOriginalData.getDirection() ==null ? "":tblCameraDetailOriginalData.getDirection());
        }
        if(j==8){
            ncell.setCellValue(tblCameraDetailOriginalData.getLane()  ==null ? "":tblCameraDetailOriginalData.getLane());
        }
        if(j==9){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraState()==null ? "":tblCameraDetailOriginalData.getLane());
        }
        if(j==10){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraType() ==null ? "":tblCameraDetailOriginalData.getCameraType());
        }
        if(j==11){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraUse() ==null ? "":tblCameraDetailOriginalData.getCameraUse());
        }
        if(j==12){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraFeature() ==null ? "":tblCameraDetailOriginalData.getCameraFeature());
        }
        if(j==13){
            ncell.setCellValue(tblCameraDetailOriginalData.getPlatName() ==null ? "":tblCameraDetailOriginalData.getPlatName());
        }
        if(j==14){
            ncell.setCellValue(tblCameraDetailOriginalData.getResolutionType() ==null ? "":tblCameraDetailOriginalData.getResolutionType());
        }
        if(j==15){
            ncell.setCellValue(tblCameraDetailOriginalData.getFieldNo() ==null ? "":tblCameraDetailOriginalData.getFieldNo());
        }
        if(j==16){
            ncell.setCellValue(tblCameraDetailOriginalData.getCompass() ==null ? "":tblCameraDetailOriginalData.getCompass());
        }
        if(j==17){
            ncell.setCellValue(tblCameraDetailOriginalData.getNvr() ==null ? "":tblCameraDetailOriginalData.getNvr());
        }
        if(j==18){
            ncell.setCellValue(tblCameraDetailOriginalData.getGroupId() ==null ? "":tblCameraDetailOriginalData.getGroupId());
        }
        if(j==19){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraId() ==null ? "":tblCameraDetailOriginalData.getCameraId());
        }
        if(j==20){
            ncell.setCellValue(tblCameraDetailOriginalData.getSubstatus() ==null ? "":tblCameraDetailOriginalData.getSubstatus());
        }
        if(j==21){
            ncell.setCellValue(tblCameraDetailOriginalData.getVcnCameraUse() ==null ? "":tblCameraDetailOriginalData.getVcnCameraUse());
        }
        if(j==22){
            ncell.setCellValue(tblCameraDetailOriginalData.getAnalysisList() ==null ? "":tblCameraDetailOriginalData.getAnalysisList());
        }
        if(j==23){
            ncell.setCellValue(tblCameraDetailOriginalData.getAnalysisType() ==null ? "":tblCameraDetailOriginalData.getAnalysisType());
        }
        if(j==24){
            ncell.setCellValue(tblCameraDetailOriginalData.getTaskTypeList() ==null ? "":tblCameraDetailOriginalData.getTaskTypeList());
        }
        if(j==25){
            ncell.setCellValue(tblCameraDetailOriginalData.getTaskType()==null ? "":tblCameraDetailOriginalData.getLane());
        }
        if(j==26){
            ncell.setCellValue(tblCameraDetailOriginalData.getIpIvs() ==null ? "":tblCameraDetailOriginalData.getIpIvs());
        }
    }

    private void getCones(HSSFCell ncell, TblCameraDetailOriginalData tblCameraDetailOriginalData, int j){

        if(j==0){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraSn()== null ? "":tblCameraDetailOriginalData.getCameraSn());
        }
        if(j==1){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraName()== null ? "":tblCameraDetailOriginalData.getCameraName());
        }
        if(j==2){
            ncell.setCellValue(tblCameraDetailOriginalData.getMountHeight() ==null ? "":tblCameraDetailOriginalData.getMountHeight());
        }
        if(j==3){
            ncell.setCellValue(tblCameraDetailOriginalData.getLongitude() ==null ? "":tblCameraDetailOriginalData.getLongitude());
        }
        if(j==4){
            ncell.setCellValue(tblCameraDetailOriginalData.getLatitude() ==null ? "":tblCameraDetailOriginalData.getLatitude());
        }
        if(j==5){
            ncell.setCellValue(tblCameraDetailOriginalData.getAddress() ==null ? "":tblCameraDetailOriginalData.getAddress());
        }
        if(j==6){
            ncell.setCellValue(tblCameraDetailOriginalData.getStreamUrl() ==null ? "":tblCameraDetailOriginalData.getStreamUrl());
        }
        if(j==7){
            ncell.setCellValue(tblCameraDetailOriginalData.getDirection() ==null ? "":tblCameraDetailOriginalData.getDirection());
        }
        if(j==8){
            ncell.setCellValue(tblCameraDetailOriginalData.getLane()  ==null ? "":tblCameraDetailOriginalData.getLane());
        }
        if(j==9){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraState()==null ? "":tblCameraDetailOriginalData.getLane());
        }
        if(j==10){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraType() ==null ? "":tblCameraDetailOriginalData.getCameraType());
        }
        if(j==11){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraUse() ==null ? "":tblCameraDetailOriginalData.getCameraUse());
        }
        if(j==12){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraFeature() ==null ? "":tblCameraDetailOriginalData.getCameraFeature());
        }
        if(j==13){
            ncell.setCellValue(tblCameraDetailOriginalData.getPlatName() ==null ? "":tblCameraDetailOriginalData.getPlatName());
        }
        if(j==14){
            ncell.setCellValue(tblCameraDetailOriginalData.getResolutionType() ==null ? "":tblCameraDetailOriginalData.getResolutionType());
        }
        if(j==15){
            ncell.setCellValue(tblCameraDetailOriginalData.getFieldNo() ==null ? "":tblCameraDetailOriginalData.getFieldNo());
        }
        if(j==16){
            ncell.setCellValue(tblCameraDetailOriginalData.getCompass() ==null ? "":tblCameraDetailOriginalData.getCompass());
        }
        if(j==17){
            ncell.setCellValue(tblCameraDetailOriginalData.getNvr() ==null ? "":tblCameraDetailOriginalData.getNvr());
        }
        if(j==18){
            ncell.setCellValue(tblCameraDetailOriginalData.getGroupId() ==null ? "":tblCameraDetailOriginalData.getGroupId());
        }
        if(j==19){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraId() ==null ? "":tblCameraDetailOriginalData.getCameraId());
        }
        if(j==20){
            ncell.setCellValue(tblCameraDetailOriginalData.getSubstatus() ==null ? "":tblCameraDetailOriginalData.getSubstatus());
        }
        if(j==21){
            ncell.setCellValue(tblCameraDetailOriginalData.getVcnCameraUse() ==null ? "":tblCameraDetailOriginalData.getVcnCameraUse());
        }
        if(j==22){
            ncell.setCellValue(tblCameraDetailOriginalData.getAnalysisList() ==null ? "":tblCameraDetailOriginalData.getAnalysisList());
        }
        if(j==23){
            ncell.setCellValue(tblCameraDetailOriginalData.getAnalysisType() ==null ? "":tblCameraDetailOriginalData.getAnalysisType());
        }
        if(j==24){
            ncell.setCellValue(tblCameraDetailOriginalData.getTaskTypeList() ==null ? "":tblCameraDetailOriginalData.getTaskTypeList());
        }
        if(j==25){
            ncell.setCellValue(tblCameraDetailOriginalData.getTaskType()==null ? "":tblCameraDetailOriginalData.getLane());
        }
        if(j==26){
            ncell.setCellValue(tblCameraDetailOriginalData.getIpIvs() ==null ? "":tblCameraDetailOriginalData.getIpIvs());
        }
    }

    private void getCons(HSSFCell ncell, TblCameraDatailOriginalImageData tblCameraDetailOriginalData, int j){

        if(j==0){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraSn() ==null ? "":tblCameraDetailOriginalData.getCameraSn());
        }
        if(j==1){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraName() ==null ? "":tblCameraDetailOriginalData.getCameraName());
        }
        if(j==2){
            ncell.setCellValue(tblCameraDetailOriginalData.getMountHeight() ==null ? "":tblCameraDetailOriginalData.getMountHeight());
        }
        if(j==3){
            ncell.setCellValue(tblCameraDetailOriginalData.getLongitude() ==null ? "":tblCameraDetailOriginalData.getLongitude());
        }
        if(j==4){
            ncell.setCellValue(tblCameraDetailOriginalData.getLatitude() ==null ? "":tblCameraDetailOriginalData.getLatitude());
        }
        if(j==5){
            ncell.setCellValue(tblCameraDetailOriginalData.getAddress() ==null ? "":tblCameraDetailOriginalData.getAddress());
        }
        if(j==6){
            ncell.setCellValue(tblCameraDetailOriginalData.getStreamUrl() ==null ? "":tblCameraDetailOriginalData.getStreamUrl());
        }
        if(j==7){
            ncell.setCellValue(tblCameraDetailOriginalData.getDirection() ==null ? "":tblCameraDetailOriginalData.getDirection());
        }
        if(j==8){
            ncell.setCellValue(tblCameraDetailOriginalData.getLane() ==null ? "":tblCameraDetailOriginalData.getLane());
        }
        if(j==9){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraState()==null ? "":tblCameraDetailOriginalData.getLane());
        }
        if(j==10){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraType() ==null ? "":tblCameraDetailOriginalData.getCameraType());
        }
        if(j==11){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraUse() ==null ? "":tblCameraDetailOriginalData.getCameraUse());
        }
        if(j==12){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraFeature() ==null ? "":tblCameraDetailOriginalData.getCameraFeature());
        }
        if(j==13){
            ncell.setCellValue(tblCameraDetailOriginalData.getPlatName() ==null ? "":tblCameraDetailOriginalData.getPlatName());
        }
        if(j==14){
            ncell.setCellValue(tblCameraDetailOriginalData.getResolutionType() ==null ? "":tblCameraDetailOriginalData.getResolutionType());
        }
        if(j==15){
            ncell.setCellValue(tblCameraDetailOriginalData.getFieldNo() ==null ? "":tblCameraDetailOriginalData.getFieldNo());
        }
        if(j==16){
            ncell.setCellValue(tblCameraDetailOriginalData.getCompass() ==null ? "":tblCameraDetailOriginalData.getCompass());
        }
        if(j==17){
            ncell.setCellValue(tblCameraDetailOriginalData.getNvr() ==null ? "":tblCameraDetailOriginalData.getNvr());
        }
        if(j==18){
            ncell.setCellValue(tblCameraDetailOriginalData.getGroupId() ==null ? "":tblCameraDetailOriginalData.getGroupId());
        }
        if(j==19){
            ncell.setCellValue(tblCameraDetailOriginalData.getCameraId() ==null ? "":tblCameraDetailOriginalData.getCameraId());
        }
        if(j==20){
            ncell.setCellValue(tblCameraDetailOriginalData.getSubstatus() ==null ? "":tblCameraDetailOriginalData.getSubstatus());
        }
        if(j==21){
            ncell.setCellValue(tblCameraDetailOriginalData.getVcnCameraUse() ==null ? "":tblCameraDetailOriginalData.getVcnCameraUse());
        }
        if(j==22){
            ncell.setCellValue(tblCameraDetailOriginalData.getAnalysisList() ==null ? "":tblCameraDetailOriginalData.getAnalysisList());
        }
        if(j==23){
            ncell.setCellValue(tblCameraDetailOriginalData.getAnalysisType() ==null ? "":tblCameraDetailOriginalData.getAnalysisType());
        }
        if(j==24){
            ncell.setCellValue(tblCameraDetailOriginalData.getTaskTypeList() ==null ? "":tblCameraDetailOriginalData.getTaskTypeList());
        }
        if(j==25){
            ncell.setCellValue(tblCameraDetailOriginalData.getTaskType()==null ? "":tblCameraDetailOriginalData.getLane());
        }
        if(j==26){
            ncell.setCellValue(tblCameraDetailOriginalData.getIpIvs() ==null ? "":tblCameraDetailOriginalData.getIpIvs());
        }
        if(j==27){
            ncell.setCellValue(tblCameraDetailOriginalData.getStartTime() ==null ? "":tblCameraDetailOriginalData.getStartTime());
        }
        if(j==28){
            ncell.setCellValue(tblCameraDetailOriginalData.getEndTime() ==null ? "":tblCameraDetailOriginalData.getEndTime());
        }
        if(j==29){
            ncell.setCellValue(tblCameraDetailOriginalData.getType() ==null ? "":tblCameraDetailOriginalData.getType());
        }
        if(j==30){
            ncell.setCellValue(tblCameraDetailOriginalData.getPictureCount()==null ? "":tblCameraDetailOriginalData.getLane());
        }
        if(j==31){
            ncell.setCellValue(tblCameraDetailOriginalData.getEffectiveCamera() ==null ? "":tblCameraDetailOriginalData.getEffectiveCamera());
        }
    }

    /**
     * 判断Excel的版本,获取Workbook
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(File file) throws IOException{
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if(file.getName().toLowerCase().endsWith("excel_xls")){     //Excel&nbsp;2003
            wb = new HSSFWorkbook(in);
        }else if(file.getName().toLowerCase().endsWith("excel_xlsx")){    // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }
}
