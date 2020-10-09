package com.huawei.vi.workorder.mapper;

import com.huawei.vi.entity.po.WorkOrderPicturePO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface WorkOrderPictureMapper {

    int  insertOrUpdateOrderPic(WorkOrderPicturePO workOrderPicture);

    int updateOrderPic(WorkOrderPicturePO workOrderPicture);
}
