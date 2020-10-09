
package com.jovision.jaws.common.info;

/**
 * 全局唯一变量类
 *
 * @version 1.0, 2018年6月15日
 * @since 2018-06-15
 */
public class ProgressInfo {

    /**
     * ProgressInfo 对象
     */
    private static ProgressInfo instance;

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
     * 服务器地理位置
     */
    private static String servIpLocation;

    /**
     * 客户端地理位置
     */
    private static String clinIpLocation;

    /**
     * 语言类型
     */
    private static String languageTyle;

    /**
     * 是否打开IVS配置开关
     */
    private static boolean isHaveIvs;

    /**
     * 是否打开Q4的图像诊断配置开关
     */
    private static boolean isHaveImageDiagnosisService;

    // 层级临时变量
    private int tmpLevel;

    private String[] titleFields = null; // 组网关系表列名称数组

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
     * 不需要走采集分析流程，在导完组网表之后立即生效，获取最新组网表层级数
     *
     * @return int
     */
    public int getTmpLevel() {
        return tmpLevel;
    }

    public void setTmpLevel(int tmpLevel) {
        this.tmpLevel = tmpLevel;
    }

    public String[] getTitleFields() {
        return titleFields == null ? null : titleFields.clone();
    }

    public void setTitleFields(String[] titleFields) {
        this.titleFields = titleFields;
    }

    /**
     * 内部类进行单例对象初始化
     *
     * @since 2019-11-06
     */
    private static class ProgressInfoBuild {
        private static ProgressInfo instance = new ProgressInfo();
    }
}
