/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;
import com.huawei.utils.CommonSymbolicConstant;
import com.huawei.utils.NumConstant;
import com.huawei.vi.entity.vo.ProgressInfo;
import com.jovision.jaws.common.constant.CommonConst;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 报告工具类
 *
 * @version 1.0, 2018年6月20日
 * @since 2019-08-27
 */
public final class ReportConstant {
    /**
     * 空格
     */
    public static final String SPACE = " ";

    /**
     * sqsl关键字where
     */
    public static final String WHERE = "where";

    /**
     * 属性faultType
     */
    public static final String FAULTTYPE = "table1.fault_type";

    /**
     * 故障占比率
     */
    public static final String FAULT_RATE = "fault_rate";

    /**
     * 故障现象
     */
    public static final String FAULT_APPEARANCE = "fault_appearance";

    /**
     * 故障设备名称
     */
    public static final String DEVICE_NAME = "device_name";

    /**
     * 组织层级
     */
    public static final String IPCLEVEL = "ipcLevel";

    /**
     * 派单状态
     */
    public static final String ORDERSTATE = "orderstate";

    /**
     * 诊断输入源
     */
    public static final String CAMERATYPE = "camera_type";

    /**
     * ip
     */
    public static final String IP = "table1.ip";

    /**
     * camera_code
     */
    public static final String CAMERACODE = "camera_code";

    /**
     * 左括号 (
     */
    public static final String RIGHTBRACKETS = "(";

    /**
     * 右括号 )
     */
    public static final String LEFTBRACKETS = ")";

    /**
     * 故障所属机构
     */
    public static final String ORGANIZATION = "organization";

    /**
     * 故障设备所属平台
     */
    public static final String PLATFORM = "platform";

    /**
     * 排查建议
     */
    public static final String ADVICE = "advice";

    /**
     * 等号
     */
    public static final String EQUALSIAG = "=";

    /**
     * in
     */
    public static final String IN = "in";

    /**
     * 排序
     */
    public static final String OEDER = "order by start_time desc";

    /**
     * >=
     */
    public static final String MORETHANEQUALSIAG = ">=";

    /**
     * !=
     */
    public static final String NOEQUALSIAG = "!=";

    /**
     * like
     */
    public static final String LIKESIAG = "like";

    /**
     * %
     */
    public static final String LIKESIAGTYPE = "%";

    /**
     * 查看数据模板tbl_report_
     */
    public static final String SQLMODEL = " select table1.*,table2.orderState from ";

    /**
     * 关联ipc
     */
    public static final String SQLJOIN = " table1 join tbl_order_record table2 ";

    /**
     * 关联条件
     */
    public static final String SQLJOINTERM = " AND table1.ip = table2.ip AND table1.fault_type = table2.fault_type ";

    /**
     * videoinsight
     */
    public static final String DATABASENAME = "videoinsight";

    /**
     * 报告表前缀
     */
    public static final String REPORTNAME = "tbl_report_";

    /**
     * HW_VideoInsight_
     */
    public static final String PLATFORMSUBBFIX = "HW_VideoInsight_";

    /**
     * 整数3000
     */
    public static final int INT_NUMTHREETHOUSAND = 3000;

    /**
     * 0
     */
    public static final int INT_NUMZERO = 0;

    /**
     * 1
     */
    public static final int INT_NUMONE = 1;

    /**
     * 2
     */
    public static final int INT_NUMTWO = 2;

    /**
     * 3
     */
    public static final int INT_NUMTHREE = 3;

    /**
     * 4
     */
    public static final int INT_NUMFOUR = 4;

    /**
     * 8
     */
    public static final int INT_NUMEIGHT = 8;

    /**
     * 11
     */
    public static final int INT_NUMELEVEN = 11;

    /**
     * 14
     */
    public static final int INT_NUMFOURTHEEN = 14;

    /**
     * 16
     */
    public static final int INT_NUMSIXTHEEN = 16;

    /**
     * 19
     */
    public static final int INT_NUMNINETHEN = 19;

    /**
     * 24
     */
    public static final int INT_NUMNTWENTYFOUR = 24;

    /**
     * Excel容量
     */
    public static final int EXCELSIZE = 600000;

    /**
     * and
     */
    public static final String AND = "and";

    /**
     * \
     */
    public static final String QUOTATIONMARK = "\'";

    /**
     * union all
     */
    public static final String UNION = "union all";

    /**
     * 分页 LIMIT 0, 3000
     */
    public static final String LIMIT = " LIMIT 0, 3000";

    /**
     * 逗号
     */
    public static final String SPILT = ",";

    /**
     * :
     */
    public static final String COLON = ":";

    /**
     * .
     */
    public static final String POINT = ".";

    /**
     * history_
     */
    public static final String HISTORY = "history_";

    /**
     * .csv
     */
    public static final String SUFFIX = ".csv";

    /**
     * .xml
     */
    public static final String XML = ".xml";

    /**
     * .xlsm
     */
    public static final String XLSM = ".xlsm";

    /**
     * statisticsReport
     */
    public static final String STATISTICS_REPORT = "statisticsReport";

    /**
     * .zip
     */
    public static final String SUFFIXZIP = ".zip";

    /**
     * DIR_BASE
     */
    public static final String DIR_BASE = System.getProperty("webAppRootPath_VideoInsight");

    /**
     * DIR_I18N
     */
    public static final String DIR_I18N = Paths.get(DIR_BASE, "i18n").toString();

    /**
     * 文件夹目录tmp
     */
    public static final String TMP = "tmp";

    /**
     * 英文国际化资源文件
     */
    public static final String DIR_I18N_EN_PROPERTIES =
        Paths.get(DIR_I18N, CommonConstant.LANGUAGE_EN_US, "VideoInsight.properties").toString();

    /**
     * 中文国际化资源文件
     */
    public static final String DIR_I18N_ZH_PROPERTIES =
        Paths.get(DIR_I18N, CommonConstant.LANGUAGE_ZH_CN, "VideoInsight.properties").toString();

    /**
     * DB资源文件，里面存放工具语言配置参数
     */
    public static final String DIR_CONFIG_DB_PROPERTIES =
        Paths.get(DIR_BASE, "config", "db", "db_init.properties").toString();

    /**
     * DIR_EXPORT
     */
    public static final String DIR_EXPORT = DIR_BASE + File.separator + "exportReport";

    /**
     * DIR_ALARM_EXPORT_REPORT
     */
    public static final String DIR_ALARM_EXPORT_REPORT = DIR_BASE + File.separator + "exportAlarmReport";

    /**
     * DIR_VIDEO_QUALITY_EXPORT_REPORT
     */
    public static final String DIR_VIDEO_QUALITY_EXPORT_REPORT =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "videoQualityReport").toString();

    /**
     * 报表管理模块，top离线报表生成路径
     */
    public static final String DIR_TOP_OFFLINE_EXPORT_REPORT =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "topOfflineReport").toString();

    /**
     * 报表管理模块，top视频质量报表生成路径
     */
    public static final String DIR_TOP_VIDEO_QUALITY_EXPORT_REPORT =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "topVideoQualityReport").toString();

    /**
     * DIR_OFFLINE_EXPORT_REPORT
     */
    public static final String DIR_OFFLINE_EXPORT_REPORT =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "offlineReport").toString();

    /**
     * DIR_VIDEO_COMPLETE_EXPORT_REPORT
     */
    public static final String DIR_VIDEO_COMPLETE_EXPORT_REPORT =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "videoCompleteReport").toString();

    /**
     * DIR_PERFORMANCE_TREND_EXPORT_REPORT
     */
    public static final String DIR_PERFORMANCE_TREND_EXPORT_REPORT =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "performanceTrend").toString();

    /**
     * DIR_EXPORT
     */
    public static final String DIR_EXPORT_STATISC = DIR_BASE + File.separator + "exportStatisc";

    /**
     * DIR_TEMPLATE
     */
    public static final String DIR_TEMPLATE = Paths.get(DIR_BASE, "template").toString();

    /**
     * ZIP_FOLDER
     */
    public static final String ZIP_FOLDER = "zipFolder";

    /**
     * 给星火文件生成报告的路径
     */
    public static final String PLATFORMREPORT = Paths.get(DIR_BASE, TMP, "output").toString();

    /**
     * 给星火文件的历史文件记录
     */
    public static final String PLATFORMHISTORY = Paths.get(DIR_BASE, TMP, "historyReport").toString();

    /**
     * zipFolder
     */
    public static final String DOWNLOAD_ZIP_PATH = Paths.get(DIR_BASE, "exportReport", ZIP_FOLDER).toString();

    /**
     * zipFolder
     */
    public static final String DOWNLOAD_ZIP_ALARM_PATH =
        Paths.get(DIR_BASE, "exportAlarmReport", ZIP_FOLDER).toString();

    /**
     * zipFolder
     */
    public static final String DOWNLOAD_ZIP_VIDEO_QUALITY_PATH =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "videoQualityReport", ZIP_FOLDER).toString();

    /**
     * 报表管理模块，top视频质量报表压缩包路径
     */
    public static final String DOWNLOAD_ZIP_TOP_VIDEO_QUALITY_PATH =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "topVideoQualityReport", ZIP_FOLDER).toString();

    /**
     * zipFolder
     */
    public static final String DOWNLOAD_ZIP_OFFLINE_PATH =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "offlineReport", ZIP_FOLDER).toString();

    /**
     * 报表管理模块，top离线故障报表压缩包路径
     */
    public static final String DOWNLOAD_TOP_ZIP_OFFLINE_PATH =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "topOfflineReport", ZIP_FOLDER).toString();

    /**
     * zipFolder
     */
    public static final String DOWNLOAD_ZIP_VIDEO_COMPLETE_PATH =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "videoCompleteReport", ZIP_FOLDER).toString();

    /**
     * 报表管理模块，top录像完整性报表压缩包路径
     */
    public static final String DOWNLOAD_ZIP_TOP_VIDEO_COMPLETE_PATH =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "topVideoCompleteReport", ZIP_FOLDER).toString();

    /**
     * 报表管理模块，top录像完整性报表生成路径
     */
    public static final String DIR_TOP_VIDEO_COMPLETE_EXPORT_REPORT =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "topVideoCompleteReport").toString();

    /**
     * zipFolder
     */
    public static final String DOWNLOAD_ZIP_STATISC_PATH = Paths.get(DIR_BASE, "exportStatisc", ZIP_FOLDER).toString();

    /**
     * 性能趋势报表
     */
    public static final String DOWNLOAD_ZIP_PERFORMANCETREND_PATH =
        Paths.get(DIR_BASE, STATISTICS_REPORT, "performanceTrend", ZIP_FOLDER).toString();

    /**
     * tmp目录
     */
    public static final String DIR_TMP = DIR_BASE + TMP;

    /**
     * output目录
     */
    public static final String DIR_OUTPUT = DIR_TMP + File.separator + "output";

    /**
     * csvupload文件上傳路徑
     */
    public static final String DIR_OUT_CSVUPLOAD = DIR_TMP + File.separator + "csvupload";

    /**
     * DIR_LOG_EXPORT_REPORT
     */
    public static final String DIR_LOG_EXPORT_REPORT = DIR_BASE + File.separator + "exportLogReport";

    /**
     * 工单报告名称
     */
    public static final String ORDER_EXPORT_NAME = "workOrderReport";

    /**
     * 工单报告路径
     */
    public static final String DIR_ORDER_EXPORT_REPORT = DIR_BASE + ORDER_EXPORT_NAME;

    private static boolean isInitFlag = false;

    private static List<String> tableHeadDataStatistics = new ArrayList<String>();

    /**
     * CSV导出文件表头数据，中文
     */
    private static List<String> tableHeadDatas = new ArrayList<String>();

    private static List<String> tableHeadDataDongzhis = new ArrayList<String>();

    /**
     * 告警详情表头数据
     */
    private static List<String> tableHeadAlarmDetails = new ArrayList<String>();

    /**
     * 构造函数
     */
    private ReportConstant() {
    }

    /**
     * 初始化导出文件信息
     */
    public static void initTbleName() {
        tableHeadDatas.clear();
        tableHeadDataDongzhis.clear();
        tableHeadDataStatistics.clear();
        tableHeadAlarmDetails.clear();

        String tableHeadData = BaseConfig.getFileMoreConfig().getTableHeadData();
        String[] tableHeadDataPres = tableHeadData.split(CommonSymbolicConstant.SYMBOL_SHIFT_3)[NumConstant.NUM_0]
            .split(CommonSymbolicConstant.SYMBOL_SHIFT_2);
        String[] tableHeadDataEnds = tableHeadData.split(CommonSymbolicConstant.SYMBOL_SHIFT_3)[NumConstant.NUM_1]
            .split(CommonSymbolicConstant.SYMBOL_SHIFT_2);
        tableHeadDatas = new ArrayList<String>(Arrays.asList(tableHeadDataPres));
        addThreeAndFourPlatformName(tableHeadDatas);
        tableHeadDatas.addAll(new ArrayList<String>(Arrays.asList(tableHeadDataEnds)));

        // 告警详情表头
        initFaultDetailsHead();

        // ++++++++++++++++++++有东智数据++++++++++++++++++++
        String tableHeadDataDongzhi = BaseConfig.getFileMoreConfig().getTableHeadDataDongzhi();
        String[] tableHeadDataDongzhiPres =
            tableHeadDataDongzhi.split(CommonSymbolicConstant.SYMBOL_SHIFT_3)[NumConstant.NUM_0]
                .split(CommonSymbolicConstant.SYMBOL_SHIFT_2);
        String[] tableHeadDataDongzhiEnds =
            tableHeadDataDongzhi.split(CommonSymbolicConstant.SYMBOL_SHIFT_3)[NumConstant.NUM_1]
                .split(CommonSymbolicConstant.SYMBOL_SHIFT_2);
        tableHeadDataDongzhis = new ArrayList<String>(Arrays.asList(tableHeadDataDongzhiPres));
        addThreeAndFourPlatformName(tableHeadDataDongzhis);
        tableHeadDataDongzhis.addAll(new ArrayList<String>(Arrays.asList(tableHeadDataDongzhiEnds)));

        String exportReportHead = BaseConfig.getFileMoreConfig().getExportReportHead();
        String[] headPres = exportReportHead.split(CommonSymbolicConstant.SYMBOL_SHIFT_3)[NumConstant.NUM_0]
            .split(CommonSymbolicConstant.SYMBOL_SHIFT_2);
        String[] headEnds = exportReportHead.split(CommonSymbolicConstant.SYMBOL_SHIFT_3)[NumConstant.NUM_1]
            .split(CommonSymbolicConstant.SYMBOL_SHIFT_2);
        tableHeadDataStatistics = new ArrayList<String>(Arrays.asList(headPres));
        addThreeAndFourPlatformName(tableHeadDataStatistics);
        tableHeadDataStatistics.addAll(new ArrayList<String>(Arrays.asList(headEnds)));
    }

    /**
     * 初始化故障详情表头
     */
    private static void initFaultDetailsHead() {
        String tableHeadAlarmDetail = BaseConfig.getFileMoreConfig().getTableHeadFaultDetails();
        String[] tableHeadAlarmDetailPres =
            tableHeadAlarmDetail.split(CommonSymbolicConstant.SYMBOL_SHIFT_3)[NumConstant.NUM_0]
                .split(CommonSymbolicConstant.SYMBOL_SHIFT_2);
        String[] tableHeadAlarmDetailEnds =
            tableHeadAlarmDetail.split(CommonSymbolicConstant.SYMBOL_SHIFT_3)[NumConstant.NUM_1]
                .split(CommonSymbolicConstant.SYMBOL_SHIFT_2);
        tableHeadAlarmDetails = new ArrayList<String>(Arrays.asList(tableHeadAlarmDetailPres));
        addlevelNames(tableHeadAlarmDetails);
        tableHeadAlarmDetails.addAll(new ArrayList<String>(Arrays.asList(tableHeadAlarmDetailEnds)));
    }

    /**
     * 返回表头数据此集合
     *
     * @return List
     */
    public static List<String> getTableHeadList() {
        // 初始化表头数据
        if (!isInitFlag) {
            initTbleName();
            isInitFlag = true;
        }
        if (GlobalConfigProperty.getGlobalMap().get(CommonConst.DEPLOYMENTTYPE).contains(CommonConst.DEPLOYMENT_DZ)) {
            return tableHeadDataDongzhis;
        } else {
            return tableHeadDatas;
        }
    }

    /**
     * 返回故障详情的表头
     *
     * @return List
     */
    public static List<String> getFaultDetailsHeadList() {
        // 初始化表头数据
        if (!isInitFlag) {
            initTbleName();
            isInitFlag = true;
        }
        return tableHeadAlarmDetails;
    }

    /**
     * 返回表头数据此集合
     *
     * @return List
     */
    public static List<String> getStatisticsTableHeadList() {
        // 初始化表头数据
        if (!isInitFlag) {
            initTbleName();
            isInitFlag = true;
        }
        return tableHeadDataStatistics;
    }

    /**
     * 根据组网表层级添加表头
     *
     * @param list 表头
     */
    public static void addlevelNames(List<String> list) {
        int level = ProgressInfo.getInstance().getTmpLevel(); // getTmpLevel()一导入组网表就能获取到组网层级
        String[] names = ProgressInfo.getInstance().getTitleFields();
        for (int ii = 0; ii < level; ii++) {
            list.add(names[ii]);
        }
    }

    /**
     * 加三四级平台机构名称
     *
     * @param list 表头
     */
    public static void addThreeAndFourPlatformName(List<String> list) {
        int level = ProgressInfo.getInstance().getTmpLevel(); // getTmpLevel()一导入组网表就能获取到组网层级
        String[] names = ProgressInfo.getInstance().getTitleFields();
        list.add(names[0]); // 一级名称
        list.add(names[INT_NUMONE]); // 二级名称
        if (level >= INT_NUMFOUR) {
            list.add(names[INT_NUMTWO]); // 三级名称
            list.add(names[INT_NUMTHREE]); // 四级名称
        }
    }

    public static List<String> getTableHeadDataDongzhis() {
        // 初始化表头数据
        if (!isInitFlag) {
            initTbleName();
            isInitFlag = true;
        }
        return tableHeadDataDongzhis;
    }
}
