/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;
import java.util.Map;

/**
 * 属性文件解析
 *
 * @version 1.0, 2018年6月20日
 * @since 2018-06-20
 */
public final class GlobalConfigProperty {
    // properties文件名最大长度
    private static final int MAX_LENGTH = NumConstant.NUM_200;

    private static Map<String, String> globalMap = null;

    private GlobalConfigProperty() {
    }

    /**
     * 解析属性文件
     *
     * @param propertiesFilePath 属性文件路径
     */
    public static void parse(String propertiesFilePath) {
        globalMap = UtilMethod.readFromPropertyFile(propertiesFilePath, MAX_LENGTH);
    }

    public static Map<String, String> getGlobalMap() {
        return globalMap;
    }

    public static void setGlobalMap(Map<String, String> globalMap) {
        GlobalConfigProperty.globalMap = globalMap;
    }
}