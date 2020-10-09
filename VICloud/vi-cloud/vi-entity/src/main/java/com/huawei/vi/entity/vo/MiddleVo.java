/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.entity.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述 中间件对象
 *
 * @since 2019-10-31
 */
@Getter
@Setter
public class MiddleVo {
    private String httpport;

    private String ip;

    private String keep;

    private String middleid;

    private String port;

    private String pwd;

    private String sipport;

    private String tcpport;

    private String user;
}
