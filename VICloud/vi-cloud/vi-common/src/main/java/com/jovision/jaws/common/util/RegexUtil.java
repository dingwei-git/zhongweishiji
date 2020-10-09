/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

import java.util.regex.Pattern;

/**
 * 正则表达式常量
 *
 * @since 2019-08-13
 */
public final class RegexUtil {
    /**
     * Pattern对象: number
     */
    public static final Pattern PATTERN_NUMBER = Pattern.compile("^[\\d]+$");

    /**
     * 中文
     */
    public static final Pattern PATTERN_CHINESE = Pattern.compile("^[\u4e00-\u9fa5]{1,}$");

    /**
     * email
     */
    public static final Pattern PATTERN_EMAIL =
        Pattern.compile("^\\w+([-+.]\\w+)\\*@\\w+([-.]\\w+)\\*\\.\\w+([-.]\\w+)\\*$");

    /**
     * URL校验：
     */
    public static final String URL = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    /**
     * 身份证ID 15位或18位 [1-9] 第一位1-9中的一个； \\d{5} 五位数字； (18|19|20)
     * 18xx-20xx年；\\d{2}年份；((0[1-9])|(10|11|12))月份；(([0-2][1-9])|10|20|30|31)01；\\d{3} 第十七位奇数代表男，偶数代表女；[0-9Xx]校验值
     */
    public static final Pattern PATTERN_ID =
        Pattern.compile("(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|"
            + "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)");

    /**
     * Pattern对象：IP
     */
    public static final Pattern PATTERN_IP =
        Pattern.compile("^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$");

    /**
     * Pattern对象:字母数字
     */
    public static final Pattern PATTERN_NUMBER_OR_LETTER = Pattern.compile("^[A-Za-z0-9]+$");

    /**
     * Pattern对象：字母数字下划线
     */
    public static final Pattern PATTERN_NORMAL_SYMBOL = Pattern.compile("^\\w+$");

    /**
     * Pattern对象：字母数字下划线加号等号斜杠
     */
    public static final Pattern PATTERN_NORMAL_PASSWORD = Pattern.compile("^[0-9a-zA-Z_+=/]+$");

    /**
     * Pattern对象：字母数字下划线中文
     */
    public static final Pattern PATTERN_CHINESE_AND_NORMAL_SYMBOL = Pattern.compile("^[A-Za-z0-9_\u4e00-\u9fa5]+$");

    /**
     * Pattern对象：校验文件名称是否有效
     */
    public static final Pattern PATTERN_FILE_NAME_SPECIAL_SYMBOL = Pattern.compile("^[^\\/:*?\"<>|]+$");

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final Pattern PATTERN_DATA_FORMAT =
        Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$");

    /**
     * Pattern对象:匹配告警时间段，半小时整点粒度 20:30:00
     */
    public static final Pattern PATTERN_ALARM_TIME = Pattern.compile("([0-1][0-9]|2[0-3]):[03][0]:([0]{2})");

    /**
     * Pattern对象:字母数字汉字
     */
    public static final Pattern PATTERN_NUMBER_OR_LETTER_OR_CHINESE =
        Pattern.compile("^[A-Za-z0-9\u4e00-\u9fa5]{1,32}$");

    /**
     * Pattern对象:中英文数字带空格
     */
    public static final Pattern PATTERN_ENGLISH_NAME = Pattern.compile("^[\u4e00-\u9fa5a-zA-Z0-9\\s]{1,32}$");

    /**
     * 匹配单个非数字字符
     */
    public static final Pattern PATTERN_NOT_SINGLE_NUM = Pattern.compile("\\D");

    /**
     * 经度正则
     */
    public static final Pattern PATTERN_LONGITUDE_RE =
        Pattern.compile("^[\\-\\+]?(0?\\d{1,2}\\.\\d{1,5}|1[0-7]?\\d{1}\\.\\d{1,5}|180\\.0{1,5})$");

    /**
     * 纬度正则
     */
    public static final Pattern PATTERN_LATITUDE_RE =
        Pattern.compile("^[\\-\\+]?([0-8]?\\d{1}\\.\\d{1,5}|90\\.0{1,5})$");

    /**
     * 固话号码正则
     */
    public static final Pattern PATTERN_FIXED_LINE_PHONE_RE =
        Pattern.compile("^(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,14}$");

    /**
     * 端口号的正则
     */
    public static final Pattern PATTERN_PORT_RE =
        Pattern.compile("^([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])$");

    /**
     * 网址的正则
     */
    public static final Pattern PATTERN_WEBADDRESS_RE = Pattern.compile(URL);

    /**
     * Pattern对象:非特殊字符
     */
    public static final Pattern NON_SPECIAL_CHARACTERS = Pattern.compile("^[A-Za-z0-9\u4e00-\u9fa5]+$");

    /**
     * Pattern对象:非特殊字符，英文带空格
     */
    public static final Pattern NON_SPECIAL_CHARACTERS_ENGLISH = Pattern.compile("^[A-Za-z0-9\u4e00-\u9fa5\\s]+$");

    /**
     * 构造函数
     */
    private RegexUtil() {
    }
}
