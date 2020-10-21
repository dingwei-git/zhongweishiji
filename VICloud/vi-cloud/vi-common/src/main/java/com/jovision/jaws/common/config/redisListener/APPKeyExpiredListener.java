package com.jovision.jaws.common.config.redisListener;

import com.jovision.jaws.common.config.redis.RedisOperatingService;
import com.jovision.jaws.common.constant.TokenConstant;
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
public class APPKeyExpiredListener extends KeyExpirationEventMessageListener {
    private final static Logger logger = LoggerFactory.getLogger(APPKeyExpiredListener.class);

    @Autowired
    private RedisOperatingService redisOperatingService;
    public APPKeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //key=iot:device:command:868474043308814_b39d7fb91ef74ff6a205761b62cff0fb_aaaaaa
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        //过期的key
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        if(key.contains(TokenConstant.TIKEN)){
            logger.info("=====================================app redis key 过期：删除tiken={}",key);
            String userId = key.substring(0,key.indexOf(TokenConstant.TIKEN));
            redisOperatingService.delTikenByKey(key);
            //删除token
            redisOperatingService.delTokenByUserId(userId);
            redisOperatingService.setLoginSuccessCount(userId,-1);
        }
    }
}
