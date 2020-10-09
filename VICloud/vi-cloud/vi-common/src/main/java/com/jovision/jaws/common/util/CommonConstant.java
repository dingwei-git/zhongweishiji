package com.jovision.jaws.common.util;

import com.jovision.jaws.common.constant.CommonConst;

/**
 * 定义常用的常量：比如：数字
 */
public final class CommonConstant {

    /**
     * zh-CN
     */
    public static final String LANGUAGE_ZH_CN = "zh-CN";

    /**
     * en-US
     */
    public static final String LANGUAGE_EN_US = "en-US";

    // OPERATING SYSTEM OS

    /**
     * os.name
     */
    public static final String OS_NAME = "os.name";

    /**
     * linux
     */
    public static final String OS_LINUX = "linux";

    // Charset

    /**
     * Charset UTF-8
     */
    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * Charset GBK
     */
    public static final String CHARSET_GBK = "GBK";

    /**
     * 常量：flag
     */
    public static final String FLAG = "flag";

    /**
     * 编码格式
     */
    public static final String ENCODE = "utf-8";

    /**
     * config目录(先暂时从CommonConst类中引用，后期删除CommonConst类)
     */
    public static final String DIR_CONFIG = CommonConst.DIR_CONFIG;

    // 文件路径:自适应中英文
    /**
     * baseCofig属性配置文件
     */
//    public static final String FILE_BASE_CONFIG = Paths.get(DIR_CONFIG, CONFIG, "baseCofig.xml").toString();

    public static final String FILE_BASE_CONFIG = "/config/zh-config/baseCofig.xml";

    /**
     * config.properties属性配置文件
     */
    public static final String FILE_CONFIG_PROPERTIES = "config/zh-config/config.properties";

    private CommonConstant() {
    }
}
