package com.huawei.vi.workorder.service.impl;


import com.huawei.vi.entity.po.FaultTypePO;
import com.huawei.vi.entity.vo.OrderDetailVO;
import com.huawei.vi.workorder.mapper.FaultTypeMapper;
import com.huawei.vi.workorder.mapper.OrderMapper;
import com.huawei.vi.workorder.service.FaultTypeService;
import com.jovision.jaws.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaultTypeServiceImpl implements FaultTypeService {

    @Autowired
    private FaultTypeMapper faultTypeMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List queryAll() {
        return faultTypeMapper.queryAll();
    }


    @Override
    public List queryFaultInfo(String orderCode) {

        if(StringUtils.isNotEmpty(orderCode)){
            OrderDetailVO order = orderMapper.getOrderInfo(orderCode);
            String faultTypes = order.getFaultTypes();
            String[] faults =  faultTypes.split(",");
            List<FaultTypePO> list = faultTypeMapper.queryFaultInfo(faults);
            return list;
        }else{
            return faultTypeMapper.queryFaultInfo(null);
        }

    }
}
