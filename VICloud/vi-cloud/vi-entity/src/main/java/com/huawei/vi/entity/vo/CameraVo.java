package com.huawei.vi.entity.vo;

import lombok.Data;

@Data
public class CameraVo {

    /*
    *设备名称
    * */
    private String cameraName;

    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页行数
     */
    private int rowsPerPage;

}
