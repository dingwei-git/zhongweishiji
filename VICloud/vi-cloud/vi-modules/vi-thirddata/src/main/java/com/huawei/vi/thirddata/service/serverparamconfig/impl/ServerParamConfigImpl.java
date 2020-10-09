/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.serverparamconfig.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.FortifyUtiltools.FortifyUtils;

import com.huawei.utils.*;
import com.huawei.vi.entity.po.ActiveMqVo;
import com.huawei.vi.entity.vo.*;
import com.huawei.vi.thirddata.mapper.TblServerParamConfigMapper;
import com.huawei.vi.thirddata.service.ServiceCommonConst;
import com.huawei.vi.thirddata.service.baseserv.impl.BaseServImpl;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;
import com.huawei.vi.thirddata.service.serverparamconfig.IserverParamConfigService;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.properties.ServerParamConfig;
import com.jovision.jaws.common.util.*;
import com.jovision.jaws.common.util.CommonSymbolicConstant;
import com.jovision.jaws.common.util.NumConstant;
import com.jovision.jaws.common.util.UtilMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 服务器参数配置Service实现类
 *
 * @version 1.0, 2018年7月2日
 * @since 2018-08-07
 */
@Service(value = "serverParamConfigServ")
public class ServerParamConfigImpl extends BaseServImpl<TblServerParamConfig, Integer>
    implements IserverParamConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(ServerParamConfigImpl.class);

    /**
     * 状态
     */
    private static final String STATUS = "status";

    /**
     * 消息服务器
     */
    private static final String ACTIVEMQ = "activemq";

    /**
     * 文件服务器
     */
    private static final String PICSERVER = "picserver";

    /**
     * 诊断服务器
     */
    private static final String DIAGNOSIS = "diagnosis";

    /**
     * 采集服务器
     */
    private static final String COLLECT = "collect";

    /**
     * 中间件服务器
     */
    private static final String MIDDLE = "middle";

    /**
     * 视频监控平台
     */
    private static final String SERVER = "server";

    private TblServerParamConfigMapper serverParamConfigDao;

    /**
     * 利用自动注入的方式，导入图像诊断服务器参数的配置
     */
    @Autowired
    private ServerParamConfig serverParamConfigs;

    private final HttpClientUtil httpClientUtil = new HttpClientUtil();

    /**
     * 设置数据源
     *
     * @param serverParamConfigDao dao类
     */
    @Resource(name = "serverParamConfigDao")
    public void setServerParamConfigDao(TblServerParamConfigMapper serverParamConfigDao) {
        this.serverParamConfigDao = serverParamConfigDao;
        super.setBaseDao(serverParamConfigDao);
    }

    /**
     * 服务器参数配置修改
     *
     * @param serverParamConfig 入参
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int updateServerParamConfig(TblServerParamConfig serverParamConfig) {
        try {
            return serverParamConfigDao.updateByPrimaryKeySelective(serverParamConfig);
        } catch (DataAccessException e) {
            LOG.error("updateByPrimaryKeySelective DataAccessException {} ", e.getMessage());
        }
        return 0;
    }

    /**
     * 图像诊断时，保存内容处理
     *
     * @param serverParamConfig 对象实体
     * @return 是否存在服务器参数配置的异常
     */
    @Override
    public boolean isImageDiagnosisFail(TblServerParamConfig serverParamConfig) {
        Integer serviceSubType = serverParamConfig.getServiceSubType();
        if (CommonUtil.isNull(serviceSubType)) {
            return false;
        }
        TblServerParamConfig tblServerParam = new TblServerParamConfig();
        tblServerParam.setId(serverParamConfig.getId());
        List<TblServerParamConfig> lists = getAllServerParamConfig(tblServerParam);
        if (CollectionUtil.isEmpty(lists)) {
            return false;
        }
        switch (serviceSubType) {
            case ServiceCommonConst.TASK_SERVER:
                return !updateTaskServerParamConfig(serverParamConfig, lists);
            case ServiceCommonConst.DIAGNOSTIC_SERVER:
                return !updateDiagnosticServerParamConfig(serverParamConfig, lists);
            default:
                break;
        }
        return false;
    }

    /**
     * 更新实体类属性值
     *
     * @param serverParamConfig 实体类
     * @param lists 数据库对象集
     * @return 是否更新成功：true成功，false失败
     */
    private boolean updateTaskServerParamConfig(TblServerParamConfig serverParamConfig,
        List<TblServerParamConfig> lists) {
        if (StringUtil.isEmpty(lists.get(0).getServiceSubId())) {
            // dz总控程序
            serverParamConfig.setDzPort(String.valueOf(serverParamConfigs.getDzPort()));
        } else if (lists.get(0)
            .getServiceSubId()
            .startsWith(String.valueOf(serverParamConfigs.getMqId()).substring(0, 1))) {
            // 消息服务器
            String mqUser = serverParamConfigs.getMqUser();
            if (StringUtil.isEmpty(mqUser) || StringUtil.isEmpty(serverParamConfigs.getMqPwd())) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FILE_FAIL);
                return false;
            }
            serverParamConfig.setDzPort(String.valueOf(serverParamConfigs.getMqPort()));
            serverParamConfig.setServiceName(mqUser);
            serverParamConfig.setServicePassword(Rsa.encryptString(serverParamConfigs.getMqPwd()));
        } else if (lists.get(0)
            .getServiceSubId()
            .startsWith(String.valueOf(serverParamConfigs.getFileServerIp1Id()).substring(0, 1))) {
            // 文件服务器
            String fileServerIp1Type = serverParamConfigs.getFileServerIp1Type();
            if (StringUtil.isEmpty(fileServerIp1Type)) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FILE_FAIL);
                return false;
            }
            serverParamConfig.setDzPort(String.valueOf(serverParamConfigs.getFileServerIp1Port()));
            serverParamConfig.setFileServiceType(fileServerIp1Type);
        }
        return true;
    }

    /**
     * 更新实体类属性值
     *
     * @param serverParamConfig 实体类
     * @param lists 数据库对象集
     * @return 是否更新成功：true成功，false失败
     */
    private boolean updateDiagnosticServerParamConfig(TblServerParamConfig serverParamConfig,
        List<TblServerParamConfig> lists) {
        if (lists.get(0).getServiceSubId().startsWith(String.valueOf(serverParamConfigs.getDiagId()).substring(0, 1))) {
            // 诊断服务器
            String diagUser = serverParamConfigs.getDiagUser();
            if (StringUtil.isEmpty(diagUser) || StringUtil.isEmpty(serverParamConfigs.getDiagPwd())) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FILE_FAIL);
                return false;
            }
            serverParamConfig.setDzPort(String.valueOf(serverParamConfigs.getDiagPort()));
            serverParamConfig.setServiceLinkId(null);
            serverParamConfig.setKeepType(null);
            serverParamConfig.setServiceName(diagUser);
            serverParamConfig.setServicePassword(Rsa.encryptString(serverParamConfigs.getDiagPwd()));
        } else if (lists.get(0)
            .getServiceSubId()
            .startsWith(String.valueOf(serverParamConfigs.getCollectId()).substring(0, 1))) {
            // 采集服务器
            String collectUser = serverParamConfigs.getCollectUser();
            if (StringUtil.isEmpty(collectUser) || StringUtil.isEmpty(serverParamConfigs.getCollectPwd())) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FILE_FAIL);
                return false;
            }
            serverParamConfig.setDzPort(String.valueOf(serverParamConfigs.getCollectPort()));
            serverParamConfig.setServiceLinkId(null);
            serverParamConfig.setKeepType(null);
            serverParamConfig.setServiceName(collectUser);
            serverParamConfig.setServicePassword(Rsa.encryptString(serverParamConfigs.getCollectPwd()));
        } else if (lists.get(0)
            .getServiceSubId()
            .startsWith(String.valueOf(serverParamConfigs.getMidIp1Id()).substring(0, 1))) {
            // 中间件
            serverParamConfig.setMiddlePort(String.valueOf(serverParamConfigs.getMidIp1Port()));
            serverParamConfig.setMiddleHttpPort(String.valueOf(serverParamConfigs.getMidIp1HttpPort()));
            serverParamConfig.setMiddleTcpPort(String.valueOf(serverParamConfigs.getMidIp1TcpPort()));
        }
        return true;
    }

    /**
     * 服务器参数配置删除
     *
     * @param id 服务器id
     * @return int
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int deleteServerParamConfig(Integer id) {
        try {
            return serverParamConfigDao.deleteByPrimaryKey(id);
        } catch (DataAccessException e) {
            LOG.error("deleteByPrimaryKey DataAccessException {} ", e.getMessage());
        }
        return 0;
    }

    /**
     * 查询所有参数配置返回List<TblServerParamConfig/>集合
     *
     * @param serverParamConfig 入参
     * @return List<TblServerParamConfig/>
     */
    @Override
    public List<TblServerParamConfig> getAllServerParamConfig(TblServerParamConfig serverParamConfig) {
        return selectByPage(serverParamConfig, null, null);
    }

    /**
     * 服务器参数配置修改
     *
     * @param serverParamConfig 入参
     * @return int
     */
    @Override
    public int updateServerParamConfigByIdAndIpAddress(TblServerParamConfig serverParamConfig) {
        try {
            return serverParamConfigDao.updateByPrimaryKeyAndIpAddressSelective(serverParamConfig);
        } catch (DataAccessException e) {
            LOG.error("updateByPrimaryKeyAndIpAddressSelective DataAccessException {} ", e.getMessage());
        }
        return 0;
    }

    @Override
    protected Map<String, Object> getMap(TblServerParamConfig clz, Map<String, Object> condition) {
        Map<String, Object> maps = new HashMap<String, Object>();
        if (MapUtils.isNotEmpty(condition)) {
            maps.putAll(condition);
        }
        maps.putAll(Bean2Map.bean2Map(clz));
        return maps;
    }

    @Override
    public void processTxzdParamConfig(TblServerParamConfig tblServerParamConfig, JSONObject jsonObject) {
        TblServerParamConfig tblServerParam = new TblServerParamConfig();
        tblServerParam.setServiceIpAddress(tblServerParamConfig.getServiceIpAddress());
        tblServerParam.setServiceFlag(tblServerParamConfig.getServiceFlag());

        // 一个IP最多支持两个子类型服务器，第三个来提示重复
        if (serverParamConfigDao.getServerSubTypeCountByIp(tblServerParam) > 1) {
            LOG.error("Duplicate Server IP Address,Please Check ");

            // 一个IP最多支持两个子类型服务器，请检查
            jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.SERVER_IP_ADDRESS_MORE);
            return;
        }

        // 记录此子类型的IP是第几次输入，默认0首次录入
        int frequency = 0;
        tblServerParam.setServiceIpAddress(null);
        Integer serviceSubType = tblServerParamConfig.getServiceSubType();
        tblServerParam.setServiceSubType(serviceSubType);
        List<TblServerParamConfig> records = getAllServerParamConfig(tblServerParam);
        if (CollectionUtil.isNotEmpty(records)) {
            String ipTmp = "";
            for (TblServerParamConfig serverParam : records) {
                if (!ipTmp.equals(serverParam.getServiceIpAddress())) {
                    frequency++;
                    ipTmp = serverParam.getServiceIpAddress();
                }
            }
        }

        // 前台传入，null旧的，1任务接收服务器，2诊断服务器，3视频监控平台
        switch (serviceSubType) {
            // 暂只支持1台采集、1台文件服务器、1台中间件，其它随意
            case ServiceCommonConst.TASK_SERVER:
                addTaskServerParamConfig(tblServerParamConfig, frequency, jsonObject);
                break;
            case ServiceCommonConst.DIAGNOSTIC_SERVER:
                addDiagnosticServerParamConfig(tblServerParamConfig, frequency, jsonObject);
                break;
            case ServiceCommonConst.VIDEO_PLATFORM_SERVER:
                addPlatformServerParamConfig(tblServerParamConfig, frequency, jsonObject);
                break;
            default:
                break;
        }
    }

    @Override
    public void refreshServerStatus(List<TblServerParamConfig> lists, TblServerParamConfig serverParamConfig) {
        TblServerParamConfig tblServerParam = new TblServerParamConfig();
        tblServerParam.setServiceFlag(serverParamConfig.getServiceFlag());

        // 查询全部参数统一刷新
        List<TblServerParamConfig> serverList = getAllServerParamConfig(tblServerParam);
        if (isServerParamConfigFailed(lists, serverParamConfig, serverList)) {
            LOG.error("Refresh server status is failed !");
            return;
        }
        String dzIp = "";
        String dzPort = "";
        List<PicServerVo> picServerList = new ArrayList<PicServerVo>();
        List<MonitorPlatformVo> monitorPlatformList = new ArrayList<MonitorPlatformVo>();
        List<DiagnosisCollectVo> diagnosisList = new ArrayList<DiagnosisCollectVo>();
        List<DiagnosisCollectVo> collectList = new ArrayList<DiagnosisCollectVo>();
        List<MiddleVo> middleList = new ArrayList<MiddleVo>();
        StringBuilder activeMqStringBuilder = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        for (TblServerParamConfig serverOne : serverList) {
            if (StringUtil.isEmpty(serverOne.getServiceSubId())) {
                if (serverOne.getServiceSubType() == ServiceCommonConst.TASK_SERVER) {
                    dzIp = serverOne.getServiceIpAddress();
                    dzPort = serverOne.getDzPort();
                }
                continue;
            }
            switch (serverOne.getServiceSubType()) {
                // 1任务接收服务器，2诊断服务器，3视频监控平台
                case ServiceCommonConst.TASK_SERVER:
                    constructTaskServerParam(picServerList, activeMqStringBuilder, serverOne);
                    break;
                case ServiceCommonConst.DIAGNOSTIC_SERVER:
                    constructDiagnosisCollectParam(diagnosisList, collectList, middleList, serverOne);
                    break;
                case ServiceCommonConst.VIDEO_PLATFORM_SERVER:
                    constructMonitorPlatformParam(monitorPlatformList, serverOne);
                    break;
                default:
                    break;
            }
        }
        if (StringUtil.isEmpty(dzIp) || StringUtil.isEmpty(dzPort)) {
            return;
        }
        JSONObject json = constructServerParamJson(picServerList, activeMqStringBuilder, diagnosisList, collectList);
        refreshServerListByUrl(dzIp, dzPort, monitorPlatformList, middleList, json);
    }

    /**
     * 通过调用dz总控程序接口，修改服务器状态
     *
     * @param dzIp 总控IP
     * @param dzPort 总控端口
     * @param monitorPlatformList 监控平台列表
     * @param middleList 中间件列表
     * @param json 服务器同步接口入参json字符串
     */
    private void refreshServerListByUrl(String dzIp, String dzPort, List<MonitorPlatformVo> monitorPlatformList,
        List<MiddleVo> middleList, JSONObject json) {
        if (CollectionUtil.isNotEmpty(monitorPlatformList)) {
            json.put(SERVER, monitorPlatformList);
        }
        if (CollectionUtil.isNotEmpty(middleList)) {
            json.put(MIDDLE, middleList);
        }

        // 调用服务器同步接口
        if (!isServerList(dzIp, dzPort, json)) {
            return;
        }

        // 调用服务器状态查询接口
        if (!isServerStatus(dzIp, dzPort)) {
            return;
        }

        // 调用国标平台和中间件关联接口
        serverrelevancy(monitorPlatformList, middleList, dzIp, dzPort);
    }

    /**
     * 调用国标平台和中间件关联接口
     *
     * @param monitorPlatformList 监控平台列表
     * @param middleList 中间件列表
     * @param ipAddress 总控Ip
     * @param port 总控端口
     */
    private void serverrelevancy(List<MonitorPlatformVo> monitorPlatformList, List<MiddleVo> middleList,
        String ipAddress, String port) {
        if (CollectionUtil.isEmpty(monitorPlatformList) || CollectionUtil.isEmpty(middleList)) {
            return;
        }
        JSONArray jsonArray = new JSONArray();
        for (MonitorPlatformVo monitorVo : monitorPlatformList) {
            JSONObject json = new JSONObject();
            json.put("platformid", monitorVo.getPlatformid());
            json.put("middleid", middleList.stream().map(middleVo -> middleVo.getMiddleid()).toArray(String[]::new));
            jsonArray.add(json);
        }
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("relevancy", jsonArray);
        String url = ProtocolConfig.getParameterConfig()
            .getImageDiagnosisUrl()
            .replaceFirst(ServiceCommonConst.HOST, ipAddress)
            .replaceFirst(ServiceCommonConst.PORT, port) + "serverrelevancy";
        String result = httpClientUtil.doPostMasterControl(url, jsonParam, null);
        if (StringUtil.isNull(result)) {
            LOG.error("serverrelevancy jsonResult is null ");
            return;
        }
        JSONObject jsonResult = JSONObject.parseObject(result);
        if (CommonUtil.isNull(jsonResult)) {
            LOG.error("serverrelevancy jsonResult is null ");
            return;
        }
        if (!jsonResult.containsKey(ServiceCommonConst.STATE)
            || !String.valueOf(NumConstant.NUM_0).equals(jsonResult.get(ServiceCommonConst.STATE))) {
            LOG.error("serverrelevancy jsonResult is failed ");
        }
    }

    /**
     * 构造诊断服务器参数
     *
     * @param diagnosisList 诊断服务器列表
     * @param collectList 采集服务器列表
     * @param middleList 中间件列表
     * @param serverOne 数据源
     */
    private void constructDiagnosisCollectParam(List<DiagnosisCollectVo> diagnosisList,
        List<DiagnosisCollectVo> collectList, List<MiddleVo> middleList, TblServerParamConfig serverOne) {
        DiagnosisCollectVo diagnosisCollectVo = new DiagnosisCollectVo();

        // 采集服务器
        if (serverOne.getServiceSubId().startsWith(String.valueOf(serverParamConfigs.getCollectId()).substring(0, 1))) {
            diagnosisCollectVo.setIp(serverOne.getServiceIpAddress());
            diagnosisCollectVo.setPort(serverOne.getDzPort());
            diagnosisCollectVo.setUser(serverOne.getServiceName());
            diagnosisCollectVo.setPwd(Rsa.decryptString(serverOne.getServicePassword()));
            diagnosisCollectVo.setId(serverOne.getServiceSubId());
            collectList.add(diagnosisCollectVo);
        } else if (serverOne.getServiceSubId()
            .startsWith(String.valueOf(serverParamConfigs.getDiagId()).substring(0, 1))) {
            // 诊断服务器
            diagnosisCollectVo.setIp(serverOne.getServiceIpAddress());
            diagnosisCollectVo.setPort(serverOne.getDzPort());
            diagnosisCollectVo.setUser(serverOne.getServiceName());
            diagnosisCollectVo.setPwd(Rsa.decryptString(serverOne.getServicePassword()));
            diagnosisCollectVo.setId(serverOne.getServiceSubId());
            diagnosisList.add(diagnosisCollectVo);
        } else if (serverOne.getServiceSubId()
            .startsWith(String.valueOf(serverParamConfigs.getMidIp1Id()).substring(0, 1))) {
            // 中间件信息
            MiddleVo middleVo = new MiddleVo();
            middleVo.setHttpport(serverOne.getMiddleHttpPort());
            middleVo.setIp(serverOne.getServiceIpAddress());
            middleVo.setKeep(serverOne.getKeepType());
            middleVo.setMiddleid(serverOne.getServiceLinkId());
            middleVo.setPort(serverOne.getDzPort());
            middleVo.setPwd(Rsa.decryptString(serverOne.getServicePassword()));
            middleVo.setSipport(serverOne.getMiddlePort());
            middleVo.setTcpport(serverOne.getMiddleTcpPort());
            middleVo.setUser(serverOne.getServiceName());
            middleList.add(middleVo);
        }
    }

    /**
     * 构造监控平台参数
     *
     * @param monitorPlatformList 监控平台列表
     * @param serverOne 数据源
     */
    private void constructMonitorPlatformParam(List<MonitorPlatformVo> monitorPlatformList,
        TblServerParamConfig serverOne) {
        // 视频监控平台
        MonitorPlatformVo monitorPlatformVo = new MonitorPlatformVo();
        monitorPlatformVo.setIp(serverOne.getServiceIpAddress());
        monitorPlatformVo.setPort(serverOne.getDzPort());
        monitorPlatformVo.setUser(serverOne.getServiceName());
        monitorPlatformVo.setPwd(Rsa.decryptString(serverOne.getServicePassword()));
        monitorPlatformVo.setPlatformid(serverOne.getServiceLinkId());
        monitorPlatformList.add(monitorPlatformVo);
    }

    /**
     * 构造服务器同步接口入参json字符串
     *
     * @param picServerList 文件服务器列表
     * @param activeMqStringBuilder 消息服务器列表字符串
     * @param diagnosisList 诊断服务器列表
     * @param collectList 采集服务器列表
     * @return json json
     */
    private JSONObject constructServerParamJson(List<PicServerVo> picServerList, StringBuilder activeMqStringBuilder,
                                                List<DiagnosisCollectVo> diagnosisList, List<DiagnosisCollectVo> collectList) {
        JSONObject json = new JSONObject();
        if (activeMqStringBuilder.length() > 1) {
            activeMqStringBuilder.setLength(activeMqStringBuilder.length() - 1);
            activeMqStringBuilder.append(")?initialReconnectDelay=1000");
            ActiveMqVo activeMqVo = new ActiveMqVo();
            activeMqVo.setUser(serverParamConfigs.getMqUser());
            activeMqVo.setPwd(serverParamConfigs.getMqPwd());
            activeMqVo.setUrl("failover:(" + activeMqStringBuilder.toString());
            json.put(ACTIVEMQ, activeMqVo);
        }
        if (CollectionUtil.isNotEmpty(picServerList)) {
            json.put(PICSERVER, picServerList);
        }
        if (CollectionUtil.isNotEmpty(diagnosisList)) {
            json.put(DIAGNOSIS, diagnosisList);
        }
        if (CollectionUtil.isNotEmpty(collectList)) {
            json.put(COLLECT, collectList);
        }
        return json;
    }

    /**
     * 调用服务器状态查询接口
     *
     * @param dzIp 总控Ip
     * @param dzPort 总控端口
     * @return 是否调用成功
     */
    private boolean isServerStatus(String dzIp, String dzPort) {
        String url = ProtocolConfig.getParameterConfig()
            .getImageDiagnosisUrl()
            .replaceFirst(ServiceCommonConst.HOST, dzIp)
            .replaceFirst(ServiceCommonConst.PORT, dzPort) + "serverstatus";
        JSONObject json = new JSONObject();
        json.put("keepalive", NumConstant.NUM_1);
        String result = httpClientUtil.doPostMasterControl(url, json, null);
        if (StringUtil.isNull(result)) {
            LOG.error("serverstatus jsonResult is null ");
            return false;
        }
        JSONObject jsonResult = JSONObject.parseObject(result);
        if (CommonUtil.isNull(jsonResult)) {
            LOG.error("serverstatus jsonResult is null ");
            return false;
        }

        // 诊断服务器
        boolean isDiagSuccess =
            refreshStatusByJsonArray(jsonResult.getJSONArray(DIAGNOSIS), ServiceCommonConst.DIAGNOSTIC_SERVER);

        // 采集服务器
        boolean isCollectSuccess =
            refreshStatusByJsonArray(jsonResult.getJSONArray(COLLECT), ServiceCommonConst.DIAGNOSTIC_SERVER);

        // 中间件服务器
        boolean isMidSuccess =
            refreshStatusByJsonArray(jsonResult.getJSONArray(MIDDLE), ServiceCommonConst.DIAGNOSTIC_SERVER);

        // 监控平台信息
        boolean isPlatformSuccess =
            refreshStatusByJsonArray(jsonResult.getJSONArray(SERVER), ServiceCommonConst.VIDEO_PLATFORM_SERVER);

        // 全部服务器状态得刷新一遍，不能因为一个服务器刷新失败了就直接短路了，其它服务器状态就不刷新了
        return isDiagSuccess || isCollectSuccess || isMidSuccess || isPlatformSuccess;
    }

    /**
     * 通过ip端口修改服务器状态
     *
     * @param jsonArray 服务器状态接口返回的json字符串
     * @param serviceSubType 服务器子类型
     * @return 是否刷新成功
     */
    private boolean refreshStatusByJsonArray(JSONArray jsonArray, int serviceSubType) {
        if (CommonUtil.isNull(jsonArray)) {
            return false;
        }
        TblServerParamConfig serverParamConfig = new TblServerParamConfig();
        int successCount = 0;
        for (int ii = 0; ii < jsonArray.size(); ii++) {
            JSONObject jsonData = jsonArray.getJSONObject(ii);
            serverParamConfig.setServiceSubType(serviceSubType);
            serverParamConfig.setServiceIpAddress(jsonData.getString(CommonConst.IP));
            serverParamConfig.setMiddlePort(jsonData.getString(ServiceCommonConst.PORT));
            transMessageByStatus(jsonData.getInteger(STATUS), serverParamConfig);
            if (updateServerParamConfigByIdAndIpAddress(serverParamConfig) > 0) {
                successCount++;
            }
        }
        return successCount > 0;
    }

    /**
     * 通过状态值更新服务器参数属性
     *
     * @param status 状态值
     * @param serverParamConfig 实体类
     */
    @Override
    public void transMessageByStatus(Integer status, TblServerParamConfig serverParamConfig) {
        String languageType = ProgressInfo.getLanguageTyle();
        if (CommonConstant.LANGUAGE_ZH_CN.equals(languageType)) { // 中文
            if (status == NumConstant.NUM_0) {
                serverParamConfig.setServiceStatus(String.valueOf(NumConstant.NUM_0));
                serverParamConfig.setMessage(ServiceCommonConst.PING_SUCCESS);
            } else if (status == NumConstant.NUM_1_NEGATIVE) {
                serverParamConfig.setMessage(ServiceCommonConst.PING_FALSE);
            } else {
                serverParamConfig.setMessage(ServiceCommonConst.PING_TIME_OUT);
            }
        } else { // 英文
            if (status == NumConstant.NUM_0) {
                serverParamConfig.setServiceStatus(String.valueOf(NumConstant.NUM_0));
                serverParamConfig.setMessage(ServiceCommonConst.PING_SUCCESS_EN);
            } else if (status == NumConstant.NUM_1_NEGATIVE) {
                serverParamConfig.setMessage(ServiceCommonConst.PING_FALSE_EN);
            } else {
                serverParamConfig.setMessage(ServiceCommonConst.PING_TIME_OUT_EN);
            }
        }
    }

    /**
     * 调用服务器同步接口
     *
     * @param ipAddress 总控Ip
     * @param port 总控端口
     * @param json 服务器同步接口入参json字符串
     * @return 是否调用成功
     */
    private boolean isServerList(String ipAddress, String port, JSONObject json) {
        if (CommonUtil.isNull(json)) {
            LOG.error("constructServerParamJson is null ");
            return false;
        }
        String url = ProtocolConfig.getParameterConfig()
            .getImageDiagnosisUrl()
            .replaceFirst(ServiceCommonConst.HOST, ipAddress)
            .replaceFirst(ServiceCommonConst.PORT, port) + "serverlist";
        String result = httpClientUtil.doPostMasterControl(url, json, null);
        if (StringUtil.isNull(result)) {
            LOG.error("serverlist jsonResult is null ");
            return false;
        }
        JSONObject jsonResult = JSONObject.parseObject(result);
        if (CommonUtil.isNull(jsonResult)) {
            LOG.error("serverlist jsonResult is null ");
            return false;
        }
        return jsonResult.containsKey(ServiceCommonConst.STATE)
            && String.valueOf(NumConstant.NUM_0).equals(jsonResult.get(ServiceCommonConst.STATE));
    }

    private boolean isServerParamConfigFailed(List<TblServerParamConfig> lists, TblServerParamConfig serverParamConfig,
        List<TblServerParamConfig> serverList) {
        if (CollectionUtil.isEmpty(serverList)) {
            LOG.error("server param config is empty...");
            return true;
        }
        int count = 1;
        Integer serviceSubType = serverParamConfig.getServiceSubType();
        if (serviceSubType == ServiceCommonConst.TASK_SERVER
            || serviceSubType == ServiceCommonConst.DIAGNOSTIC_SERVER) {
            // 1IP对应任务接收服务器、诊断服务器最多3个
            count = NumConstant.NUM_3;
        }
        if (CollectionUtil.isNotEmpty(lists) && lists.size() > count) {
            // 查询结果多余，表示输入的IP重复，则返回
            LOG.error("Duplicate Server IP Address,Please Check ");
            return true;
        }

        // 不存在dz总控程序，直接退出，即无法刷新状态
        return serverList.stream()
            .noneMatch(one -> one.getServiceSubType() == ServiceCommonConst.TASK_SERVER
                && CommonUtil.isNull(one.getServiceSubId()));
    }

    /**
     * 构造同步服务器参数集
     *
     * @param picServerList 文件服务器列表
     * @param activeMqStringBuilder 消息服务器列表字符串
     * @param serverOne 数据源
     */
    private void constructTaskServerParam(List<PicServerVo> picServerList, StringBuilder activeMqStringBuilder,
        TblServerParamConfig serverOne) {
        // 消息服务器tcp://192.168.0.87:61616,tcp://192.168.0.149:61616,tcp://192.168.0.150:61616
        if (serverOne.getServiceSubId().startsWith(String.valueOf(serverParamConfigs.getMqId()).substring(0, 1))) {
            activeMqStringBuilder.append("tcp://")
                .append(serverOne.getServiceIpAddress())
                .append(CommonSymbolicConstant.SYMBOL_COLON)
                .append(serverOne.getDzPort())
                .append(CommonSymbolicConstant.SYMBOL_SEPARATOR);
        } else if (serverOne.getServiceSubId()
            .startsWith(String.valueOf(serverParamConfigs.getFileServerIp1Id()).substring(0, 1))) {
            // 文件服务器
            PicServerVo picServerVo = new PicServerVo();
            StringBuilder picStringBuilder = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
            String ipAddress = serverOne.getServiceIpAddress();
            picStringBuilder.append(ServiceCommonConst.HTTP)
                .append(ipAddress)
                .append(CommonSymbolicConstant.SYMBOL_COLON)
                .append(serverOne.getDzPort())
                .append(UtilMethod.SLASH)
                .append("upload");
            picServerVo.setUrl(picStringBuilder.toString());
            picServerVo.setType(serverOne.getFileServiceType());
            picServerVo.setId(serverOne.getServiceSubId());
            picServerList.add(picServerVo);
            TblServerParamConfig serverParamConfig = new TblServerParamConfig();

            // 通过针对IP的Ping方式来刷新文件服务器状态
            transMessageByStatus(judgmentPingResoult(ipAddress), serverParamConfig);
            serverParamConfig.setServiceSubType(ServiceCommonConst.TASK_SERVER);
            serverParamConfig.setServiceIpAddress(ipAddress);
            serverParamConfig.setMiddlePort(serverOne.getDzPort());
            updateServerParamConfigByIdAndIpAddress(serverParamConfig);

            // 刷新dz总控程序状态
            serverParamConfig.setMiddlePort(String.valueOf(serverParamConfigs.getDzPort()));
            updateServerParamConfigByIdAndIpAddress(serverParamConfig);

            // 刷新消息服务器状态
            serverParamConfig.setMiddlePort(String.valueOf(serverParamConfigs.getMqPort()));
            updateServerParamConfigByIdAndIpAddress(serverParamConfig);
        }
    }

    /**
     * 保存诊断服务器参数
     *
     * @param tblServerParamConfig 实体类
     * @param frequency 第几次录入
     * @param jsonObject 返回体
     */
    private void addDiagnosticServerParamConfig(TblServerParamConfig tblServerParamConfig, int frequency,
        JSONObject jsonObject) {
        String diagUser = serverParamConfigs.getDiagUser();
        if (StringUtil.isEmpty(diagUser) || StringUtil.isEmpty(serverParamConfigs.getDiagPwd())) {
            LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FILE_FAIL);
            jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.SERVER_CONFIG_FILE_ERROR);
            return;
        }
        if (frequency == 0) {
            // 中间件
            tblServerParamConfig.setServiceSubId(String.valueOf(serverParamConfigs.getMidIp1Id()));
            tblServerParamConfig.setMiddlePort(String.valueOf(serverParamConfigs.getMidIp1Port()));
            tblServerParamConfig.setMiddleHttpPort(String.valueOf(serverParamConfigs.getMidIp1HttpPort()));
            tblServerParamConfig.setMiddleTcpPort(String.valueOf(serverParamConfigs.getMidIp1TcpPort()));
            int createSize = create(tblServerParamConfig);
            if (createSize <= 0) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FAIL);
                jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.ADD_SERVICE_CONFIG_FAIL);
                return;
            }

            // 诊断服务器1
            tblServerParamConfig.setShowFlag(ServiceCommonConst.SHOW_NOT);
            tblServerParamConfig.setDzPort(String.valueOf(serverParamConfigs.getDiagPort()));
            tblServerParamConfig.setServiceSubId(String.valueOf(serverParamConfigs.getDiagId()));
            tblServerParamConfig.setServiceName(diagUser);
            tblServerParamConfig.setServicePassword(Rsa.encryptString(serverParamConfigs.getDiagPwd()));
            tblServerParamConfig.setMiddlePort(null);
            tblServerParamConfig.setMiddleHttpPort(null);
            tblServerParamConfig.setMiddleTcpPort(null);
            tblServerParamConfig.setKeepType(CommonConst.COMMON_MIDDLELINE);
            tblServerParamConfig.setServiceLinkId(CommonConst.COMMON_MIDDLELINE);
            createSize = create(tblServerParamConfig);
            if (createSize <= 0) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FAIL);
                jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.ADD_SERVICE_CONFIG_FAIL);
                return;
            }

            // 采集服务器1
            String collectUser = serverParamConfigs.getCollectUser();
            if (StringUtil.isEmpty(collectUser) || StringUtil.isEmpty(serverParamConfigs.getCollectPwd())) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FILE_FAIL);
                jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.SERVER_CONFIG_FILE_ERROR);
                return;
            }
            tblServerParamConfig.setServiceSubId(String.valueOf(serverParamConfigs.getCollectId()));
            tblServerParamConfig.setDzPort(String.valueOf(serverParamConfigs.getCollectPort()));
            tblServerParamConfig.setServiceName(collectUser);
            tblServerParamConfig.setServicePassword(Rsa.encryptString(serverParamConfigs.getCollectPwd()));
            createSize = create(tblServerParamConfig);
            processResult(jsonObject, createSize);
        } else {
            proCollectServerParamConfig(tblServerParamConfig, frequency, jsonObject);
        }
    }

    /**
     * 第二次含第二次以后诊断采集服务器参数保存
     *
     * @param tblServerParamConfig 实体类
     * @param frequency 第几次录入
     * @param jsonObject 返回体
     */
    private void proCollectServerParamConfig(TblServerParamConfig tblServerParamConfig, int frequency,
        JSONObject jsonObject) {
        tblServerParamConfig.setKeepType(CommonConst.COMMON_MIDDLELINE);
        tblServerParamConfig.setServiceLinkId(CommonConst.COMMON_MIDDLELINE);

        // 采集服务器369
        if (frequency % NumConstant.NUM_3 == 0) {
            String collectUser = serverParamConfigs.getCollectUser();
            if (StringUtil.isEmpty(collectUser) || StringUtil.isEmpty(serverParamConfigs.getCollectPwd())) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FILE_FAIL);
                jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.SERVER_CONFIG_FILE_ERROR);
                return;
            }

            // 计算采集服务器Id
            int collectId = serverParamConfigs.getCollectId() + frequency / NumConstant.NUM_3;
            tblServerParamConfig.setServiceSubId(String.valueOf(collectId));
            tblServerParamConfig.setDzPort(String.valueOf(serverParamConfigs.getCollectPort()));
            tblServerParamConfig.setServiceName(collectUser);
            tblServerParamConfig.setServicePassword(Rsa.encryptString(serverParamConfigs.getCollectPwd()));
            tblServerParamConfig.setShowFlag(ServiceCommonConst.SHOW_NOT);
            int createSize = create(tblServerParamConfig);
            if (createSize <= 0) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FAIL);
                jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.ADD_SERVICE_CONFIG_FAIL);
                return;
            }
        }

        // 诊断服务器，即采集服务器同时也是诊断服务器
        int diagPort = serverParamConfigs.getDiagPort();
        int diagId = serverParamConfigs.getDiagId() + frequency;
        tblServerParamConfig.setServiceSubId(String.valueOf(diagId));
        tblServerParamConfig.setDzPort(String.valueOf(diagPort));
        tblServerParamConfig.setServiceName(serverParamConfigs.getDiagUser());
        tblServerParamConfig.setServicePassword(Rsa.encryptString(serverParamConfigs.getDiagPwd()));
        tblServerParamConfig.setShowFlag(ServiceCommonConst.SHOW_YES);
        int createSize = create(tblServerParamConfig);
        processResult(jsonObject, createSize);
    }

    private void processResult(JSONObject jsonObject, int createSize) {
        if (createSize > 0) {
            jsonObject.put(CommonConst.SUCCESS, true);
            jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.ADD_SERVICE_CONFIG_SUCCESS);
        } else {
            LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FAIL);
            jsonObject.put(CommonConst.SUCCESS, false);
            jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.ADD_SERVICE_CONFIG_FAIL);
        }
    }

    /**
     * 保存监控平台参数
     *
     * @param tblServerParamConfig 实体类
     * @param frequency 第几次录入
     * @param jsonObject 返回体
     */
    private void addPlatformServerParamConfig(TblServerParamConfig tblServerParamConfig, int frequency,
        JSONObject jsonObject) {
        int platformId = serverParamConfigs.getPlatformId();
        if (frequency > 0) {
            platformId = platformId + frequency;
        }

        // 监控平台
        tblServerParamConfig.setServiceSubId(String.valueOf(platformId));
        int createSize = create(tblServerParamConfig);
        processResult(jsonObject, createSize);
    }

    /**
     * 保存任务同步服务器参数
     *
     * @param tblServerParamConfig 实体类
     * @param frequency 第几次录入
     * @param jsonObject 返回体
     */
    private void addTaskServerParamConfig(TblServerParamConfig tblServerParamConfig, int frequency,
        JSONObject jsonObject) {
        int mqIp1Id = serverParamConfigs.getMqId();
        String mqUser = serverParamConfigs.getMqUser();
        if (StringUtil.isEmpty(mqUser) || StringUtil.isEmpty(serverParamConfigs.getMqPwd())) {
            LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FILE_FAIL);
            jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.SERVER_CONFIG_FILE_ERROR);
            return;
        }
        if (frequency == 0) {
            // dz总控程序
            tblServerParamConfig.setDzPort(String.valueOf(serverParamConfigs.getDzPort()));
            int createSize = create(tblServerParamConfig);
            if (createSize <= 0) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FAIL);
                jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.ADD_SERVICE_CONFIG_FAIL);
                return;
            }

            // 消息服务器
            tblServerParamConfig.setShowFlag(ServiceCommonConst.SHOW_NOT);
            if (getMqCreateSize(tblServerParamConfig, mqIp1Id, mqUser, serverParamConfigs.getMqPwd()) <= 0) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FAIL);
                jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.ADD_SERVICE_CONFIG_FAIL);
                return;
            }

            // 文件服务器
            String fileServerIp1Type = serverParamConfigs.getFileServerIp1Type();
            if (StringUtil.isEmpty(fileServerIp1Type)) {
                LOG.error(ServiceCommonConst.SERVER_PARAM_CONFIG_FILE_FAIL);
                jsonObject.put(CommonConst.MESSAGE, ServiceCommonConst.SERVER_CONFIG_FILE_ERROR);
                return;
            }
            tblServerParamConfig.setDzPort(String.valueOf(serverParamConfigs.getFileServerIp1Port()));
            tblServerParamConfig.setServiceSubId(String.valueOf(serverParamConfigs.getFileServerIp1Id()));
            tblServerParamConfig.setServiceName(CommonConst.COMMON_MIDDLELINE);
            tblServerParamConfig.setFileServiceType(fileServerIp1Type);
            createSize = create(tblServerParamConfig);
            processResult(jsonObject, createSize);
        } else {
            // 消息服务器
            int createSize =
                getMqCreateSize(tblServerParamConfig, mqIp1Id + frequency, mqUser, serverParamConfigs.getMqPwd());
            processResult(jsonObject, createSize);
        }
    }

    private int getMqCreateSize(TblServerParamConfig tblServerParamConfig, int mqIp1Id, String mqUser, String mqPwd) {
        tblServerParamConfig.setDzPort(String.valueOf(serverParamConfigs.getMqPort()));
        tblServerParamConfig.setServiceSubId(String.valueOf(mqIp1Id));
        tblServerParamConfig.setServiceName(mqUser);
        tblServerParamConfig.setServicePassword(Rsa.encryptString(mqPwd));
        return create(tblServerParamConfig);
    }

    /**
     * 用于执行ping 的命令
     *
     * @param command ping 命令
     * @return String ping 结果
     */
    private StringBuilder executePingCommand(String command, String ipAddress) {
        StringBuilder sb = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        if (!UtilMethod.checkIp(ipAddress)) {
            LOG.error("IP address regular check failed.");
            return sb;
        }
        String pingCommand = command.replaceFirst("IP", ipAddress);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(pingCommand);
        } catch (IOException e) {
            LOG.error("IOException: {}", e.getMessage());
        }
        if (process == null) {
            LOG.error("process is null.");
            return sb;
        }
        try (InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, CommonConstant.CHARSET_GBK);
            BufferedReader br = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = FortifyUtils.readLine(br)) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            LOG.error("executePingCommand {}", e.getMessage());
        }
        return sb;
    }

    /**
     * 通过Ping的方式，判断该IP的连接状态
     *
     * @param ipAddress ipAddress
     * @return 0：连接成功；1:连接超时；-1：连接失败
     */
    @Override
    public int judgmentPingResoult(String ipAddress) {
        String systemName = FortifyUtils.getSysProperty(CommonConstant.OS_NAME).toLowerCase(Locale.ENGLISH);
        String pingCommand = ServiceCommonConst.PING_COMMAND_WINDOW;
        if (systemName.startsWith(CommonConstant.OS_LINUX)) {
            pingCommand = ServiceCommonConst.PING_COMMAND_LINUX;
        }
        StringBuilder stringBuilder = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        stringBuilder.append(UtilMethod.dateToString(new Date()))
            .append(System.lineSeparator())
            .append(executePingCommand(pingCommand, ipAddress));
        Pattern pattern = Pattern.compile(ServiceCommonConst.PING_RESOULT_PATTERN);
        Matcher matcher = pattern.matcher(stringBuilder.toString());
        while (matcher.find()) {
            float floatNum = Float.parseFloat(matcher.group(1));
            return CommonUtil.floatIsEqual(floatNum, 0) ? NumConstant.NUM_0 : NumConstant.NUM_1;
        }
        return NumConstant.NUM_1_NEGATIVE;
    }

    @Override
    public int updateServerParamConfigIp(TblServerParamConfig serverParamConfig) {
        try {
            return serverParamConfigDao.updateByPrimaryKeySelectiveIp(serverParamConfig);
        } catch (DataAccessException e) {
            LOG.error("updateServerParamConfigIp DataAccessException {} ", e.getMessage());
        }
        return 0;
    }
}
