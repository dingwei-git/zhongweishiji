package com.huawei.vi.login.mapper;

import com.huawei.vi.login.po.WebLoginRecordPO;
import com.huawei.vi.login.po.WebUserPO;
import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper
@Component
public interface WebLoginMapper {
    /**
     * 根据userName查询信息
     * @param usertmpName 参数
     * @return WebLoginRecordPO
     * @throws DataAccessException sql异常
     */
     WebLoginRecordPO selectByUserName(String usertmpName);

    /**
     * 根据userName查询信息
     *
     * @param usertmpName 参数
     * @throws DataAccessException sql异常
     */
    void deleteUserName(String usertmpName);

    WebUserPO selectUser(@NonNull Map<String, Object> map);
}
