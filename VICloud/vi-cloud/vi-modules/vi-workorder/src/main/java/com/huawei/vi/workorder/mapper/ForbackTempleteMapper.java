package com.huawei.vi.workorder.mapper;

import com.huawei.vi.entity.po.ForbackTempletePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ForbackTempleteMapper {
    List<ForbackTempletePO> getTemplete(@Param("ids") String[] ids);
}
