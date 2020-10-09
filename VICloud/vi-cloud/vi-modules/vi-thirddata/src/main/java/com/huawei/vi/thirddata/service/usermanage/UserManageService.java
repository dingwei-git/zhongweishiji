package com.huawei.vi.thirddata.service.usermanage;

import com.huawei.vi.thirddata.service.baseserv.IbaseServ;
import com.jovision.jaws.common.pojo.User;
import lombok.NonNull;

import java.util.List;

public interface UserManageService extends IbaseServ<User, Integer> {

    /**
     * 根据用户名查询用户
     *
     * @param userName 要查询的用户名
     * @return 查询出的对象
     */
    List<User> selectUserByName(@NonNull String userName);
}
