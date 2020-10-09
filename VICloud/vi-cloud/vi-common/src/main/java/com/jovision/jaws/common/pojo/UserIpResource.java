
package com.jovision.jaws.common.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户ip资源实体
 *
 * @since 2020-05-14
 */
@Getter
@Setter
public class UserIpResource implements Serializable {
    /**
     * tbl_user_ipc_resource表中对应的字段user_id
     */
    public static final String USER_ID = "userId";

    /**
     * tbl_user_ipc_resource表传入的key值ipList
     */
    public static final String IP_LIST = "ipList";

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    // 用户id
    private int userId;

    // 用户id所对应的ipc
    private String ip;
}
