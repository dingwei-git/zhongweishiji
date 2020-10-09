/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述 登录用户的session信息
 *
 * @since 2019-09-04
 */
@Getter
@Setter
public class SsoUser {
    /**
     * 用户IP
     */
    private String ip;

    /**
     * 登录记录时间
     */
    private long startTime;

    /**
     * 登录用户的sessionId
     */
    private String sessionId;
}