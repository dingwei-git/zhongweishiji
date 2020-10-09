package com.jovision.jaws.common.config.jwt;



import com.jovision.jaws.common.constant.TokenConstant;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt token工具
 *
 * @Author: ABug
 * @Date: 2020/1/4 19:12
 * @Version V1.0.0
 **/
@Component
@Slf4j
public class JwtTokenUtil {

    /**
     * 用户登录成功后生成Jwt
     * 使用HS256算法
     *
     * @param userId 用户ID
     * @param userName 用户名
     * @return
     */
    public String createJWTToken(String userId, String userName , Long tokenExpire) {
        //实际使用的秘钥,默认用最新的
        String useJwtToken = TokenConstant.TOKEN_SECRET;
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userId);
        claims.put("user_name", userName);
        claims.put("tokenExpire",tokenExpire);
        claims.put("expireDate",nowMillis+tokenExpire*1000);
        Key key = Keys.hmacShaKeyFor(useJwtToken.getBytes());
        //下面就是在为payload添加各种标准声明和私有声明了
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
//                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(userId)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(key, signatureAlgorithm);
        return builder.compact();
    }
}