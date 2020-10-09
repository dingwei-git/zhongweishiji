package com.jovision.jaws.common.config.password;


import com.jovision.jaws.common.exception.BusinessErrorEnum;
import com.jovision.jaws.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * 密码加密工具
 */
@Configuration
@Slf4j
public class PasswordEncryptionConfig {

    public final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";

    /**
     * 生成密文
     *
     * @param password 明文密码
     * @param salt     盐值
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public String getEncryptedPassword(char[] password, String salt){
        try {
//            Integer pbkdf2Iterations = Integer.valueOf(ApplicationConfig.getConfig("user.password.pbkdf2-iterations").toString());
//            Integer hashBitSize = Integer.valueOf(ApplicationConfig.getConfig("user.password.hash-bit-size").toString());
            Integer pbkdf2Iterations =1000;
            Integer hashBitSize =256;
            KeySpec spec = new PBEKeySpec(password , fromHex(salt), pbkdf2Iterations, hashBitSize);
            SecretKeyFactory f = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return toHex(f.generateSecret(spec).getEncoded());
        } catch (Exception e) {
            log.info("生成密码失败:"+e.getMessage());
            throw new BusinessException(BusinessErrorEnum.ENCODE_PASSWORD_ERROR);
        }
    }


    /**
     * 通过提供加密的强随机数生成器 生成盐
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String generateSalt(){
        //Integer saltByteSize = Integer.valueOf(ApplicationConfig.getConfig("user.password.salt-byte-size").toString());
        Integer saltByteSize =8;
        byte[] salt = new byte[saltByteSize];
        try {
            SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            log.info("获取密码加密盐值失败:" + e.getMessage());
            throw new BusinessException(BusinessErrorEnum.ENCODE_PASSWORD_ERROR);
        }
        return toHex(salt);
    }

    /**
     * 十六进制字符串转二进制字符串
     *
     * @param hex the hex string
     * @return the hex string decoded into a byte array
     */
    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    /**
     * 二进制字符串转十六进制字符串
     *
     * @param array the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }


    //d40b648e4ef094c5dce3048eb4e1461612b5c3459c2b539a528f74ab9ceab570
    //20200312-3.WR3jaA0UtRvY29OZJt0zsg==
    //271035b840deeeef

//    @SneakyThrows
//    public static void main(String[] args) {
//        char[] password = "a1234567".toCharArray();
//        String salt = "21cdcaee702e5645";
//
//        Integer pbkdf2Iterations = Integer.valueOf(1000);
//        Integer hashBitSize = Integer.valueOf(256);
//        KeySpec spec = new PBEKeySpec(password , fromHex(salt), pbkdf2Iterations, hashBitSize);
//        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//        System.out.println(toHex(f.generateSecret(spec).getEncoded()));
//
//
//
//    }
}
