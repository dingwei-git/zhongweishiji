package com.jovision.jaws.common.constant;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonConst {

    /**
     * 数据库配置的是mysql还是gaussdb
     */
    public static final String DATABASE_TYPE = "dataBaseType";

    /**
     * mybatis配置文件mysql连接的url
     */
    public static final String MYSQL_SITE_URL = "mysql.site.url";

    /**
     * mybatis配置文件gaussdb连接的url
     */
    public static final String GAUSSDB_SITE_URL = "gaussdb.site.url";

    /**
     * mybatis配置文件mysql连接的用户名
     */
    public static final String MYSQL_SITE_USERNAME = "mysql.site.username";

    /**
     * mybatis配置文件gaussdb连接的用户名
     */
    public static final String GAUSSDB_SITE_USERNAME = "gaussdb.site.username";

    /**
     * 网元纬度
     */
    public static final String INTELLIGENTTRACE = "intelligenttrace";

    /**
     * mybatis配置文件mysql连接的密码
     */
    public static final String MYSQL_SITE_PASSWORD = "mysql.site.secret";

    /**
     * mybatis配置文件gaussdb连接的密码
     */
    public static final String GAUSSDB_SITE_PASSWORD = "gaussdb.site.secret";

    /**
     * tomcat配置文件
     */
    public static final String HTTPS_TOMCAT_KEY = "sys.https_tomcat_key";

    /**
     * tomcat配置文件
     */
    public static final String HTTPS_TOMCAT_PLATPATH = "sys.platpath";

    /**
     * tomcat配置文件
     */
    public static final String HTTPS_TOMCAT_NAME = "sys.name";

    /**
     * 告警结果集
     */
    public static final String ALARMS = "alarms";

    /**
     * 告警天表表头
     */
    public static final String ALARM_TABLE_HEAD = "tbl_alarm_";

    /**
     * 智能诊断页面故障天表表名
     */
    public static final String TBL_FAULT_STATISTICAL_DAY = "TBL_FAULT_STATISTICAL_DAY_";

    /**
     * 前缀
     */
    public static final String PREFIX = "prefix";

    /**
     * mybatis的配置文件-分析-mysql
     */
    public static final String MYBATIS_MYSQL_FILE = "mybatis_configuration_mysql.xml";

    /**
     * mybatis的配置文件-采集-mysql
     */
    public static final String MYBATIS_MYSQL_COLLECTER_FILE = "mybatis_configuration_mysql_collecter.xml";

    /**
     * 项目WebContent 根目录
     */
   // public static final String DIR_BASE = System.getProperty("webAppRootPath_VideoInsight");
    public static final String DIR_BASE = "/";

    /**
     * 项目VideoInsightCollecter 根目录
     */
    public static final String DIR_BASE_C =
            FileUtils.getFile(DIR_BASE).getParent() + File.separator + "VideoInsightCollecter";

    /**
     * config目录
     */
    public static final String DIR_CONFIG = DIR_BASE + "config";

    /**
     * config目录 COLLECTER
     */
    public static final String COLLECTER_DIR_CONFIG = DIR_BASE_C + File.separator + "config";

    /**
     * 文件目录
     */
    public static final String DIR_DOCS = DIR_BASE + File.separator + "docsService";

    /**
     * 属性配置文件
     */
    public static final String FILE_PROTOCOLCONFIG = DIR_CONFIG + File.separator + "protocolConfig.xml";

    /**
     * tmp目录
     */
    public static final String DIR_TMP = DIR_BASE + File.separator + "tmp";

    /**
     * 备份文件 path对象
     */
    public static final Path DIR_BACKUP = Paths.get(DIR_TMP, "backupFiles");

    /**
     * 语言配置文件
     */
    public static final String LANGUANG_BASE_CONFIG = DIR_CONFIG + File.separator + "language.properties";

    /**
     * baseCofig.xml备份文件 path对象
     */
    public static final Path AUTOCONFIG_BACKUP = Paths.get(DIR_BACKUP.toString(), "baseCofig.xml");

    /**
     * 界面展示文件 path对象
     */
    public static final Path DIR_SHOW = Paths.get(DIR_TMP, "showFile");

    /**
     * baseCofig.xml备份文件 path对象
     */
    public static final Path AUTOCONFIG_SHOW = Paths.get(DIR_SHOW.toString(), "baseCofig.xml");

    /**
     * 文件目录
     */
    public static final String DIR_PREDOCS = DIR_TMP + File.separator + "preDocs";

    /**
     * RSAKey目录
     */
    public static final String DIR_RSAKEY = System.getProperty("user.dir") + File.separator + "user";

    /**
     * 保存生成的密钥对的文件名称
     */
    public static final String FILE_RSA_PAIR_TXT = DIR_RSAKEY + File.separator + "magic.dat";

    /**
     * jdbc属性配置文件，中文
     */
    public static final String FILE_JDBC_PROPERTIES = DIR_CONFIG + File.separator + "jdbc.properties";

    /**
     * jdbc属性配置文件，(COLLECTER)
     */
    public static final String COLLECTER_FILE_JDBC_PROPERTIES =
            COLLECTER_DIR_CONFIG + File.separator + "jdbc.properties";

    /**
     * jdbc属性配置文件
     */
    public static final String FILE_JDBC_COLLECTER_PROPERTIES =
            DIR_CONFIG + File.separator + "jdbc_collecter.properties";

    /**
     * jdbc属性配置文件
     */
    public static final String COLLECTER_FILE_JDBC_COLLECTER_PROPERTIES =
            COLLECTER_DIR_CONFIG + File.separator + "jdbc_collecter.properties";

    /**
     * tomcat配置文件
     */
    public static final String FILE_TOMCAT_PROPERTIES = DIR_CONFIG + File.separator + "tomcatSecret.properties";

    /**
     * dz故障字段配置
     */
    public static final String FIELD_FILE_DZ_CONFIG = "config/xml/dzFaultTypeConfig.xml";

    /**
     * dz诊断相关配置
     */
    public static final String FIELD_FILE_DZ_PROPERTY = "config/properties/dZDiagnosisConfig.properties";

    /**
     * 用户权限表初始化配置
     */
    public static final String FILE_PERMISSION = "config/xml/permission.xml";

    /**
     * 角色权限表初始化配置
     */
    public static final String FILE_ROLE = "config/xml/roles.xml";

    /**
     * 属性配置文件
     */
    public static final String FILE_FIELD_CONFIG = DIR_CONFIG + File.separator + "fieldConfig.xml";

    /**
     * iMOC推送属性配置文件
     */
    public static final String FILE_IMOC_CONFIG = DIR_CONFIG + File.separator + "promtionIMOC.xml";

    /**
     * iMOC推送可选字段配置
     */
    public static final String FIELD_FILE_IMOC_CONFIG = "config/xml/ImocFieldConfig.xml";

    /**
     * 摄像机类型配置
     */
    public static final String CAMERA_TYPE_CONFIG = "config/xml/cameraTypeConfig.xml";

    /**
     * 导出报告表头存放文件
     */
    public static final String REPORT_TITLE_FILE = "config/xml/reportTitles.xml";

    /**
     * db目录
     */
    public static final String FILE_INIT_DB = DIR_CONFIG + File.separator + "db";

    /**
     * db_init.properties属性配置文件
     */
    public static final String FILE_INIT_DB_PROPERTIES = FILE_INIT_DB + File.separator + "db_init.properties";

    /**
     * license 目录
     */
    public static final String FILE_LICENSE_DIR_PATH = DIR_CONFIG + File.separator + "license";

    /**
     * jdbc属性配置文件
     */
    public static final String FILE_NETWORKSHIP = DIR_DOCS + File.separator + "IPC组网关系表.xlsx";

    /**
     * 组网关系表导出
     */
    public static final String FILE_NETWORKSHIP_DOWNPATH = "docsService" + File.separator + "IPC组网关系表.xlsx";

    /**
     * 分析数据的log
     */
    public static final String FILE_ANALYSIS_DATA_LOG = DIR_TMP + File.separator + "analysisDatalog";

    /**
     * 推送数据的log文件夹
     */
    public static final String FLODER_PUSH_DATA_LOG = DIR_TMP + File.separator + "pushDatalog";

    /**
     * 推送数据的log文件
     */
    public static final String FILE_PUSH_DATA_LOG = FLODER_PUSH_DATA_LOG + File.separator + "pushDatalog_DateStr.txt";

    /**
     * 页面操作的独立日志
     */
    public static final String FILE_OPERATE_DATA_LOG = DIR_TMP + File.separator + "operateDatalog";

    /**
     * 存储操作日志与运行日志的目录
     */
    public static final String LOG_FILES = DIR_TMP + File.separator + "logFiles";

    /**
     * 增量ipc信息导出
     */
    public static final String FILE_IPC_ADDED = DIR_TMP + File.separator;

    /**
     * 增量ipc信息导出
     */
    public static final String FILE_IPC_ADDED_DOWNPATH = "tmp" + File.separator;

    /**
     * 组网关系表时间存储路径
     */
    public static final String FILE_TIME_TXT_READPATH = DIR_TMP + File.separator + "txt";

    /**
     * 网段配置表时间存储路径
     */
    public static final String FILE_CSV_TIME_TXT_READPATH = FILE_TIME_TXT_READPATH + File.separator + "csvtxt";

    /**
     * 工具名称
     */
    public static final String TOOL_NAME = "tool_name";

    /**
     * 系统初始用户名
     */
    public static final String SYSTEM_USER = "system_username";

    /**
     * 系统初始密码
     */
    public static final String SYSTEM_PASS = "system_password";

    /**
     * 星火对接初始密码
     */
    public static final String XINGHUO_PASS = "xinghuo_password";

    /**
     * 备份目录
     */
    public static final String ANALYSE_OVER_PATH = DIR_TMP + File.separator + "analyseOver";

    /**
     * admin用户编号
     */
    public static final String USER_TYPE_ADMIN = "000";

    /**
     * 分局用户编号
     */
    public static final String USER_TYPE_ORGANIZATION = "001";

    /**
     * 派出所用户编号
     */
    public static final String USER_TYPE_PLATFORM = "002";

    /**
     * 用户名
     */
    public static final String LOGIN_USER = "userName";

    /**
     * 管理员权限
     */
    public static final String ADMIN_NAME = "管理员";

    /**
     * 管理员英文
     */
    public static final String ADMIN_US_NAME = "admin";

    /**
     * 分页关键字
     */
    public static final String LIMIT = "limit";

    /**
     * 按天统计表 day 标识符
     */
    public static final String DAY_FLAG = "DAY";

    /**
     * 下划线
     */
    public static final String SYMBOL_UNDERLINE = "_";

    /**
     * 中划线
     */
    public static final String COMMON_MIDDLELINE = "-";

    /**
     * &&符号
     */
    public static final String SYMBOL_AND = "&&";

    /**
     * 配置文件分隔符逗号
     */
    public static final String SEPARATOR = ",";

    /**
     * 数据库表名:TCP原始数据表
     */
    public static final String TABLE_ORIGINAL_DATA = "tbl_original_data";

    /**
     * 数据库表名:趋势图
     */
    public static final String TABLE_TREND_GRAPH_DATA = "tbl_trend_graph_data";

    /**
     * 数据库表名:UDP原始数据表
     */
    public static final String TABLE_ORIGINAL_DATA_UDP = "tbl_original_data_udp";

    /**
     * 数据库表名:东智服务器数据表
     */
    public static final String TBL_DZ_ORIGINAL_DATA = "tbl_dz_original_data";

    /**
     * 数据库表名:T故障记录表
     */
    public static final String TABLE_FAULT_RECORD = "tbl_fault_record";

    /**
     * 数据库表名:TCP字段筛选表
     */
    public static final String TABLE_TCP_SESSION = "tbl_tcp_session";

    /**
     * 数据库表名:UDP字段筛选表
     */
    public static final String TABLE_UDP_SESSION = "tbl_udp_session";

    /**
     * 数据库表名:统计总表
     */
    public static final String TABLE_STATISTICS_ALL = "tbl_statistics_all";

    /**
     * 数据库表名:按分局统计表
     */
    public static final String TABLE_STATISTICS_ORGANIZATION = "tbl_statistics_organization";

    /**
     * 数据库表名:按派出所统计表
     */
    public static final String TABLE_STATISTICS_PLATFORM = "tbl_statistics_platform";

    /**
     * 数据库表名:按周期统计表
     */
    public static final String TABLE_STATISTICS_BY_STATION = "tbl_statistics_by_station";

    /**
     * 数据库表名:IPC管理表
     */
    public static final String TABLE_IPC_IP = "tbl_ipc_ip";

    /**
     * 数据库表名:组网关系表，母表
     */
    public static final String TABLE_NETWORK_RELATIONSHIP = "tbl_network_relationship";

    /**
     * 数据库表名:总的权限记录表
     */
    public static final String TABLE_ADMIN_NETWORK = "tbl_admin_network";

    /**
     * 数据库表名:报告表
     */
    public static final String TABLE_REPORT = "tbl_report";

    /**
     * 数据库表名：故障汇总
     */
    public static final String TBL_FAULT_STATISTICAL = "tbl_fault_statistical";

    /**
     * 数据库表名：报表模块，IPC详情统计
     */
    public static final String TBL_FAULT_STATISTICAL_FOR_RM = "tbl_fault_statistical_for_rm";

    /**
     * 数据库表名:星火报告表头
     */
    public static final String FILE_NAME_REPORT = "HW_VideoInsight_";

    /**
     * 故障类型编号:001
     */
    public static final String FAULTTYPE_OFFLINE_KEY = "001";

    /**
     * 故障类型编号:002
     */
    public static final String FAULTTYPE_LOST_KEY = "002";

    /**
     * 故障类型编号:003
     */
    public static final String FAULTTYPE_NORMAL_KEY = "004";

    /**
     * 故障类型编号:004
     */
    public static final String FAULTTYPE_POOR_KEY = "003";

    /**
     * 故障类型编号:005
     */
    public static final String FAULTTYPE_OK_KEY = "005";

    /**
     * 故障类型编号:006
     */
    public static final String FAULTTYPE_DEFINITION_KEY = "006";

    /**
     * 故障类型编号:007
     */
    public static final String FAULTTYPE_BRIGHTNESS_KEY = "007";

    /**
     * 故障类型编号:008
     */
    public static final String FAULTTYPE_SNOW_KEY = "008";

    /**
     * 故障类型编号:009
     */
    public static final String FAULTTYPE_STRIPE_KEY = "009";

    /**
     * 故障类型编号:010
     */
    public static final String FAULTTYPE_COLOUR_KEY = "010";

    /**
     * 故障类型编号:011
     */
    public static final String FAULTTYPE_FREEZE_KEY = "011";

    /**
     * 故障类型编号:012
     */
    public static final String FAULTTYPE_COVER_KEY = "012";

    /**
     * 故障类型编号:013
     */
    public static final String FAULTTYPE_SIGNAL_KEY = "013";

    /**
     * 故障类型编号:014
     */
    public static final String FAULTTYPE_SCENE_KEY = "014";

    /**
     * 故障类型编号:015
     */
    public static final String FAULTTYPE_SPEED_KEY = "015";

    /**
     * 故障类型编号:016
     */
    public static final String FAULTTYPE_NOPICTURE_KEY = "016";

    /**
     * 故障类型编号:017
     */
    public static final String FAULTTYPE_INDICIA_KEY = "017";

    /**
     * 故障类型编号：018
     */
    public static final String FAULTTYPE_ABNORMALIMGMOVE_KEY = "018";

    /**
     * 故障类型编号:019
     */
    public static final String FAULTTYPE_JITTER_KEY = "019";

    /**
     * 故障类型:视频质量一般名称
     */
    public static final String FAULTTYPE_NORMAL_CNNAME = "视频传输质量一般";

    /**
     * 故障类型:视频质量差名称
     */
    public static final String FAULTTYPE_POOR_CNNAME = "视频传输质量差";

    /**
     * 故障类型:正常的名称--zh
     */
    public static final String FAULTTYPE_INTACT_CNNAME = "正常";

    /**
     * 故障类型:正常的名称--en
     */
    public static final String FAULTTYPE_INTACT_ENNAME = "Normal";

    /**
     * 故障类型显示故障:无
     */
    public static final String FAULTADVICE_INTACT_CNNAME = "无";

    /**
     * 故障现象:无
     */
    public static final String FAULTAAPPEARANCE_INTACT_CNNAME = "无";

    /**
     * 协议类型：DZ
     */
    public static final String PROTOCOL_DZ = "DZ";

    /**
     * 协议类型：TCP
     */
    public static final String PROTOCOL_TCP = "TCP";

    /**
     * 协议类型：UDP
     */
    public static final String PROTOCOL_UDP = "UDP";

    /**
     * 协议类型：ALL
     */
    public static final String PROTOCOL_ALL = "ALL";

    /**
     * 配置常量:层级起始编码
     */
    public static final String STATION_STARTENCODING = "stationStartEncoding";

    /**
     * 输出给星火的目标目录
     */
    public static final String OUTPUT_PATH = "outputPath";

    /**
     * 周期时长，以秒为单位
     */
    public static final String PERIOD_TIME = "periodTime";

    /**
     * 24小时共有多少个周期
     */
    public static final String NUMBER_PERIOD = "numberOfPeriod";

    /**
     * 线程池维护的线程最大数量
     */
    public static final String MAXINUM_POOL_SIZE = "maxinumPoolSize";

    /**
     * SQL
     */
    public static final String SQL = "sql";

    /**
     * 线程池维护线程所允许的空闲时间
     */
    public static final String KEEP_ALIVE_TIME = "keepAliveTime";

    /**
     * 每次插入数据库的最大数量
     */
    public static final String NUM_OF_INSER = "numOfInsert";

    /**
     * 定时器的定时时间
     */
    public static final String TIME_OF_TIMER = "timeOfTimer";

    /**
     * 采集URL
     */
    public static final String COLLECTION_URL = "collectionUrl";

    /**
     * 554端口
     */
    public static final String TARGET_PORT554 = "targetPort554";

    /**
     * 8200端口
     */
    public static final String TARGET_PORT8200 = "targetPort8200";

    /**
     * 日志传给前台记录条数
     */
    public static final String FILE_LIST_RECORD = "fileListRecord";

    /**
     * 554端口默认网络应用
     */
    public static final String NETWORKAPPLICATION = "networkApplication";

    /**
     * udp数据流类型
     */
    public static final String FLOWTYPE = "flowType";

    /**
     * subtractionTime
     */
    public static final String SUBTRACTION_TIME = "subtractionTime";

    /**
     * 源数据字段配置:时间（KEY）
     */
    public static final String PERIOD_START_TIME = "PERIOD_START_TIME";

    /**
     * 目标IP地址
     */
    public static final String IP = "ip";

    /**
     * callid
     */
    public static final String CALLID = "call_id";

    /**
     * 流类型
     */
    public static final String FLOW_TYPE = "flow_type";

    /**
     * 目标端口
     */
    public static final String TARGET_PORT = "target_port";

    /**
     * 网络应用
     */
    public static final String NETWORK_APPLICATION = "network_application";

    /**
     * 服务器TCP重传率
     */
    public static final String RETRANSMISSION_RATE = "retransmission_rate";

    /**
     * 比特率 (tcp)码率(udp)
     */
    public static final String BIT_RATE = "bit_rate";

    /**
     * 持续时间
     */
    public static final String DURATION_TIME = "duration_time";

    /**
     * 开始时间
     */
    public static final String START_TIME = "start_time";

    /**
     * 结束时间
     */
    public static final String END_TIME = "end_time";

    /**
     * 故障设备名称
     */
    public static final String DEVICE_NAME = "device_name";

    /**
     * 故障设备所属机构
     */
    public static final String ORGANIZATION = "organization";

    /**
     * 故障设备所属平台
     */
    public static final String PLATFORM = "platform";

    /**
     * 平均丢包率
     */
    public static final String AVERAGE_PACKET_LOSS_RATE = "average_packet_loss_rate";

    /**
     * 平均视频MOS
     */
    public static final String AVERAGE_VIDEO_MOS = "average_video_mos";

    /**
     * faultDiagnosisRuleCofig.xml配置文件中的属性:faultType
     */
    public static final String XML_FAULTTYPE = "faultType";

    /**
     * faultDiagnosisRuleCofig.xml配置文件中的属性:faultName
     */
    public static final String XML_FAULTNAME = "faultName";

    /**
     * faultDiagnosisRuleCofig.xml配置文件中的属性:faultAppearance
     */
    public static final String XML_FAULTAPPEARANCE = "faultAppearance";

    /**
     * faultDiagnosisRuleCofig.xml配置文件中的属性:isPoleCamera
     */
    public static final String XML_HASPOLECAMERA = "isPoleCamera";

    /**
     * faultDiagnosisRuleCofig.xml配置文件中的属性:poleFaultFeature
     */
    public static final String XML_POLEFAULTFEATURE = "poleFaultFeature";

    /**
     * faultDiagnosisRuleCofig.xml配置文件中的属性:advice
     */
    public static final String XML_ADVICE = "advice";

    /**
     * IP正则
     */
    public static final String DEVICENAME_REG = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})"; // ip

    /**
     * 组网关系表导入标志:文件为空
     */
    public static final String IMPORT_NETWORK_NULL = "null";

    /**
     * 组网关系表导入标志:标题列校验失败
     */
    public static final String IMPORT_NETWORK_FIELD_ERROR = "fieldnull";

    /**
     * 组网关系表导入标志:标题列校验失败
     */
    public static final String IMPORT_NETWORK_PLATFORM_IP_ERROR = "platformIPError";

    /**
     * 组网关系表导入标志：层级错误
     */
    public static final String IMPORT_NETWORK_LEVEL_ERROR = "levelerr";

    /**
     * 组网关系表导入标志：层级变更
     */
    public static final String IMPORT_NETWORK_LEVEL_CHAGE = "levelchage";

    /**
     * 组网关系表导入标志:导入成功
     */
    public static final String IMPORT_NETWORK_SUCCESS = "success";

    /**
     * 组网关系表导入标志:校验组网关系表中的ipc数目是否满足license限制
     */
    public static final String IMPORT_NETWORK_IPC_NUMBER_WARNING = "ipcNumWaring";

    /**
     * 组网关系表导入标志:其他异常
     */
    public static final String IMPORT_NETWORK_ERROR = "error";

    /**
     * session键值
     */
    public static final String SESSION_KEY_NEWDATE = "newDate";

    /**
     * 连接状态
     */
    public static final String CONFIG_CONNECT = "connect";

    /**
     * 端口号
     */
    public static final String CONFIG_PORT = "port";

    /**
     * 持续时长
     */
    public static final String CONFIG_SESSIONTIME = "sessiontime";

    /**
     * 获取系统的名称的键
     */
    public static final String OS_NAME = "os.name";

    /**
     * linux系统名
     */
    public static final String LINUX = "linux";

    /**
     * 成功键
     */
    public static final String SUCCESS = "success";

    /**
     * 信息键
     */
    public static final String MESSAGE = "message";

    /**
     * 所属机构信息键
     */
    public static final String FILEMORECONFIG = "fileMoreConfig";

    /**
     * 所属机构信息是否匹配键
     */
    public static final String FILEMOREFLAG = "fileMoreFlag";

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 空
     */
    public static final String BLANK = "";

    /**
     * 部署方式-科来
     */
    public static final String DEPLOYMENT_KL = "1";

    /**
     * 部署方式-东智
     */
    public static final String DEPLOYMENT_DZ = "2";

    /**
     * 合一部署，字符串1,2
     */
    public static final String DEPLOYMENT_KL_AND_DZ = "1,2";

    /**
     * 一机一档部署
     */
    public static final String DEVICESTATUS_OPEN = "1";

    /**
     * http(s)请求协议
     */
    public static final String HTTP_REG = "^https?://.+";

    /**
     * 一个小时是多少秒
     */
    public static final int HOUR_TO_SECONDS = 3600;

    /**
     * 派单的最大容量
     */
    public static final int MAX_ORDER = 2000;

    /**
     * 初始容量
     */
    public static final int INIT_CAPACITY = 16;

    /**
     * 加载因子
     */
    public static final float LOAD_FACTOR = Float.parseFloat("0.75");

    /**
     * StinrgBuffer拼接不可信数据最大长度
     */
    public static final int STR_MAX_LENGTH = 40960;

    /**
     * ".txt"后缀
     */
    public static final String TXTSUFFIX = ".txt";

    /**
     * 拼接字符串最大长度
     */
    public static final int MAX_DATA = 256;

    /**
     * StringnBuffer初始化容量
     */
    public static final int EXPECTED_BUFFER_DATA = 2048;

    /**
     * 白名单校验正则输入字符串长度
     */
    public static final int MAX_STRING_TO_ARRAY = 128;


    /**
     * 多维监控初始化参数
     */
    public static final String INIT_CONFIG = "initConfig";

    /**
     * 多维监控当前参数
     */
    public static final String CURRENT_CONFIG = "currentConfig";

    /**
     * 多维监控最近完成周期参数
     */
    public static final String FINISHED_CONFIG = "finishedConfig";

    /**
     * 多维监控最近分析周期参数
     */
    public static final String ANALYZING_CONFIG = "analyzingConfig";

    /**
     * 标注检测开启的标识
     */
    public static final String INDICIADETECTION = "yes";

    /**
     * 标注非离线分析
     */
    public static final String NO_OFFLINE_ALYSIS = "no";

    /**
     * 字符串 table
     */
    public static final String TABLE = "table";

    /**
     * 字符串deploymentType
     */
    public static final String DEPLOYMENTTYPE = "deploymentType";

    /**
     * 数据呈现方式
     */
    public static final String PRESENTATION_TYPE = "presentationType";

    /**
     * 字符串choose
     */
    public static final String CHOOSE = "choose";

    /**
     * 字符串id
     */
    public static final String ID = "id";

    /**
     * 字符串label
     */
    public static final String LABEL = "label";

    /**
     * 字符串%.2f
     */
    public static final String NUMBER_FORMAT = "%.2f";

    /**
     * 字符串%.3f
     */
    public static final String FORMAT_THREE_BITS = "%.3f";

    /**
     * 字符串color
     */
    public static final String COLOR = "color";

    /**
     * 字符串DOWNPATH_24_MORE
     */
    public static final String DOWNPATH_24_MORE_KEY = "DOWNPATH_24_MORE";

    /**
     * 字符串FILEPATH_24_MORE
     */
    public static final String FILEPATH_24_MORE_KEY = "FILEPATH_24_MORE";

    /**
     * 字符串STATUS_TOP_1
     */
    public static final String STATUS_TOP_1_KEY = "STATUS_TOP_1";

    /**
     * 字符串STATUS_TOP_2
     */
    public static final String STATUS_TOP_2_KEY = "STATUS_TOP_2";

    /**
     * 字符串STATUS_TOP_3
     */
    public static final String STATUS_TOP_3_KEY = "STATUS_TOP_3";

    /**
     * 字符串COMMENT_BARCHAR_MORE
     */
    public static final String COMMENT_BARCHAR_MORE_KEY = "COMMENT_BARCHAR_MORE";

    /**
     * 字符串DOWNPATH_PEROID_MORE
     */
    public static final String DOWNPATH_PEROID_MORE_KEY = "DOWNPATH_PEROID_MORE";

    /**
     * 字符串FILEPATH_PEROID_MORE_KEY
     */
    public static final String FILEPATH_PEROID_MORE_KEY = "FILEPATH_PEROID_MORE";

    /**
     * \"
     */
    public static final String RIGHT_SLASH = "\"";

    /**
     * \"@\"
     */
    public static final String SPECIAL_SYMBOL = "\"@\"";

    /**
     * 字符串 24小时
     */
    public static final String HOUR_24 = "24小时";

    /**
     * 字符串 小时(中文)
     */
    public static final String HOUR_ZH = "小时";

    /**
     * 字符串 分钟(中文)
     */
    public static final String MINUTE_ZH = "分钟";

    /**
     * 字符串 小时(英文)
     */
    public static final String HOUR_EH = "hour";

    /**
     * 字符串 分钟(英文)
     */
    public static final String MINUTE_EH = "minute";

    /**
     * Gis地图开启的标识
     */
    public static final String GISKEY = StringNumConstant.STRING_NUM_1;

    /**
     * 100常量定义
     */
    public static final int NUM100 = 100;

    /**
     * indexof匹配判断标志
     */
    public static final int INDEX_FLAG = -1;

    /**
     * 自定义请求头
     */
    public static final String VIDEOINSIGHTCOLLECTERAUTHCODE = "VideoInsightCollecterAuthCode";

    /**
     * 不可逆加密算法盐值
     */
    public static final String ENCRYSALT = "EncryptorSalt";

    /**
     * 常用前后台标记字段开始时间startTime
     */
    public static final String STARTTIME = "startTime";

    /**
     * 常用前后台标记字段结束时间endTime
     */
    public static final String ENDTIME = "endTime";

    /**
     * 字段报告report
     */
    public static final String REPORT = "report";

    /**
     * 常用判断标识字段flag
     */
    public static final String FLAG = "flag";

    /**
     * 常用字段参数param
     */
    public static final String PARAM = "param";

    /**
     * 常用字段PID
     */
    public static final String PID = "pid";

    /**
     * 常用字段故障类型faultType
     */
    public static final String FAULT_TYPE = "faultType";

    /**
     * 常用字段故障率faultRate
     */
    public static final String FAULT_RATE = "faultRate";

    /**
     * 常用字段摄像机编码cameraCode
     */
    public static final String CAMERA_CODE = "cameraCode";

    /**
     * 常用字段id集合idList
     */
    public static final String ID_LIST = "idList";

    /**
     * 常用字段设备名称deviceName
     */
    public static final String DEVICENAME = "deviceName";

    /**
     * 摄像机类型ipType字段
     */
    public static final String IP_TYPE = "ipType";

    /**
     * 常用字段listTable
     */
    public static final String LIST_TABLE = "listTable";

    /**
     * 故障详情数量
     */
    public static final String FAULT_DETAIL_COUNT = "faultDetailCount";

    /**
     * 常用字段记录数recordCount
     */
    public static final String RECORD_COUNT = "recordCount";

    /**
     * 常用字段编码
     */
    public static final String CODE = "code";

    /**
     * 常用字段导出权限
     */
    public static final String EXPORT_AUTHORITY = "exportAuthority";

    /**
     * 不可逆加密算法迭代次数
     */
    public static final int ITERATIONS = 10000;

    /**
     * 数据库表名，组网关系表临时表
     */
    public static final String TBL_IPC_IP_TMP = "tbl_ipc_ip_tmp";

    /**
     * 数据校验结果字段
     */
    public static final String CHECK_RESULT = "checkResult";

    /**
     * 档案内容的集合
     */
    public static final String DOSSIER_CONTENTS = "dossierContents";

    /**
     * 档案操作类型
     */
    public static final String DOSSIER_OPERATION_TYPE = "create";

    /**
     * 工单故障图片
     */

    public static final String ORDER_FAULT_PICTURE = "faultPicture";

    /**
     * 工单复检图片
     */

    public static final String ORDER_RECHECK_PICTURE = "recheckPicture";

    /**
     * 工单现场图片
     */

    public static final String ORDER_ONSITE_PICTURE = "onsitePicture";

    /**
     * 工单图像存储路径
     */
    public static final String ORDER_PICTURE_PATH = DIR_BASE + "theme";

    /**
     * 设备管理同步设备新增
     */
    public static final String DEVICE_SYNCDATA_ADD = "add";

    /**
     * 设备管理同步设备更新
     */
    public static final String DEVICE_SYNCDATA_UPDATE = "update";


    /**
     * 设备管理同步设备新增
     */
    public static final String DEVICE_SYNCDATA_TASKID = "taskid";

    /**
     * 设备管理同步设备新增
     */
    public static final String DEVICE_SYNCDATA_LEVELID = "levelid";

    /**
     * 分页偏移量
     */
    public static final String DEVICE_SYNCDATA_PAGE_OFFSET = "offset";


    /**
     * 分页行数
     */
    public static final String DEVICE_SYNCDATA_PAGE_ROWS = "rows";

    public static final String DEVICE_SYNCDATA_CAMERAID = "cameraid";

    public static final String DEVICE_SYNCDATA_CURRENTLEVEL = "currentlevels";

    /**
     * 分析目录
     */
    public static final String ANALYSEPATH = DIR_TMP + File.separator + "analyse";

    /**
     * 备份目录
     */
    public static final String ANALYSEOVERPATH = DIR_TMP + File.separator + "analyseOver";

    /**
     * 属性配置文件
     */
    String FILE_BASE_CONFIG = DIR_CONFIG + File.separator + "baseCofig.xml";

    /**
     * 抓拍图片原始表名
     */
    public static final String IMAGE_TABLENAME = "tbl_image_day_statistics";

    /**
     * 原始文件目录
     */
    public static final String DIR_ORIGINALDATACSV = DIR_TMP + File.separator + "csvFile";

    /**
     * license.dat文件
     */
    String FILE_LICENSE_DAT = FILE_LICENSE_DIR_PATH + File.separator + "license.dat";

    /*
     * 数据库表名：Image_Day_Statistics_VCN
     * */
    public static final String Image_Day_Statistics_VCN = "tbl_image_day_statistics_vcn";

    /*
     *数据库表名：tbl_image_hour_statistics_vcn
     * */
    public static final String Image_Hour_Statistics_VCN = "tbl_image_hour_statistics_vcn";

    /*
     *数据库表名 tbl_server_param_config
     * */
    public static final String Server_Param_Config = "tbl_server_param_config";

    /*
    *数据库表名tbl_ipc_source
    * */
    public static final String Ipc_Source ="tbl_ipc_source";


    /**
     * 东智服务器参数配置入库标志
     */
    public static final String SQL_DZ_FLAG = "2";

    /**
     * 科来服务器参数配置入库标志
     */
    public static final String SQL_KL_FLAG = "1";

    /**
     * 非554端口持续时长阀值
     */
    String DURATION_TIME_THRESHOLD = "durationTimeThreshold";

    /**
     * 服务器地理位置正则（匹配xx区）
     */
    public static final String SERVER_IP_LOCATION_REG = "[\u4E00-\u9FA5A-Za-z0-9_]+@_(.*)";

    /**
     * 客户端端地理位置（匹配xx组）
     */
    public static final String CLIENT_IP_LOCATION_REG = "([\u4E00-\u9FA5A-Za-z0-9]+@)";

    /**
     * 初始空闲线程为10
     */
    public static final int COREPOOLSIZE = 10;

    /**
     * 最大线程数为50
     */
    public static final int MAXIMUMPOOLSIZE = 50;

    /**
     * 线程存活时间
     */
    public static final long KEEPALIVETIME = 60L;

    /**
     * 工作队列的大小
     */
    public static final int WORKQUEUESIZE = 3000;

    /**
     * 线程休眠时长
     */
    public static final Long KEEPSLEEPTIME = 100L;

    /**
     * 档案id（摄像机）
     */
    public static final String ARCHIVESID = "100001001";
    /**
     * 档案id（基础设施）
     */
    public static final String ARCHIVESID1 = "200001001";
    /**
     * 厂家id(摄像机)
     */
    public static final String MANUFACTURERID = "100002001";
    /**
     * 厂家id(基础设施)
     */
    public static final String MANUFACTURERID1 = "200002002";
    /**
     * 日期id(摄像机)
     */
    public static final String DATEID = "100012002";
    /**
     * 日期id(基础设施)
     */
    public static final String DATEID1 = "200008002";

    public static final String DBTYPE_GAUSS = "gauss";

    public static final String DBTYPE_MYSQL = "mysql";

    /**
     * SSL port
     */
    int PORT = 443;

    private CommonConst() {
    }
}
