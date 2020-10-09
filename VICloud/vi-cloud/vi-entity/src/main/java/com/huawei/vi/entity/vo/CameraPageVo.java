package com.huawei.vi.entity.vo;

import lombok.Data;

@Data
public class CameraPageVo {

    /*
    * 设备名称
    * */
    private String cameraName;

    /*
    * 设备ip
    * */
    private String cameraIp;

    /*
    * 设备编码
    * */
    private String cameraId;

    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页行数
     */
    private int rowsPerPage;



}
