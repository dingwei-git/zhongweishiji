package com.jovision.jaws.common.util;

import java.util.regex.Pattern;

/**
 * service层常量类
 *
 * @since 2019-11-08
 */
public final class ServiceCommonConst {
    /**
     * 周期任务标识
     */
    public static final String PERIODIC_TASK = StringNumConstant.STRING_NUM_0;

    /**
     * 即时任务标识
     */
    public static final String INSTANT_TASK = StringNumConstant.STRING_NUM_1;

    // 派单相关业务
    /**
     * 派单状态未确认
     */
    public static final String AUTO_ORDER_0 = StringNumConstant.STRING_NUM_0;

    /**
     * 派单状态自动派单
     */
    public static final String AUTO_ORDER_1 = StringNumConstant.STRING_NUM_1;

    /**
     * 派单状态手动派单
     */
    public static final String MANUAL_ORDER = StringNumConstant.STRING_NUM_2;

    /**
     * 派单状态为已忽略的状态
     */
    public static final String IGNORE = StringNumConstant.STRING_NUM_3;

    /**
     * 配置文件automaticOrderConfig.xml中的自动派单的key值
     */
    public static final String COFIG_AUTO_ORDER_KEY = StringNumConstant.STRING_NUM_2;

    /**
     * 配置文件automaticOrderConfig.xml中的状态是否被选中(1：被选中；0：未被选中)
     */
    public static final String IS_SELECTED = StringNumConstant.STRING_NUM_1;

    /**
     * 开启dlk服务化改造标识
     */
    public static final String SERVICE_MODE_FLAG = StringNumConstant.STRING_NUM_1;

    /**
     * dlk采集方式和dlk服务化改造方式采集共同拥有的故障类型标识
     */
    public static final String COMMON_TYPES = StringNumConstant.STRING_NUM_0;

    /**
     * dlk服务化改造方式采集所特有的故障类型标识
     */
    public static final String SERVICE_MODE_TYPES = StringNumConstant.STRING_NUM_2;

    /**
     * 东芝
     */
    public static final String FLAG_DONGZHI = "DongZhi";

    /**
     * 业务状态码：成功
     */
    public static final int CODE_SUCCESS = NumConstant.NUM_0;

    /**
     * 业务状态码：失败
     */
    public static final int CODE_FAILURE = NumConstant.NUM_1_NEGATIVE;

    /**
     * 业务状态码：内部错误
     */
    public static final int CODE_ERROR = NumConstant.NUM_500;

    /**
     * localhost
     */
    public static final String LOCALHOST = "localhost";

    /**
     * 是否是迪兰科
     */
    public static final String ISDLK = "isDlk";

    // 轮训诊断业务：自定义code码 100-199(接口状态码)
    /**
     * 轮训诊断/已经被删除
     */
    public static final int CODE_101 = NumConstant.NUM_101;

    /**
     * 轮训诊断/任务被其他人执行
     */
    public static final int CODE_102 = NumConstant.NUM_102;

    /**
     * 轮训诊断/服务器连接异常
     */
    public static final int CODE_103 = NumConstant.NUM_103;

    /**
     * 轮训诊断/分组摄像机信息为空
     */
    public static final int CODE_104 = NumConstant.NUM_104;

    /**
     * 轮训诊断/时间冲突
     */
    public static final int CODE_105 = NumConstant.NUM_105;

    /**
     * 轮训诊断/任务冲突
     */
    public static final int CODE_106 = NumConstant.NUM_106;

    /**
     * 故障未选中标志
     */
    public static final String FALSE = "false";

    /**
     * 故障选中标志
     */
    public static final String TRUE = "true";


    /**
     * 东芝服务标志常量定义
     */
    public static final String DZ_SERVICE_FLAG = "dzServiceFlag";

    /**
     * result
     */
    public static final String PRE_RESULT = "result";

    // FLAG 专区
    /**
     * tbl_login_record 用户是否被锁定
     */
    public static final String FLAG_USER_LOCK = StringNumConstant.STRING_NUM_1;

    /**
     * tbl_user 用户密码是否是默认密码标识
     */
    public static final int FLAG_USER_INIT_PAS = NumConstant.NUM_0;

    /**
     * 有导出权限的标识
     */
    public static final int FLAG_EXPORT_AUTHORITY_YES = NumConstant.NUM_1;

    /**
     * 无导出权限的标识
     */
    public static final int FLAG_EXPORT_AUTHORITY_NO = NumConstant.NUM_0;

    /**
     * 有管理子账户权限的标识
     */
    public static final int FLAG_MANAGE_AUTHORITY_YES = NumConstant.NUM_1;

    /**
     * 无管理子账户权限的标识
     */
    public static final int FLAG_MANAGE_AUTHORITY_NO = NumConstant.NUM_0;

    /**
     * 普通摄像机类型标识
     */
    public static final int FLAG_CAMERATYPE_VIDEO = NumConstant.NUM_1;

    /**
     * 卡口摄像机类型标识
     */
    public static final int FLAG_CAMERATYPE_PICTURE = NumConstant.NUM_2;

    /**
     * 探针数据源标识
     */
    public static final int FLAG_TZ_DATA_RESOURCE = NumConstant.NUM_1;

    /**
     * 只有dlk的数据源
     */
    public static final int FLAG_DLK_DATA_SOURCE = NumConstant.NUM_2;

    /**
     * 既有dlk的数据源又有探针的数据源的标识
     */
    public static final int FLAG_TZ_AND_DLK_DATA_SOURCE = NumConstant.NUM_3;

    // PAGE 专区

    /**
     * page 前后台传参的标识
     */
    public static final String PAGE = "page";

    /**
     * page页面：导入license页面
     */
    public static final String PAGE_LICESELEAD = "liceseLead";

    /**
     * page页面：视频性能监控页面
     */
    public static final String PAGE_PERFORMANCEMONITORING = "performanceMonitoring";

    /**
     * page页面：界面告警
     */
    public static final String PAGE_NOTICE = "notice";

    /**
     * page页面：分析页面
     */
    public static final String PAGE_ANALYSIS = "analysis";

    /**
     * page页面：导入组网关系表页面
     */
    public static final String PAGE_CAMERAMANAGE = "cameraManage";

    /**
     * page页面：服务器参数配置页面
     */
    public static final String PAGE_PARAMETERDEPLOY = "parameterDeploy";


    /**
     * status 开始或结束采集、分析
     */
    public static final String STATUS = "status";

    /**
     * 结束采集、分析
     */
    public static final String END = "end";

    /**
     * 开始采集、分析
     */
    public static final String START = "start";

    /**
     * 采集是否结束
     */
    public static final String IS_END = "isEnd";

    /**
     * 告警页面复检标识
     */
    public static final String REVIEW = "review";

    /**
     * 工单页面复检标识
     */
    public static final String ORDER_REVIEW = "orderReview";

    /**
     * 告警传参关键key
     */
    public static final String FAULT_INFO = "faultInfo";

    /**
     * 组网关系表层级的固定字段filed
     */
    public static final String FILED = "filed";

    /**
     * 数据集合
     */
    public static final String DATA_LIST = "dataList";

    /**
     * 任务id的key
     */
    public static final String TASK_ID = "taskId";

    /**
     * DZ任务状态key
     */
    public static final String STATE = "state";

    /**
     * dz任务删除标记
     */
    public static final String DELETE = "delete";

    /**
     * 摄像机编码
     */
    public static final String CAMERANUM = "cameraNum";


    /**
     * 文件的大小上线 10M
     */
    public static final long CSV_MAX_SIZE = 10 * 1024 * 1024L;

    /**
     * 文件后缀名 1、小写；2、可配置多个如：.xlsx$|.xls$
     */
    public static final Pattern SUFFIX_PATTERN = Pattern.compile(".xlsx$");

    /**
     * ping服务名称
     */
    public static final int PING_ID = 1210;

    /**
     * 一机一档权限配置的id
     */
    public static final int DOSSOER_ID = NumConstant.NUM_17;

    private ServiceCommonConst() {
    }
}
