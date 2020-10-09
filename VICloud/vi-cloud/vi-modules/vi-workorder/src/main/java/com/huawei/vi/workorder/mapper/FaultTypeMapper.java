package com.huawei.vi.workorder.mapper;

import com.huawei.vi.entity.po.FaultTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface FaultTypeMapper {

    List queryAll();
    List<FaultTypePO> queryFaultInfo(@Param("ids") String[] ids);
}
