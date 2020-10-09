/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;

/**
 * CsrfTokenManager
 *
 * @since 2019-03-02
 */
public final class CsrfTokenManager {
    /**
     * LOG 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(CsrfTokenManager.class);

    /**
     * 静态对象实力锁
     */
    private static final Object LOCK = new Object();

    private static final byte[] SEEDS = SecureRandom.getSeed(NumConstant.NUM_20);

    /**
     * token令牌名称
     */
    private static final String CSRF_PARAM_NAME = "token";

    private CsrfTokenManager() {
    }

    /**
     * getTokenFromRequest
     *
     * @param request 请求
     * @return String
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        return request.getParameter(CSRF_PARAM_NAME);
    }

    /**
     * 获取随机数 随机数对2求余为0？追加数字：追加字母
     *
     * @return String
     */
    public static String getRandom() {
        SecureRandom random = new SecureRandom();
        random.setSeed(SEEDS);
        StringBuilder builder = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        for (int loop = 0; loop < NumConstant.NUM_24; loop++) {
            builder.append(random.nextInt(NumConstant.NUM_2) % NumConstant.NUM_2 == 0
                ? (char) (NumConstant.NUM_97 + random.nextInt(NumConstant.NUM_26))
                : String.valueOf(random.nextInt(NumConstant.NUM_10)));
        }
        random.nextBytes(SEEDS);
        return builder.toString();
    }
}
