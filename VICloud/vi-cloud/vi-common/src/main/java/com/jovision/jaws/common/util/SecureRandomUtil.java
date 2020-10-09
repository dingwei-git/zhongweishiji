package com.jovision.jaws.common.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * 生成四位随机验证码
 *
 * @Author: ABug
 * @Date: 2020/1/6 17:45
 * @Version V1.0.0
 **/
@Slf4j
public class SecureRandomUtil {
    public static SecureRandom secureRandom = new SecureRandom();

    /**
     * 生成6位随机数
     *
     * @return
     */
    public static String createCode() {
        int code = 0;
        do {
            code = secureRandom.nextInt(1000000);
        } while (code < 100000);
        return String.valueOf(code);
    }

    /**
     * 获取tiken盐值
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String randomIV16bit() throws NoSuchAlgorithmException {
        byte[] salt = new byte[8];
            SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
        return toHex(salt);
    }

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
}
