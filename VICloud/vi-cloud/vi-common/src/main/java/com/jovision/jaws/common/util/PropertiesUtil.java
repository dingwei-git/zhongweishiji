package com.jovision.jaws.common.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PropertiesUtil {

    private static final String CHARSET_UTF8 = "UTF-8";

    public static Properties getPropertiesByFile(String path){
        Properties properties = new Properties();
        Resource moderes = new ClassPathResource(path);
        try {
            properties = PropertiesLoaderUtils.loadProperties(moderes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static InputStream getInputStreamByFile(String path){
        InputStream is = null;
        Resource moderes = new ClassPathResource(path);
        try {
            is = moderes.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public static Map<String, String> getMapByFile(String path){
        Map<String, String> propertyMap = new HashMap<String, String>();
        Properties properties = new Properties();
        Resource moderes = new ClassPathResource(path);
        try {
            properties = PropertiesLoaderUtils.loadProperties(moderes);
            Enumeration<?> en = properties.propertyNames();
            while (en.hasMoreElements()) {
                String key=(String) en.nextElement();
                String value=properties.getProperty(key);
                propertyMap.put(key,value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertyMap;
    }

    /**
     * 读取Properties文件到Map
     *
     * @param filePath 配置文件路径
     * @return map 转换后map
     * @throws IOException IO异常
     */
    public static Map<String, String> loadToMap(Path filePath) throws IOException {
        Objects.requireNonNull(filePath);
        return propertiesToMap(load(filePath));
    }
    /**
     * properties转map
     *
     * @param properties properties
     * @return map 转换后map
     */
    public static Map<String, String> propertiesToMap(Properties properties) {
        Objects.requireNonNull(properties);
        Map<String, String> resourceMap = new LinkedHashMap(properties.size());
        properties.keySet().parallelStream().forEach(key -> {
            resourceMap.put(key.toString(), properties.getProperty(key.toString()));
        });
        return resourceMap;
    }

    /**
     * 读取Properties文件到Map
     *
     * @param filePath 配置文件路径
     * @return map 转换后map
     * @throws IOException IO异常
     */
    public static Map<String, String> loadToMap(String filePath) throws IOException {
        Objects.requireNonNull(filePath);
        return loadToMap(Paths.get(filePath));
    }
    /**
     * 加载指定的资源文件 默认编码为utf8
     *
     * @param filePath 文件路径
     * @return property property
     * @throws IOException IO异常
     */
    public static Properties load(Path filePath) throws IOException {
        return load(filePath, CHARSET_UTF8);
    }
    /**
     * 加载指定的资源文件
     *
     * @param filePath file path对象
     * @param charsetName 字符集名称
     * @return properties对象
     * @throws IOException IO异常
     */
    public static Properties load(Path filePath, String charsetName) throws IOException {
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(charsetName);
        return load(getBufferReader(filePath, charsetName));
    }
    /**
     * 加载指定的资源文件
     *
     * @param filePath path 对象
     * @param charsetName 字符集名称
     * @return properties对象
     * @throws IOException IO异常
     */
    private static BufferedReader getBufferReader(Path filePath, String charsetName) throws IOException {
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(charsetName);
        return Files.newBufferedReader(filePath, Charset.forName(charsetName));
    }
    /**
     * 加载指定的资源文件
     *
     * @param reader BufferedReader对象
     * @return properties对象
     * @throws IOException IO异常
     */
    public static Properties load(BufferedReader reader) throws IOException {
        Objects.requireNonNull(reader);
        Properties properties = new Properties();
        try {
            properties.load(reader);
        } finally {
            reader.close();
        }
        return properties;
    }
}
