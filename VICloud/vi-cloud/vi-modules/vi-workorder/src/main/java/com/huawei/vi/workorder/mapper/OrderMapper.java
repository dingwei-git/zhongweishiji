package com.huawei.vi.workorder.mapper;


import com.huawei.vi.entity.po.OrderEntity;
import com.huawei.vi.entity.vo.OrderDetailVO;
import com.huawei.vi.entity.vo.OrderListVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface OrderMapper {

    List<OrderListVO> queryOrderList(Map condition);
    int queryOrderListCount(Map condition);
    /**
     * 获取订单详情
     * */
    OrderDetailVO getOrderInfo(String orderCode);

    List getCountByStatus(String userId);

    int updateOrderStatus(OrderEntity orderEntity);

    String findByOrderCode(String orderCode);

    List<String> selectStatusByOrderCode(String orderCode);
}
