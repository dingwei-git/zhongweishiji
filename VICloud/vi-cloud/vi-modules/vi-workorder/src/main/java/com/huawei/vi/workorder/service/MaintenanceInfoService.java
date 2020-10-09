package com.huawei.vi.workorder.service;


import com.huawei.vi.entity.po.MaintenanceInfoEntity;

public interface MaintenanceInfoService {
    int updateRepairInfo(MaintenanceInfoEntity maintenanceInfoEntity);
    int updateByOrderCode(MaintenanceInfoEntity maintenanceInfoEntity);
    MaintenanceInfoEntity selectByOrderCode(String orderCode);
}
