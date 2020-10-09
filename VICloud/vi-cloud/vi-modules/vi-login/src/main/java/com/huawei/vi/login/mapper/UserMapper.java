package com.huawei.vi.login.mapper;

import com.huawei.vi.entity.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper
@Component
public interface UserMapper {
    //TODO
    UserPO selectUser(Map selectMap);


    /**
     * 校验用户是否存在
     * @param userId
     * @return
     */
    Long getTotalByUserId(String userId);
    Integer getFrozenByUserId(String user_id);
    UserPO getUser(String userId);
    UserPO getInfo(String userId);
    int updatePassword(Map map);
    /**查询用户角色*/
    Map getRole(String userName);
}
