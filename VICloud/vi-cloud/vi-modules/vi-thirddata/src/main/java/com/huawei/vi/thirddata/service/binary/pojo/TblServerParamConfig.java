/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;


import com.huawei.utils.CommonUtil;
import com.huawei.vi.thirddata.service.ivsserver.ServiceCommonConst;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务器参数配置Bean
 *
 * @version 1.0, 2018年7月2日
 * @since 2018-07-02
 */
@Getter
@Setter
public class TblServerParamConfig implements Serializable {
    /**
     * Serializable
     */
    private static final long serialVersionUID = -2178733684438854294L;

    /**
     * 主鍵
     */
    private Integer id;

    /**
     * 用于记录临时存储费ID的值
     */
    private Integer pid;

    /**
     * 对接的标识
     */
    private String serviceFlag; // KeLai DongZhi iMOC IVS

    /**
     * 服務器Ip地址
     */
    private String serviceIpAddress;

    /**
     * 服务器用户名
     */
    private String serviceName;

    /**
     * 服务器密码
     */
    private String servicePassword;

    /**
     * 服务器链路ID
     */
    private String serviceLinkId;

    /**
     * 数据协议类型
     */
    private String serviceDataType;

    /**
     * 链接状态
     */
    private String serviceStatus;

    /**
     * 东芝的参数-端口
     */
    private String dzPort; // 东芝的参数-端口

    /**
     * 用户id-预留
     */
    private String message;

    /**
     * 用户操作时间-预留
     */
    private Date serviceTime;

    /**
     * 服务器子类型
     */
    private Integer serviceSubType;

    /**
     * 服务器子类型ID
     */
    private String serviceSubId;

    /**
     * 中间件port
     */
    private String middlePort;

    /**
     * 中间件httpport
     */
    private String middleHttpPort;

    /**
     * 中间件tcpport
     */
    private String middleTcpPort;

    /**
     * keep中间件类型
     */
    private String keepType;

    /**
     * 文件服务器类型
     */
    private String fileServiceType;

    /**
     * 显示标识 1显示，0不显示,默认1显示
     */
    private int showFlag = ServiceCommonConst.SHOW_YES;

    /**
     * 备注
     */
    private String remark;

    /**
     * 服务器参数配置时间get方法空校验
     *
     * @return Date
     */
    public Date getServiceTime() {
        if (serviceTime != null) {
            return (Date) serviceTime.clone();
        } else {
            return null;
        }
    }

    /**
     * 服务器参数配置时间set方法空校验
     *
     * @param serviceTime 默认时间
     */
    public void setServiceTime(Date serviceTime) {
        if (CommonUtil.isNull(serviceTime)) {
            this.serviceTime = null;
        } else {
            this.serviceTime = (Date) serviceTime.clone();
        }
    }
}