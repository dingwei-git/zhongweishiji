package com.jovision.jaws.common.constant;

public enum AppResultEnum {
    ERROE(0,"失败"),
    SUCCESS(1000,"成功"),
    LOGIN_FAIL(1101,"登录失败"),
    USER_PASSWORD_FAIL(1103,"用户名或密码错误"),
    USER_LOCKED(1104,"已经被锁定,请{0}秒后重试"),
    USER_AUTHORITY_FAIL(1105,"用户权限失效"),
    USER_DATA_EMPTY(1106,"数据为空"),
    PASSWORD_INCONFORMITY(1107,"两次输入的密码不一致"),
    PASSWORD_IS_EQUAL(1108,"新旧密码相同"),
    OLD_PASSWORD_ERROR(1109,"原密码错误"),
    NOT_FIND_USER(1110,"获取用户失败"),
    TOKEN_TIME_OUT(1111,"token失效"),
    PWD_LENGTH_SMALL(1112,"密码长度小于8位"),
    PWD_LENGTH_BIG(1113,"密码长度大于32位"),
    PWD_CONTAIN_ALPHABET(1114,"至少包含一个字母"),
    PWD_CONTAIN_NUM(1115,"至少包含一个数字"),
    PWD_CONTAIN_SYMBOL(1116,"至少包含以下一种特殊字符：!@#$%^&*()_+{}:|<>?[];'\\,./"),
    PWD_CONTAIN_PASSWORD(1117,"密码不能包含password"),
    NO_PERMISSION(1118,"权限不足"),
    NOT_SUPPORT_OPERTION(1119,"该工单状态不支持该操作"),
    PARAM_IS_NULL(1120,"参数不能为空"),
    ORDERCODE_PARAM_IS_NULL(1121,"工单号不能为空"),
    DATA_NOT_EXIT(1122,"数据状态异常，请联系管理员"),
    DELAY_TIME_SHORT(1123,"延期时间应大于{0}，请重新选择"),
    FORBACKCONTENT_IS_NULL(1124,"反馈内容不能为空"),
    PICTURE_IS_NULL(1125,"图片不能为空"),
    REVIEW_IS_FAULT(1126,"复检失败"),
    LOGIN_COUNT_OUT(1127,"登录失败，当前在线人数达到最大值"),
    TIKEN_TIME_OUT(1128,"tiken失效");

    private int code;
    private String message;
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    AppResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
