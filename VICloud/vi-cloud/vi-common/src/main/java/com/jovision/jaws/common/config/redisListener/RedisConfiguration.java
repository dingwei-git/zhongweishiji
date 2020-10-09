package com.jovision.jaws.common.config.redisListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
/**
 * 配置redis监听事件
 * Created by lpgu on 2019/8/1.
 */
@Configuration
public class RedisConfiguration {
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(){
        RedisMessageListenerContainer redisMessageListenerContainer=new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        return redisMessageListenerContainer;
    }
    @Bean
    public KeyExpirationEventMessageListener keyExpirationEventMessageListener(){
        return new KeyExpirationEventMessageListener(this.redisMessageListenerContainer());
    }

}
