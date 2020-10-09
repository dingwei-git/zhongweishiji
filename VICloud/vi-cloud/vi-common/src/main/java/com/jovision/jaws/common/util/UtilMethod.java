/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

import com.jovision.jaws.common.constant.CommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Closeable;
import java.io.IOException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共方法类
 *
 * @version 1.0, 2018年6月14日
 * @since 2019-09-18
 */
public final class UtilMethod {
    /**
     * 编码格式
     */
    public static final String ENCODE = "utf-8";

    /**
     * 文件分隔符
     */
    public static final String SLASH = "/";

    /**
     * 格式化时间格式
     */
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化时间格式3
     */
    public static final String TIME_FORMAT3 = "yyyyMMddHHmmss";

    /**
     * 格式化时间格式4
     */
    public static final String TIME_FORMAT4 = "yyyy-MM-dd HH.mm.ss";

    /**
     * 匹配时间
     */
    public static final String REG_DATE4 = "\\d{4}-\\d{1,2}-\\d{1,2} .*\\d{1,2}.\\d{1,2}.\\d{1,2}(\\.\\d+)?";

    /**
     * 匹配时间
     */
    public static final String REG_DATE = "\\d{4}-\\d{1,2}-\\d{1,2} .*\\d{1,2}:\\d{1,2}:\\d{1,2}(\\.\\d+)?";

    /**
     * BYTE8192
     */
    public static final int BYTE8192 = 8192;

    /**
     * 全局日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UtilMethod.class);

    /**
     * 构造函数
     */
    private UtilMethod() {
    }

    /**
     * 将时间字符串转化为Date
     *
     * @param dateTime 时间
     * @return Date
     */
    public static Date stringToDate(String dateTime) {
        return dateFormat(dateTime, REG_DATE, TIME_FORMAT);
    }

    /**
     * "yyyy-MM-dd HH.mm.ss";将这种格式化的字符串时间转化为Date
     *
     * @param dateTime 时间
     * @return Date
     */
    public static Date stringPeriodToDate(String dateTime) {
        return dateFormat(dateTime, Normalizer.normalize(REG_DATE4, Form.NFKC), TIME_FORMAT4);
    }

    /**
     * stringToDate公共方法
     *
     * @param dateTime 时间
     * @param regex 正则
     * @param pattern pattern
     * @return date 转换后时间
     */
    private static Date dateFormat(String dateTime, String regex, String pattern) {
        Date out = null;
        if ((dateTime != null) && (dateTime.trim().length() > 0)) {
            if (dateTime.matches(regex)) {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                try {
                    out = sdf.parse(dateTime);
                } catch (ParseException e) {
                    LOGGER.error("ParseException:{} ", e.getMessage());
                }
            } else {
                LOGGER.error("stringToDate error");
            }
        }
        return out;
    }

    /**
     * 获取数据表名
     *
     * @param preTableName 表名前缀
     * @param periodStartTime 周期开始时间
     * @return String
     */
    public static String getTableName(String preTableName, Date periodStartTime) {
        String tableName = null;
        String dateStr = new SimpleDateFormat(TIME_FORMAT3).format(periodStartTime);
        tableName = preTableName + CommonConst.SYMBOL_UNDERLINE + dateStr;
        return tableName;
    }

    /**
     * 将Timestamp转化为时间字符串，形如："2007-08-17 10:00:00"
     *
     * @param date 时间
     * @return String
     */
    public static String dateToString(Date date) {
        String out = null;
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
            out = sdf.format(date);
        }
        return out;
    }

    /**
     * 获取周期开始时间的字符串
     *
     * @param filePath 文件路径
     * @return String
     */
    public static String getPeriodStartDate(String filePath) {
        StringBuffer sb = new StringBuffer(NumConstant.EXPECTED_BUFFER_DATA);
        /**
         * 20180730 add追加 增加对文件名称中&&的支持 [-&][&]? 比较简单，但存在-&情况，(-||(\\&\\&)) 书写比较复杂，但是可以杜绝-&类型
         */
        sb.append("(\\d{4}-[01]\\d-[0123]\\d\\s{1,2}[012]\\d\\.[0-6]\\d\\.[0-6]\\d)");
        sb.append("[-&][&]?\\d{4}-[01]\\d-[0123]\\d\\s{1,2}[012]\\d\\.[0-6]\\d\\.[0-6]\\d\\.csv");
        Pattern regex = Pattern.compile(sb.toString());
        Matcher regexMatcher = regex.matcher(Normalizer.normalize(filePath, Form.NFKC));
        if (regexMatcher.find()) {
            return regexMatcher.group(1);
        }
        return "error";
    }

    /**
     * 关闭文件流
     *
     * @param fis 需要关闭的流
     */
    public static void closeFileStreamNotThrow(Closeable fis) {
        // 关闭流
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                LOGGER.error("close stream exception:{}", e.getMessage());
            }
        }
    }

    /**
     * 将long类型的数字转换为时间格式类型的字符串
     *
     * @param longNumber (时间类型的数字串：1528963200000)
     * @return String 2018-06-14 16:05:28
     */
    public static String parseStringNumberToStringDate(String longNumber) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        Long slong = Long.parseLong(longNumber);
        return sdf.format(slong);
    }

    /**
     * 字符串转Date
     *
     * @param time 需要转换的时间字符串
     * @return Date
     */
    public static Date formatStringToDate(String time) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            LOGGER.error("error: {}", e.getMessage());
        }
        return date;
    }

    /**
     * 校验ip是否合法
     *
     * @param ipAddress ipAddress
     * @return boolean
     */
    public static boolean checkIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    /**
     * 获得property属性文件里的信息
     *
     * @param propertyFilePath 文件路径
     * @param maxlength 文件名最大长度
     * @return Map
     */
    public static Map<String, String> readFromPropertyFile(String propertyFilePath, int maxlength) {
        Map<String, String> propertyMap = new HashMap<String, String>();

        if (propertyFilePath != null && propertyFilePath.trim().length() > 0
                && propertyFilePath.trim().length() < maxlength) {
            try {
//                propertyMap = PropertiesUtil.loadToMap(propertyFilePath);
                propertyMap = PropertiesUtil.getMapByFile(propertyFilePath);
            } catch (Exception e) {
                if (!Pattern.matches("^[A-z]:\\\\([^|><?*\":\\/]*\\\\)*([^|><?*\":\\/]*)?$", propertyFilePath)) {
                    LOGGER.info("error readFromPropertyFile");
                } else {
                    LOGGER.error("error readFromPropertyFile ");
                }
            }
        }
        return propertyMap;
    }

    /**
     * 登陆成功之后更换sessionId,防止固化攻击
     *
     * @param request HttpServletRequest 请求参数
     */
    public static void changeSessionId(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession();
            Map<String, Object> tempMap = new HashMap<String, Object>(); // 首先将原session中的数据转移至一临时map中
            Enumeration<String> sessionNames = session.getAttributeNames();
            Object sessionValue = null;
            while (sessionNames.hasMoreElements()) {
                String sessionName = sessionNames.nextElement();
                sessionValue = session.getAttribute(sessionName);
                if (!BlackListCheck.isContainSpecialSymbol(sessionName)
                        && !BlackListCheck.isContainSpecialSymbol(sessionValue.toString())) {
                    tempMap.put(sessionName, sessionValue);
                }
                if (sessionName.equals(CommonConst.LOGIN_USER)) {
                    session.removeAttribute(CommonConst.LOGIN_USER);
                }
            }
            session.invalidate(); // 注销原session，为的是重置sessionId
            session = request.getSession(); // 将临时map中的数据转移至新session
            for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
                session.setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 获客户端真实IP地址：
     *
     * @param request 网络请求
     * @return String
     */
    public static String getIpAddr(HttpServletRequest request) {
        String headIp = "";
        if (request == null) {
            return headIp;
        }
        if (isAddress(request.getHeader("x-forwarded-for"))) {
            headIp = request.getHeader("x-forwarded-for");
        }

        if (headIp == null || headIp.isEmpty() || "unknown".equalsIgnoreCase(headIp)) {
            if (isAddress(request.getHeader("Proxy-Client-IP"))) {
                headIp = request.getHeader("Proxy-Client-IP");
            }
            if (isAddress(request.getHeader("WL-Proxy-Client-IP"))) {
                headIp = request.getHeader("WL-Proxy-Client-IP");
            }
            if (isAddress(request.getRemoteAddr())) {
                headIp = request.getRemoteAddr();
            }
        } else {
            LOGGER.info("else getIpAddr.");
        }
        headIp = headIp == null ? "" : headIp;
        return headIp.length() < CommonConst.MAX_DATA ? headIp : "";
    }

    /**
     * 校验入参是否为IP
     *
     * @param str str
     * @return boolean
     */
    public static boolean isAddress(String str) {
        if (str == null) {
            return false;
        }
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}