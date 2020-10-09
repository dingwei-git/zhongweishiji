package com.huawei.vi.workorder.service.impl;

import com.huawei.vi.entity.po.WorkOrderPicturePO;
import com.huawei.vi.workorder.mapper.WorkOrderPictureMapper;
import com.huawei.vi.workorder.service.WorkOrderPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderPictureServiceImpl implements WorkOrderPictureService {
    @Autowired
    private WorkOrderPictureMapper workOrderPictureMapper;
    @Override
    public int insertOrUpdateOrderPic(WorkOrderPicturePO workOrderPicture) {
        return workOrderPictureMapper.insertOrUpdateOrderPic(workOrderPicture);
    }

    @Override
    public int updateOrderPic(WorkOrderPicturePO workOrderPicture) {
        return workOrderPictureMapper.updateOrderPic(workOrderPicture);
    }
}
