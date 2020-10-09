package com.huawei.vi.thirddata.mapper;


import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import com.jovision.jaws.common.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * 用户管理的mapper
 *
 * @since 2020-05-09
 */
@Repository(value = "userManagerMapper")
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface UserManagerMapper extends IbaseDao<User, Integer>{


}
