/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @since 2019-08-15
 */

@Component("tokenAuth")
public class TokenAuth {
    @Value("#{'TOKEN.KEY'}")
    private String tokenKey;

    @Value("#{'APP.NAME'}")
    private String appName;

    @Value("#{'EFFECTIVETIME'}")
    private String effectiveTime;

    @Value("#{'ISSUSER'}")
    private String issUser;

    @Value("#{'USER'}")
    private String userName;

    @Value("#{'VIDEOINSIGHT.KEY'}")
    private String authVideoId;

    @Value("#{'COLLECTER.KEY'}")
    private String collecterId;

    /**
     * 返回 tokenKey
     *
     * @return tokenKey值
     */
    public String getTokenKey() {
        return tokenKey;
    }

    /**
     * 返回 appName
     *
     * @return appName值
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 返回 effectiveTime
     *
     * @return effectiveTime值
     */
    public long getEffectiveTime() {
        return Long.parseLong(effectiveTime);
    }

    /**
     * 返回 issUser
     *
     * @return issUser值
     */
    public String getIssUser() {
        return issUser;
    }

    /**
     * 返回 userName
     *
     * @return userName值
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 返回 authVideoId
     *
     * @return authVideoId值
     */
    public String getAuthVideoId() {
        return authVideoId;
    }

    /**
     * 返回 collecterId
     *
     * @return collecterId值
     */
    public String getCollecterId() {
        return collecterId;
    }
}
