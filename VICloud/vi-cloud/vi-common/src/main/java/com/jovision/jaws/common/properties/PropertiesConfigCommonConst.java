/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.properties;

/**
 * 放置properties配置文件的常量类
 *
 * @since 2019-12-23
 */
public final class PropertiesConfigCommonConst {
    // db_init.properties
    /**
     * 工具名称
     */
    public static final String INIT_TOOL_NAME = "tool_name";

    /**
     * 系统用户名称
     */
    public static final String INIT_SYSTEM_USERNAME = "system_username";

    /**
     * 系统用户密码
     */
    public static final String INIT_SYSTEM_PASSWORD = "system_password";

    /**
     * 是否进行初始化口令的标志
     */
    public static final String INIT_INIT_SYSTEM_PASS_FLAG = "initSystemPassFlag";

    /**
     * 数据库类型
     */
    public static final String INIT_DATA_BASE_TYPE = "dataBaseType";

    /**
     * 语言类型
     */
    public static final String INIT_LANGUAGE_TYPE = "languageType";

    /**
     * 数据对接方式
     */
    public static final String INIT_DEPLOYMENTTYPE = "deploymentType";

    /**
     * 数据呈现方式
     */
    public static final String INIT_PRESENTATIONTYPE = "presentationType";

    /**
     * 运维管理界面
     */
    public static final String INIT_DEVICE_STATUS = "DeviceStatus";

    /**
     * 允许连续登陆失败次数控制标识
     */
    public static final String INIT_LOGIN_FAILURES = "loginFailures";

    /**
     * 登录失败时，锁定时长
     */
    public static final String INIT_LOCKTIME = "lockTime";

    /**
     * 数据老化控制标识
     */
    public static final String INIT_THIRTYDAYS_AGAO = "thirtydaysAgao";

    /**
     * log文件单个大小控制标识
     */
    public static final String INIT_LOG_FILE_SIZE = "logFileSize";

    /**
     * logFiles目录所包含的文件个数控制标识
     */
    public static final String INIT_CATALOGSIZE = "catalogSize";

    /**
     * gis地图是否开放控制标识
     */
    public static final String INIT_GISKEY = "gisKey";

    /**
     * 标注检测功能是否开启控制标识
     */
    public static final String INIT_INDICIA_DETECTION = "indiciaDetection";

    /**
     * IVS控制标识
     */
    public static final String INIT_SNAPMODE = "SnapMode";

    /**
     * 盐值
     */
    public static final String INIT_ENCRYPTORSALT = "EncryptorSalt";

    /**
     * dz诊断结果错误码
     */
    public static final String DZ_ERROR_CODE = "errorCode";

    /**
     * dz诊断结果错误码错误状态值
     */
    public static final String DZ_ERROR_CODE_VALUES = "dzErrorCodeValues";

    /**
     * dz诊断结果用作判断的状态字段
     */
    public static final String DZ_DECTION_STATUS = "dectionstatus";

    /**
     * 建立mq连接超时时间，单位毫秒
     */
    public static final String DZ_MQ_TIME_OUT = "dzMqTimeOut";

    /**
     * db_init.properties 轮询开关ServiceMode
     */
    public static final String INIT_SERVICEMODE = "ServiceMode";

    /**
     * db_init.properties 报表开关ReportSwitch
     */
    public static final String INIT_REPOTRSWITCH = "ReportSwitch";

    /**
     * db_init.properties 工单管理开关 WorkOrderSwitch
     */
    public static final String INIT_WORKORDERSWITCH = "WorkOrderSwitch";

    // monitorParamConfig.properties 图像多维监控模块配置
    /**
     * 抓拍在线率统计时长
     */
    public static final String MONITOR_SNAPSHOTS_ONLINE_RATE_TIME = "captureOnlineRateTime";

    /**
     * 全网抓拍告警使能开关
     */
    public static final String MONITOR_ALLNET_ALARM_ENABLE = "allNetCaptureAlarmEnable";

    /**
     * 全网无抓拍告警时间
     */
    public static final String MONITOR_NOSNAPSHOT_ALARM_TIME = "allNetCaptureAlarmTime";

    /**
     * 人脸抓拍上升门限值
     */
    public static final String MONITOR_FACE_SNAPSHOT_INCREASE = "faceCaptureIncreaseRate";

    /**
     * 人脸抓拍下降门限值
     */
    public static final String MONITOR_FACE_SNAPSHOT_DECREASE = "faceCaptureDropRate";

    /**
     * 车辆抓拍上升门限值
     */
    public static final String MONITOR_CAR_SNAPSHOT_INCREASE = "carCaptureIncreaseRate";

    /**
     * 车辆抓拍下降门限值
     */
    public static final String MONITOR_CAR_SNAPSHOT_DECREASE = "carCaptureDropRate";

    // tokenAuth.properties 微服务鉴权
    /**
     * TOKEN秘钥
     */
    public static final String AUTH_TOKEN_KEY = "TOKEN.KEY";

    /**
     * 应用名称
     */
    public static final String AUTH_APP_NAME = "APP.NAME";

    /**
     * token时效
     */
    public static final String AUTH_EFFECTIVE_TIME = "EFFECTIVETIME";

    /**
     * 受用者
     */
    public static final String AUTH_ISSUSER = "ISSUSER";

    /**
     * 使用者
     */
    public static final String AUTH_USER = "USER";

    /**
     * video insight密钥
     */
    public static final String AUTH_VIDEOINSIGHT_KEY = "VIDEOINSIGHT.KEY";

    /**
     * collector密钥
     */
    public static final String AUTH_COLLECTER_KEY = "COLLECTER.KEY";

    // config.properties 基础配置key值
    /**
     * 分局起始编码
     */
    public static final String CONFIG_STATION_START_CODE = "stationStartEncoding";

    /**
     * 分析后输出给星火的目标目录
     */
    public static final String CONFIG_OUTPUT_PATH = "outputPath";

    /**
     * 周期时长
     */
    public static final String CONFIG_PERIOD_TIME = "periodTime";

    /**
     * 24小时共有周期
     */
    public static final String CONFIG_NUMBER_OF_PERIOD = "numberOfPeriod";

    /**
     * 累加时间减去的时间
     */
    public static final String CONFIG_SUBTRACTION_TIME = "subtractionTime";

    /**
     * 核心线程数
     */
    public static final String CONFIG_COREPOOL_SIZE = "corePoolSize";

    /**
     * 线程池维护的线程最大数量
     */
    public static final String CONFIG_MAX_POOL_SIZE = "maxinumPoolSize";

    /**
     * 线程池维护线程所允许的空闲时间,秒
     */
    public static final String CONFIG_KEEP_ALIVE_TIME = "keepAliveTime";

    /**
     * 插入数据库的最大数量
     */
    public static final String CONFIG_MAX_INSERT_NUM = "numOfInsert";

    /**
     * 文件分析定时扫描时间,毫秒
     */
    public static final String CONFIG_FILES_ANALYSIS_PERIOD = "timeOfTimer";

    /**
     * 554端口
     */
    public static final String CONFIG_PORT554 = "targetPort554";

    /**
     * 8200端口
     */
    public static final String CONFIG_PORT8200 = "targetPort8200";

    /**
     * 分析界面，后台记录的最近记录的条数
     */
    public static final String CONFIG_FILE_LIST_RECORD = "fileListRecord";

    /**
     * 网络应用，554端口默认网络应用
     */
    public static final String CONFIG_NETWORK_APPLICATION = "networkApplication";

    /**
     * udp数据流类型筛选规则
     */
    public static final String CONFIG_FLOW_TYPE = "flowType";

    /**
     * 采集url
     */
    public static final String CONFIG_COLLECTION_URL = "collectionUrl";

    /**
     * 是否为离线分析(yes代表分析的是离线数据,no代表分析的是非离线数据)
     */
    public static final String CONFIG_OFFLINE_ALYSIS = "offlineAnalysis";

    /**
     * iMOC接口ip
     */
    public static final String CONFIG_IMOC_IP = "iMOCip";

    /**
     * iMOC端口
     */
    public static final String CONFIG_IMOC_PORT = "iMOCPort";

    /**
     * iMOC接口推送url
     */
    public static final String CONFIG_PROMTIONALARM_URL = "promtionAlarmUrl";

    /**
     * 性能数据接口推送url
     */
    public static final String CONFIG_PROMTION_PROPERTY_URL = "promtionPropertyUrl";

    /**
     * 证书距离有效期到期提醒（单位：天）
     */
    public static final String CONFIG_VALIDITEPERIOD_DAY = "ValiditePeriodDay";

    /**
     * iMOC鉴权接口
     */
    public static final String CONFIG_AUTHENTICATION = "authentication";

    /**
     * License到期提醒最小天数
     */
    public static final String CONFIG_REMAIN_MIN_DAYS = "remainDayMin";

    /**
     * License到期提醒剩余最大天数
     */
    public static final String CONFIG_REMAIN_MAX_DAYS = "remainDayMax";

    /**
     * videoinsight.i18n.properties国际化中的 UserManageController.The.Pass.Can.Not.Be.Empty属性(传入参数不为空)
     */
    public static final String VI_PARAM_NOT_BE_EMPTY = "UserManageController.The.Pass.Can.Not.Be.Empty";

    /**
     * videoinsight.i18n.properties国际化中的 the.incoming.parameters.are.incorrect属性(传入参数不正确)
     */
    public static final String I18N_PARAM_ARE_INCORRECT = "the.incoming.parameters.are.incorrect";

    /**
     * videoinsight.i18n.properties国际化中的 remoteAddr.pattern.error属性(IP格式错误)
     */
    public static final String I18N_IP_PATTERN_ERROR = "remoteAddr.pattern.error";

    /**
     * videoinsight.i18n.properties国际化中的noExportAuthority(暂无导出权限)
     */
    public static final String I18N_NO_EXPORTAUTHORITY = "noExportAuthority";

    /**
     * videoinsight.i18n.properties国际化中的exportFileFailed(生成文件失败)
     */
    public static final String I18N_EXPORT_FILEFAILED = "exportFileFailed";

    /**
     * videoinsight.i18n.properties国际化中的noData(无数据)
     */
    public static final String I18N_NODATA = "noData";

    /**
     * videoinsight.i18n.properties国际化中的FaultAnalyseServiceImpl.reportDzAnalyse.timeout(超时)
     */
    public static final String I18N_TIMEOUT = "FaultAnalyseServiceImpl.reportDzAnalyse.timeout";

    /**
     * videoinsight.i18n.properties国际化中的abnormal.video.quality.count.导出报告中视频质量异常数
     */
    public static final String I18N_ABNORMAL_VIDEO_QUALITY_COUNT = "abnormal.video.quality.count";

    /**
     * videoinsight.i18n.properties国际化中IpcReportManagementController.topN.message(TopN连续离线统计提示)
     */
    public static final String I18N_TOPN_TITILE_MESSAGE = "IpcReportManagementController.topN.message";

    /**
     * videoinsight.i18n.properties国际化中IpcReportManagementController.topN.notShowMessage(TopN页面无数据提示)
     */
    public static final String I18N_TOPN_NOTSHOW_MESSAGE = "IpcReportManagementController.topN.notShowMessage";

    /**
     * videoinsight.i18n.properties国际化中IpcReportManagementController.topN.noResultMessage(ipc详情报表查询无数据提示)
     */
    public static final String I18N_REPORT_NOTSHOW_MESSAGE = "IpcReportManagementController.topN.noResultMessage";

    /**
     * 从配置文件中获取,是否属于外场管理员场景,1：是外场管理员场景；0：没有外场管理员
     */
    public static final String IS_HAVE_FIELD_ORGANIZATION_MANAGER = "isHaveFieldOrganizationManager";

    private PropertiesConfigCommonConst() {
    }
}
