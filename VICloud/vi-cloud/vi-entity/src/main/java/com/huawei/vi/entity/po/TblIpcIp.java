/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.entity.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * IPC管理类
 *
 * @version 1.0, 2018年6月28日
 * @since 2019-09-04
 */
@Getter
@Setter
public class TblIpcIp implements Serializable, Cloneable {
    /**
     * 表:tbl_ipc_ip中定义的列名: ip
     */
    public static final String IP = "ip";

    /**
     * cameraName 设备名称
     */
    public static final String CAMERA_NAME = "device_name";

    /**
     * organization 分局
     */
    public static final String ORGANIZATION = "organization";

    /**
     * platform 平台
     */
    public static final String PLATFORM = "platform";

    /**
     * 日志
     */

    private static final long serialVersionUID = 1L;

    /**
     * ip
     */
    protected String ip;

    /**
     * 摄像机名称
     */
    protected String cameraName;

    /**
     * 组织
     */
    protected String organization;

    /**
     * 平台
     */
    protected String platform;

    /**
     * 摄像机编码
     */
    protected String cameraCode;

    /**
     * 离线时间
     */
    protected String lastOnlineTime;

    /**
     * 摄像机ID
     */
    protected String cameraId;

    /**
     * 共杠编码
     * */
    protected String copoleCode;
    /**
     * 设备类型
     * */
    protected String ipType;
    /**
     * 经度
     * */
    protected String longitude;
    /**
     * 维度
     * */
    protected String latitude;

    /**
     * 分辨率
     * */
    private String hd;
    /**厂家*/
private String manufacturer;




    @Override
    public TblIpcIp clone() {
        try {
            return (TblIpcIp) super.clone();
        } catch (CloneNotSupportedException e) {
            return new TblIpcIp();
        }
    }
}
