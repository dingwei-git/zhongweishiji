package com.jovision.jaws.common.config.redisListener;

import com.jovision.jaws.common.config.redis.RedisOperatingService;
import com.jovision.jaws.common.util.StringUtils;
import com.jovision.jaws.common.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * redis的key过期监听
 * Created by lpgu on 2019/8/1.
 */
@Component
public class KeyExpiredListener extends KeyExpirationEventMessageListener {
    private final static Logger logger = LoggerFactory.getLogger(KeyExpiredListener.class);
    @Autowired
    private RedisOperatingService redisOperatingService;
    @Value("${WEB.webLoginLimitExpire}")
    private int webLoginLimitExpire = 10;
    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //key=iot:device:command:868474043308814_b39d7fb91ef74ff6a205761b62cff0fb_aaaaaa
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        //过期的key
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        deleteWand(key);
        logger.info("redis key 过期：pattern={},channel={},key={}", new String(pattern), channel, key);
    }

    private void deleteWand(String key){
        try{
            if(StringUtils.isNotEmpty(key)&&key.contains("_web_")){
                String[] str = key.split("_");
//                String keys = key.substring(0,1)+"_web_count";
                String keys = str[0]+"_"+str[1]+"_count";
                synchronized(this){
                    String value = redisOperatingService.getValueByKey(keys);
                    String wand = Utils.getWand("delete",value,webLoginLimitExpire);
                    redisOperatingService.setTokenByTime(keys,wand);
                }
            }
        }catch (Exception e){
            logger.error("清除令牌失败={}"+e.getMessage());
        }

    }

}
