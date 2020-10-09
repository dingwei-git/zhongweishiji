package com.huawei.vi.workorder.service.impl;


import com.huawei.vi.entity.po.ForbackTempletePO;
import com.huawei.vi.entity.vo.OrderDetailVO;
import com.huawei.vi.workorder.mapper.ForbackTempleteMapper;
import com.huawei.vi.workorder.mapper.OrderMapper;
import com.huawei.vi.workorder.service.ForbackTempleteService;
import com.jovision.jaws.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForbackTempleteServiceImpl implements ForbackTempleteService {
    @Autowired
    private ForbackTempleteMapper forbackTempleteMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public ForbackTempletePO getTemplete(String orderCode) {
        List<ForbackTempletePO> resultList = new ArrayList();
        String[] all ={"99"};
        if(StringUtils.isNotEmpty(orderCode)){
            OrderDetailVO order = orderMapper.getOrderInfo(orderCode);
            String faultTypes = order.getFaultTypes();

            String[] faults =  faultTypes.split(",");
            if(faults!=null&&faults.length<2){
                resultList = forbackTempleteMapper.getTemplete(faults);
            }else{
                resultList = forbackTempleteMapper.getTemplete(all);
            }
        }
        StringBuffer models = new StringBuffer();
        if(resultList!=null&&resultList.size()>0){
            ForbackTempletePO forbackTempletePO = resultList.get(0);
            String model = forbackTempletePO.getModel();
            System.out.println(model);
            model.replaceAll("\\\\\\\\","\\\\");
            forbackTempletePO.setModel(model);
            return forbackTempletePO;
        }else {
            return null;
        }

    }
}
