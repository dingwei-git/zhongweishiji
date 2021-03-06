package com.huawei.vi.workorder.mapper;

import com.huawei.vi.entity.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface UserMapper {

    UserPO getInfo(String userId);

    List queryPermission(Map condition);
}
