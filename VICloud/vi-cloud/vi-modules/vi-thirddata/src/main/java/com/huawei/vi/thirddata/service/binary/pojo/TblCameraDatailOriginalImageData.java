package com.huawei.vi.thirddata.service.binary.pojo;

import java.io.Serializable;

public class TblCameraDatailOriginalImageData implements Serializable {

    /**
     * 摄像机详细信息以及图片vo层
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 摄像机编码
     */
    private String cameraSn;

    /**
     * 摄像机名称
     */
    private String cameraName;

    /**
     * 安装高度
     */
    private String mountHeight;

    /**
     * 摄像机经度
     */
    private String longitude;

    /**
     * 摄像机纬度
     */
    private String latitude;

    /**
     * 摄像机地址
     */
    private String address;

    /**
     * 摄像机IP,取流url
     */
    private String streamUrl;

    /**
     * 摄像机方位
     */
    private String direction;

    /**
     * 车道
     */
    private String lane;

    /**
     * 摄像机在线状态0：离线，1：在线
     */
    private String cameraState;

    /**
     * 摄像机类型
     */
    private String cameraType;

    /**
     * 摄像机用途
     */
    private String cameraUse;

    /**
     * 摄像机功能
     */
    private String cameraFeature;

    /**
     * 平台名称
     */
    private String platName;

    /**
     * 分辨率
     */
    private String resolutionType;

    /**
     * 域编码
     */
    private String fieldNo;

    /**
     * 罗盘
     */
    private String compass;

    /**
     * 摄像机nvr
     */
    private String nvr;

    /**
     * 组织id
     */
    private String groupId;

    /**
     * 摄像机id
     */
    private String cameraId;

    /**
     * 订阅状态0:未订阅 1:已订阅
     */
    private String substatus;

    /**
     * 相机属性0：普通相机 1：人脸卡口物理相机 2：车物理卡口相机 3：未知的其他相机 4：机非人相机
     */
    private String vcnCameraUse;

    /**
     * 智能分析列表
     */
    private String analysisList;

    /**
     * 分析类型0001，车牌识别；0010，人脸识别；0100，行为分析；1000，人体分析；1001，人车混合结构化；
     */
    private String analysisType;

    /**
     * 智能任务列表
     */
    private String taskTypeList;

    /**
     * 智能任务类型0: 行为分析 1: 车牌分析 2: 人脸分析 4: 人体分析 9：人车混合结构化
     */
    private String taskType;

    /**
     * IVS的IP地址
     */
    private String ipIvs;

    /*
    *开始时间
    *  */
    private String startTime;

    /*
    * 结束时间
    * */
    private String endTime;

    /*
    * 类型0为过车，1为过脸
    * */
    private String type;

    /*
    * 图片数量
    * */
    private String pictureCount;

    /*
    * 有效摄像机标志
    * */
    private String effectiveCamera;


    public String getCameraSn() {
        return cameraSn;
    }

    public void setCameraSn(String cameraSn) {
        this.cameraSn = cameraSn;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getMountHeight() {
        return mountHeight;
    }

    public void setMountHeight(String mountHeight) {
        this.mountHeight = mountHeight;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getCameraState() {
        return cameraState;
    }

    public void setCameraState(String cameraState) {
        this.cameraState = cameraState;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getCameraUse() {
        return cameraUse;
    }

    public void setCameraUse(String cameraUse) {
        this.cameraUse = cameraUse;
    }

    public String getCameraFeature() {
        return cameraFeature;
    }

    public void setCameraFeature(String cameraFeature) {
        this.cameraFeature = cameraFeature;
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
    }

    public String getResolutionType() {
        return resolutionType;
    }

    public void setResolutionType(String resolutionType) {
        this.resolutionType = resolutionType;
    }

    public String getFieldNo() {
        return fieldNo;
    }

    public void setFieldNo(String fieldNo) {
        this.fieldNo = fieldNo;
    }

    public String getCompass() {
        return compass;
    }

    public void setCompass(String compass) {
        this.compass = compass;
    }

    public String getNvr() {
        return nvr;
    }

    public void setNvr(String nvr) {
        this.nvr = nvr;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getSubstatus() {
        return substatus;
    }

    public void setSubstatus(String substatus) {
        this.substatus = substatus;
    }

    public String getVcnCameraUse() {
        return vcnCameraUse;
    }

    public void setVcnCameraUse(String vcnCameraUse) {
        this.vcnCameraUse = vcnCameraUse;
    }

    public String getAnalysisList() {
        return analysisList;
    }

    public void setAnalysisList(String analysisList) {
        this.analysisList = analysisList;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public String getTaskTypeList() {
        return taskTypeList;
    }

    public void setTaskTypeList(String taskTypeList) {
        this.taskTypeList = taskTypeList;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getIpIvs() {
        return ipIvs;
    }

    public void setIpIvs(String ipIvs) {
        this.ipIvs = ipIvs;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(String pictureCount) {
        this.pictureCount = pictureCount;
    }

    public String getEffectiveCamera() {
        return effectiveCamera;
    }

    public void setEffectiveCamera(String effectiveCamera) {
        this.effectiveCamera = effectiveCamera;
    }

}
