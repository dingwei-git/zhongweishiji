package com.jovision.jaws.common.config.jwt;

import com.jovision.jaws.common.constant.TokenConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密工具类
 */
@Slf4j
@Component
public class TikenUtil {

    public String aesEncode(String content , String iv) {
        //String useJwtToken = ApplicationConfig.getConfig(ChangeConfigEnum.JWT_TIKEN_SECRET_CURRENT.getTitle()).toString();
        String useJwtToken = TokenConstant.TIKEN_SECRET;
        try {
//            if(ApplicationConfig.expireMap.containsKey(ChangeConfigEnum.JWT_TIKEN_SECRET_CURRENT.getTitle())){
//                //获取延时生效时间，比对下是否超时，如果超时，直接干掉该值即可
//                if(System.currentTimeMillis()/1000 > ApplicationConfig.expireMap.get(ChangeConfigEnum.JWT_TIKEN_SECRET_CURRENT.getTitle())){
//                    ApplicationConfig.expireMap.remove(ChangeConfigEnum.JWT_TIKEN_SECRET_CURRENT.getTitle());
//                }else{
//                    //还未到生效时间，直接用旧的密钥
//                    useJwtToken = ApplicationConfig.getConfig(ChangeConfigEnum.JWT_TIKEN_SECRET_OLD.getTitle()).toString();
//                }
//            }
            //log.info("tiken使用的秘钥：" + useJwtToken);
            byte[] raw = useJwtToken.substring(0,16).getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            return new BASE64Encoder().encode(encrypted);
        } catch (Exception e) {
            log.error("账号加密失败,", e);
            return null;
        }
    }

    private String aesDecodeBySecret(String content , String secret , String iv){
        try {
            byte[] raw = secret.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(content);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            log.error("账号解密失败,", e);
            return null;
        }
    }

    /**
     * tiken解密
     * 先用新密钥进行解密 , 解密失败在用旧密钥进行解密 不成功返回null
     * @param content
     * @return
     */
    public String aesDecode(String content , String iv) {
        String originalString = null;
//        originalString = aesDecodeBySecret(content , ApplicationConfig.getConfig("jwt.tiken.secret.current").toString().substring(0,16) , iv);
//        if(originalString == null){
//            originalString = aesDecodeBySecret(content , ApplicationConfig.getConfig("jwt.tiken.secret.old").toString().substring(0,16) , iv);
//        }
        originalString = aesDecodeBySecret(content , "tikentikentikentikentikentikentikentikentikentikentikentiken".substring(0,16) , iv);
        if(originalString == null){
            originalString = aesDecodeBySecret(content , "tikentikentikentikentikentikentikentikentikentikentikentiken".substring(0,16) , iv);
        }
        return originalString;
    }

}
