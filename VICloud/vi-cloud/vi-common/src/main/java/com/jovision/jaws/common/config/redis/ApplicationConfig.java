package com.jovision.jaws.common.config.redis;



import com.jovision.jaws.common.util.EncAgentUtils;
import com.jovision.jaws.common.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全平台配置参数配置类
 * @author ABug
 */
@Slf4j
public class ApplicationConfig {
    /**
     * 配置文件内存对应内容
     */
    public static Map<String , Object> configMap = new HashMap<>();
    /**
     * 配置文件级联关系绑定,单层嵌套引用  关联
     */
    public static Map<String , List<String>> relatedConfigMap = new HashMap<>();
    /**
     * 需要解密的配置参数key集合
     */
    public static Map<String , Boolean> encMap = new HashMap<>();
    /**
     * 延迟生效key集合
     */
    public static Map<String,Long> expireMap = new HashMap<>();
    /**
     * 变更需要延时生效的key
     */
    public static List<String> changeKeyList = Arrays.asList("jwt.token.secret.current","jwt.tiken.secret.current","jwt.saas.secret.current");

    /**
     * 初始化内存参数设置
     * @param key
     * @param value
     */
    public static void setInitConfig(String key , Object value){
        configMap.put(key , value);
        //log.info("========初始化内存参数变更["+key+"]=["+value+"]");
    }


    /**
     * 设置配置参数  转化
     * @param key 配置文件key
     * @param value 最新value值(apollo)
     */
    public static void setConfig(String key , Object value){
        String realValue = value.toString();
        if(realValue.startsWith("ENC(")){
            //加密属性需要解密
            List<String> list_enc = RegexUtils.extractMessageByRegular(realValue);
            if(list_enc.size()>0){
                realValue = list_enc.get(0);
            }
        }
        dealExpireKey(key);
        String parentValue = (encMap.get(key)!=null) ? EncAgentUtils.getDecodeConfig(key , realValue) : realValue;
        configMap.put(key , parentValue);
        //log.info("========内存参数变更["+key+"]=["+parentValue+"]");

        // 查询是否有级联变更内容
        // TODO: 2020/2/6 针对无限级应用需要增加递归 , 暂时不做递归处理
        List<String> childKeys = relatedConfigMap.get(key);
        if (childKeys!=null){
            // 子节点
            for (String childKey : childKeys){
                String childValue = (encMap.get(childKey)!=null) ? EncAgentUtils.getDecodeConfig(key , realValue) : realValue;
                dealExpireKey(childKey);
                configMap.put(childKey , childValue);
                //log.info("========内存参数变更[child]["+childKey+"]=["+childValue+"]");
            }
        }
    }

    private static void dealExpireKey(String key){
        if(changeKeyList.contains(key)){
            //如果有延时生效的key,放入延时生效map,当前是延长10min
            expireMap.put(key,System.currentTimeMillis()/1000 + 10*60);
        }
    }

    /**
     * 获取配置参数
     * @param key
     * @return
     */
    public static Object getConfig(String key){
        return configMap.get(key);
    }
}
