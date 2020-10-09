
package com.jovision.jaws.common.util;

import com.jovision.jaws.common.constant.CommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * 数据采集参数配置XML配置
 *
 */
public final class ProtocolConfig {
    /**
     * 公共日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolConfig.class);

    private static ParameterConfig parameterConfig;

    private static TitleConfig titleConfig;

    private static Map<String, String> globalMap = null;

    static {
        ProtocolConfigInfo beanProtocolConfigInfo =
            XmlUtil.xmlToObject(CommonConst.FILE_PROTOCOLCONFIG, ProtocolConfigInfo.class); // 配置Bean
        if (beanProtocolConfigInfo != null) {
            setParameterConfig(beanProtocolConfigInfo.getParameterConfig());
            setTitleConfig(beanProtocolConfigInfo.getTitleConfig());
        } else {
            LOG.info("beanProtocolConfigInfo is null.");
        }
        try {
            globalMap = PropertiesUtil.loadToMap(CommonConst.LANGUANG_BASE_CONFIG);
        } catch (IOException e) {
            LOG.error("loadToMap IOException {}", e.getMessage());
        }
    }

    /**
     * 构造函数
     */
    private ProtocolConfig() {
    }

    public static ParameterConfig getParameterConfig() {
        return parameterConfig;
    }

    private static void setParameterConfig(ParameterConfig parameter) {
        parameterConfig = parameter;
    }

    public static TitleConfig getTitleConfig() {
        return titleConfig;
    }

    public static void setTitleConfig(TitleConfig title) {
        titleConfig = title;
    }

    public static Map<String, String> getGlobalMap() {
        return globalMap;
    }

    public static void setGlobalMap(Map<String, String> globalMap) {
        ProtocolConfig.globalMap = globalMap;
    }
}
