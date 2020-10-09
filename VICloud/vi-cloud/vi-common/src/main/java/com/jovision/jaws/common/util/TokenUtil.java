/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;


import com.huawei.utils.NumConstant;
import com.huawei.utils.SpringUtil;
import com.jovision.jaws.common.properties.TokenAuth;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * token生成工具类
 *
 * @since 2019-11-13
 */
public class TokenUtil {
    /**
     * token废弃池
     */
    public static final ConcurrentHashMap<String, Long> TOKEN_OVERDUE_MAP = new ConcurrentHashMap<String, Long>();

    private static final Logger LOG = LoggerFactory.getLogger(TokenUtil.class);

    private static final byte[] SEEDS = SecureRandom.getSeed(NumConstant.NUM_20);

    /**
     * 数字范围 0-9
     */
    private static final int NUM_RANGE = 10;

    /**
     * 26个英文字母
     */
    private static final int LETTER_RANGE = 26;

    /**
     * 小写a对应的ASCII
     */
    private static final int ASCII_LOWER_A = 97;

    private static TokenUtil instance;

    @Autowired
    private TokenAuth tokenAuth;

    private TokenUtil() {
        if (tokenAuth == null) {
            tokenAuth = SpringUtil.getBean(TokenAuth.class);
        }
    }

    public static TokenUtil getInstance() {
        return TokenUtilBuild.instance;
    }

    /**
     * 对过期token进行删除
     */
    public static void tokenAuthRemove() {
        long nowMillis = System.currentTimeMillis();
        Iterator<Entry<String, Long>> it = TOKEN_OVERDUE_MAP.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Long> entry = it.next();
            if (entry.getValue() - nowMillis < 0) {
                it.remove();
            }
        }
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
            String str = random.nextInt(NumConstant.NUM_2) % NumConstant.NUM_2 == 0 ? "num" : "char";
            if ("char".equalsIgnoreCase(str)) {
                builder.append((char) (ASCII_LOWER_A + random.nextInt(LETTER_RANGE)));
            } else if ("num".equalsIgnoreCase(str)) {
                builder.append(random.nextInt(NUM_RANGE));
            }
        }
        random.nextBytes(SEEDS);
        return builder.toString();
    }

    /**
     * token获取
     *
     * @return token
     */
    public synchronized String getToken() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Key key = null;
        try {
            key = Keys.hmacShaKeyFor(tokenAuth.getCollecterId().getBytes(StandardCharsets.UTF_8));
        } catch (WeakKeyException e) {
            LOG.error("token get error: {}", e.getMessage());
            return "-1";
        }
        SecretKey secretKey = (SecretKey) key;

        JwtBuilder builder = Jwts.builder()
            .setId(getRandom())
            .setSubject(tokenAuth.getIssUser())
            .setIssuedAt(now)
            .signWith(signatureAlgorithm, secretKey);

        long expMillis = nowMillis + tokenAuth.getEffectiveTime();
        Date expDate = new Date(expMillis);
        //builder.setExpiration(expDate);
        return builder.compact();
    }

    /**
     * 内部类进行单例对象初始化
     *
     * @since 2019-11-06
     */
    private static class TokenUtilBuild {
        private static TokenUtil instance = new TokenUtil();
    }
}
