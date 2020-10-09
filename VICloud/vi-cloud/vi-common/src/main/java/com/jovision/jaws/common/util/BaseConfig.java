/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

import com.huawei.vi.entity.po.FiledInfo;
import com.huawei.vi.entity.po.SessionFiled;
import com.jovision.jaws.common.config.vi.AbstractParamConfig;
import com.jovision.jaws.common.config.vi.AutomaticOrderConfig;
import com.jovision.jaws.common.config.vi.BusinessParamConfig;
import com.jovision.jaws.common.config.vi.FileMoreConfig;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.info.BaseCofigInfo;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置类
 *
 * @version 1.0, 2018年7月30日
 * @since 2018-07-30
 */
public final class  BaseConfig {
    /**
     * 公共日志类
     */
    private static final Logger LOG = LoggerFactory.getLogger(BaseConfig.class);

    private static final String SYMBOL = "@";

    private static List<FiledInfo> tcpFiledInfoList;

    private static List<FiledInfo> udpFiledInfoList;

    private static Map<String, FiledInfo> tcpFiledInfoMap;

    private static Map<String, FiledInfo> udpFiledInfoMap;

    private static Map<String, String> globalMap = null;

    private static List<String> udpFilterList = null;

    private static final int MAX_LENGTH = 200;

    private static final String ZH_CN = CommonConstant.LANGUAGE_ZH_CN;

    private static FileMoreConfig fileMoreConfig;

    @Getter
    private static BaseCofigInfo baseCofigInfo;

    private static AutomaticOrderConfig automaticOrderConfig;

    private static String platform;

    private static String organization;

    /**
     * 构造函数
     */
    private BaseConfig() {
    }
    static {
        newInstanceBaseConfig();
    }

    /**
     * 更新baseconfig.xml最新内容
     */
    public static void newInstanceBaseConfig() {
        baseCofigInfo = getAnalyseBaseConfigInfo();
        automaticOrderConfig = baseCofigInfo.getAutomaticOrderConfig();
        settingValue(baseCofigInfo);
        globalMap = UtilMethod.readFromPropertyFile(CommonConstant.FILE_CONFIG_PROPERTIES, MAX_LENGTH);
    }

    private static BaseCofigInfo getAnalyseBaseConfigInfo() {
        if (!AbstractParamConfig.getParamConfigMaps().containsKey(BusinessParamConfig.ANALYSE_BUSINESS_PARAM_CONFIG)) {
            AbstractParamConfig.getParamConfigMaps()
                    .put(BusinessParamConfig.ANALYSE_BUSINESS_PARAM_CONFIG,
                            XmlUtil.xmlToObject(CommonConstant.FILE_BASE_CONFIG, BaseCofigInfo.class));
        }
        return (BaseCofigInfo) AbstractParamConfig.getParamConfigMaps()
                .get(BusinessParamConfig.ANALYSE_BUSINESS_PARAM_CONFIG);
    }

    /**
     * 给参数设置值
     *
     * @param beanBaseConfigInfo 基础配置信息
     */
    private static void settingValue(@NonNull BaseCofigInfo beanBaseConfigInfo) {
        fileMoreConfig = beanBaseConfigInfo.getFileMoreConfig();
        platform = beanBaseConfigInfo.getFilterplatform();
        organization = beanBaseConfigInfo.getFilterOrganization();
        // 离线分析
        setFieldConfig();
    }

    private static void setFieldConfig() {
        SessionFiled sessionFiled = XmlUtil.xmlToObject(CommonConst.FILE_FIELD_CONFIG, SessionFiled.class);
        if (sessionFiled != null) {
            tcpFiledInfoList = sessionFiled.getTcpFiledInfoList();
            udpFiledInfoList = sessionFiled.getUdpFiledInfoList();
            changeValue(tcpFiledInfoList);
            changeValue(udpFiledInfoList);
            tcpFiledInfoMap = parseToMap(tcpFiledInfoList);
            udpFiledInfoMap = parseToMap(udpFiledInfoList);
        } else {
            LOG.error("sessionFiled is null.");
            return;
        }
    }

    /**
     * 换成分局或者所(防止代码重复率，提出的公共代码)
     *
     * @param list 字段列表
     */
    private static void changeValue(List<FiledInfo> list) {
        for (FiledInfo filedInfo : list) {
            if ("device_name".equals(filedInfo.getName())) {
                filedInfo.setRule(filedInfo.getRule().replaceAll(SYMBOL, platform));
            }
            if ("organization".equals(filedInfo.getName())) {
                filedInfo.setRule(filedInfo.getRule().replaceAll(SYMBOL, organization));
            }
            if ("platform".equals(filedInfo.getName())) {
                filedInfo.setRule(filedInfo.getRule().replaceAll(SYMBOL, platform));
            }
        }
    }

    /**
     * 获取配置文件信息
     *
     * @return tcpFiledInfoList
     */
    public static List<FiledInfo> getFiledInfoList() {
        return tcpFiledInfoList;
    }

    public static List<FiledInfo> getUdpFiledInfoList() {
        return udpFiledInfoList;
    }

    public static void setUdpFiledInfoList(List<FiledInfo> udpFiledInfoList) {
        BaseConfig.udpFiledInfoList = udpFiledInfoList;
    }

    /**
     * 获取tcp信息
     *
     * @param cite key值
     * @return FiledInfo
     */
    public static FiledInfo getTcpFiledInfo(String cite) {
        return tcpFiledInfoMap.get(cite);
    }

    /**
     * 获取udp信息
     *
     * @param cite key值
     * @return FiledInfo
     */
    public static FiledInfo getUdpFiledInfo(String cite) {
        return udpFiledInfoMap.get(cite);
    }

    public static Map<String, String> getGlobalMap() {
        return globalMap;
    }

    public static void setGlobalMap(Map<String, String> globalMap) {
        BaseConfig.globalMap = globalMap;
    }

    public static List<String> getUdpFilterList() {
        return udpFilterList;
    }

    private static Map<String, FiledInfo> parseToMap(List<FiledInfo> filedInfoList) {
        Map<String, FiledInfo> filedInfoMap = new HashMap<String, FiledInfo>();
        for (FiledInfo filedInfo : filedInfoList) {
            filedInfoMap.put(filedInfo.getName(), filedInfo);
        }
        return filedInfoMap;
    }

    private static List<String> parseTargetPort(String targetPort) {
        return Arrays.asList(targetPort.split(CommonConst.SEPARATOR));
    }

    public static FileMoreConfig getFileMoreConfig() {
        return fileMoreConfig;
    }
    public static AutomaticOrderConfig getAutomaticOrderConfig() {
        return automaticOrderConfig;
    }
    public static String getPlatform() {
        return platform;
    }
    public static String getOrganization() {
        return organization;
    }
}
