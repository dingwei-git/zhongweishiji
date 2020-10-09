package com.huawei.vi.workorder.mapper;

import com.huawei.vi.entity.po.LogManagerPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface LogManagerMapper {
    List<LogManagerPO> queryLog(String orderCode);

    int insertLog(LogManagerPO logManagerPO);

    List<LogManagerPO> queryForback(Map map);
}
