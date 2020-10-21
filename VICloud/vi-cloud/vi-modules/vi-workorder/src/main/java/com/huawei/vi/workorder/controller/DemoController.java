package com.huawei.vi.workorder.controller;

import com.jovision.jaws.common.config.redis.RedisOperatingService;
import com.jovision.jaws.common.constant.AppResultEnum;
import com.jovision.jaws.common.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/demo/")
public class DemoController {
    public static void main(String[] args) {
        String str= "103_app_token_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiMTAzIiwidXNlcl9uYW1lIjoibGNoX3dlaXhpdSIsImV4cGlyZURhdGUiOjE2MDEyNjAyODAyODQsInRva2VuRXhwaXJlIjo2MDAsImlhdCI6MTYwMTI1OTY4MCwic3ViIjoiMTAzIn0.XkIcsvDaMBM_qG356I6iaBGXrTZYQECsOO6DOSGVPTw";
        String str1 = str.substring(0,str.indexOf("_app_token_"));
        System.out.println(str1);
    }

    private static String[] reqCache = new String[100]; // 请求 ID 存储集合
    private static Integer reqCacheCounter = 0; // 请求计数器（指示 ID 存储的位置）
    @Autowired
    private RedisOperatingService redisOperatingService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("/test")
    public RestResult test(String id) {
        RestResult restResult = repeat("test:"+id);
        if(!(restResult.getCode()==AppResultEnum.SUCCESS.getCode())){
            return restResult;
        }
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),"success",null);
    }

    private RestResult repeat(String key){
        long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            redisTemplate.expire(key, 60, TimeUnit.SECONDS);
        }
        if (count > 1) {
            return RestResult.generateRestResult(AppResultEnum.REPEAT_COMMIT.getCode(),AppResultEnum.REPEAT_COMMIT.getMessage(),null);
        }
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),null);
    }
}
