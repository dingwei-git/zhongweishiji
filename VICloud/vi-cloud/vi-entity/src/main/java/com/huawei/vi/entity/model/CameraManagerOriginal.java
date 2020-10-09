package com.huawei.vi.entity.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CameraManagerOriginal implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /*
    * 摄像机编码
    * */
    private String cameraSn;

    /*
    * 摄像机名称
    * */
    private String cameraName;

    /*
    * 安装高度
    * */
    private String mountHeight;

    /*
    * 摄像机经度
    * */
    private String longitude;

    /*
    * 摄像机纬度
    * */
    private String latitude;

    /*
    * 摄像机地址
    * */
    private String address;

    /*
    * 摄像机IP，取流url
    * */
    private String streamUrl;

    /*
    * 摄像机方位
    * */
    private String direction;

    /*
    * 车道
    * */
    private String lane;

    /*
    * 摄像机在线状态0：离线，1：在线
    * */
    private Integer cameraState;

    /*
    * 摄像机类型
    * */
    private String cameraType;

    /*
    * 摄像机用途
    * */
    private String cameraUse;

    /*
    * 摄像机功能
    * */
    private String cameraFeature;

    /*
    * 平台名称
    * */
    private String playName;

    /*
    * 分辨率
    * */
    private String resolutionType;

    /*
    * 域编码
    * */
    private String fieldNo;

    /*
    * 罗盘
    * */
    private String compass;

    /*
    * 摄像机nvr
    * */
    private String nvr;

    /*
    * 组织id
    * */
    private String groupId;

    /*
    * 摄像机id
    * */
    private String cameraId;

    /*
    * 0:未订阅  1：已订阅
    * */
    private Integer substatus;

    /*
    * 相机属性0：普通相机 1：人脸卡口物理相机
    * */
    private Integer vcnCameraUse;

    /*
    * 智能分析列表
    * */
    private String analysisList;

    /*
    * 分析类型0001，车牌识别；0010，人脸识别；0100，行为分析；1000，人体分析；1001，人车混合结构化；,
    * */
    private String analysisType;

    /*
    * 智能任务列表
    * */
    private String taskTypeList;

    /*
    * 智能任务类型0：行为分析 1：车牌分析 2：人脸分析 4：人体分析 9：人车混合结构化
    * */
    private Integer taskType;

    /*
    * IVS平台IP地址
    * */
    private String ipIvs;

}
