package com.huawei.vi.workorder.mapper;

import com.huawei.vi.entity.po.AlarmPO;
import com.huawei.vi.entity.po.FaultTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface AlarmMapper {

    AlarmPO queryAlarmInfo(Map map);

    /**查询报警编号对应的报警信息所在表*/
    String queryAlarmTblDate(@Param("alarmCode") String alarmCode, @Param("orderCode") String orderCode);

    List<String> queryAlarmCode(String orderCode);

}
