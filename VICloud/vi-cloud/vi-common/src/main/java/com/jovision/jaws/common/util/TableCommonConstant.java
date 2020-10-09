/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

/**
 * 用于收录了数据表格中定义列名和实体之间的对应关系，便于替换，在代码中采用此常量形成键值对
 *
 * @version 1.0, 2018年6月19日
 * @since 2019-06-19
 */
public final class TableCommonConstant {
    /**
     * tbl_server_param_config 表--对应的实体字段名称
     */

    /**
     * tbl_server_param_config 表 id-实体id
     */
    public static final String TBL_SERVER_PARAM_CONFIG_ID = "id";

    /**
     * tbl_server_param_config 表 service_ip_address-实体serviceIpAddress
     */

    public static final String TBL_SERVER_PARAM_CONFIG_SERVICE_IP_ADDRESS = "serviceIpAddress";

    /**
     * tbl_server_param_config 表 service_name-实体serviceName
     */
    public static final String TBL_SERVER_PARAM_CONFIG_SERVICE_NAME = "serviceName";

    /**
     * tbl_server_param_config
     */
    public static final String TBL_SERVER_PARAM_CONFIG_SERVICE_SECRET = "servicePassword";

    /**
     * tbl_server_param_config 表service_link_id-实体serviceLinkId
     */
    public static final String TBL_SERVER_PARAM_CONFIG_SERVICE_LINK_ID = "serviceLinkId";

    /**
     * 小时维度统计表名
     */
    public static final String STATISTICS_TABLENAME = "tbl_image_hour_statistics";

    /**
     * tbl_server_param_config 表 service_data_type-实体serviceDataType
     */

    public static final String TBL_SERVER_PARAM_CONFIG_SERVICE_DATA_TYPE = "serviceDataType";

    /**
     * tbl_server_param_config 表 service_status-实体serviceStatus
     */

    public static final String TBL_SERVER_PARAM_CONFIG_SERVICE_STATUS = "serviceStatus";

    /**
     * tbl_server_param_config 表 id-实体id
     */
    public static final String TBL_SERVER_PARAM_CONFIG_USERNAMEID = "usernameid";

    /**
     * tbl_server_param_config 表 id-实体id
     */

    public static final String TBL_SERVER_PARAM_CONFIG_SERVICE_TIME = "serviceTime";
    /**
     * tbl_server_param_config 表 id-实体id
     */
    public static final String TBL_SERVER_PARAM_CONFIG = "tbl_server_param_config";

    /**
     * tbl_network_config表--对应的实体字段名称
     */

    /**
     * tbl_network_config表 location-实体 location
     */
    public static final String TBL_NETWORK_CONFIG_LOCATION = "location";

    /**
     * tbl_network_config表network_rules-实体 networkRules
     */

    public static final String TBL_NETWORK_CONFIG_NETWORK_RULES = "networkRules";

    /**
     * 数据库表名:摄像机详细信息表
     */
    public static final String TBL_CAMERA_MANAGER_ORIGINAL = "tbl_camera_manager_original";

    /**
     * tbl_image_day_statistics表 实体 tblImageDayStatistics
     */
    public static final String TABLE_IMAGE_DAY_STATISTICS = "tbl_image_day_statistics";

    /**
     * 小时维度统计表名
     */
    public static final String TBL_IMAGE_HOUR_STATISTICS = "tbl_image_hour_statistics";

    /**
     * limit 分页 页数
     */
    public static final String PAGEBEAN_PAGENUM = "pageNum";

    /**
     * limit 分页 单页记录数
     */
    public static final String PAGEBEAN_ROWSPERPAGE = "rowsPerPage";

    /**
     * 数据库表名:报告天表
     */
    public static final String TABLE_REPORT_DAY = "tbl_report_day";

    /**
     * 数据库表名:record天表
     */
    public static final String TABLE_RECORD_DAY = "tbl_fault_record_day";

    /**
     * 数据库表名:故障天表
     */
    public static final String TABLE_FAULT_STATISTICAL_DAY = "tbl_fault_statistical_day";

    /*
    * 数据库表名
    * */
    public static final String TBL_IPC_IP_TMP = "tbl_ipc_ip_tmp";

    /*
    * 数据库表名
    * */
    public static final String TBL_IPC_SOURCE = "tbl_ipc_source";


    /**
     * 数据库执行字段：30天时间格式
     */
    public static final String SQL_THIRTYDAYSDATEFORMATE = "thirtyDaysDateFormate";

    /**
     * 数据库执行字段：tabName
     */
    public static final String SQL_TABLENAM = "tabName";

    /**
     * 数据库执行字段：deletePeriodList
     */
    public static final String SQL_DELETEPERIODLIST = "deletePeriodList";

    /**
     * 数据库执行字段：周期开始时间
     */
    public static final String SQL_PERIODSTARTTIME = "periodStartTime";

    /**
     * 其他表内容补充...
     */

    /**
     * sql引用的键值，上级节点Id
     */
    public static final String SQL_PARENT_ID = "parentId";

    /**
     * sql引用的键值，faultRecordTable表名键值
     */
    public static final String FAULT_RECORD_TABLE = "faultRecordTable";

    /**
     * sql引用的键值，statisTable表名键值
     */
    public static final String STATIS_TABLE = "statisTable";

    /**
     * sql引用的键值，reportTable表名键值
     */
    public static final String REPORT_TABLE = "reportTable";

    /**
     * sql引用的键值，组网关系表中Id的代号
     */
    public static final String STATION_ID = "stationId";

    /**
     * sql引用的key值，周期管理表中时长的引用day
     */
    public static final String SQL_DAY = "day";

    /**
     * sql引用的key值，周期管理表中时长的引用hour
     */
    public static final String SQL_HOUR = "hour";

    /**
     * sql引用的key值，用户名
     */
    public static final String SQL_USERNAME = "userName";

    /**
     * sql引用的key值，要入参的数据集合
     */
    public static final String SQL_NRLIST = "nrList";

    /**
     * sql引用的key值，要入参的id编码长度
     */
    public static final String SQL_LENGTH = "length";

    /**
     * sql引用的key值，摄像机编码
     */
    public static final String SQL_CAMERA_CODE = "cameraCode";

    /**
     * sql字段，父id为空
     */
    public static final String PID_IS_NULL = "pidIsNull";

    /**
     * sql引用key值，数据分析的规则
     */
    public static final String SQL_RULE = "rule";

    /**
     * sql引用key值，摄像机id，cameraId
     */
    public static final String SQL_CAMERA_ID = "cameraId";

    /**
     * sql引用key值，DZ数据分析的规则调价
     */
    public static final String SQL_CONDITION_WHERE = "conditionWhere";

    /**
     * sql引用key值，tbl_statistics_all，organization，platform统计表的字段名，permissionName
     */
    public static final String SQL_PERMISSIONNAME = "permissionName";

    /**
     * sql传入的value值，代表tbl_statistics_organization表中的字段organization_ip_count
     */
    public static final String SQL_ORGANIZATION = "organization";

    /**
     * sql传入的key值，向tbl_statistics_all表中插入数据的key值tblStatisticsAll
     */
    public static final String SQL_TBLSTATISTICSALL = "tblStatisticsAll";

    /**
     * sql传入的key值，向tbl_fault_record表中插入数据的key值faults
     */
    public static final String SQL_FAULTS = "faults";

    /**
     * 正序
     */
    public static final String SQL_SORTING1 = "asc";

    /**
     * 倒序
     */
    public static final String SQL_SORTING2 = "desc";

    /**
     * 排序
     */
    public static final String SQL_SORT = "sort";

    /**
     * 截取前10条
     */
    public static final String SQL_LIMIT_10 = "limit 0,10";

    /**
     * 截取前200条
     */
    public static final String SQL_LIMIT_200 = "limit 0,200";

    /**
     * 构造函数
     */
    private TableCommonConstant() {
    }
}
