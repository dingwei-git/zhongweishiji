/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.jovision.jaws.common.util;

import com.huawei.threadfactory.DefaultThreadFactory;
import lombok.Getter;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 定时任务线程池生成工具类
 *
 * @since 2020-04-10
 */
public class ScheduledThreadPoolUtil {
    @Getter
    private ScheduledThreadPoolExecutor offlineScheduledThreadPool;

    @Getter
    private ScheduledThreadPoolExecutor collectionScheduledThreadPool;

    private ScheduledThreadPoolUtil() {
        /**
         * 离线定时器线程池
         */
        offlineScheduledThreadPool =
            new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("offlineScheduledThread"));
        /**
         * 周期采集定时器线程池
         */
        collectionScheduledThreadPool =
            new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("collectionScheduledThread"));
    }

    /**
     * 单例模式
     *
     * @return Host
     */
    public static ScheduledThreadPoolUtil getInstance() {
        return SingletonHolder.THREAD_POOL_UTIL_POOLUTIL;
    }

    /**
     * 通过static内部类实现单例
     *
     * @since 2020-04-10
     */
    private static class SingletonHolder {
        private static final ScheduledThreadPoolUtil THREAD_POOL_UTIL_POOLUTIL = new ScheduledThreadPoolUtil();
    }
}
