package com.huawei.vi.entity.vo;

import lombok.Data;

@Data
public class ImageDayStatisticsVcnVO {

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
    * 类型1位人脸，2为过车
    * */
    private Integer type;

    /*
    * 大小图片类型，1为小图2大图
    * */
    private Integer picType;

    /*
    * 图片传输统计类型，1存储2转发
    * */
    private Integer tranType;

    /*
    * 备注 设备名称
    * */
    private String remark;

    /*
    * 来源
    * */
    private String source;

    /*
    * 用户名
    * */
    private String userName;

    /*
    * 密码
    * */
    private String password;

    /*
    * ip地址
    * */
    private String addressIp;

    /*
    * 端口号
    * */
    private String port;

}
