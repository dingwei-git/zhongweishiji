package com.jovision.jaws.common.util;

import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;

/**
 * 修改描述信息
 *
 * @author : ABug
 * @Date : 2019/6/18 14:24
 * @UpdateDate : 2019/6/18 14:24
 * @Version V1.0.0
 **/
public class Base64ImageUtil {

    /**
     * base64图片去掉前缀
     * @param base64ImageString
     * @return
     */
    public static String getBase64Data(String base64ImageString){
       return base64ImageString.substring(base64ImageString.indexOf(",")+1 , base64ImageString.length());
    }

    /**
     * <p>将文件转成base64 字符串</p>
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }
    public static String encodeBase64File(File file) throws Exception {
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }
}
