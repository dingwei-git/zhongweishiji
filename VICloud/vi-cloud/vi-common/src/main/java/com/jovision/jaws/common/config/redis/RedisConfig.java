package com.jovision.jaws.common.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;


/**
 * Redis数据连接池及公共参数
 * todo 暂未处理apollo变更业务逻辑
 * @Author: ABug
 * @Date: 2020/1/4 20:17
 * @Version V1.0.0
 **/
@EnableCaching
@Configuration
public class RedisConfig {
    @Value("${spring.redis.jedis.pool.max-active}")
    private int redisPoolMaxActive;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int redisPoolMaxWait;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int redisPoolMaxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int redisPoolMinIdle;

    public RedisConnectionFactory getRedisConnectionFactory(String hostName,
                                                            String password, int port, int timeout, int database) { // 是负责建立Factory的连接工厂类
        JedisConnectionFactory jedisFactory = new JedisConnectionFactory();
        jedisFactory.setHostName(hostName);
        jedisFactory.setPort(port);
        jedisFactory.setPassword(password);
        jedisFactory.setDatabase(database);
        jedisFactory.setTimeout(timeout);
        JedisPoolConfig poolConfig = new JedisPoolConfig(); // 进行连接池配置
        poolConfig.setMaxTotal(redisPoolMaxActive);
        poolConfig.setMaxIdle(redisPoolMaxIdle);
        poolConfig.setMinIdle(redisPoolMinIdle);
        poolConfig.setMaxWaitMillis(redisPoolMaxWait);
        jedisFactory.setPoolConfig(poolConfig);
        jedisFactory.afterPropertiesSet(); // 初始化连接池配置
        return jedisFactory;
    }

    /**
     * UMS redis配置
     */
    @Value("${spring.redis.redis-jwt-token.host}")
    private String redisUmsHost;
    @Value("${spring.redis.redis-jwt-token.port}")
    private int redisUmsPort;
    @Value("${spring.redis.redis-jwt-token.password}")
    private String redisUmsPassword;
    @Value("${spring.redis.redis-jwt-token.timeout}")
    private int redisUmsTimeout;
    @Value("${spring.redis.redis-jwt-token.database}")
    private int redisUmsDatabase;

    @Bean("redisUmsTemplate")
    public StringRedisTemplate getRedisTokenTemplate() {
        RedisConnectionFactory factory = this.getRedisConnectionFactory(
                redisUmsHost, redisUmsPassword, redisUmsPort,redisUmsTimeout,redisUmsDatabase); // 建立Redis的连接
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    @Bean("redisConnectionFac")
    public RedisConnectionFactory getRedisConnectionFactory() {
        RedisConnectionFactory factory = this.getRedisConnectionFactory(
                redisUmsHost, redisUmsPassword, redisUmsPort,redisUmsTimeout,redisUmsDatabase); // 建立Redis的连接
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        return factory;
    }
}
