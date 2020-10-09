/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.vo;

import com.huawei.cn.components.verifyLicense.LicenseManager;
import com.huawei.cn.components.verifyLicense.data.LicenseObject;
import com.huawei.utils.UtilMethod;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * license公共变量类
 *
 * @version 1.0, 2019年4月17日
 * @since 2019-09-06
 */
@Data
public final class LicenseInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseInfo.class);

    private static LicenseInfo instance;

    private int ipcNum; // license授权IPC个数

    private Date licenseEndDate; // license到期时间

    private boolean isPermanentFlag; // 是否永久license标志

    private boolean isCollectionFlag; // true==>正在采集；false==》未采集

    private boolean isEndCollection; // false==>未点击停止采集按钮;true==>点击了停止采集按钮

    private boolean isEndAnalyse; // false==>未点击停止分析按钮;true==>点击了停止分析按钮

    private String collectionPort; // 获取采集工具端口

    private LicenseInfo() {
    }

    /**
     * 通过静态块初始化实现单例效果
     */
    static {
        instance = new LicenseInfo();
    }

    /**
     * 获取单例
     *
     * @return LicenseInfo
     */
    public static synchronized LicenseInfo getInstance() {
        if (instance == null) {
            instance = new LicenseInfo();
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @exception IllegalArgumentException LicenseManager 管理器存在问题，直接抛异常
     */
    public void init() throws IllegalArgumentException {
        LicenseObject licenseObject = LicenseManager.getInstance().getLicenseObject();
        if (licenseObject == null) {
            // LicenseManager 管理器存在问题，直接抛异常
            throw new IllegalArgumentException("LicenseObject");
        }
        LicenseInfo licenseInfo = LicenseInfo.instance;
        licenseInfo.setIpcNum(licenseObject.getIpcNum());
        licenseInfo.setLicenseEndDate(UtilMethod.stringPeriodToDate(licenseObject.getEndDate()));
        licenseInfo.setPermanentFlag("permanent".equals(licenseObject.getTimeType()));
    }
}
