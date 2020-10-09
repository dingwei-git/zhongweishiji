package com.huawei.vi.login.mapper;

import com.huawei.vi.login.po.UserInfo;
import com.huawei.vi.login.po.WebUserPO;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

@Mapper
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface WebUserManagerMapper {
    List<WebUserPO> selectByCondition(Map<String, Object> condition) throws DataAccessException;

    /**
     * 单个用户信息查询
     *
     * @param userTblName 用户名
     * @return 单个用户信息
     */
    UserInfo selectUserInfo(@Param("userTblName") String userTblName);
}
