package com.huawei.vi.workorder.service;

import com.huawei.vi.entity.po.TblIpcIp;

import java.util.Map;

public interface IpcIpService {

    /**查询设备详情*/
    Map getIpcInfo(String cameraId);
}
