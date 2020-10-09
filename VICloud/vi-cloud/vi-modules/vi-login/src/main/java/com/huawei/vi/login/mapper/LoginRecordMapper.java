package com.huawei.vi.login.mapper;

import com.huawei.vi.entity.po.LoginRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Param;
@Mapper
@Component
public interface LoginRecordMapper {
    /**
     * 根据用户名查询
     * */
    LoginRecordPO selectByUser(@Param("userName")String userName,@Param("origin") String origin);

    /**
     * 根据用户名删除登录记录
     * */
    void deleteUserName(@Param("userTblName") String userName,@Param("origin") String origin);

    int insert(LoginRecordPO loginRecordPO);

    int update(LoginRecordPO loginRecordPO);

}
