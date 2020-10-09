/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统信息实体类
 *
 * @since 2019-09-17
 */
@Getter
@Setter
public class Host {
    private String scheme;

    private int localPort;

    private String ip;

    private Host() {
    }

    /**
     * 单例模式
     *
     * @return Host
     */
    public static Host getInstance() {
        return SingletonHolder.HOST_INSTANCE;
    }

    /**
     * 通过static内部类实现单例
     *
     * @since 2019-09-17
     */
    private static class SingletonHolder {
        private static final Host HOST_INSTANCE = new Host();
    }
}
