package com.huawei.vi.workorder.service;


import com.huawei.vi.entity.po.WorkOrderPicturePO;

public interface WorkOrderPictureService {

    int insertOrUpdateOrderPic(WorkOrderPicturePO workOrderPicture);
    int updateOrderPic(WorkOrderPicturePO workOrderPicture);

}
