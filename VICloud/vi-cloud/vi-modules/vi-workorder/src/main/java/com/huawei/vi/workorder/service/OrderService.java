package com.huawei.vi.workorder.service;

import com.huawei.vi.entity.vo.OrderDetailVO;
import com.huawei.vi.entity.vo.OrderOperateVo;
import com.huawei.vi.entity.vo.RepairVo;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface OrderService {

    /**
     * 根据工单状态和维修人员查询工单列表
     * */
    RestResult queryOrderList(Map condition);
    /**
     *根据工单id，查询工单详情
     * */
    OrderDetailVO getOrderInfo(String orderCode);
    /**
     * 工单管理页面接口，查询待做、进行中、已完成工单的数量
     * */
    List getCountByStatus(String userId);

    /**
     * 工单维修操作
     *
     * @return*/
    RestResult updateRepair(MultipartFile[] files, RepairVo repairVo,String userId,String recheck,String token);

    RestResult updateRepairFeedBack(OrderOperateVo operateVo, String userId);



}
