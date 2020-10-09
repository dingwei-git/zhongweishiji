/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service;

import com.huawei.utils.NumConstant;

/**
 * service层常量类
 *
 * @since 2019-11-08
 */
public final class ServiceCommonConst {
    /**
     * http
     */
    public static final String HTTP = "http://";

    /**
     * https
     */
    public static final String HTTPS = "https://";

    /**
     * 服务器地址
     */
    public static final String HOST = "host";

    /**
     * 端口
     */
    public static final String PORT = "port";

    /**
     * 状态
     */
    public static final String STATE = "state";

    /**
     * 图像诊断服务器参数配置入库失败提示信息
     */
    public static final String SERVER_PARAM_CONFIG_FAIL = "Add Server Param Config Fail.";

    /**
     * serverParamConfig.properties配置文件错误提示信息
     */
    public static final String SERVER_PARAM_CONFIG_FILE_FAIL = "Server Param Config File Error.";

    /**
     * 连接失败-中文
     */
    public static final String PING_FALSE = "\u8FDE\u63A5\u5931\u8D25";

    /**
     * 连接失败-EN
     */
    public static final String PING_FALSE_EN = "Connection failed";

    /**
     * 连接成功- 中文
     */
    public static final String PING_SUCCESS = "\u8FDE\u63A5\u6210\u529F";

    /**
     * 连接成功-EN
     */
    public static final String PING_SUCCESS_EN = "Connection successful";

    /**
     * PING 超时-中文
     */
    public static final String PING_TIME_OUT = "\u8FDE\u63A5\u8D85\u65F6";

    /**
     * PING 超时-EN
     */
    public static final String PING_TIME_OUT_EN = "Request timed out";

    /**
     * 语言类型
     */
    public static final String LANGUAGE_TYPE = "languageType";

    /**
     * IVS开关标识
     */
    public static final String HAVE_IVS = "haveIvs";

    /**
     * 图像诊断服务器配置开关标识
     */
    public static final String HAVE_IMAGE_DIAGNOSIS_SERVICE = "haveImageDiagnosisService";

    /**
     * 必须和国际化VideoInsight.properties的key保持一致：Failed to modify the server parameters.
     */
    public static final String UPDATE_SERVICE_CONFIG_FAIL = "change.server.parameters.failure";

    /**
     * 必须和国际化VideoInsight.properties的key保持一致：Server parameters are modified successfully.
     */
    public static final String UPDATE_SERVICE_CONFIG_SUCCESS = "change.server.parameters.success";

    /**
     * 必须和国际化VideoInsight.properties的key保持一致：Failed to added the server.
     */
    public static final String ADD_SERVICE_CONFIG_FAIL = "add.server.parameters.failure";

    /**
     * 必须和国际化VideoInsight.properties的key保持一致：The server is added successfully.
     */
    public static final String ADD_SERVICE_CONFIG_SUCCESS = "add.server.parameters.success";

    /**
     * 必须和国际化VideoInsight.properties的key保持一致：Server IP address is duplicated.
     */
    public static final String SERVER_IP_ADDRESS_REPEAT = "server.parameters.ip.address.repeat";

    /**
     * 必须和国际化VideoInsight.properties的key保持一致：One IP supports up to two subtype servers.
     */
    public static final String SERVER_IP_ADDRESS_MORE = "server.parameters.ip.address.more";

    /**
     * 必须和国际化VideoInsight.properties的key保持一致：Image Diagnostic Server parameter configuration file error.
     */
    public static final String SERVER_CONFIG_FILE_ERROR = "server.parameters.config.file.error";

    /**
     * Windows ping的命令
     */
    public static final String PING_COMMAND_WINDOW = "ping -n 4 IP";

    /**
     * Linux ping的命令
     */
    public static final String PING_COMMAND_LINUX = "ping -c 4 IP";

    /**
     * ping的正则
     */
    public static final String PING_RESOULT_PATTERN = "([0-9.]+)%";

    /**
     * 10分钟对应的时间戳
     */
    public static final int TIMESTAMP_TEN = 1000 * 60 * 10;

    /**
     * 任务接收服务器
     */
    public static final int TASK_SERVER = NumConstant.NUM_1;

    /**
     * 诊断服务器
     */
    public static final int DIAGNOSTIC_SERVER = NumConstant.NUM_2;

    /**
     * 视频监控平台
     */
    public static final int VIDEO_PLATFORM_SERVER = NumConstant.NUM_3;

    /**
     * 显示标识 1显示，0不显示
     */
    public static final int SHOW_YES = NumConstant.NUM_1;

    /**
     * 显示标识 1显示，0不显示
     */
    public static final int SHOW_NOT = NumConstant.NUM_0;

    private ServiceCommonConst() {
    }
}
