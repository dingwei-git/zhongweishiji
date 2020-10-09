package com.huawei.vi.login.service;

import com.huawei.vi.entity.po.UserPO;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.vo.ExecuteResult;

import java.util.Map;

public interface UserService {

    UserPO selectUser(Map selectMap);
    /**
     * 校验用户是否存在
     * @param userId
     * @return
     */
    Long getTotalByUserId(String userId);

    Integer getFrozenByUserId(String user_id);

    RestResult getUser(String userId);
    UserPO getInfo(String userId);
    /**
     * 修改密码
     * */
    int updatePassword(String userId,String newPasswordToEncryption);
    /**查询用户角色*/
    Map getRole(String userName);
}
