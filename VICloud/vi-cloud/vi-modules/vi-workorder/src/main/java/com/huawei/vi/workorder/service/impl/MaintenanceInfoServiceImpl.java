package com.huawei.vi.workorder.service.impl;


import com.huawei.vi.entity.po.MaintenanceInfoEntity;
import com.huawei.vi.workorder.mapper.MaintenanceInfoMapper;
import com.huawei.vi.workorder.service.MaintenanceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceInfoServiceImpl implements MaintenanceInfoService {

    @Autowired
    private MaintenanceInfoMapper maintenanceInfoMapper;
    @Override
    public int updateRepairInfo(MaintenanceInfoEntity maintenanceInfoEntity) {
        return maintenanceInfoMapper.updateRepairInfo(maintenanceInfoEntity);
    }

    @Override
    public int updateByOrderCode(MaintenanceInfoEntity maintenanceInfoEntity) {
        return maintenanceInfoMapper.updateByOrderCode(maintenanceInfoEntity);
    }

    @Override
    public MaintenanceInfoEntity selectByOrderCode(String orderCode) {
        return maintenanceInfoMapper.selectByCode(orderCode);
    }
}
