package com.huawei.vi.entity.model;

import lombok.Data;

import java.util.List;

/**
 * @ClassName : IPCLevel
 * @Description : 设备层级关系
 * @Author : pangxh
 * @Date: 2020-08-13 22:40
 */
@Data
public class IPCLevel {

    /**
     * 设备层级名称
     */
    private String levelName;

    /**
     * 层级id
     */
    private String levelId;

    /**
     * 子层级
     */
    private List<IPCLevel> childLevel;

    /**
     * 子层级的设备信息
     */
    private List<TblIpcIpSyncRecord> childIpcInfo;

}
