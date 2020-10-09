
package com.huawei.vi.entity.vo;

import lombok.Data;

/**
 * 全局唯一变量类
 *
 * @version 1.0, 2018年6月15日
 * @since 2018-06-15
 */
@Data
public class ProgressInfo {
    /**
     * 静态对象实力锁
     */
    private static final Object LOCK = new Object();

    /**
     * 用于判断是否完成一次采集周期
     */
    private static boolean isOnceCollectionOver;

    /**
     * 用于判断是否正在采集yes:正在采集 no:采集完成
     */
    private static String iscollecting = "no";

    /**
     * ProgressInfo 对象
     */
    private static ProgressInfo instance;

    /**
     * 服务器地理位置
     */
    private static String servIpLocation;

    /**
     * 客户端地理位置
     */
    private static String clinIpLocation;

    // 当前层次数
    private int level;

    // 层级临时变量
    private int tmpLevel;

    /**
     * 语言类型
     */
    private static String languageTyle;

    /**
     * 是否打开IVS配置开关
     */
    private static boolean isHaveIvs;

    private String[] titleFields = null; // 组网关系表列名称数组

    /**
     * 是否打开Q4的图像诊断配置开关
     */
    private static boolean isHaveImageDiagnosisService;

    private boolean importIpcFlag; // 导入组网关系表的标志，true:在导入组网关系表 false:没有在导入组网关系表

    /**
     * 获取单例
     *
     * @return ProgressInfo
     */
    public static ProgressInfo getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ProgressInfo();
            }
            return instance;
        }
    }

    /**
     * 一次采集结束
     *
     * @return Boolean
     */
    public boolean getIsOnceCollectionOver() {
        return isOnceCollectionOver;
    }

    /**
     * 设置采集结束标志
     *
     * @param isOnceCollectionOver 采集结束标志
     */
    public static void setIsOnceCollectionOver(boolean isOnceCollectionOver) {
        synchronized (LOCK) {
            ProgressInfo.isOnceCollectionOver = isOnceCollectionOver;
        }
    }

    /**
     * 正在采集
     *
     * @return String
     */
    public String getIscollecting() {
        return iscollecting;
    }

    /**
     * 设置采集标志
     *
     * @param iscollecting 参数标志
     */
    public static void setIscollecting(String iscollecting) {
        synchronized (LOCK) {
            ProgressInfo.iscollecting = iscollecting;
        }
    }

    public static String getServIpLocation() {
        return servIpLocation;
    }

    public static void setServIpLocation(String servIpLocation) {
        ProgressInfo.servIpLocation = servIpLocation;
    }

    public static String getClinIpLocation() {
        return clinIpLocation;
    }

    public static void setClinIpLocation(String clinIpLocation) {
        ProgressInfo.clinIpLocation = clinIpLocation;
    }

    public static String getLanguageTyle() {
        return languageTyle;
    }

    public static void setLanguageTyle(String languageTyle) {
        ProgressInfo.languageTyle = languageTyle;
    }

    /**
     * 返回 isHaveIvs
     *
     * @return isHaveIvs值
     */
    public static boolean isHaveIvs() {
        return isHaveIvs;
    }

    /**
     * 对isHaveIvs进行赋值
     *
     * @param isHaveIvsFlg isHaveIvs值
     */
    public static void setHaveIvs(boolean isHaveIvsFlg) {
        ProgressInfo.isHaveIvs = isHaveIvsFlg;
    }

    public static boolean isHaveImageDiagnosisService() {
        return isHaveImageDiagnosisService;
    }

    public static void setHaveImageDiagnosisService(boolean isHaveImageDiagnosis) {
        ProgressInfo.isHaveImageDiagnosisService = isHaveImageDiagnosis;
    }

    /**
     * 返回importIpcFlag
     *
     * @return importIpcFlag 返回importIpcFlag
     */
    public boolean isImportIpcFlag() {
        return importIpcFlag;
    }

    /**
     * 对importIpcFlag进行赋值
     *
     * @param isImportIpcFlag 对importIpcFlag进行赋值
     */
    public void setImportIpcFlag(boolean isImportIpcFlag) {
        importIpcFlag = isImportIpcFlag;
    }
}
