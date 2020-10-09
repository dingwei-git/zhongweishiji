/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.jovision.jaws.common.config.vi;

import lombok.Getter;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数配置的抽象实现
 *
 * @param <> 参数配置界面对应的Bean
 * @since 2020-03-23
 */
public class AbstractParamConfig<T> {
    /**
     * 参数配置map
     */
    @Getter
    protected static volatile Map<String, Object> paramConfigMaps;

    static {
        paramConfigMaps = new HashMap<>();
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParamConfig.class);
}
