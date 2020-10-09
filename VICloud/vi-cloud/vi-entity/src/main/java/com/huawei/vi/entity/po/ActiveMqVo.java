/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.entity.po;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述 消息服务器对象
 *
 * @since 2019-10-31
 */
@Getter
@Setter
public class ActiveMqVo {
    private String url;

    private String user;

    private String pwd;
}
