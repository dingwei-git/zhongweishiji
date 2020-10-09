/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.entity.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述 组网关系实体类
 *
 * @version 1.0, 2018年6月28日
 * @since 2019-09-04
 */
@Getter
@Setter
public class TblNetworkRelationship implements Serializable {
    /**
     * 日志
     */
    private static final long serialVersionUID = 1L;

    /**
     * 分局/派出所/警务室/共杆编码/摄像机IP
     */
    private String name;

    /**
     * zTree组网关系Id
     */
    private String id;

    /**
     * zTree组网关系父Id
     */
    private String pid;

    private String pname;

    /**
     * 子集合(用于配置zTree无限扩展)
     */
    private List<TblNetworkRelationship> childList;

    /**
     * 分局
     */
    private String organization;

    /**
     * 派出所
     */
    private String platform;

    /**
     * 摄像机IP
     */
    private String ip;

    /**
     * 摄像机名称
     */
    private String cameraName;

    /**
     * 是否免考核
     */
    private String checkFlag;

    /**
     * 摄像机编码
     */
    private String cameraNum;

    /**
     * 杆编码
     */
    private String poleNum;

    private String station;

    // 摄像机在线个数
    private int onlineCount;

    // 摄像机总个数
    private int totalCount;

    /**
     * TblNetworkRelationship
     *
     * @param name name
     * @param id id
     * @param pid pid
     */
    public TblNetworkRelationship(String name, String id, String pid) {
        this.name = name;
        this.id = id;
        this.pid = pid;
    }

    /**
     * TblNetworkRelationship
     */
    public TblNetworkRelationship() {
    }

    /**
     * toString
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuffer networkRelationshipInfo = new StringBuffer(1024);
        networkRelationshipInfo.append("TblNetworkRelationship [name=" + name);
        networkRelationshipInfo.append(", id=" + id);
        networkRelationshipInfo.append(", pid=" + pid);
        networkRelationshipInfo.append(", childList=" + childList);
        networkRelationshipInfo.append(",camera_name=" + cameraName);
        networkRelationshipInfo.append(",organization=" + organization);
        networkRelationshipInfo.append(",ip=" + ip);
        networkRelationshipInfo.append(",platform=" + platform + "]");
        return networkRelationshipInfo.toString();
    }
}
