package com.huawei.vi.entity.po;

import lombok.Data;

@Data
public class ImageDayStatisticsVcn {

    /*
    * 域编码
    * */
    private String domainCode;

    /*
    * 摄像机编码
    * */
    private String cameraSn;

    /*
    * 开始时间
    * */
    private String startTime;

    /*
    * 结束时间
    * */
    private String endTime;

    /*
    * 摄像机名称
    * */
    private String cameraName;

    /*
    * 类型1位人脸，2为过车
    * */
    private Integer type;

    /*
    * 大小图片类型，1小图2大图
    * */
    private Integer picType;

    /*
    * 图片传输统计类型，1存储2转发
    * */
    private Integer tranType;

    /*
    * 来源1：VCM 2:VCN
    * */
    private Integer source;

    /*
    * 图片数量
    * */
    private Integer pictureCount;

    /*
    * 有效摄像机标志
    * */
    private Integer effectiveCamera;


}
