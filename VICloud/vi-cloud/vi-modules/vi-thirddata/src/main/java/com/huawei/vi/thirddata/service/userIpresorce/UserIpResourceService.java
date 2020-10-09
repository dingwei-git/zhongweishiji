package com.huawei.vi.thirddata.service.userIpresorce;

import com.huawei.vi.thirddata.service.baseserv.IbaseServ;
import com.jovision.jaws.common.pojo.UserIpResource;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public interface UserIpResourceService extends IbaseServ<UserIpResource, String> {

    /**
     * 通过用户名和层级数(层级数若不传则默认查所有层级)查询用户所拥有的组织
     *
     * @param userName 用户名
     * @param levelNum 层级数
     * @param tableName 要进行关联的表名（tbl_ipc_ip或tbl_ipc_ip_tmp）
     * @return Map 组织层级map
     */
    Map<String, List<Map<String, String>>> getOrganizationsByUser(@NonNull String userName, Integer levelNum,
                                                                  @NonNull String tableName);



    /**
     * 根据用户只查询具体的ip
     *
     * @param loginUserName 查询的用户名
     * @param id 需要查询的组织id
     * @param tableName 需要查询的表名，tbl_ipc_ip是分析后，tbl_ipc_ip_tmp是最新组网表中ip
     * @return list ipc结果集
     */
    List<String> getIpcIpByUserOrId(@NonNull String loginUserName, List<String> id, @NonNull String tableName);



    /**
     * 根据用户查询所有的ipc的详细信息
     *
     * @param loginUserName 查询的用户名
     * @param id 需要查询的组织id
     * @param tableName 需要查询的表名，tbl_ipc_ip是分析后，tbl_ipc_ip_tmp是最新组网表中ip
     * @return list ipc结果集
     */
    List<Map<String, Object>> getIpcsByUser(@NonNull String loginUserName, List<String> id, @NonNull String tableName);

}
