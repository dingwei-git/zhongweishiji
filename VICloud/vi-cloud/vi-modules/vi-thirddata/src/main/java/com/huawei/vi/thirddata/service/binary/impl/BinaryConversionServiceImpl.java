/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.FortifyUtiltools.FortifyUtils;
import com.huawei.utils.NumConstant;
import com.huawei.utils.*;
import com.huawei.vi.entity.vo.ProgressInfo;
import com.huawei.vi.thirddata.mapper.*;
import com.huawei.vi.thirddata.service.binary.BinaryConversionService;
import com.huawei.vi.thirddata.service.binary.pojo.*;
import com.huawei.vi.thirddata.service.ivsserver.ServiceCommonConst;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.util.CommonSymbolicConstant;
import com.jovision.jaws.common.util.UtilMethod;
import com.jovision.jaws.common.util.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 该类主要用于实现登录科来服务器获取二进制码数据并将二进制码数据进行解析入库 连接服务器；请求数据；返回二进制码；解析二进制码；获取周期存入周期管理表；解析后的数据入库；生成.scv文件；多个服务器请求处理的数据进行合并
 *
 * @since 2019-08-06
 */
@Service(value = "binaryConversionServ")
public class BinaryConversionServiceImpl implements BinaryConversionService {
    /**
     * 公共日志打印
     */
    private static final Logger LOG = LoggerFactory.getLogger(BinaryConversionServiceImpl.class);

    private static final int NUMBERSIX = 6;

    private static final int NUMBERFOUR = 4;

    private static final int NUMBERSIXTY = 16;

    private static final int NUMBERTWO = 2;

    private static final int ZERX4F = 0XFFFF;

    private static final int ZERX2F = 0XFF;

    /**
     * 采集结束线程等待5秒
     */
    private static final int SLEEPFIVESCEONDS = 5000;

    private static String charset = CommonConstant.CHARSET_UTF8;

    private static String start = "var varJsonData = {";

    private static String tcp = "TCP";

    private static String udp = "UDP";

    private static String tcpUdp = "TCP&UDP";

    private static String beginTime = "begintime";

    private static String endTime = "endtime";

    private static String tabName = "tabName";

    private static String title = "title";

    private static String sqlString = "sqlStr";

    private static String dataList = "dataList";

    private static String csv = ".csv";

    private static String conversation = "会话";

    private static String protocol = "700";

    private static String regularPort = "99999";

    private static String languType = "zh-CN";

    private static final String SESSION = "session";

    private static final String LOGIN_ERRCODE = "login_errcode";

    private static final String TABLENAME = "tableName";

    private static final String APOSTROPHE = "\"";

    private static final String SYMBOL = "\"@\"";

    private static final String LINKID = "linkId";

    private static final int SHIFTLEFTSIXTHREE = 63;

    private static final int SHIFTLEFTSIXTWO = 62;

    private static final int NAGETIVE_ONE = -1;

    private static final int NUM_0 = NumConstant.NUM_0;

    /**
     * 1分钟对应的毫秒值
     */
    private static final int ONEMINUTE = 60000;

    /**
     * 数据入库每次批量处理单元
     */
    private static final int BATCHUNIT = 1000;

    private ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(CommonConst.COREPOOLSIZE,
        CommonConst.MAXIMUMPOOLSIZE, CommonConst.KEEPALIVETIME, TimeUnit.SECONDS,
        new LinkedBlockingQueue<Runnable>(CommonConst.WORKQUEUESIZE), new ThreadPoolExecutor.DiscardPolicy());

    @Autowired
    private TblOriginalDataMapper originalDataDao;

    @Autowired
    private TblNetworkConfigMapper networkConfigDao;

    @Autowired
    private TblServerParamConfigMapper serverParamConfigDao;

    @Autowired
    private TblPeriodManageMapper periodManageDao;

    @Autowired
    private TblDiscardIpCollectionMapper tblDiscardIpCollectionDao;

    private HttpClientUtil httpClientUtil = null;

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

    @Value("${configbean.periodtime}")
    private long periodTime;

    /**
     * httpClientUtil
     */
    public BinaryConversionServiceImpl() {
        httpClientUtil = new HttpClientUtil();
    }

    /**
     * 执行登录，获取session，如果登录失败，打印返回信息
     *
     * @param url url
     * @param usertmpName 用户名
     * @param secretCode 密码
     * @return JSONObject
     */
    @Override
    public JSONObject login(String url, String usertmpName, String secretCode) {
        LOG.info("inter className method");
        if (CommonUtil.isExistsNull(url, usertmpName, secretCode)) {
            LOG.error("login param is exist null");
            return null;
        }
        StringBuilder loginUrl = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        StringBuilder encode = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        String returnJson = null;
        JSONObject obj = null;
        try {
            loginUrl.append(url)
                .append(ProtocolConfig.getParameterConfig().getCallBack())
                .append("&param=")
                .append(URLEncoder.encode(encode.append("{\"username\":\"")
                    .append(usertmpName)
                    .append("\",\"password\":\"")
                    .append(secretCode)
                    .append("\"}")
                    .toString(), charset));
            returnJson = httpClientUtil.doGet(loginUrl.toString());
        } catch (UnsupportedEncodingException e) {
            LOG.error("login(): keLai fail");
        }
        if (returnJson != null && returnJson.startsWith(start)) {
            obj = getResultJsonObject(returnJson);
        }
        LOG.info("out class method login");
        return obj;
    }

    /**
     * 执行登出，回溯只允许两个session，多次登录可能会失败，所以使用完毕后一定要执行登出操作
     *
     * @param url 登出URL片段
     * @param session 会话
     */
    @Override
    public void logout(String url, String session) {
        if (StringUtil.isNull(url)) {
            LOG.error("login url is null");
            return;
        }
        byte[] datas = null;
        String returnJson = null;
        if (session != null) {
            String lgOutApi = ProtocolConfig.getParameterConfig().getLogoutApi();
            String loginOutUrl = url.replace(lgOutApi, session + "/" + lgOutApi);
            try {
                datas = httpClientUtil.doPost4Stream(loginOutUrl, null, charset);
                if (CommonUtil.isNull(datas)) {
                    LOG.info("loginOut Exception");
                    return;
                }
                returnJson = new String(datas, charset);
                Thread.sleep(SLEEPFIVESCEONDS); // 延迟5秒开始下一次采集
                if (returnJson.indexOf("OK") != NAGETIVE_ONE) {
                    LOG.info("loginOut success");
                } else {
                    LOG.info("loginOut fail");
                }
            } catch (IOException e) {
                LOG.error("logoutIOException (): {}", e.getMessage());
            } catch (InterruptedException e) {
                LOG.error("logout InterruptedException(): {}", e.getMessage());
            }
        }
    }

    /**
     * 根据服务器配置的参数，依次进行数据采集，入库，生成csv文件开始
     *
     * @param tblServerParamConfig tblServerParamConfig
     * @param begin beginTime
     */
    @Override
    public void dataProcessingStart(List<TblServerParamConfig> tblServerParamConfig, Date begin) {
        LOG.info("inter dataProcessingStart");
        StringBuffer resultFlag = new StringBuffer(NumConstant.EXPECTED_BUFFER_DATA);
        Date edTime = getEndTime(begin); // 结束时间
        getVideoInsightConfig(); // 设置反向数据匹配规则
        getLanguage(); // 设置语言类型
        if (CollectionUtil.isEmpty(tblServerParamConfig)) {
            LOG.error("keLai server paramer config is empty");
            return;
        }
        JSONObject obj = null;
        boolean isReturnFlag = true;
        for (TblServerParamConfig tspc : tblServerParamConfig) {
            if (CommonConst.SQL_KL_FLAG.equals(tspc.getServiceFlag())) { // 过滤科来服务器参数
                isReturnFlag = false;
                String lgUrl =
                    ParamToUrl.getUrl(ProtocolConfig.getParameterConfig().getLoginApi(), tspc.getServiceIpAddress());
                obj = login(lgUrl, tspc.getServiceName(), Rsa.decryptString(tspc.getServicePassword()));
            } else {
                continue;
            }
            if (CommonUtil.isNull(obj)) {
                LOG.info("JSONObject is null; and out dataProcessingStart");
                LOG.error("JSONObject is null");
                continue;
            }
            updateServerConnectStatus(obj, tspc);

            // 处理数据并登出
            dataProcessAndLoOut(begin, resultFlag, edTime, obj, tspc);
        }
        if (isReturnFlag) {
            return;
        }
        while (mThreadPool.getActiveCount() >= 1) {
            try {
                Thread.sleep(CommonConst.KEEPSLEEPTIME);
            } catch (InterruptedException e) {
                LOG.error("Thread.sleep: {}", e.getMessage());
            }
        }
        if (resultFlag.indexOf("true") != NAGETIVE_ONE) {
            dataProcessingSuccess(begin);
        } else {
            LOG.info("collecter data failed...");
        }
        LOG.info("out dataProcessingStart");
    }

    /**
     * 数据采集处理及登出
     *
     * @param begin 开始时间
     * @param resultFlag 结果标志
     * @param edTime 结束时间
     * @param obj json
     * @param tspc 服务器参数配置
     */
    private void dataProcessAndLoOut(Date begin, StringBuffer resultFlag, Date edTime, JSONObject obj,
        TblServerParamConfig tspc) {
        String session = CommonSymbolicConstant.SYMBOL_BLANK;
        if (obj.containsKey(SESSION)) {
            session = obj.getString(SESSION);
            dataProcessing(tspc, begin, edTime, session, resultFlag); // 数据入库处理入口
            // 登出服务器
            String loUrl =
                ParamToUrl.getUrl(ProtocolConfig.getParameterConfig().getLogoutApi(), tspc.getServiceIpAddress());
            logout(loUrl, session);
        } else {
            LOG.info("session exist ?");
        }
    }

    /**
     * dataProcessing 操作成功的后置处理
     *
     * @param begin beginTime
     */
    private void dataProcessingSuccess(Date begin) {
        String udpTable = getTableByDataBaseType(begin, CommonConst.TABLE_ORIGINAL_DATA_UDP);
        String tcpTable = getTableByDataBaseType(begin, CommonConst.TABLE_ORIGINAL_DATA);
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put(TABLENAME, udpTable);
        udpTable = originalDataDaoShowTableByname(conditions);
        conditions.put(TABLENAME, tcpTable);
        tcpTable = originalDataDaoShowTableByname(conditions);

        if (CommonUtil.isNotNull(udpTable)) {
            createCsvByProtol(udp, begin);
            LOG.info("create udp csv end");
        }

        if (CommonUtil.isNotNull(tcpTable)) {
            createCsvByProtol(tcp, begin);
            LOG.info("create tcp csv end");
        }
    }

    /**
     * 开始采集数据
     *
     * @param tspc 服务器参数配置
     * @param begin 开始时间
     * @param edTime 结束时间
     * @param session 登录成功之后返回的sessionId
     * @param resultFlag 入库成功记录
     * @return String [返回类型说明]
     */
    private String dataProcessing(TblServerParamConfig tspc, Date begin, Date edTime, String session,
        StringBuffer resultFlag) {
        String linkId = tspc.getServiceLinkId();
        String serverIp = tspc.getServiceIpAddress();
        String serviceDataType = tspc.getServiceDataType();
        if (CommonUtil.isExistsNull(linkId, serverIp, serviceDataType)) {
            LOG.error("linkId serverIp serviceDataType exists null");
            return resultFlag.toString();
        }
        String[] typeArrs = serviceDataType.split("&");
        for (String type : typeArrs) {
            createTableByServiceDataType(type, begin); // 根据协议类型创建相应的表tbl_original_data tbl_original_data_udp
            JSONObject json = parameterConfig(type);
            json.put(beginTime, begin.getTime());
            json.put(endTime, edTime.getTime());
            String[] linkIdStrs = linkId.split(CommonSymbolicConstant.SYMBOL_SEPARATOR);
            for (String netlink : linkIdStrs) {
                List<JSONObject> jsonList = getBinaryData(Integer.parseInt(netlink), session, json, serverIp);
                if (CollectionUtil.isEmpty(jsonList)) {
                    continue;
                }
                startThread(jsonList, begin, type, resultFlag);
            }
        }
        return resultFlag.toString();
    }

    /**
     * 修改服务器登录状态
     *
     * @param obj 登录成功之后返回的json对象
     * @param tspc 服务器对象
     */
    @Override
    public void updateServerConnectStatus(JSONObject obj, TblServerParamConfig tspc) {
        if (CommonUtil.isExistsNull(obj, tspc)) {
            return;
        }
        if (obj.containsKey(LOGIN_ERRCODE) && obj.getIntValue(LOGIN_ERRCODE) == 0 && obj.containsKey(SESSION)) {
            if (languType.equals(ProgressInfo.getLanguageTyle())) {
                tspc.setMessage(ProtocolConfig.getGlobalMap().get("connectSucessCn"));
            } else {
                tspc.setMessage(ProtocolConfig.getGlobalMap().get("connectSucessEn"));
            }
        } else {
            if (languType.equals(ProgressInfo.getLanguageTyle())) {
                tspc.setMessage(ProtocolConfig.getGlobalMap().get("connectFailedCn"));
            } else {
                tspc.setMessage(ProtocolConfig.getGlobalMap().get("connectFailedEn"));
            }
        }
        tspc.setServiceStatus(obj.getString(LOGIN_ERRCODE));
        serverParamConfigDaoUpdateByPrimaryKeySelective(tspc); // 修改服务器状态
    }

    private void serverParamConfigDaoUpdateByPrimaryKeySelective(TblServerParamConfig tspc) {
        try {
            serverParamConfigDao.updateByPrimaryKeySelective(tspc);
        } catch (DataAccessException e) {
            LOG.error("updateByPrimaryKeySelective DataAccessException {} ", e.getMessage());
        }
    }

    /**
     * 单链路数据入库
     *
     * @param jsonList 经过转化的json数据
     * @param startTime 开始采集数据的周期时间
     * @param type 协议类型udp或者tcp
     * @return boolean
     */
    private boolean originalDataIntoDatabase(List<JSONObject> jsonList, Date startTime, String type) {
        boolean result = false;
        if (CollectionUtil.isNull(jsonList)) {
            return false;
        }
        Map<String, Object> conditions = new HashMap<String, Object>();
        if (jsonList.isEmpty()) {
            return true;
        }
        if (udp.equals(type)) {
            String table = UtilMethod.getTableName(CommonConst.TABLE_ORIGINAL_DATA_UDP, startTime);
            List<UdpJsonBean> udbList = udpDataFormat(jsonList);
            if (CollectionUtil.isEmpty(udbList)) {
                return true;
            }
            List<UdpJsonBean> tempList =
                udbList.stream().filter(udb -> protocol.equals(udb.getProtocol())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(tempList)) {
                LOG.info("insert tbl_discardip_collection udpList size {}", tempList.size());
                conditions.put(tabName, table);
                conditions.put(title, ProtocolConfig.getTitleConfig().getUdpTitle());
                conditions.put("dataList", tempList);
                result = originalDataDaoInsertOriginalUdpData(conditions);
            } else {
                result = true;
            }
        } else {
            String table = UtilMethod.getTableName(CommonConst.TABLE_ORIGINAL_DATA, startTime);
            List<TcpJsonBean> tcpList = tcpDataFormat(jsonList);
            if (CollectionUtil.isNotEmpty(tcpList)) {
                LOG.info("insert tbl_discardip_collection tcpList size {}", tcpList.size());
                conditions.put(tabName, table);
                conditions.put(title, ProtocolConfig.getTitleConfig().getTcpTitle());
                conditions.put(dataList, tcpList);
                return originalDataDaoInsertOriginalTcpData(conditions);
            } else {
                result = true;
            }
        }
        return result;
    }

    private boolean originalDataDaoInsertOriginalUdpData(Map<String, Object> condition) {
        try {
            return originalDataDao.insertOriginalUdpData(condition) > NUM_0;
        } catch (DataAccessException e) {
            LOG.error("insertOriginalUdpData DataAccessException {} ", e.getMessage());
        }
        return false;
    }

    private boolean originalDataDaoInsertOriginalTcpData(Map<String, Object> condition) {
        try {
            return originalDataDao.insertOriginalTcpData(condition) > NUM_0;
        } catch (DataAccessException e) {
            LOG.error("insertOriginalTcpData DataAccessException {} ", e.getMessage());
        }
        return false;
    }

    /**
     * 通过协议类型创建相应的csv文件
     *
     * @param type 协议类型
     * @param begin 周期开始时间
     */
    private void createCsvByProtol(String type, Date begin) {
        List<String> listStrs = new ArrayList<String>();
        String timeKey = null;
        if (tcp.equals(type)) {
            Map<String, Object> conditions = new HashMap<String, Object>();
            String table = UtilMethod.getTableName(CommonConst.TABLE_ORIGINAL_DATA, begin);
            conditions.put(tabName, table);
            List<TcpOriginalData> tcpList = originalDataDaoGetAllTcpData(conditions);
            if (CollectionUtil.isEmpty(tcpList)) {
                LOG.error("tcpList is null or empty");
                return;
            }
            if (languType.equals(ProgressInfo.getLanguageTyle())) {
                listStrs.add(ProtocolConfig.getTitleConfig().getTcpCsvCn()); // 添加中文标题
            } else {
                listStrs.add(ProtocolConfig.getTitleConfig().getTcpCsvUs()); // 添加英文标题
            }
            timeKey = getCsvTimeKey(UtilMethod.dateToString(begin));
            for (TcpOriginalData tcpOriginalData : tcpList) {
                listStrs.add(tcpCreateCsv(tcpOriginalData, timeKey));
            }
            createCsv(File.separator + getFileName(type, timeKey), listStrs);
        } else {
            Map<String, Object> conditions = new HashMap<String, Object>();
            String table = UtilMethod.getTableName(CommonConst.TABLE_ORIGINAL_DATA_UDP, begin);
            conditions.put(tabName, table);
            if (languType.equals(ProgressInfo.getLanguageTyle())) {
                listStrs.add(ProtocolConfig.getTitleConfig().getUdpCsvCn()); // 中文标题
            } else {
                listStrs.add(ProtocolConfig.getTitleConfig().getUdpCsvUs()); // 英文标题
            }
            List<UdpOriginalData> udpList = originalDataDaoGetAllUdpData(conditions);
            if (CollectionUtil.isNotEmpty(udpList)) {
                timeKey = getCsvTimeKey(UtilMethod.dateToString(begin));
                for (UdpOriginalData udpOriginalData : udpList) {
                    listStrs.add(udpCreateCsv(udpOriginalData, timeKey));
                }
                createCsv(File.separator + getFileName(type, timeKey), listStrs);
            }
        }
    }

    private List<TcpOriginalData> originalDataDaoGetAllTcpData(Map<String, Object> condition) {
        try {
            return originalDataDao.getAllTcpData(condition);
        } catch (DataAccessException e) {
            LOG.error("getAllTcpData DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    private List<UdpOriginalData> originalDataDaoGetAllUdpData(Map<String, Object> condition) {
        try {
            return originalDataDao.getAllUdpData(condition);
        } catch (DataAccessException e) {
            LOG.error("getAllUdpData DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    /**
     * 获取文件名称
     *
     * @param type type
     * @param timeKey timeKey
     * @return str 文件名
     */
    private String getFileName(String type, String timeKey) {
        StringBuilder sb = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        sb.append(type)
            .append(conversation)
            .append(CommonSymbolicConstant.SYMBOL_BLANK)
            .append(timeKey.replace(CommonSymbolicConstant.SYMBOL_COLON, CommonSymbolicConstant.SYMBOL_POINT)
                .replace(CommonSymbolicConstant.SYMBOL_AND, CommonSymbolicConstant.SYMBOL_MIDDLELINE))
            .append(csv);
        return sb.toString();
    }

    private String tcpCreateCsv(TcpOriginalData tcpOriginalData, String timeKey) {
        StringBuilder sb = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        sb.append(APOSTROPHE)
            .append(timeKey + SYMBOL)
            .append(tcpOriginalData.getClientIpAddr() + SYMBOL)
            .append(tcpOriginalData.getClientIpLocation() + SYMBOL)
            .append(tcpOriginalData.getClientPort() + SYMBOL)
            .append(tcpOriginalData.getServerIpAddr() + SYMBOL)
            .append(tcpOriginalData.getServerIpLocation() + SYMBOL)
            .append(tcpOriginalData.getServerPort() + SYMBOL)
            .append(tcpOriginalData.getApplicationId() + SYMBOL)
            .append(tcpOriginalData.getProtocol() + SYMBOL)
            .append(tcpOriginalData.getTcpStatus() + SYMBOL)
            .append(tcpOriginalData.getTotalPacket() + SYMBOL)
            .append(tcpOriginalData.getServerTotalPacket() + SYMBOL)
            .append(tcpOriginalData.getClientTotalPacket() + SYMBOL)
            .append(tcpOriginalData.getFlowStartTime() + SYMBOL)
            .append(tcpOriginalData.getFlowEndTime() + SYMBOL)
            .append(tcpOriginalData.getFlowDuration() + SYMBOL)
            .append(tcpOriginalData.getTotalBitps() + SYMBOL)
            .append(tcpOriginalData.getClientBitps() + SYMBOL)
            .append(tcpOriginalData.getServerBitps() + SYMBOL)
            .append(tcpOriginalData.getClientTcpRetransmissionRate() + SYMBOL)
            .append(tcpOriginalData.getServerTcpRetransmissionRate() + SYMBOL)
            .append(tcpOriginalData.getClientTcpSegmentLostPacket() + SYMBOL)
            .append(tcpOriginalData.getServerTcpSegmentLostPacket() + SYMBOL)
            .append(tcpOriginalData.getLinkId())
            .append(APOSTROPHE);
        return sb.toString();
    }

    private String udpCreateCsv(UdpOriginalData udpOriginalData, String timeKey) {
        StringBuilder sb = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        sb.append(APOSTROPHE)
            .append(timeKey + SYMBOL)
            .append(udpOriginalData.getCallerIp() + SYMBOL)
            .append(udpOriginalData.getCallerIpName() + SYMBOL)
            .append(udpOriginalData.getCallerPort() + SYMBOL)
            .append(udpOriginalData.getCalleeIp() + SYMBOL)
            .append(udpOriginalData.getCalleeIpName() + SYMBOL)
            .append(udpOriginalData.getCalleePort() + SYMBOL)
            .append(udpOriginalData.getCallId() + SYMBOL)
            .append(udpOriginalData.getFlowType() + SYMBOL)
            .append(udpOriginalData.getProtocol() + SYMBOL)
            .append(udpOriginalData.getSsrc() + SYMBOL)
            .append(udpOriginalData.getFlowStartTime() + SYMBOL)
            .append(udpOriginalData.getFlowEndTime() + SYMBOL)
            .append(udpOriginalData.getDurationTime() + SYMBOL)
            .append(udpOriginalData.getAvgLostPacketRate() + SYMBOL)
            .append(udpOriginalData.getTotalCodeBitps() + SYMBOL)
            .append(udpOriginalData.getVideoMosAvg() + SYMBOL)
            .append(udpOriginalData.getJitterAvg() + SYMBOL)
            .append(udpOriginalData.getLinkId())
            .append(APOSTROPHE);
        return sb.toString();
    }

    /**
     * sql语句创建表
     *
     * @param type UDP或者TCP
     * @param date 开始采集数据的日期
     * @return boolean
     */
    private boolean createTableByServiceDataType(String type, Date date) {
        String[] titleArrs = null;
        String sqlStr = null;
        Map<String, Object> conditions = new HashMap<String, Object>();
        String tcpTableName = UtilMethod.getTableName(CommonConst.TABLE_ORIGINAL_DATA, date);
        String udpTableName = UtilMethod.getTableName(CommonConst.TABLE_ORIGINAL_DATA_UDP, date);
        String tcpTitle = ProtocolConfig.getTitleConfig().getTcpTitle();
        String udpTitle = ProtocolConfig.getTitleConfig().getUdpTitle();
        if (udp.equals(type)) {
            titleArrs = udpTitle.split(CommonSymbolicConstant.SYMBOL_SEPARATOR);
            sqlStr = createTable(titleArrs);
            conditions.put("tabName", udpTableName);
            conditions.put("indexField", "callee_ip");
        } else {
            titleArrs = tcpTitle.split(CommonSymbolicConstant.SYMBOL_SEPARATOR);
            sqlStr = createTable(titleArrs);
            conditions.put(tabName, tcpTableName);
            conditions.put("indexField", "server_ip_addr");
        }
        conditions.put(sqlString, sqlStr);
        boolean result = originalDataDaoCreateOriginalData(conditions) != NAGETIVE_ONE; // 创建 tableName if not exists
        originalDataDaoCreateOrigIndex(conditions); // 创建索引
        return result;
    }

    private int originalDataDaoCreateOriginalData(Map<String, Object> condition) {
        try {
            return originalDataDao.createOriginalData(condition);
        } catch (DataAccessException e) {
            LOG.error("createOriginalData DataAccessException {} ", e.getMessage());
        }
        return NAGETIVE_ONE;
    }

    private void originalDataDaoCreateOrigIndex(Map<String, Object> condition) {
        try {
            originalDataDao.createOrigIndex(condition);
        } catch (DataAccessException e) {
            LOG.error("createOrigIndex DataAccessException {} ", e.getMessage());
        }
    }

    /**
     * 创建表时sql拼接
     *
     * @param titleArr 通过配置文件或者表头内容，然后分割拼接为创建表的sql语句
     * @return String
     */
    private String createTable(String[] titleArr) {
        // 后缀
        String sqlSuffix = " VARCHAR(255) NOT NULL COMMENT ''";

        // 分隔符
        String sqlDelimiter = sqlSuffix + ",";
        StringJoiner stringJoiner = new StringJoiner(sqlDelimiter, CommonSymbolicConstant.SYMBOL_BLANK, sqlSuffix);
        Arrays.stream(titleArr).forEach(str -> {
            stringJoiner.add(str);
        });
        return stringJoiner.toString();
    }

    /**
     * 获取二进制数据集合 将json参数拼接至url，通过post请求发送至服务器请求二进制数据
     *
     * @param session 登录成功后返回的session记录
     * @param serverIp 服务器IP
     * @param json 请求的数据参数
     * @param linkId 请求的数据id参数
     * @return byte[]
     */
    private List<JSONObject> getBinaryData(int linkId, String session, JSONObject json, String serverIp) {
        byte[] datas = null;
        List<JSONObject> lists = null;
        json.put("netlink", linkId);
        StringBuilder sb = new StringBuilder(ServiceCommonConst.HTTPS);
        sb.append(serverIp)
            .append(ProtocolConfig.getParameterConfig().getSomeUrl())
            .append(session)
            .append(ProtocolConfig.getParameterConfig().getDataApi())
            .append(ProtocolConfig.getParameterConfig().getCallBack())
            .append("&param=");
        try {
            sb.append(URLEncoder.encode(((JSON) json).toJSONString(), charset));
            LOG.info("=== start request time===：{}", UtilMethod.dateToString(new Date()));
            datas = httpClientUtil.doPost4Stream(sb.toString(), null, charset);
        } catch (UnsupportedEncodingException e) {
            LOG.error("getBinaryData(): {}", e.getMessage());
        } catch (IOException e) {
            LOG.error("getBinaryData(): {}", e.getMessage());
        }
        if (datas != null && datas.length > 0) {
            ByteBuffer buffer = ByteBuffer.wrap(datas);
            lists = parseStatisticsticTableData(buffer);
            LOG.info("=== request time end===：{}", UtilMethod.dateToString(new Date()));
            LOG.info(CommonUtil.isNull(lists) ? "list is null" : "list size is {}", lists.size());
        } else {
            LOG.info("response data is null or empty");
        }
        return lists;
    }

    /**
     * udpJson数据转换
     *
     * @param list list
     * @return List udp数据
     */
    private List<UdpJsonBean> udpDataFormat(List<JSONObject> list) {
        List<UdpJsonBean> udpList = new ArrayList<UdpJsonBean>();
        List<TblDiscardIpCollection> tdicList = new ArrayList<TblDiscardIpCollection>();
        LOG.info("----------->UDP Collector data Size ：{}", list.size());
        for (JSONObject jsonObj : list) {
            String callerIp = jsonObj.getString("caller_ip");
            String collerIpName = getTblNetworkConfig(callerIp);
            String calleeIp = jsonObj.getString("callee_ip");
            String colleeIpName = getTblNetworkConfig(calleeIp);
            if (StringUtil.isNotEmpty(colleeIpName) && StringUtil.isNotEmpty(collerIpName)) {
                udpList.add(jsonObject2UdpJsonBean(colleeIpName, collerIpName, calleeIp, callerIp, jsonObj));
            } else {
                tdicList.add(getTblDiscardIpCollection(calleeIp, colleeIpName, callerIp, collerIpName));
            }
        }
        if (CollectionUtil.isNotEmpty(tdicList)) {
            Map<String, Object> keyMap = new HashMap<String, Object>();
            keyMap.put("tdicList", tdicList);
            try {
                tblDiscardIpCollectionDao.insertDiscardIp(keyMap); // 入库 tbl_discardip_collection
            } catch (DataAccessException e) {
                LOG.error("insertDiscardIp DataAccessException {} ", e.getMessage());
            }
        }
        return udpList;
    }

    private UdpJsonBean jsonObject2UdpJsonBean(String colleeIpName, String collerIpName, String calleeIp,
        String callerIp, JSONObject jsonObj) {
        UdpJsonBean udpJson = new UdpJsonBean();
        if (Pattern.compile(ProgressInfo.getServIpLocation()).matcher(colleeIpName).find()
            && Pattern.compile(ProgressInfo.getClinIpLocation()).matcher(collerIpName).find()) { // 反向数据调整
            udpJson.setCallerIpName(colleeIpName);
            udpJson.setCalleeIpName(collerIpName);
            udpJson.setCallerIp(calleeIp);
            udpJson.setCalleeIp(callerIp);
            udpJson.setCallerPort(regularPort); // 规整为99999
        } else {
            udpJson.setCallerIpName(collerIpName);
            udpJson.setCalleeIpName(colleeIpName);
            udpJson.setCallerIp(callerIp);
            udpJson.setCalleeIp(calleeIp);
            udpJson.setCallerPort(jsonObj.getString("caller_port"));
        }
        udpJson.setCalleePort(jsonObj.getString("callee_port"));
        udpJson.setCallId(jsonObj.getString("call_id"));
        udpJson.setFlowType(jsonObj.getString("flow_type"));
        udpJson.setDurationTime(jsonObj.getString("duration_time"));
        udpJson.setAvgLostPacketRate(jsonObj.getString("avg_lost_packet_rate"));
        udpJson.setTotalCodeBitps(jsonObj.getString("total_code_bitps"));
        udpJson.setVideoMosAvg(jsonObj.getString("video_mos_avg"));
        udpJson.setFlowEndTime(jsonObj.getString("flow_end_time"));
        udpJson.setProtocol(jsonObj.getString("protocol"));
        udpJson.setSsrc(jsonObj.getString("ssrc"));
        udpJson.setJitterAvg(jsonObj.getString("jitter_avg"));
        udpJson.setLinkId(jsonObj.getString(LINKID));
        udpJson.setTimeKey(getTimeKey(jsonObj));
        udpJson.setFlowStartTime(jsonObj.getString("flow_start_time"));
        return udpJson;
    }

    private TblDiscardIpCollection getTblDiscardIpCollection(String calleeIp, String colleeIpName, String callerIp,
        String collerIpName) {
        TblDiscardIpCollection tbdc = new TblDiscardIpCollection();
        tbdc.setIp(calleeIp);
        tbdc.setIpName(colleeIpName);
        tbdc.setPip(callerIp);
        tbdc.setPipName(collerIpName);
        return tbdc;
    }

    /**
     * tcp数据转换
     *
     * @param list list
     * @return tcpJsonBeanList
     */
    private List<TcpJsonBean> tcpDataFormat(List<JSONObject> list) {
        List<TcpJsonBean> tcpList = new ArrayList<TcpJsonBean>();
        List<TblDiscardIpCollection> tdicList = new ArrayList<TblDiscardIpCollection>();
        Map<String, Object> keyMap = new HashMap<String, Object>();
        LOG.info("---------->TCP tcpDataFormat Size：{}  and {}", list.size(), Thread.currentThread().getName());
        for (JSONObject jsonObj : list) {
            String serverIpAddr = jsonObj.getString("server_ip_addr"); // 服务器ip
            String clientIpAddr = jsonObj.getString("client_ip_addr"); // 客户端ip
            String clientIpLocation = getTblNetworkConfig(clientIpAddr); // 客户端地理位置
            String serverIpLocation = getTblNetworkConfig(serverIpAddr); // 服务端地理位置
            if (clientIpLocation != null && !clientIpLocation.equals(CommonSymbolicConstant.SYMBOL_BLANK)
                && serverIpLocation != null && !serverIpLocation.equals(CommonSymbolicConstant.SYMBOL_BLANK)) {
                tcpList
                    .add(jsonObj2TcpJsonBean(serverIpLocation, clientIpLocation, clientIpAddr, serverIpAddr, jsonObj));
            } else {
                tdicList.add(getTblDiscardIpCollection(serverIpAddr, serverIpLocation, clientIpAddr, clientIpLocation));
            }
        }
        if (CollectionUtil.isNotEmpty(tdicList)) {
            keyMap.put("tdicList", tdicList);
            try {
                tblDiscardIpCollectionDao.insertDiscardIp(keyMap);
            } catch (DataAccessException e) {
                LOG.error("insertDiscardIp DataAccessException {} ", e.getMessage());
            }
        }
        return tcpList;
    }

    private TcpJsonBean jsonObj2TcpJsonBean(String serverIpLocation, String clientIpLocation, String clientIpAddr,
        String serverIpAddr, JSONObject jsonObj) {
        TcpJsonBean tjb = new TcpJsonBean();
        if (Pattern.compile(ProgressInfo.getServIpLocation()).matcher(clientIpLocation).find()
            && Pattern.compile(ProgressInfo.getClinIpLocation()).matcher(serverIpLocation).find()) { // 反向数据规整
            tjb.setClientIpLocation(serverIpLocation);
            tjb.setServerIpLocation(clientIpLocation);
            tjb.setServerIpAddr(clientIpAddr);
            tjb.setClientIpAddr(serverIpAddr);
            tjb.setServerPort(regularPort);
            tjb.setClientTcpRetransmissionRate(jsonObj.getString("server_tcp_retransmission_rate"));
            tjb.setServerTcpRetransmissionRate(jsonObj.getString("client_tcp_retransmission_rate"));
            tjb.setClientTcpSegmentLostPacket(jsonObj.getString("server_tcp_segment_lost_packet"));
            tjb.setServerTcpSegmentLostPacket(jsonObj.getString("client_tcp_segment_lost_packet"));
            tjb.setServerTotalPacket(jsonObj.getString("client_total_packet"));
            tjb.setClientTotalPacket(jsonObj.getString("server_total_packet"));
            tjb.setClientBitps(jsonObj.getString("server_bitps"));
            tjb.setServerBitps(jsonObj.getString("client_bitps"));
        } else {
            tjb.setClientIpLocation(clientIpLocation);
            tjb.setServerIpLocation(serverIpLocation);
            tjb.setServerIpAddr(serverIpAddr);
            tjb.setClientIpAddr(clientIpAddr);
            tjb.setServerPort(jsonObj.getString("server_port"));
            tjb.setClientTcpRetransmissionRate(jsonObj.getString("client_tcp_retransmission_rate"));
            tjb.setServerTcpRetransmissionRate(jsonObj.getString("server_tcp_retransmission_rate"));
            tjb.setClientTcpSegmentLostPacket(jsonObj.getString("client_tcp_segment_lost_packet"));
            tjb.setServerTcpSegmentLostPacket(jsonObj.getString("server_tcp_segment_lost_packet"));
            tjb.setServerTotalPacket(jsonObj.getString("server_total_packet")); // 目标IP地址数据包数
            tjb.setClientTotalPacket(jsonObj.getString("client_total_packet")); // 源IP地址数据报数
            tjb.setClientBitps(jsonObj.getString("client_bitps"));
            tjb.setServerBitps(jsonObj.getString("server_bitps"));
        }
        tjb.setClientPort(jsonObj.getString("client_port"));
        tjb.setProtocol(jsonObj.getString("protocol"));
        tjb.setApplicationId(jsonObj.getString("application_id"));
        tjb.setTcpStatus(jsonObj.getString("tcp_status"));
        tjb.setTotalPacket(jsonObj.getString("total_packet"));
        tjb.setFlowEndTime(jsonObj.getString("flow_end_time"));
        tjb.setFlowDuration(jsonObj.getString("flow_duration"));
        tjb.setTotalBitps(jsonObj.getString("total_bitps"));
        tjb.setLinkId(jsonObj.getString(LINKID));
        tjb.setTimeKey(getTimeKey(jsonObj));
        tjb.setFlowStartTime(jsonObj.getString("flow_start_time"));
        return tjb;
    }

    /**
     * 获取2018-06-14 16.00.00&&2018-06-14 16.10.00
     *
     * @param json （ 服务器获取的json对象）
     * @return String 时间key
     */
    private String getTimeKey(JSONObject json) {
        Date fstartTime = (Date) json.get("date");
        String stTime = UtilMethod.dateToString(fstartTime); // 开始时间
        Date edTime = getEndTime(fstartTime);
        String flowEndTime = UtilMethod.dateToString(edTime); // 结束时间
        String timeKey = stTime + "&&" + flowEndTime;
        return timeKey;
    }

    /**
     * 生成csv第一列时间戳
     *
     * @param date 日期时间
     * @return String 返回String类型的时间key类似于"2018-06-14 16.00.00&&2018-06-14 16.10.00"
     */
    private String getCsvTimeKey(String date) {
        Date edDate = UtilMethod
            .stringPeriodToDate(date.replace(CommonSymbolicConstant.SYMBOL_COLON, CommonSymbolicConstant.SYMBOL_POINT));
        String timeKey = CommonSymbolicConstant.SYMBOL_BLANK;
        if (edDate != null) {
            Date edTime = getEndTime(edDate);
            String flowEndTime = UtilMethod.dateToString(edTime); // 结束时间
            timeKey = date + CommonSymbolicConstant.SYMBOL_AND + flowEndTime;
        }
        return timeKey;
    }

    /**
     * 获取结束时间 date类型的开始时间入参，在开始时间的基础上增加10分钟（配置文件读取）作为周期结束时间返回
     *
     * @param begin 开始时间
     * @return Date
     */
    private Date getEndTime(Date begin) {
        Calendar cal = Calendar.getInstance();
        int addedTime = (int) periodTime / ONEMINUTE; // 定时器时长、也为数据采集的间隔时长
        cal.setTime(begin);
        cal.add(Calendar.MINUTE, addedTime); // 通过日历对象对开始时间进行处理，增加十分钟
        return cal.getTime();
    }

    /**
     * 根据ip获取地址
     *
     * @param ip ip
     * @return String
     */
    private String getTblNetworkConfig(String ip) {
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("networkRules", ip);
        try {
            return networkConfigDao.getDataByIp(conditions);
        } catch (DataAccessException e) {
            LOG.error("getDataByIp DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    /**
     * 查询url参数配置 默认送一分钟时间，查询分钟桶，应用过滤条件：application_id>=300000&&application_id<=500000 (upm配置的应用ID范围�?3万到5�?)
     *
     * @param serviceDataType 协议类型
     * @return JSONObject json对象 url 查询地址 table 查询的表 keys group字段 fields 需要查询的字段 filter 过滤条件
     * application_id>=300000&&application_id<=500000 sortField 排序字段（必须在查询字段里有才行） sortType 排序方式
     * ：0：不排序；1：升序；2：降序； limit 限制返回条数 beginTime 开始时间 endTime 结束时间 timeUnit 时间桶： 0：自动计时 1：毫秒 1000：1秒 10000:10秒
     * 60000:1分钟 600000:10分钟 3600000:1小时 86400000:1天
     */
    private JSONObject parameterConfig(String serviceDataType) {
        JSONObject json = new JSONObject();
        if (udp.equals(serviceDataType)) {
            json.put("table", ProtocolConfig.getParameterConfig().getUdpTable());
            json.put("keys",
                ProtocolConfig.getParameterConfig().getUdpKeys().split(CommonSymbolicConstant.SYMBOL_SEPARATOR));
            json.put("fields",
                ProtocolConfig.getParameterConfig().getUdpFields().split(CommonSymbolicConstant.SYMBOL_SEPARATOR));
            json.put("sortfield", ProtocolConfig.getParameterConfig().getUdpSortField());
        } else if (tcp.equals(serviceDataType)) {
            json.put("table", ProtocolConfig.getParameterConfig().getTcpTable());
            json.put("keys",
                ProtocolConfig.getParameterConfig().getTcpKeys().split(CommonSymbolicConstant.SYMBOL_SEPARATOR));
            json.put("fields",
                ProtocolConfig.getParameterConfig().getTcpFields().split(CommonSymbolicConstant.SYMBOL_SEPARATOR));
            json.put("sortfield", ProtocolConfig.getParameterConfig().getTcpSortField());
        }
        Integer sortType = Integer.parseInt(ProtocolConfig.getParameterConfig().getSortType());
        String filter = ProtocolConfig.getParameterConfig().getFilter(); // 根据实际情况过滤
        json.put("sorttype", sortType);
        json.put("filter", filter);
        Integer limit = Integer.parseInt(ProtocolConfig.getParameterConfig().getTopCount());
        json.put("topcount", limit);
        json.put("keycount", null);
        json.put("fieldcount", null);
        Integer timeUnit = Integer.parseInt(ProtocolConfig.getParameterConfig().getTimeUnit());
        json.put("timeunit", timeUnit);
        return json;
    }

    /**
     * json串转换为JSONObject对象
     *
     * @param returnJson json
     * @return jsonObject jsonObject
     */
    private JSONObject getResultJsonObject(String returnJson) {
        String jsonStr = returnJson.substring(returnJson.indexOf("{"), returnJson.lastIndexOf("}") + 1);
        return JSON.parseObject(jsonStr);
    }

    /**
     * 获取字符串
     *
     * @param buffer 字节流
     * @return String String
     */
    private static String getString(ByteBuffer buffer) {
        try {
            int strLength = buffer.getInt();
            if (strLength > 0) {
                byte[] strs = new byte[strLength];
                buffer.get(strs, 0, strLength);
                return new String(strs, 0, strLength - 1, charset);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("getString() : {}", e.getMessage());
        }
        return CommonSymbolicConstant.SYMBOL_BLANK;
    }

    /**
     * 指定长度字节流转String
     *
     * @param buffer buffer
     * @param length 长度
     * @return str string
     */
    private static String getString(ByteBuffer buffer, int length) {
        try {
            if (length > 0) {
                byte[] strs = new byte[length];
                buffer.get(strs, 0, length);
                return new String(strs, 0, length, charset);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("getString(): {}", e.getMessage());
        }
        return CommonSymbolicConstant.SYMBOL_BLANK;
    }

    /**
     * 解析统计表数据源
     *
     * @param buffer buffer
     * @return List 二进制集合
     */
    private static List<JSONObject> parseStatisticsticTableData(ByteBuffer buffer) {
        // 如果buffer的position=limit，则无数据
        if (buffer == null || !buffer.hasRemaining()) {
            return null;
        }
        List<JSONObject> lists = new ArrayList<JSONObject>();

        // errcode 2 错误标志
        // errmsg 错误信息
        int errorcode = buffer.getShort() & ZERX4F;
        getString(buffer);
        if (0 != errorcode) {
            LOG.info("errorcode {} ", errorcode);
            return lists;
        }

        // 获取字段个数
        int keycount = NAGETIVE_ONE;
        try {
            keycount = buffer.getShort() & ZERX4F;
        } catch (BufferUnderflowException e) {
            LOG.error("BufferUnderflowException : {}", e.getMessage());
            return lists;
        }

        // 取字段信, key:字段名称 value:字段类型
        List<FieldInfo> fields = new ArrayList<FieldInfo>();
        for (int ii = 0; ii < keycount; ii++) {
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setFieldName(getString(buffer));
            fieldInfo.setFieldType(buffer.get());
            fields.add(fieldInfo);
        }

        // 获取网络链路ID
        int linkNum = buffer.get() & ZERX2F;
        for (int kk = 0; kk < linkNum; kk++) {
            // 获取链路ID
            int link = buffer.getShort() & ZERX4F;

            // 查询这段时间的记录，按照单位时间划分为多少个单位时间，就有多少个时间记录集
            int timeRecodes = buffer.getInt();
            for (int nn = 0; nn < timeRecodes; nn++) {
                // 获取时间1526558400000
                Date date = new Date(buffer.getLong());

                // 记录数
                int linkRecords = buffer.getInt();
                for (int ii = 0; ii < linkRecords; ii++) {
                    JSONObject json = new JSONObject();
                    json.put("date", date);
                    json.put(LINKID, link);
                    jsonOper(fields, json, buffer);
                    lists.add(json);
                }
            }
        }
        return lists;
    }

    private static void jsonOper(List<FieldInfo> fields, JSONObject json, ByteBuffer buffer) {
        for (FieldInfo fieldInfo : fields) {
            json.put(fieldInfo.getFieldName(), getValue(buffer, fieldInfo.getFieldType()));
        }
    }

    /**
     * 根据字段类型取buffer流数�?
     *
     * @param buffer 缓冲流
     * @param type 类型
     * @return obj buffer流数
     */
    private static Object getValue(ByteBuffer buffer, byte type) {
        int ii;
        long llL;
        double ddD;
        if (EnumFieldDataType.class != null && EnumFieldDataType.get(type) != null) {
            switch (EnumFieldDataType.get(type)) {
                case CSRAS_FT_UINT8: // 1字节的无符号整数
                    ii = buffer.get() & ZERX2F;
                    return getUnsignedData(ii);
                case CSRAS_FT_UINT16: // 2字节的无符号整数
                    ii = buffer.getShort() & ZERX4F;
                    return getUnsignedData(ii);
                case CSRAS_FT_UINT32: // 4字节的无符号整数
                    ii = buffer.getInt();
                    return getUnsignedData(ii);
                case CSRAS_FT_UINT64: // 8字节的无符号整数
                    llL = buffer.getLong();
                    return getUnsignedData(llL);
                case CSRAS_FT_DOUBLE: // 8字节的浮点数
                    ddD = buffer.getDouble();
                    return ddD;
                case CSRAS_FT_DATETIME: // 日期和时间类型，显示为：2000-01-01 20:00:00
                    return new Date(buffer.getLong());
                case CSRAS_FT_TEXT: // 文本，UTF8字符串，值表示为：长度（2B�?+值（长度�? 长度包括’\0’结识符
                    return getString(buffer);
                case CSRAS_FT_PERCENT: // 百分值，长度1字节，无小数，�?�范围为�?0 - 100
                    return buffer.get();
                case CSRAS_FT_MAC: // MAC地址6个字节，以网络字节序方式提供 字符串格式：00:00:00:00:00:00
                    return getString(buffer, NUMBERSIX);
                case CSRAS_FT_IPADDR: // 1字节的IP版本�?4字节的IPV4地址或�??16字节的IPV6地址组成，即IPV4: 4 + value[4]，IPV6: 6 + value[16]
                    return getIp(buffer, buffer.get());
                case CSRAS_FT_TRANSCONTENT: // 交易内容�? 值表示为：长度（4B�?+值（长度�?
                    return getString(buffer);
                default:
                    break;
            }
        }
        return null;
    }

    /**
     * 有符号数转化为无符号数
     *
     * @param longData 待转化的数据
     * @return String 无符号数
     */
    private static String getUnsignedData(long longData) {
        BigInteger bigInteger = null;
        String data = CommonSymbolicConstant.SYMBOL_BLANK;
        if (longData < 0) {
            bigInteger = new BigInteger("2");
            bigInteger = bigInteger.shiftLeft(SHIFTLEFTSIXTHREE).subtract(bigInteger.shiftLeft(SHIFTLEFTSIXTWO));
            bigInteger = bigInteger.add(new BigInteger(String.valueOf(longData & Long.MAX_VALUE)));
            data = bigInteger.toString();
        } else {
            data = Long.toString(longData);
        }
        return data;
    }

    /**
     * 字节流转IP
     *
     * @param buffer buffer
     * @param ipVersion ipVersion
     * @return String ip
     */
    private static String getIp(ByteBuffer buffer, int ipVersion) {
        if (ipVersion == NUMBERFOUR) {
            return getIpv4(buffer);
        } else if (ipVersion == NUMBERSIX) {
            return getIpv6(buffer);
        } else {
            return null;
        }
    }

    /**
     * 获取IPv4地址
     *
     * @param buffer buffer
     * @return String IPv4地址
     */
    private static String getIpv4(ByteBuffer buffer) {
        StringBuilder stringBuilder = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        byte[] buffs = new byte[NUMBERFOUR];
        buffer.get(buffs, 0, NUMBERFOUR);
        for (byte bit : buffs) {
            stringBuilder.insert(0, bit & ZERX2F);
            stringBuilder.insert(0, ".");
        }
        return stringBuilder.deleteCharAt(0).toString();
    }

    /**
     * 获取IPv6地址
     *
     * @param buffer buffer
     * @return String IPv6地址
     */
    private static String getIpv6(ByteBuffer buffer) {
        StringBuilder stringBuilder = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        byte[] buffs = new byte[NUMBERSIXTY];
        buffer.get(buffs, 0, NUMBERSIXTY);
        int cite = 1;
        for (byte bit : buffs) {
            stringBuilder.append(String.format(Locale.ENGLISH, "%02x", bit));
            if (0 == cite % NUMBERTWO && 0 != cite) {
                stringBuilder.append(CommonSymbolicConstant.SYMBOL_COLON);
            }
            cite++;
        }
        return stringBuilder.toString();
    }

    /**
     * 生成CSV文件的方法
     *
     * @param fileName fileName
     * @param listStr 生成CSV文件的字符串集合
     * @return true/false 是否成功
     */
    private boolean createCsv(String fileName, List<String> listStr) {
        boolean isFlag;
        OutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bos = null;
        try {
            File file = FileUtils.getFile(CommonConst.DIR_ORIGINALDATACSV);
            if (!file.exists()) {
                // CI清理-增加返回值
                boolean isSuccess = file.mkdirs();
                if (!isSuccess) {
                    LOG.error("mkdirs file fail . file is /tmp/csvFile");
                    return false;
                }
            }
            File fileOutput = FileUtils.getFile(file.getCanonicalPath() + fileName);
            out = FortifyUtils.newFileOutputStream(fileOutput);
            outputStreamWriter = new OutputStreamWriter(out, CommonConstant.CHARSET_GBK);
            bos = new BufferedWriter(outputStreamWriter);
            for (String string : listStr) {
                String[] strs = string.split(CommonSymbolicConstant.SYMBOL_SHIFT_2);
                for (int ii = 0; ii < strs.length; ii++) {
                    bos.write(strs[ii]);
                    if (ii != strs.length - 1) {
                        bos.write(CommonSymbolicConstant.SYMBOL_SEPARATOR);
                    } else {
                        bos.write(System.lineSeparator());
                    }
                }
            }
            bos.flush();
            isFlag = true;
        } catch (IOException e) {
            LOG.error("createCsv Exception :{}", e.getMessage());
            isFlag = false;
        } finally {
            UtilMethod.closeFileStreamNotThrow(bos);
            UtilMethod.closeFileStreamNotThrow(outputStreamWriter);
            UtilMethod.closeFileStreamNotThrow(out);
        }
        return isFlag;
    }

    /**
     * 原始数据入库线程启动
     *
     * @param jsonList 原始数据
     * @param begin 开始时间
     * @param type 协议类型
     * @param result 入库成功的返回值
     */
    private void startThread(List<JSONObject> jsonList, Date begin, String type, StringBuffer result) {
        List<List<JSONObject>> jsonLists = CollectionUtil.list2Lists(jsonList, BATCHUNIT);
        while (!jsonLists.isEmpty()) {
            mThreadPool.execute(new JsonRunnable(jsonLists.remove(0), begin, type, result));
        }
    }

    /**
     * 调用业务参数配置页面获取数据筛选字段
     */
    private void getVideoInsightConfig() {
        LOG.info("inter getVideoInsightConfig");
        Map<String, String> parmMap = new HashMap<String, String>();
        String url = ProtocolConfig.getParameterConfig()
            .getVideoinsightUrl()
            .replaceFirst(ServiceCommonConst.HOST, videoInsightHost)
            .replaceFirst(ServiceCommonConst.PORT, videoInsightPort);
        String cfgUrl = url + "getTextOptions.do?param=VideoInsightConfig"; // 获取数据分析筛选字段：分局/所；区/组...默认为分局和派出所
        Object object = httpClientUtil.doVedioPost(cfgUrl, parmMap, charset, true);
        if (CommonUtil.isNotNull(object) && !object.toString().equals("isLoginFlag")) {
            JSONObject jsonObject = JSONObject.parseObject(object.toString());
            if (jsonObject == null) {
                LOG.error("jsonObject is null . maybe this url was filter");
                return;
            }
            JSONObject jsonText = jsonObject.getJSONObject("data");
            if (jsonText == null) {
                LOG.error("jsonText is null . maybe this url was filter");
                return;
            }
            String platform = jsonText.getString("platform");
            String organization = jsonText.getString("organization");
            ProgressInfo.setServIpLocation(
                CommonConst.SERVER_IP_LOCATION_REG.replace(CommonSymbolicConstant.SYMBOL_SHIFT_2, platform));
            ProgressInfo.setClinIpLocation(
                CommonConst.CLIENT_IP_LOCATION_REG.replace(CommonSymbolicConstant.SYMBOL_SHIFT_2, organization));
        } else {
            ProgressInfo.setServIpLocation(
                CommonConst.SERVER_IP_LOCATION_REG.replace(CommonSymbolicConstant.SYMBOL_SHIFT_2, "组"));
            ProgressInfo.setClinIpLocation(
                CommonConst.CLIENT_IP_LOCATION_REG.replace(CommonSymbolicConstant.SYMBOL_SHIFT_2, " 区"));
            LOG.info("Data filter field failed to get ");
        }
        LOG.info("out getVideoInsightConfig");
    }

    /**
     * 从videoinsight获取语言
     */
    @Override
    public void getLanguage() {
        // 当单例中的language type有值时，则不需要在往下执行发送获取语言的请求操作，直接返回当前语言
        if (StringUtil.isNotEmpty(ProgressInfo.getLanguageTyle())) {
            return;
        }
        Map<String, String> parmMap = new HashMap<String, String>();
        String url = ProtocolConfig.getParameterConfig()
            .getVideoinsightUrl()
            .replaceFirst(ServiceCommonConst.HOST, videoInsightHost)
            .replaceFirst(ServiceCommonConst.PORT, videoInsightPort);
        String languageUrl = url + "getParameterConfig.do?param=VideoInsightLanguageConfig"; // 获取语言类型
        Object languObj = httpClientUtil.doVedioPost(languageUrl, parmMap, charset, true);
        if (languObj != null) {
            JSONObject jsonObject = JSONObject.parseObject(languObj.toString());
            JSONObject jsonData = jsonObject.getJSONObject("data");
            if (jsonData == null) {
                LOG.error("jsonData is null . maybe this url was filter");
                return;
            }
            ProgressInfo.setLanguageTyle(jsonData.getString(ServiceCommonConst.LANGUAGE_TYPE)); // 设置语言
            ProgressInfo.setHaveIvs(jsonData.getBooleanValue(ServiceCommonConst.HAVE_IVS)); // 设置IVS是否配置
            // 设置图像诊断服务器参数是否配置
            ProgressInfo.setHaveImageDiagnosisService(
                jsonData.getBooleanValue(ServiceCommonConst.HAVE_IMAGE_DIAGNOSIS_SERVICE));
        } else {
            ProgressInfo.setLanguageTyle(languType); // 默认选择中文
        }
    }

    /**
     * 数据采集完毕之后周期表入库 当DLK数据或者探针数据任何一个采集入库完成即在周期管理表中插入当前周期数据
     *
     * @param begin 数据采集时间
     */
    @Override
    public void insertPeriodStartTime(Date begin) {
        Map<String, Object> conditions = new HashMap<String, Object>();
        String configTable = ProtocolConfig.getTitleConfig().getOriginalTableName();
        String[] tables = configTable.split(CommonSymbolicConstant.SYMBOL_SEPARATOR);
        boolean isFlag = true;
        for (String tableName : tables) {
            conditions.put(TABLENAME, getTableByDataBaseType(begin, tableName));
            String searchName = originalDataDaoShowTableByname(conditions);
            if (CommonUtil.isNotNull(searchName)) {
                TblPeriodManage tbpm = new TblPeriodManage();
                tbpm.setIsAnalyseOver("0");
                tbpm.setPeriodStartTime(begin);
                isFlag = periodManageDaoInsert(tbpm) > 0; // 周期管理表中入库
                break;
            } else {
                isFlag = false;
            }
        }
        if (isFlag) {
            LOG.info("insert tbl_period_manage success！");
        } else {
            LOG.info("insert tbl_period_manage failed！");
        }
    }

    private String originalDataDaoShowTableByname(Map<String, Object> condition) {
        try {
            return originalDataDao.showTableByname(condition);
        } catch (DataAccessException e) {
            LOG.error("showTableByname DataAccessException {} ", e.getMessage());
            return null;
        }
    }

    private int periodManageDaoInsert(TblPeriodManage tbpm) {
        try {
            return periodManageDao.insert(tbpm);
        } catch (DataAccessException e) {
            LOG.error("insert DataAccessException {} ", e.getMessage());
        }
        return NAGETIVE_ONE;
    }

    /**
     *
     */
    @Override
    public void uploadNetWorkConfig() {
        LOG.info("inter uploadNetWorkConfig");
        Map<String, String> parmMap = new HashMap<String, String>();
        String url = ProtocolConfig.getParameterConfig().getVideoinsightUrl()
            .replaceFirst(ServiceCommonConst.HOST, videoInsightHost)
            .replaceFirst(ServiceCommonConst.PORT, videoInsightPort);
        String cfgUrl = url + "networkconfig/loadcsv.do?param=VideoInsightCSVConfig";
        httpClientUtil.doVedioPost(cfgUrl, parmMap, charset, true);
        LOG.info("out uploadNetWorkConfig");
    }

    /**
     * 根据数据库类型返回响应数据库
     *
     * @param begin 数据采集开始时间
     * @param tableName 表名字
     * @return str 根据数据库类型返回相应的
     */
    private String getTableByDataBaseType(Date begin, String tableName) {
        String dataBaseType = GlobalConfigProperty.getGlobalMap().get("dataBaseType");
        String table = null;
        if ("mysql".equals(dataBaseType)) {
            table =  com.jovision.jaws.common.util.UtilMethod.getTableName(tableName, begin);
        } else {
            table = UtilMethod.getTableName(tableName.toUpperCase(Locale.ENGLISH), begin);
        }
        return table;
    }

    /**
     * 探针数据分批入库（优化 多线程对探针数据采集大量数据进行入库优化
     *
     * @since 2018-09-05
     */
    public class JsonRunnable implements Runnable {
        private List<JSONObject> jsons;

        private Date beginTime;

        private String type;

        private StringBuffer result;

        /**
         * 构造函数
         *
         * @param jsons 返回值
         * @param beginTime 周期开始时间
         * @param type 协议类型
         * @param result 入库结果
         */
        private JsonRunnable(List<JSONObject> jsons, Date beginTime, String type, StringBuffer result) {
            this.jsons = jsons;
            if (CommonUtil.isNotNull(beginTime)) {
                this.beginTime = (Date) beginTime.clone();
            } else {
                this.beginTime = null;
            }
            this.type = type;
            this.result = result;
        }

        /**
         * run
         */
        @Override
        public void run() {
            boolean flag = originalDataIntoDatabase(jsons, beginTime, type);
            result.append(flag);
            try {
                Thread.sleep(CommonConst.KEEPSLEEPTIME);
            } catch (InterruptedException e) {
                LOG.error("{}:{}", Thread.currentThread().getName(), e.getMessage());
            }
        }
    }
}
