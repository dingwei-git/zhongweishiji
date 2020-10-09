/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

import com.huawei.utils.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 黑名单检查
 *
 * @since 2019-03-02
 */
public final class BlackListCheck {
    // 特殊符号
    private static final Pattern PATTERN_SPECIAL_SYMBOL = Pattern.compile("[\\/:*?<>|$]+");

    // xml特殊符号
    private static final Pattern PATTERN_XML_SPECIAL_SYMBOL = Pattern.compile("[-<!>]+");

    /**
     * 构造函数
     */
    private BlackListCheck() {
    }

    /**
     * 是否只包含 字符、数字、下划线
     *
     * @param str 传入的字符换
     * @return boolean
     */
    public static boolean isNormalSymbol(String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        Matcher matcher = RegexUtil.PATTERN_NORMAL_SYMBOL.matcher(str);
        return matcher.matches();
    }

    /**
     * 是否只包含 字符、数字、下划线、加号、等号
     *
     * @param str 传入的字符换
     * @return boolean
     */
    public static boolean isNormalPassword(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Matcher matcher = RegexUtil.PATTERN_NORMAL_PASSWORD.matcher(str);
        return matcher.matches();
    }

    /**
     * 是否是合规的文件名称
     *
     * @param fileName 文件(夹)名称
     * @return boolean true:正常；false:不正常
     */
    public static boolean isNormalFileName(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return false;
        }
        Matcher matcher = RegexUtil.PATTERN_FILE_NAME_SPECIAL_SYMBOL.matcher(fileName);
        return matcher.matches();
    }

    /**
     * 是否包含特殊字符串
     *
     * @param str 传入的字符串
     * @return boolean
     */
    public static boolean isContainSpecialSymbol(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Matcher matcher = PATTERN_SPECIAL_SYMBOL.matcher(str);
        return matcher.find();
    }

    /**
     * xml是否包含特殊字符串
     *
     * @param str 传入的字符串
     * @return boolean
     */
    public static boolean isXmlContainSpecialSymbol(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Matcher matcher = PATTERN_XML_SPECIAL_SYMBOL.matcher(str);
        return matcher.find();
    }
}
