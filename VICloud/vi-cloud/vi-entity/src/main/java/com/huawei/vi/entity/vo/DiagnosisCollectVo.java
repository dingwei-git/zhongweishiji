/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.entity.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述 诊断、采集服务器对象
 *
 * @since 2019-10-31
 */
@Getter
@Setter
public class DiagnosisCollectVo {
    private String ip;

    private String port;

    private String user;

    private String pwd;

    private String id;
}
