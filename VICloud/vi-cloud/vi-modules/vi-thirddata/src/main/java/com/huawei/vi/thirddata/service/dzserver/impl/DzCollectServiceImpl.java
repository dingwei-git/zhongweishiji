package com.huawei.vi.thirddata.service.dzserver.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.utils.*;
import com.huawei.vi.entity.vo.ProgressInfo;
import com.huawei.vi.thirddata.mapper.*;
import com.huawei.vi.thirddata.service.binary.pojo.DzOriginalData;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;
import com.huawei.vi.thirddata.service.dzserver.DzCollectService;
import com.huawei.webservice.order.*;

import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.util.ProtocolConfig;
import com.jovision.jaws.common.util.UtilMethod;
import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 东智服务器数据采集service实现类
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class DzCollectServiceImpl implements DzCollectService {
    /**
     * 公共日志打印
     */
    private static final Logger LOG = LoggerFactory.getLogger(DzCollectServiceImpl.class);

    private static final String SQL_TABNAME = "tabName";

    private static final int NODATA = -1;

    /**
     * 东智服务器采集单页最大采集数据条数：2000
     */
    private static final int KEY_PAGE_LIMIT = 2000;

    private static final int BATCHUNIT = 100;

    /**
     * 超过1000条数据的时候，从第2页开始采集：2
     */
    private static final int KEY_PAGE_TWO = NumConstant.NUM_2;

    private static final String SQL_IPLIST = "ipList";

    private static final String CAMERAID = "cameraId";

    private static final String CAMERAIDID = "cameraid";

    private static final String ONEFLAG = "oneFlag";

    private static final String PAGEINDEX = "pageIndex";

    private static final String PAGESIZE = "pageSize";

    private static final String CODE = "code";

    private static final String COUNTS = "counts";

    private static final String DATETIME = "datetime";

    private static final String DATA = "data";

    @Autowired
    private TblDzOriginalDataMapper tblDzOriginalDataDao;

    @Resource(name = "serverParamConfigDao")
    private TblServerParamConfigMapper serverParamConfigDao;

    @Resource(name = "tblDiscardIpCollectionDao")
    private TblDiscardIpCollectionMapper tblDiscardIpCollectionDao;

    @Resource(name = "periodManageDao")
    private TblPeriodManageMapper periodManageDao;

    @Autowired
    private TblDzDiagnosisDataMapper tblDzDiagnosisDataDao;

    /**
     * 东智服务器数据采集
     *
     * @param beginTime 开始采集的时间
     * @param tblServerParamConfigs 服务器参数配置
     */
    @Override
    public void dzServerCollectStart(List<TblServerParamConfig> tblServerParamConfigs, Date beginTime) {
        LOG.info("inter dzServerCollectStart");
        if (CollectionUtil.isEmpty(tblServerParamConfigs)) {
            LOG.error("server param config is empty...");
            return;
        }
        tblServerParamConfigs.stream()
            .filter(config -> CommonConst.SQL_DZ_FLAG.equals(config.getServiceFlag())) // 过滤东智服务器参数
            .forEach(config -> {
                String url = ProtocolConfig.getParameterConfig()
                    .getDzCollectUrl()
                    .replaceAll("IPADDR", config.getServiceIpAddress())
                    .replaceAll("PORT", config.getDzPort());
                try {
                    dzServerCollect(beginTime, url);
                } catch (AxisFault axisFault) {
                    axisFault.printStackTrace();
                }
            });
        LOG.info("out dzServerCollectStart");
    }

    /**
     * 东智服务器Q4新需求的数据采集开始
     *
     * @param tblServerParamConfigs 服务器参数配置
     * @param beginTime 开始采集的时间
     */
    @Override
    public void dzServerInternalCollect(List<TblServerParamConfig> tblServerParamConfigs, Date beginTime) {
        LOG.info("inter dzServerInternalCollect");
        if (CollectionUtil.isEmpty(tblServerParamConfigs)) {
            LOG.error("server param config is empty...");
            return;
        }
        if (tblServerParamConfigs.stream().noneMatch(one -> CommonConst.SQL_DZ_FLAG.equals(one.getServiceFlag()))) {
            LOG.error("dz server param config is empty...");
            return;
        }
        Map<String, Object> keyMaps = new HashMap<String, Object>();
        List<DzOriginalData> lastDiagDatas = tblDzDiagnosisDataDao.selectByCondition(keyMaps);
        if (CollectionUtil.isEmpty(lastDiagDatas)) {
            LOG.error("The latest image quality diagnosis result of the camera is empty...");
            return;
        }
        String tableName = com.jovision.jaws.common.util.UtilMethod.getTableName(CommonConst.TBL_DZ_ORIGINAL_DATA, beginTime);
        keyMaps.put(SQL_TABNAME, tableName);
        dropAndCreateTbldzOriginalData(keyMaps); // tbl_dz_original_data
        if (!firstInsertScuess(lastDiagDatas, tableName)) {
            // 摄像机最新图像质量诊断结果原始数据入库失败...
            LOG.info("The latest image quality diagnosis result of the camera failed to enter the database.");
            return;
        }

        // 更新字段值
        updateDiagnosisFlagOldDiagResultIndex(beginTime, tableName);
        LOG.info("out dzServerInternalCollect");
    }

    /**
     * 东智服务器数据采集开始
     *
     * @param beginTime 周期开始时间
     * @param url 东智服务器链接
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void dzServerCollect(Date beginTime, String url) throws AxisFault {
        List<DzOriginalData> lastDiagDatas = collectLastDiagData(url);
        if (CollectionUtil.isEmpty(lastDiagDatas)) {
            // 摄像机最新图像质量诊断结果原始数据采集异常
            LOG.error("The latest image quality diagnosis result of the camera is abnormal.");
            return;
        }
        String tableName = com.jovision.jaws.common.util.UtilMethod.getTableName(CommonConst.TBL_DZ_ORIGINAL_DATA, beginTime);
        Map<String, Object> keyMaps = new HashMap<String, Object>();
        keyMaps.put(SQL_TABNAME, tableName);
        dropAndCreateTbldzOriginalData(keyMaps); // tbl_dz_original_data
        if (!firstInsertScuess(lastDiagDatas, tableName)) {
            // 摄像机最新图像质量诊断结果原始数据入库失败...
            LOG.info("The latest image quality diagnosis result of the camera failed to enter the database.");
            return;
        }
        boolean isSecondInsertScuessFlag = true;
        /* 紧急需求更改点：增加合并同一个摄像机监测不同车道情况的数据逻辑处理 */
        List<String> repeatDatas = tblDzOriginalDataDaoGetRepeatCameraId(keyMaps); // 获取重复信息的cameraid
        if (CollectionUtil.isNotEmpty(repeatDatas)) {
            lastDiagDatas.clear();
            for (String cameraId : repeatDatas) {
                Map<String, Object> maps = new HashMap<String, Object>();
                maps.put(SQL_TABNAME, tableName);
                maps.put(CAMERAID, cameraId);
                DzOriginalData dzOrig = tblDzOriginalDataDaoGetMinData(maps); // 重复的id根据规则取最小值进行合并
                if (CommonUtil.isNull(dzOrig)) {
                    LOG.error("dzOrig is null");
                    continue;
                }
                Optional<Double> optionalMinDouble = getMinDzOrig(dzOrig);
                if (!optionalMinDouble.isPresent()) {
                    LOG.error("optionalMinDouble is null");
                    continue;
                }

                // 查询10种故障的最小值
                getMinValueMap(maps, optionalMinDouble.get(), dzOrig);
                DzOriginalData dzOriginalData = tblDzOriginalDataDaoGetPictureUrl(maps); // 设置最小值对应的图片地址
                // 设置最小值对应的最新一次诊断结果
                if (dzOriginalData != null) {
                    dzOrig.setPictureUrl(dzOriginalData.getPictureUrl());
                    dzOrig.setDiagResultIndex(dzOriginalData.getDiagResultIndex());
                }
                lastDiagDatas.add(dzOrig);
            }
            keyMaps.put("cameraIdList", repeatDatas); // 有重复cameraid的信息
            tblDzOriginalDataDaoDeleteRepeatInfo(keyMaps); // 删除tbl_dz_original_data表中所有cameraid是重复的
            keyMaps.put("lastDiagData", lastDiagDatas);
            isSecondInsertScuessFlag = tblDzOriginalDataDaoInsertDzOriginalData(keyMaps) >= 0; // 把规整之后的的重复cameraid结果入库
        }
        if (isSecondInsertScuessFlag) {
            secondInsert(beginTime, tableName, keyMaps, url);
        }
    }

    private Optional<Double> getMinDzOrig(DzOriginalData dzOrig) {
        return Arrays
            .asList(dzOrig.getSceneChange(), dzOrig.getSignalLost(), dzOrig.getColourCast(), dzOrig.getBrightness(),
                dzOrig.getSnow(), dzOrig.getCover(), dzOrig.getStripe(), dzOrig.getPtzSpeed(), dzOrig.getFreeze(),
                dzOrig.getDefinition())
            .stream()
            .min(Comparator.comparing(Double::doubleValue));
    }

    private List<String> tblDzOriginalDataDaoGetRepeatCameraId(Map<String, Object> keyMap) {
        try {
            return tblDzOriginalDataDao.getRepeatCameraId(keyMap);
        } catch (DataAccessException e) {
            LOG.error("getRepeatCameraId DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    private DzOriginalData tblDzOriginalDataDaoGetMinData(Map<String, Object> map) {
        try {
            return tblDzOriginalDataDao.getMinData(map);
        } catch (DataAccessException e) {
            LOG.error("getMinData DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    private DzOriginalData tblDzOriginalDataDaoGetPictureUrl(Map<String, Object> map) {
        try {
            return tblDzOriginalDataDao.getPictureUrl(map);
        } catch (DataAccessException e) {
            LOG.error("getPictureUrl DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    private void tblDzOriginalDataDaoDeleteRepeatInfo(Map<String, Object> keyMap) {
        try {
            tblDzOriginalDataDao.deleteRepeatInfo(keyMap);
        } catch (DataAccessException e) {
            LOG.error("deleteRepeatInfo DataAccessException {} ", e.getMessage());
        }
    }

    private void secondInsert(Date beginTime, @NonNull String tableName, Map<String, Object> keyMap, String url) {
        LOG.info("inter secondInsert");
        if (!updateDiagnosisFlagOldDiagResultIndex(beginTime, tableName)) {
            return;
        }
        List<DzOriginalData> signallingDatas = collectSignallingData(url); // 摄像机信令检测结果
        updateDlkDefaultData(signallingDatas, keyMap);
        List<DzOriginalData> deviceVideotapes = collectDeviceVideotapeData(url); // 摄像机录像巡检结果
        updateDlkDefaultData(deviceVideotapes, tableName);
        LOG.info("out secondInsert");
    }

    /**
     * 更新字段值：“上一次周期诊断标识”、“是否为最新一次诊断结果标识：1是最新诊断结果，0不是最新诊断结果”
     *
     * @param beginTime 开始采集时间
     * @param tableName 表名
     * @return 是否更新成功：true成功，false失败
     */
    private boolean updateDiagnosisFlagOldDiagResultIndex(Date beginTime, @NonNull String tableName) {
        // 获取当前周期最近的一次周期时间
        String lastPeriodTime = periodManageDaoGetLastNewDate(com.jovision.jaws.common.util.UtilMethod.dateToString(beginTime));
        Map<String, Object> updateTables = new HashMap<String, Object>();
        updateTables.put(SQL_TABNAME, tableName);
        if (lastPeriodTime != null) {
            Date date = UtilMethod.stringPeriodToDate(
                lastPeriodTime.replace(CommonSymbolicConstant.SYMBOL_COLON, CommonSymbolicConstant.SYMBOL_POINT));
            if (date == null) {
                LOG.error("lastPeriodTime is invalid .lastPeriodTime is {}", lastPeriodTime);
                return false;
            }
            String oldDzTable = com.jovision.jaws.common.util.UtilMethod.getTableName(CommonConst.TBL_DZ_ORIGINAL_DATA, date);
            updateTables.put("newTable", tableName);
            updateTables.put("oldTable", oldDzTable);

            // 假如断了一阵没有采集数据？？？？查询周期表离当前周期最近的一个周期
            tblDzOriginalDataDaoUpdateOldDiagResultIndex(updateTables);
            updateTables.put(ONEFLAG, 1); // 新旧结果不同的更新标识为1
            tblDzOriginalDataDaoUpdateDiagResultFlag(updateTables);
            updateTables.remove(ONEFLAG);
            updateTables.put("zeroFlag", 0); // 新旧结果一样的更新状态为0
            tblDzOriginalDataDaoUpdateDiagResultFlag(updateTables);
        } else {
            updateTables.put(ONEFLAG, 1); // 首次采集直接更新标识为1；
            tblDzOriginalDataDaoUpdateDiagResultFlag(updateTables);
        }
        return true;
    }

    private String periodManageDaoGetLastNewDate(String beginTime) {
        try {
            return periodManageDao.getLastNewDate(beginTime);
        } catch (DataAccessException e) {
            LOG.error("getLastNewDate DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    private void tblDzOriginalDataDaoUpdateOldDiagResultIndex(Map<String, Object> updateTable) {
        try {
            tblDzOriginalDataDao.updateOldDiagResultIndex(updateTable);
        } catch (DataAccessException e) {
            LOG.error("updateOldDiagResultIndex DataAccessException {} ", e.getMessage());
        }
    }

    private void tblDzOriginalDataDaoUpdateDiagResultFlag(Map<String, Object> updateTable) {
        try {
            tblDzOriginalDataDao.updateDiagResultFlag(updateTable);
        } catch (DataAccessException e) {
            LOG.error("showTableByname DataAccessException {} ", e.getMessage());
        }
    }

    private void updateDlkDefaultData(List<DzOriginalData> signallingData, Map<String, Object> keyMap) {
        if (CollectionUtil.isEmpty(signallingData)) {
            LOG.error("Update SignallingData Failed! because signallingData is null or empty.");
            return;
        }
        signallingData.stream().forEach(dzOriginalData -> {
            keyMap.put("rtpDelay", dzOriginalData.getRtpDelay());
            keyMap.put("sipDelay", dzOriginalData.getSipDelay());
            keyMap.put("errorCode", dzOriginalData.getErrorCode());
            keyMap.put("ifRameDelay", dzOriginalData.getIfRameDelay());
            keyMap.put("online", dzOriginalData.getOnline());
            keyMap.put(CAMERAID, dzOriginalData.getCameraId());
            try {
                tblDzOriginalDataDao.updateDlkDefaultData(keyMap);
            } catch (DataAccessException e) {
                LOG.error("updateDlkDefaultData DataAccessException {} ", e.getMessage());
            }
        });
        LOG.info("Update SignallingData Success.");
    }

    private void updateDlkDefaultData(List<DzOriginalData> deviceVideotape, String tableName) {
        if (CollectionUtil.isEmpty(deviceVideotape)) {
            LOG.error("Update DeviceVideotapeData Failed! because deviceVideotape is null or empty.");
            return;
        }
        Map<String, Object> ifIntegrityMap = new HashMap<String, Object>();
        ifIntegrityMap.put(SQL_TABNAME, tableName);
        deviceVideotape.stream().forEach(dzOriginalData -> {
            ifIntegrityMap.put(CAMERAID, dzOriginalData.getCameraId());
            ifIntegrityMap.put("recordIntegrity", dzOriginalData.getRecordIntegrity());
            ifIntegrityMap.put("videoDateTime", dzOriginalData.getVideoDateTime());
            ifIntegrityMap.put("recordRule", dzOriginalData.getRecordRule());
            ifIntegrityMap.put("recordSaveTime", dzOriginalData.getRecordSaveTime());
            ifIntegrityMap.put("lostRecord", dzOriginalData.getLostRecord());
            ifIntegrityMap.put("videoDiagResultIndex", dzOriginalData.getVideoDiagResultIndex());
            try {
                tblDzOriginalDataDao.updateDlkDefaultData(ifIntegrityMap);
            } catch (DataAccessException e) {
                LOG.error("updateDlkDefaultData DataAccessException {} ", e.getMessage());
            }
        });
        LOG.info("Update DeviceVideotapeData Success.");
    }

    /**
     * 判断入库是否成功
     *
     * @param lastDiagData 入库数据
     * @param tableName tableName
     * @return boolean
     */
    private boolean firstInsertScuess(@NonNull List<DzOriginalData> lastDiagData, @NonNull String tableName) {
        long count = 0 == lastDiagData.size() % BATCHUNIT ? lastDiagData.size() / BATCHUNIT
            : lastDiagData.size() / BATCHUNIT + 1;
        boolean isFirstInsertScuessFlag = false;
        Map<String, Object> conditons = new HashMap<String, Object>();
        conditons.put(SQL_TABNAME, tableName);
        if (ProgressInfo.isHaveImageDiagnosisService()) {
            conditons.put("rtpFlag", NumConstant.NUM_1);
        }
        for (int index = 0; index < count; index++) {
            conditons.put("lastDiagData",
                lastDiagData.stream().skip((long) index * BATCHUNIT).limit(BATCHUNIT).collect(Collectors.toList()));
            boolean isSuccess = tblDzOriginalDataDaoInsertDzOriginalData(conditons) >= 0;
            isFirstInsertScuessFlag = isFirstInsertScuessFlag || isSuccess;
        }
        return isFirstInsertScuessFlag;
    }

    private int tblDzOriginalDataDaoInsertDzOriginalData(Map<String, Object> conditon) {
        try {
            return tblDzOriginalDataDao.insertDzOriginalData(conditon);
        } catch (DataAccessException e) {
            LOG.error("insertDzOriginalData DataAccessException {} ", e.getMessage());
        }
        return 0;
    }

    /**
     * 根据条件删除并创建新表tbl_dz_original_data
     *
     * @param keyMap keyMap
     */
    private void dropAndCreateTbldzOriginalData(Map<String, Object> keyMap) {
        if (MapUtils.isNullOrEmpty(keyMap) || !keyMap.containsKey(SQL_TABNAME)) {
            LOG.error("map is null key error");
            return;
        }
        tblDzOriginalDataDaoDropTable(keyMap);
        try {
            tblDzOriginalDataDao.createDzOriginalDataTable(keyMap);
        } catch (DataAccessException e) {
            LOG.error("createDzOriginalDataTable DataAccessException{}", e.getMessage());
        }
    }

    private void tblDzOriginalDataDaoDropTable(Map<String, Object> keyMap) {
        try {
            tblDzOriginalDataDao.dropTable(keyMap);
        } catch (DataAccessException e) {
            LOG.error("dropTable DataAccessException {} ", e.getMessage());
        }
    }

    private void getMinValueMap(Map<String, Object> map, Double tmp, DzOriginalData dzOriginalData) {
        if (CommonUtil.doubleIsEqual(tmp, dzOriginalData.getSceneChange())) { // 通过最小值找寻对应的表字段
            map.put("sceneChange", tmp);
        } else if (CommonUtil.doubleIsEqual(tmp, dzOriginalData.getSignalLost())) {
            map.put("signalLost", tmp);
        } else if (CommonUtil.doubleIsEqual(tmp, dzOriginalData.getColourCast())) {
            map.put("colourCast", tmp);
        } else if (CommonUtil.doubleIsEqual(tmp, dzOriginalData.getBrightness())) {
            map.put("brightness", tmp);
        } else if (CommonUtil.doubleIsEqual(tmp, dzOriginalData.getSnow())) {
            map.put("snow", tmp);
        } else if (CommonUtil.doubleIsEqual(tmp, dzOriginalData.getCover())) {
            map.put("cover", tmp);
        } else if (CommonUtil.doubleIsEqual(tmp, dzOriginalData.getStripe())) {
            map.put("strip", tmp);
        } else if (CommonUtil.doubleIsEqual(tmp, dzOriginalData.getPtzSpeed())) {
            map.put("ptzSpeed", tmp);
        } else if (CommonUtil.doubleIsEqual(tmp, dzOriginalData.getFreeze())) {
            map.put("freezd", tmp);
        } else if (CommonUtil.doubleIsEqual(tmp, dzOriginalData.getDefinition())) {
            map.put("definition", tmp);
        }
    }

    /**
     * 获取摄像机最新图像质量诊断结果
     * @param url 访问东智服务器的连接
     * @return DzOriginalData集合
     */
    private List<DzOriginalData> collectLastDiagData(String url) throws AxisFault {
        LOG.info("inter collectLastDiagData");
        JSONObject jsonObject = new JSONObject();
        List<DzOriginalData> lastDiagList = new ArrayList<DzOriginalData>();
        jsonObject.put(PAGEINDEX, 1);
        JSONObject json = getFirstLastDiagResult(url, jsonObject); // 获取东智服务器第一页摄像机最新图像质量诊断结果数据
        if (CommonUtil.isNull(json)) {
            LOG.error("collect LastDiagData failed. json is null");
            return lastDiagList;
        }
        if (0 == json.getInteger(CODE)) {
            getLastDiagDataList(json, lastDiagList);
            int counts = json.getInteger(COUNTS);
            int page = 0 == counts % KEY_PAGE_LIMIT ? counts / KEY_PAGE_LIMIT : counts / KEY_PAGE_LIMIT + 1;
            for (int ii = KEY_PAGE_TWO; ii <= page; ii++) {
                jsonObject.put(PAGEINDEX, ii);
                json = getFirstLastDiagResult(url, jsonObject);
                if (json != null && json.getInteger(CODE) == 0) {
                    getLastDiagDataList(json, lastDiagList); // 获取东智服务器第二页以后的摄像机最新图像质量诊断结果数据
                } else {
                    continue;
                }
            }
        } else {
            LOG.info("collect LastDiagData failed CODE is {} .", json.getInteger(CODE));
        }
        LOG.info("out collectLastDiagData");
        return lastDiagList;
    }

    /**
     * 单页获取摄像机最新图像质量诊断结果
     *
     * @param json 采集到的摄像机最新图像质量诊断结果
     * @param lastDiagList json转化为bean
     */
    private void getLastDiagDataList(JSONObject json, List<DzOriginalData> lastDiagList) {
        JSONArray jsonArray = json.getJSONArray(DATA);
        if (CommonUtil.isNull(jsonArray) || jsonArray.isEmpty()) {
            LOG.error("jsonArray is null or is empty.");
            return;
        }
        for (int ii = 0; ii < jsonArray.size(); ii++) {
            JSONObject jsonData = jsonArray.getJSONObject(ii);
            DzOriginalData dzOriginalData = new DzOriginalData();
            dzOriginalData.setSceneChange(jsonData.getDouble("SceneChange"));
            dzOriginalData.setSignalLost(jsonData.getDouble("SignalLost"));
            dzOriginalData.setColourCast(jsonData.getDouble("ColourCast"));
            dzOriginalData.setBrightness(jsonData.getDouble("Brightness"));
            dzOriginalData.setSnow(jsonData.getDouble("Snow"));
            dzOriginalData.setIndicia(jsonData.getDouble("Indicia") == null ? NODATA : jsonData.getDouble("Indicia"));
            dzOriginalData.setPictureUrl(String.valueOf(jsonData.get("picurl")));
            dzOriginalData.setCover(jsonData.getDouble("Cover"));
            dzOriginalData.setStripe(jsonData.getDouble("Stripe"));
            dzOriginalData.setId(jsonData.getInteger("id"));
            dzOriginalData.setCameraId(jsonData.getString(CAMERAIDID));
            dzOriginalData.setDetecitionStatus(jsonData.getInteger("detectionStatus"));
            dzOriginalData.setChName(jsonData.getString("ChnName"));
            dzOriginalData.setPtzSpeed(jsonData.getDouble("PTZSpeed"));
            dzOriginalData.setConnectedStaus(jsonData.getInteger("connectedStatus"));
            dzOriginalData.setFreeze(jsonData.getDouble("Freeze"));
            dzOriginalData.setDateTime(jsonData.getString(DATETIME));
            dzOriginalData.setDefinition(jsonData.getDouble("Definition"));
            dzOriginalData.setLatitude(jsonData.getString("Latitude"));
            dzOriginalData.setLongtitude(jsonData.getString("Longitude"));
            dzOriginalData.setLwsx(jsonData.getString("LWSX"));
            dzOriginalData.setDeviceStatusOnline(jsonData.getInteger("detectionStatus"));
            Object cameraType = jsonData.get("cameratype");
            String tempCameraType = cameraType == null ? "1" : cameraType.toString();
            dzOriginalData.setCameraType("".equals(tempCameraType) ? 1 : Integer.parseInt(tempCameraType));
            Object dlkDiagResultIndex = jsonData.get("DiagResultIndex");
            String diagResultIndex =
                dlkDiagResultIndex == null ? (UtilMethod.dateToString(new Date())) : dlkDiagResultIndex.toString();
            dzOriginalData.setDiagResultIndex(diagResultIndex); // 设置最新一次诊断结果表识
            lastDiagList.add(dzOriginalData);

        }
    }

    /**
     * 根据摄像机编码与检测时间查询指定时间段最新一条检测结果,如果无结果返回失败
     *
     * @param url 访问东智服务器
     * @param jsonObject 访问东智服务器需要传的参数
     * @return String
     */
    private JSONObject getFirstLastDiagResult(String url, JSONObject jsonObject) throws AxisFault {
        if (CommonUtil.isExistsNull(url, jsonObject)) {
            return null;
        }
        String returnStr = null;
        JSONObject json = null;
        jsonObject.put(PAGESIZE, KEY_PAGE_LIMIT);
        ServiceInfoServiceStub stub1 = new ServiceInfoServiceStub(url);
        GetLastDiagResultInfo gldr = new GetLastDiagResultInfo();
        gldr.setArg0(jsonObject.toString());
        GetLastDiagResultInfoE getLastDiagResultInfoE = new GetLastDiagResultInfoE();
        getLastDiagResultInfoE.setGetLastDiagResultInfo(gldr);
        GetLastDiagResultInfoResponseE getLastDiagResultInfoResponse = null;
        try {
            getLastDiagResultInfoResponse = stub1.getLastDiagResultInfo(getLastDiagResultInfoE);
            returnStr = getLastDiagResultInfoResponse.getGetLastDiagResultInfoResponse().get_return();
            json = JSONObject.parseObject(returnStr);
        } catch (RemoteException e) {
            LOG.error("collect LastDiagResult(): {}", e.getMessage());
        }
        return json;
    }

    /**
     * KPI信令结果采集
     *
     * @param url 访问东智服务器的连接
     * @return 信令结果KPI返回值
     */
    private List<DzOriginalData> collectSignallingData(String url) {
        JSONObject jsonObject = new JSONObject();
        List<DzOriginalData> resultList = new ArrayList<DzOriginalData>();
        jsonObject.put(PAGEINDEX, 1);
        JSONObject json = getFirstSignallingData(url, jsonObject); // 东智服务器KPI信令结果采集第一页数据
        if (CommonUtil.isNotNull(json) && json.getInteger(CODE) == 0) {
            getSignallingDataList(json, resultList);
            int counts = json.getIntValue(COUNTS);
            int pageSize = KEY_PAGE_LIMIT;
            int page = counts % pageSize == 0 ? counts / pageSize : counts / pageSize + 1;
            for (int ii = KEY_PAGE_TWO; ii <= page; ii++) {
                jsonObject.put(PAGEINDEX, ii);
                json = getFirstSignallingData(url, jsonObject);
                if (CommonUtil.isNotNull(json) && json.getInteger(CODE) == 0) {
                    getSignallingDataList(json, resultList); // 东智服务器KPI信令结果采集第一页以后的数据
                } else {
                    continue;
                }
            }
        } else {
            LOG.error("DZ get first signalling data error . json is null or json code not equal 0.");
        }
        return resultList;
    }

    /**
     * KPI单页数据采集封装
     *
     * @param json 带参
     * @param resultlist 新建东智数据集合接收返回的数值
     */
    private void getSignallingDataList(JSONObject json, List<DzOriginalData> resultlist) {
        if (CommonUtil.isExistsNull(json, resultlist)) {
            return;
        }
        JSONArray jsonArray = json.getJSONArray(DATA);
        if (jsonArray != null && jsonArray.size() > 0) {
            for (int ii = 0; ii < jsonArray.size(); ii++) {
                JSONObject jsonData = jsonArray.getJSONObject(ii);
                DzOriginalData dzOriginalData = new DzOriginalData();
                dzOriginalData.setCameraId(jsonData.getString(CAMERAIDID));
                dzOriginalData.setChName(jsonData.getString("ChnName"));
                dzOriginalData.setDateTime(jsonData.getString(DATETIME));
                dzOriginalData.setErrorCode(jsonData.getString("errorcode"));
                dzOriginalData.setIfRameDelay(jsonData.getString("iframedelay"));
                dzOriginalData.setOnline(jsonData.getString("online"));
                dzOriginalData.setRtpDelay(jsonData.getString("rtpdelay"));
                dzOriginalData.setSipDelay(jsonData.getString("sipdelay"));
                resultlist.add(dzOriginalData);
            }
        } else {
            LOG.info("getJSONArray()");
        }
    }

    /**
     * 根据指定条件获取摄像机信令检测结果值
     *
     * @param url 访问东智服务器
     * @param jsonObject 访问东智服务器需要传的参数
     * @return JSONObject
     */
    private JSONObject getFirstSignallingData(String url, JSONObject jsonObject) {
        if (CommonUtil.isExistsNull(url, jsonObject)) {
            return null;
        }
        String returnStr = null;
        JSONObject json = null;
        try {
            jsonObject.put(PAGESIZE, KEY_PAGE_LIMIT);
            ServiceInfoServiceStub stub1 = new ServiceInfoServiceStub(url);
            GetDevSignalling getDevSignalling = new GetDevSignalling();
            getDevSignalling.setArg0(jsonObject.toString());
            GetDevSignallingE getDevSignallingE = new GetDevSignallingE();
            getDevSignallingE.setGetDevSignalling(getDevSignalling);
            GetDevSignallingResponseE getDevSignallingResponse = stub1.getDevSignalling(getDevSignallingE);
            returnStr = getDevSignallingResponse.getGetDevSignallingResponse().get_return();
            json = JSONObject.parseObject(returnStr);
        } catch (IOException e) {
            LOG.error("collectSignallingData():{}", e.getMessage());
        }
        return json;
    }

    /**
     * 获取摄像机录像巡检结果
     *
     * @param url 访问东智服务器的连接
     * @return List 摄像机录像巡检结果KPI返回值
     */
    public List<DzOriginalData> collectDeviceVideotapeData(String url) {
        JSONObject jsonObject = new JSONObject();
        List<DzOriginalData> resultlists = new ArrayList<DzOriginalData>();
        jsonObject.put(PAGEINDEX, 1);
        JSONObject json = getFirstDeviceVideotapeData(url, jsonObject); // 东智服务器获取摄像机录像巡检结果第一页数据
        if (json != null && json.getInteger(CODE) == 0) {
            getDeviceVideotapeDataList(json, resultlists);
            int counts = json.getInteger(COUNTS);
            int pageSize = KEY_PAGE_LIMIT;
            int page = counts % pageSize == 0 ? counts / pageSize : counts / pageSize + 1;
            for (int ii = KEY_PAGE_TWO; ii <= page; ii++) {
                jsonObject.put(PAGEINDEX, ii);
                json = getFirstDeviceVideotapeData(url, jsonObject);
                if (json != null && json.getInteger(CODE) == 0) {
                    getDeviceVideotapeDataList(json, resultlists); // 东智服务器摄像机录像巡检结果第一页以后的数据
                } else {
                    continue;
                }
            }
        } else {
            if (json != null) {
                LOG.info("collect DeviceVideotapeData failed");
            }
        }
        return resultlists;
    }

    /**
     * 获取摄像机录像巡检结果
     *
     * @param json 带参
     * @param resultlist 新建东智数据集合接收返回的数值
     */
    private void getDeviceVideotapeDataList(JSONObject json, List<DzOriginalData> resultlist) {
        if (CommonUtil.isExistsNull(json, resultlist)) {
            return;
        }
        JSONArray jsonArray = json.getJSONArray(DATA);
        if (jsonArray != null && jsonArray.size() > 0) {
            for (int ii = 0; ii < jsonArray.size(); ii++) {
                JSONObject jsonData = jsonArray.getJSONObject(ii);
                DzOriginalData dzOriginalData = new DzOriginalData();
                dzOriginalData.setCameraId(jsonData.getString(CAMERAIDID));
                dzOriginalData.setRecordIntegrity(jsonData.getInteger("RecordIntegrity"));
                dzOriginalData.setVideoDateTime(jsonData.getString(DATETIME));
                dzOriginalData.setRecordRule(jsonData.getIntValue("RecordRule"));
                dzOriginalData.setRecordSaveTime(jsonData.getIntValue("RecordSaveTime"));
                JSONObject lostRecordJson = jsonData.getJSONObject("lostrecord");
                String lostrecord = lostRecordJson == null ? "" : lostRecordJson.getString("lostrecord");
                dzOriginalData.setLostRecord(lostrecord);
                dzOriginalData.setVideoDiagResultIndex(jsonData.getString("DiagResultIndex"));
                resultlist.add(dzOriginalData);
            }
        } else {
            LOG.info("getDeviceVideotapeDataList getJSONArray()");
        }
    }

    /**
     * 获取摄像机录像巡检结果
     *
     * @param url 访问东智服务器
     * @param jsonObject 访问东智服务器需要传的参数
     * @return JSONObject
     */
    private JSONObject getFirstDeviceVideotapeData(String url, JSONObject jsonObject) {
        if (CommonUtil.isExistsNull(url, jsonObject)) {
            return null;
        }
        String returnStr = null;
        JSONObject json = null;
        try {
            jsonObject.put(PAGESIZE, KEY_PAGE_LIMIT);
            ServiceInfoServiceStub stub1 = new ServiceInfoServiceStub(url);
            GetDeviceVideotape gdv = new GetDeviceVideotape();
            gdv.setArg0(jsonObject.toString());
            GetDeviceVideotapeE gdve = new GetDeviceVideotapeE();
            gdve.setGetDeviceVideotape(gdv);
            GetDeviceVideotapeResponseE gdvre = stub1.getDeviceVideotape(gdve);
            returnStr = gdvre.getGetDeviceVideotapeResponse().get_return();
            json = JSONObject.parseObject(returnStr);
        } catch (IOException e) {
            LOG.error("collectDeviceVideotapeData():{}", e.getMessage());
        }
        return json;
    }

    /**
     * 查询东智服务器配置
     *
     * @return String 返回东智服务器参数配置的ip+：+port
     */
    @Override
    public String getFirstDzServerParamConfig() {
        String dzViewPort = ProtocolConfig.getParameterConfig().getDzViewPort();
        String ipAndPort = null;
        try {
            ipAndPort = serverParamConfigDao.selectFirstDzServerParamConfig();
        } catch (DataAccessException e) {
            LOG.error("selectFirstDzServerParamConfig DataAccessException {} ", e.getMessage());
        }
        return ipAndPort == null || dzViewPort == null ? "" : (ipAndPort.concat(dzViewPort));
    }

    /**
     * 批量删除同时存在于tbl_discardip_collection、tbl_network_config表中的ip
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteDiscardIpInNetworkConfig() {
        List<String> discardIpList = tblDiscardIpCollectionDaoGetDiscardIpInNetworkconfig();
        if (CollectionUtil.isNotEmpty(discardIpList)) {
            Map<String, Object> kyeMap = new HashMap<String, Object>();
            kyeMap.put(SQL_IPLIST, discardIpList);
            int resultNum = tblDiscardIpCollectionDaoDeleteIpList(kyeMap);
            if (resultNum > 0) {
                LOG.info("delete discardIpList sucess");
            } else {
                LOG.info("delete discardIpList failed.");
            }
        } else {
            // 没有同时存在于tbl_discardip_collection、tbl_network_config表中的ip。
            LOG.info("There are no ips in the tbl_discardip_collection, tbl_network_config table.");
        }
    }

    private int tblDiscardIpCollectionDaoDeleteIpList(Map<String, Object> kyeMap) {
        try {
            return tblDiscardIpCollectionDao.deleteIpList(kyeMap);
        } catch (DataAccessException e) {
            LOG.error("deleteIpList DataAccessException {} ", e.getMessage());
        }
        return 0;
    }

    private List<String> tblDiscardIpCollectionDaoGetDiscardIpInNetworkconfig() {
        try {
            return tblDiscardIpCollectionDao.getDiscardIpInNetworkconfig();
        } catch (DataAccessException e) {
            LOG.error("getDiscardIpInNetworkconfig DataAccessException {} ", e.getMessage());
        }
        return null;
    }
}
