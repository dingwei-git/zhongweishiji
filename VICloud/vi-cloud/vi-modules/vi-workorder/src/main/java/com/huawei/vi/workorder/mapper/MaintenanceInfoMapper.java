package com.huawei.vi.workorder.mapper;

import com.huawei.vi.entity.po.MaintenanceInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MaintenanceInfoMapper {
    int updateRepairInfo(MaintenanceInfoEntity maintenanceInfoEntity);
    int updateByOrderCode(MaintenanceInfoEntity maintenanceInfoEntity);
    MaintenanceInfoEntity selectByCode(String OrderCode);
}
