package com.jovision.jaws.common.util;

import com.jovision.jaws.common.constant.CommonConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;


public class JwtTokenManager {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenManager.class);

    public static Map<String, String> getTokenFromRedis(RedisUtil  tokenRedisTemplate,String jwtTokenSecretCurrent,String requestToken) {
        int index = requestToken.indexOf("_web_");
        String realtoken = requestToken.substring(index+5);
        Map<String, String> resultMap = new HashMap<>();
        Claims claims = parseJWT(realtoken, jwtTokenSecretCurrent);
        if (claims == null) {
            LOG.error("JwtTokenManager.getTokenFromRedis--claims");
            return null;
        }
        long exp = Long.parseLong(claims.get("expireDate").toString());
        //校验有效性
        if ((exp - System.currentTimeMillis()) < 0) {
            LOG.error("JwtTokenManager.getTokenFromRedis--exp");
            return null;
        }
        String userId = claims.get("user_id") == null ? "" : claims.get("user_id").toString();
        if (StringUtils.isBlank(userId)) {
            LOG.error("JwtTokenManager.getTokenFromRedis--userId");
            return null;
        }
        String userName = claims.get("user_name") == null ? "" : claims.get("user_name").toString();
        if (StringUtils.isBlank(userName)) {
            LOG.error("JwtTokenManager.getTokenFromRedis--userName");
            return null;
        }

        //redis获取token
        String value = tokenRedisTemplate.getValue(requestToken);
        if ("1".equals(value)) {
            resultMap.put("token", requestToken);
            resultMap.put("userId", userId);
            resultMap.put(CommonConst.LOGIN_USER, userName);
            return resultMap;
        }
        return null;
    }


    private static Claims parseJWT(String token, String secret) {
        //签名秘钥，和生成的签名的秘钥一模一样
        //得到DefaultJwtParser
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(secret.getBytes())
                    //设置需要解析的jwt
                    .parseClaimsJws(token).getBody();
        } catch (Exception ex) {
            return null;
        }
        return claims;
    }

}
