package com.huawei.vi.workorder.service.impl;

import com.huawei.vi.entity.po.AlarmPO;
import com.huawei.vi.workorder.mapper.AlarmMapper;
import com.huawei.vi.workorder.service.AlarmService;
import com.jovision.jaws.common.constant.AppResultEnum;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;

    @Override
    public RestResult queryAlarmInfo(String alarmCode,String orderCode) {
        System.out.println("alarmCode:"+alarmCode);
        System.out.println("orderCode:"+orderCode);
        //先去查询tbl_alarm_order表，找到报警信息所在的表
        String alarmTblDate = alarmMapper.queryAlarmTblDate(alarmCode,orderCode);
        if(StringUtils.isEmpty(alarmTblDate)){
            return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),null);
        }
        Map condition = new HashMap();
        condition.put("alarmTableDate",alarmTblDate);
        condition.put("alarmCode",alarmCode);
        AlarmPO alarmPO = alarmMapper.queryAlarmInfo(condition);
        if(alarmPO!=null){

            Date start  =alarmPO.getStartTime();
            Date end = alarmPO.getEndTime();
            Long timeLong =0L;
            if(null==end){
                timeLong = (new Date().getTime()-start.getTime())/1000;
            }else{
                timeLong = (end.getTime()-start.getTime())/1000;
            }

            alarmPO.setTimeLong(timeLong);
            return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),alarmPO);
        }else{
            return RestResult.generateRestResult(AppResultEnum.ERROE.getCode(),AppResultEnum.ERROE.getMessage(),null);
        }
    }

    @Override
    public List<String> queryAlarmCode(String orderCode) {
        return alarmMapper.queryAlarmCode(orderCode);
    }
}
