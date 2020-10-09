package com.jovision.jaws.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 平台错误码表
 * @Author: ABug
 * @Date: 2019/12/24 10:19
 * @Version V1.0.0
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BusinessErrorEnum {
    /**
     * 400=非法请求
     */
    BASE_REQUEST_ILLEGAL(400,"illegal request"),
    /**
     * 401=表示鉴权失败；响应无消息体
     */
    BASE_REQUEST_UNAUTHORIZED(401,"Unauthorized"),
    /**
     * 403=表示禁止访问；客户端没有具备对应的操作权限、或者限制某些IP、未使用SSL等；响应无消息体
     */
    BASE_REQUEST_FORBIDDEN(403,"Forbidden"),
    /**
     * 404=表示资源未发现；响应无消息体
     */
    BASE_REQUEST_RESOURCES_NOT_FOUND(404,"Not Found"),
    /**
     * 412=表示先决条件未满足，当客户端时间与平台事件相差超过15min以上时，平台返回该错误码；响应无消息体
     */
    BASE_TRAFFIC_LIMITING(412,"Precondition failed"),
    /**
     * 500=表示服务内部发生错误；响应无消息体
     */
    BASE_INTERNAL_SERVER(500,"Internal Server Error"),
    /**
     * 999=上层服务异常，请求被熔断
     */
    BASE_TOP_SERVER(999,"top service exception , request fuse..."),
    /**
     * 1002=参数格式异常
     */
    BASE_PARAM_FORMAT(1002,"param format error"),
    /**
     * 1003=数据库错误
     */
    BASE_DB(1003,"db error"),
    /**
     * 1005=无效句柄(无效的句柄或ID)
     */
    BASE_INVALID_HANDLE(1005,"invalid handle"),
    /**
     * 1006=服务器内部错误
     */
    BASE_SERVER_UNKNOWN(1006,"server internal error"),


    NO_PERMISSION(1118,"no permission"),
    /**
     * 4100=设备不支持此功能
     */
    FRS_DEVICE_NO_SUPPORT(4100,"device no support"),
    /**
     * 4101=获取参数失败
     */
    FRS_GET_PARAM(4101,"get param fail"),
    /**
     * 4102=设置参数失败
     */
    FRS_SET_PARAM(4102,"set param fail"),
    /**
     * 4103=无数据导出
     */
    FRS_NO_DATA_EXPORT(4103,"no data export"),
    /**
     * 4104=导出失败
     */
    FRS_EXPORT(4104,"export fail"),
    /**
     * 4105=父级部门已删除，操作失败，请刷新组织后重试
     */
    FRS_NOT_EXIST_PARENT_DEPARTMENT(4105,"the parent department has deleted"),
    /**
     * 4106=部门已删除，操作失败，请刷新组织后重试
     */
    FRS_NOT_EXIST_DEPARTMENT(4106,"the department has deleted"),
    /**
     * 4107=人员已经被删除
     */
    FRS_NOT_EXIST_PERSON(4107,"the person has been deleted"),
    /**
     * 4108=模板文件被改动，禁止导入
     */
    FRS_TEMPLATE(4108,"template file has been changed"),
    /**
     * 4109=无数据导入
     */
    FRS_NOT_DATA_IMPORT(4109,"no data import"),
    /**
     * 4110=获取人员导入文件失败
     */
    FRS_GET_PERSON_IMPORT(4110,"Failed to get Personnel Import File"),
    /**
     * 4111=上传文件限制100MB以内
     */
    FRS_UPLOAD_SIZE(4111,"upload file limit is less than 100 MB"),
    /**
     * 4112=文件类型不支持
     */
    FRS_FILE_TYPE(4112,"file type is not supported"),
    /**
     * 4113=文件解析失败
     */
    FRS_PARSING_FILE(4113,"file parsing failed"),
    /**
     * 4114=人员类别已存在
     */
    FRS_PERSON_TYPE_EXIST(4114,"the person type is existing"),
    /**
     * 4115=人员类别不存在
     */
    FRS_PERSON_TYPE_NOT_EXIST(4115,"the person type is not existing"),
    /**
     * 4116=平台用户对人脸组织机构无操作权限
     */
    FRS_PLATFORM_USER_ORG_UNAUTHORIZED(4116,"platform user has no authority on face organization"),
    /**
     * 4117=计算类别不存在
     */
    FRS_COMPUTE_TYPE_NOT_EXIST(4117,"the compute type does not exist"),
    /**
     * 4118=假期类别不存在
     */
    FRS_HOLIDAY_TYPE_NOT_EXIST(4118,"the holiday type does not exist"),
    /**
     * 4119=循环类别不存在
     */
    FRS_LOOP_TYPE_NOT_EXIST(4119,"the loop type does not exist"),
    /**
     * 4120=计划类别不存在
     */
    FRS_PLAN_TYPE_NOT_EXIST(4120,"the plan type does not exist"),
    /**
     * 4121=考勤异常类别不存在
     */
    FRS_ATTENDANCE_EXCEPTION_TYPE_NOT_EXIST(4121,"the attendance exception type does not exist"),
    /**
     * 4122=班组添加计划时类型不统一
     */
    FRS_CLASSINFO_PLAN_TYPE_NOT_SAME(4122,"the plan types are not the same"),
    /**
     * 4123=考勤记录不存在
     */
    FRS_RECORD_NOT_EXIST(4123,"the record does not exist"),
    /**
     * 4124=假期日期重叠
     */
    FRS_HOLIDAY_DATES_OVERLAP(4124,"the holiday dates overlap"),
    /**
     * 4125=考勤记录为正常
     */
    FRS_RECORD_IS_NORMAL(4125,"the record is normal"),
    /**
     * 4126=操作班组不存在
     */
    FRS_CLASSINFO_NOT_EXIST(4126,"opera class info not exist"),
    /**
     * 4127=计划名称重复
     */
    FRS_PLAN_NAME_REPEAT(4127,"the plan name repeat"),
    /**
     * 4128=签到有效开始时间需小于签退有效开始时间
     */
    FRS_PLAN_SIGN_IN_LT_OUT(4128,"sign in end time need before sign out begin time"),
    /**
     * 4129=上班时间需在有效签到时间之内
     */
    FRS_PLAN_START_TIME_INVALID(4129,"the plan start time is  invalid"),
    /**
     * 4130=下班时间需在有效签退时间之内
     */
    FRS_PLAN_END_TIME_INVALID(4130,"the plan end time is  invalid"),
    /**
     * 4131=允许迟到时间需在有效签到时间之内
     */
    FRS_PLAN_ALLOW_LATE_TIME_INVALID(4131,"the plan allow late time is invalid"),
    /**
     * 4132=允许早退时间需在有效签退时间之内
     */
    FRS_PLAN_ALLOW_EARLY_TIME_INVALID(4132,"the plan allow early time is invalid"),
    /**
     * 4133=计划异常类型不能重复
     */
    FRS_PLAN_EXCEPTION_TYPE_NOT_REPEAT(4133,"the plan exception type not repeat"),
    /**
     * 4134=异常类型只有迟到时，异常开始时间 需= 上班时间 + 允许迟到分钟数 + 1分钟
     */
    FRS_LATE_ONLY_EXCEPTION_BEGIN_TIME_INVALID(4134,"only late exception ,exception begin time invalid"),
    /**
     * 4135=异常类型只有迟到时，异常结束时间 = 签到有效结束时间
     */
    FRS_LATE_ONLY_EXCEPTION_ENDTIME_EQUAL_SIGNIN_ENDTIME(4135,"only late exception ,exception end time need equsl sign in  end time"),
    /**
     * 4136=异常类型只有早退时，异常开始时间 = 签退有效开始时间
     */
    FRS_EARLY_ONLY_EXCEPTION_BEGIN_TIME_INVALID(4136,"only early exception ,exception begin time need equsl sign out  begin time"),
    /**
     * 4137=异常类型只有早退时，异常结束时间 = 下班时间 – 允许早退分钟数 – 1分钟
     */
    FRS_EARLY_ONLY_EXCEPTION_END_TIME_INVALID(4137,"only early exception ,exception end time invalid"),
    /**
     * 4138=异常类型只有旷工时，异常开始时间 = 上班时间 + 允许迟到分钟数 + 1分钟
     */
    FRS_IGNORE_ONLY_EXCEPTION_BEGIN_TIME_INVALID(4138,"only ignore exception ,exception begin time invalid"),
    /**
     * 4139=异常类型只有旷工时，异常结束时间 = 下班时间 – 允许早退分钟数 – 1分钟
     */
    FRS_IGNORE_ONLY_EXCEPTION_END_TIME_INVALID(4139,"only ignore exception ,exception end time invalid"),
    /**
     * 4140=迟到异常开始时间 = 上班时间 + 允许迟到分钟数 + 1分钟
     */
    FRS_LATE_EXCEPTION_BEGIN_TIME_INVALID(4140,"late exception begin time invalid"),
    /**
     * 4141=迟到 异常结束时间 = 签到有效结束时间
     */
    FRS_LATE_EXCEPTION_END_TIME_INVALID(4141,"late exception end time invalid"),
    /**
     * 4142=早退异常开始时间 = 签退有效开始时间
     */
    FRS_EARLY_EXCEPTION_BEGIN_TIME_INVALID(4142,"early exception begin time invalid"),
    /**
     * 4143=早退异常结束时间 = 下班时间 – 允许早退分钟数 – 1分钟
     */
    FRS_EARLY_EXCEPTION_END_TIME_INVALID(4143,"early exception end time invalid"),
    /**
     * 4144=迟到异常开始时间 = 上班时间 + 允许迟到分钟数 + 1分钟
     */
    FRS_LATE_AND_IGNORE_EXCEPTION_BEGIN_TIME_INVALID(4144,"late and ignore exception begin time invalid"),
    /**
     * 4145=迟到、旷工异常同时存在时 迟到异常结束时间 = 旷工异常开始时间 – 1分钟
     */
    FRS_LATE_AND_IGNORE_EXCEPTION_END_TIME_INVALID(4145,"late and ignore exception end time invalid"),
    /**
     * 4146=迟到、旷工异常同时存在时 旷工异常开始时间 <= 签到有效结束时间
     */
    FRS_LATE_AND_IGNORE_EXCEPTION_IGNORE_END_TIME_INVALID(4146,"late and ignore exception  ignore end time invalid"),
    /**
     * 4147=早退、旷工异常同时存在时 早退异常开始时间 = 旷工异常结束时间 + 1分钟
     */
    FRS_EARLY_AND_IGNORE_EXCEPTION_BEGIN_TIME_INVALID(4147,"early and ignore exception begin time invalid"),
    /**
     * 4148=早退异常结束时间 = 下班时间 – 允许早退时间 – 1分钟
     */
    FRS_EARLY_AND_IGNORE_EXCEPTION_END_TIME_INVALID(4148,"early and ignore exception end time invalid"),
    /**
     * 4149=早退、旷工异常同时存在时 旷工异常结束时间 >= 签退有效开始时间
     */
    FRS_EARLY_AND_IGNORE_EXCEPTION_IGNORE_END_TIME_INVALID(4149,"early and ignore exception  ignore end time invalid"),
    /**
     * 4150=上班时间 < 下班时间
     */
    FRS_ON_DUTY_TIME_IS_LESS_THAN_OFF_DUTY_TIME(4150,"on duty time is less than off duty time"),
    /**
     * 4151=上班时间 + 允许迟到分钟数 < 下班时间 — 允许早退分钟数
     */
    FRS_WORKING_HOURS_PLUS_MINUTES_ALLOWED_LATE_LESS_THAN_OFF_HOURS_MINUS_MINUTES_ALLOWED_LEAVE_EARLY(4151,"working hours plus minutes allowed to be late less than off hours minus minutes allowed to leave early"),
    /**
     * 4152=异常开始时间 < 异常结束时间
     */
    FRS_EXCEPTION_START_TIME_IS_LESS_THAN_EXCEPTION_END_TIME(4152,"exception start time is less than exception end time"),
    /**
     * 4153=班组名称重复
     */
    FRS_CLASSINFO_NAME_REPEAT(4153,"the classinfo  name repeat"),


    /**
     * 4300=appKey或appSecret无效
     */
    OAS_AK_OR_SK_INVALID(4300,"the parent department has deleted"),
    /**
     * 4301=token过期
     */
    OAS_TOKEN_EXPIRED(4301,"token expired"),
    /**
     * 4302=非法请求
     */
    OAS_REQUEST_ILLEGAL(4302,"illegal request"),
    /**
     * 4303=请求失败
     */
    OAS_REQUEST_EXPIRED(4303,"request expired"),
    /**
     * 4304=资源不存在
     */
    OAS_RESOURCE_NOT_EXIST(4304,"resource does not exist"),
    /**
     * 4306=设备离线
     */
    OAS_DEVICE_OFFLINE(4305,"device is offline"),
    /**
     * 4306=连接设备失败
     */
    OAS_CONNECT_DEVICE_FAILED(4306,"connect device failed"),
    /**
     * 4307=设备不存在或未找到设备
     */
    OAS_DEVICE_NOT_EXIST(4307,"the device does not exist or is not found"),
    /**
     * 4308=云台控制的token过期无效
     */
    OAS_PTZ_TOKEN_EXPIRED(4308,"token for ptz controll expired"),
    /**
     * 4309=云台被更高级别用户使用
     */
    OAS_PTZ_NOT_CONTROLLED(4309,"ptz controlled by higher level user"),
    /**
     * 4310=设备连接数达到最大值
     */
    OAS_DEVICE_CONNECTTION_MAXIMUM(4310,"device connecttion reach maximum"),
    /**
     * 4311=设备在对讲中
     */
    OAS_VOICE_TALK_ING(4311,"voice talk is going"),
    /**
     * 4312=非合法IP
     */
    OAS_IP_NOT_LEGAL(4312,"ip not legal"),

    /**
     * 4401=token无效
     */
    PSS_TOKEN_INVALID(4401,"the token is invalid"),
    /**
     * 4402=文件路径无效
     */
    PSS_FILE_PATH_INVALID(4402,"file path is invalid"),
    /**
     * 4402=上传文件限制500MB以内
     */
    PSS_UPLOAD_SIZE(4403,"upload file limit is less than 500 MB"),
    /**
     * 4404=删除临时文件失败
     */
    PSS_TMP_DELETE(4404,"delete tmp file failed"),


    /**************************************************
     *  公共 错误码
     *  20000-20999
     **************************************************/
    /**
     * 操作失败
     */
    COMMON_OPERATION_FAILED(20001,"operation failed"),
    /**
     * 无效参数
     */
    COMMON_INVALID_PARAMETER(20002,"invalid parameter"),
    /**
     * 内部服务异常
     */
    COMMON_INTERNAL_SERVICE_EXCEPTION(20003,"Internal service exception"),
    /**
     * 请求无权限
     */
    COMMON_NO_PERMISSION_REQUEST(20004,"no permission request"),

    /**************************************************
     *  UMS 错误码
     *  21000-21999
     **************************************************/
    /**
     * 用户分组已存在
     */
    UMS_GROUP_EXISTS(21001,"user group exists"),

    /**
     * 分组不属于用户
     */
    UMS_GROUP_NOT_BELONG_USER(21002,"group not belong user"),

    /**
     * 账号不存在
     */
    UMS_ACCOUNT_NOT_EXIST(21004,"account not exists"),

    /**
     * 账号已存在
     */
    UMS_ACCOUNT_EXIST(21005,"account exists"),

    /**
     * 账号不合法
     */
    UMS_ACCOUNT_INVALID(21006,"account invalid"),

    /**
     * 验证码错误
     */
    UMS_VERIFY_CODE_WRONG(21007,"verify code wrong"),

    /**
     * 修改密码失败
     */
    UMS_FAIL_MODIFY_PASSWORD(21008,"fail to modify password"),

    /**
     * 注册用户失败
     */
    UMS_FAIL_REGISTER_ACCOUNT(21009,"fail to register account"),

    /**
     * 未查到数据内容
     */
    UMS_GET_NO_DATA(21010,"get no data"),

    /**
     * 密码格式不正确
     */
    UMS_PASSWORD_INVALID(21011,"password is invalid"),

    /**
     *密码不能与用户名或用户名倒写相同
     */
    UMS_PASSWORD_CAN_NOT_RELATION_ACCOUNT(21012,"password can not relation to account"),

    /**
     * 用户名或密码错误
     */
    UMS_ACCOUNT_OR_PASSWORD_IS_WRONG(21013 , "account or password is wrong"),

    /**
     * 账号锁定,需验证码
     */
    UMS_LOGIN_ACCOUNT_LOCKING(21014,"account is locked , need verification code"),

    /**
     * tiken 过期
     */
    UMS_TIKEN_OVERDUE(21015,"tiken overdue"),

    /**
     * token 过期
     */
    UMS_TOKEN_OVERDUE(21016 , "token overdue"),

    /**
     * 原密码错误
     */
    UMS_OLD_PASSWORD_IS_WRONG(21017 , "old password id wrong"),

    /**
     * 用户分组不存在
     */
    UMS_GROUP_NOT_EXIST(21018 , "account group not exist"),

    /**
     * 默认分组不可删除
     */
    UMS_DEFAULT_GROUP_NOT_DELETE(21019 , "default group not delete"),
    /**
     * 推送验证码失败
     */
    UMS_VERIFICATION_CODE_SEND_ERROR(21020 , "send verification code error"),
    /**
     * 设备默认分组添加失败
     */
    UMS_ADD_DEVICE_DEFAULT_GROUP_FAILED(21021 , "add device default group failed"),
    /**
     * 获取验证码频繁
     */
    UMS_GET_VERIFICATION_CODE_FREQUENTLY(21022 , "get verification code frequently"),
    /**
     * 用户不属于管理员
     */
    UMS_USER_NOT_BELONG_ADMIN(21023,"user not belong admin"),
    /**
     * 登录失败
     */
    UMS_USER_LOGIN_FAILED(21024,"user login failed"),
    /**
     * 用户不是管理员
     */
    UMS_USER_NOT_IS_ADMIN(21025,"user not is admin"),
    /**
     * 密码加密错误
     */
    ENCODE_PASSWORD_ERROR(21026,"encode password error"),
    /**
     * 默认分组不可修改
     */
    UMS_DEFAULT_GROUP_NOT_UPDATE(21027 , "default group not update"),
    /**
     * 新旧密码相同
     */
    UMS_OLD_NEW_PASSWORD_SAME(21028 , "Same as the old and new password"),
    /**
     * 添加用户默认分组失败
     */
    UMS_ADD_USER_DEFAULT_GROUP_FAILED(21029 , "add user default group failed"),
    /**
     * 上传app版本低
     */
    UMS_UPLOAD_APP_VERSION_LOW(21030 , "upload app version low"),
    /**
     * ip变更token过期
     */
    UMS_IP_CHANGE_TOKEN_OVERDUE(21031 , "ip change token overdue"),
    /**
     * 账号被踢退
     */
    UMS_ACCOUNT_KICKED_OUT(21032 , "account kicked out"),
    /**
     * 新密码不能与旧密码相同
     */
    UMS_NEW_PASSWORD_OLD_PASSWORD_CAN_NOT_BE_THE_SAME(21033 , "new password and old password cannot be the same"),
    /**
     * 上传隐私协议版本低
     */
    UMS_UPLOAD_PRIVACY_PROTOCOL_VERSION_LOW(21034 , "upload privacy protocol version low"),
    /**
     * 忘记密码被锁定 , 稍后重试
     */
    UMS_FORGET_PWD_LOCK(21035,"forget password locked"),
    /**
     * 账号锁定
     */
    UMS_USER_LOCK(21036 , "user lock"),
    /**
     * 账号被强制离线
     */
    UMS_ACCOUNT_IS_FORCED_OFFLINE(21037 , "account is forced offline"),
    /**
     * 密码错误
     */
    UMS_PASSWORD_IS_WRONG(21038 , "password is wrong"),
    /**
     * 账号已冻结
     */
    UMS_ACCOUNT_FROZEN(21039,"account frozen"),
    /**
     * 账号未冻结
     */
    UMS_ACCOUNT_NOT_FROZEN(21040,"account not frozen"),
    /**
     * 密码不能为空
     */
    UMS_PASSWORD_NOT_NULL(21041,"password not null"),
    /**
     * App错误码集已存在
     * */
    UMS_APP_ERROR_CODE_LIST_IS_ALREADY_EXIST(21042,"app error code list is already exist"),
    /**
     * App错误码集不存在
     * */
    UMS_APP_ERROR_CODE_LIST_IS_NOT_EXIST(21043,"app error code list is not exist"),
    /**
     * 地区没有数据中心
     */
    UMS_REGION_NO_DATA_CENTER(21044,"region no data center"),
    /**
     * 无效国家编号
     * */
    UMS_COUNTRY_CODE_INVALID(21045,"country code invalid"),
    /**
     * 无效语言编号
     * */
    UMS_LANGUAGE_CODE_INVALID(21046,"language code invalid"),


    PARSE_TOKEN_FAIL(21047 , "token解析失败"),

    TOKEN_OVER_LOCAL(21048 , "token到期时间小于服务器时间"),

    TOKEN_USER_NOT_FOUND(21049 , "无法从token中获取用户信息"),

    TOKEN_NOT_FOUND(21050 , "没找到token"),

    TOKEN_NOT_SAME(21051 , "传入的token与服务器上的不同"),
    /**************************************************
     *  UDMS 错误码
     *  22000-22999
     **************************************************/
    /**
     * 设备不属于用户
     */
    UDMS_DEVICE_NOT_BELONG_USER(22001,"device not belong user"),
    /**
     * 分组名称不能重复
     */
    UDMS_GROUP_NAME_CANNOT_BE_DUPLICATE(22002,"group name cannot be duplicate"),
    /**
     * 管理员未添加默认分组
     */
    UDMS_ADMINISTRATOR_DID_NOT_ADD_DEFAULT_GROUP(22003,"administrator did not add default group"),
    /**
     * 未查到数据内容
     */
    UDMS_GET_NO_DATA(22004,"get no data"),
    /**
     * 设备查询错误
     */
    UDMS_DEVICE_QUERY_ERROR(22005,"device query error"),
    /**
     * 获取设备组失败
     */
    UDMS_DEVICE_GROUP_ERROR(22006,"device group error"),
    /**
     * 序列号或验证码错误
     */
    UDMS_INCORRECT_VERIFICATION_CODE(22007,"incorrect verification code"),
    /**
     * 设备已存在
     */
    UDMS_DEVICE_ALREADY_EXISTS(22008,"device already exists"),
    /**
     * 获取分组失败
     */
    UDMS_GET_GROUP_FAILED(22009,"get group failed"),
    /**
     * 设备已被别人占用
     */
    UDMS_DEVICE_USED_BY_OTHERS(22010,"device used by others"),
    /**
     * control_token已失效
     */
    UDMS_CONTROL_TOKEN_INVALID(22011,"control token is invalid"),
    /**
     * 视图不输入用户
     */
    UDMS_VIEW_NOT_BELONG_USER(22012,"view not belong user"),
    /**
     * 视图不存在
     */
    UDMS_VIEW_NOT_EXISTS(22013,"view not exists"),
    /**
     * 视图名称已存在
     */
    UDMS_VIEW_NAME_ALREADY_EXISTS(22014,"view name already exists"),
    /**
     * 用户添加设备锁定一分钟
     */
    UDMS_ADD_DEVICE_LOCKING(22015,"Adding user device is locked"),
    /**
     * 设备未注册
     */
    UDMS_DEVICE_NOT_EXISTS(22016,"device not exists"),
    /**
     * 设备分组不存在
     */
    UDMS_DEVICEGROUP_NOT_EXISTS(22017,"device group not exists"),
    /**
     * 分享已存在
     */
    UDMS_SHARE_EXISTS(22018,"share exists"),
    /**
     * 分享不存在
     */
    UDMS_SHARE_NOT_EXISTS(22019,"share not exists"),
    /**
     * 分享接收者不合法
     */
    UDMS_SHARE_RECEIVER_ILLEGAL(22020,"share receiver illegal"),
    /**
     * 所有设备都达到分享上线
     */
    UDMS_ALL_DEVICE_SHARE_LIMIT(22021,"all device share limit"),
    /**
     * 不能给自己分享
     */
    UDMS_CAN_NOT_SHARE_TO_YOURSELF(22022,"can not share to yourself"),
    /**
     * 分组个数达到上限
     */
    UDMS_GROUP_MAX_NUMBER(22023,"group max number"),
    /**
     * 单个分组内设备个数达到上限
     */
    UDMS_GROUP_DEVICE_MAX_NUMBER(22024,"group device max number"),
    /**
     * 分享已达到上限
     */
    UDMS_SHARE_LIMIT(22025 , "share limit"),
    /**
     * 设备离线
     */
    UDMS_DEVICE_OFFLINE(22026 , "device offline"),
    /**
     * 收藏达到上限
     */
    UDMS_FAVORITE_MAX_NUMBER(22027 , "favorite max number"),

    /**
     * 该设备通道在视图中已存在
     * */
    UDMS_DEVICE_CHANNEL_IN_VIEW_IS_EXIST(22028,"device channel in view is exist"),
    /**
     * 用户数据中心不相同
     * */
    UDMS_USER_SERVICE_CENTERS_NOT_THE_SAME(22029,"user service centers not the same"),

    /**************************************************
     *  MNS 错误码
     *  23000-23999
     **************************************************/

    /**
     * 自定义报警时间不存在
     *
     * */
    MNS_ALARM_SCHEDULE_IS_NOT_EXIST(23000,"alarm schedule is not exist")

    ;

    /**
     * 异常码
     */
    private int code;
    /**
     * 异常描述 , 返回调用方
     */
    private String msg;
}
