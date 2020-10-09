package com.jovision.jaws.common.util;

import com.huawei.utils.CommonUtil;
import com.huawei.utils.MapUtils;
import com.huawei.utils.StringUtil;
import com.jovision.jaws.common.constant.CommonConst;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * 利用HttpClient进行post请求的工具类
 *
 * @since 2019-08-06
 */
public class HttpClientUtil {
    /**
     * 公共日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 流读取空行标志
     */
    private static final int FLAG = -1;

    /**
     * SslClient
     */
    private static SslClient httpClient = null;

    /**
     * 初始化
     *
     * @throws NoSuchAlgorithmException 加密算法异常
     * @throws KeyManagementException 密钥管理异常
     */
    public static void init() throws KeyManagementException, NoSuchAlgorithmException {
        httpClient = new SslClient();
    }

    public SslClient getHttpClient() {
        return httpClient;
    }

    /**
     * doPost请求
     *
     * @param url 网络连接
     * @param map 带参Map
     * @param charset 字符编码（UTF-8等）
     * @param isInitFlag 初始化标志
     * @return String
     */
    public String doPost(String url, Map<String, String> map, String charset, boolean isInitFlag) {
        HttpPost httpPost = null;
        String result = null;
        if (CommonUtil.isExistsNull(url, map, isInitFlag)) {
            return null;
        }
        if (!Pattern.matches(CommonConst.HTTP_REG, url)) {
            LOGGER.error("Invalid request...");
            return null;
        }
        try {
            if (isInitFlag) {
                init();
            }
            httpPost = new HttpPost(url);

            // 设置参数
            setParam(map, charset, httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            if (CommonUtil.isNotNull(response)) {
                HttpEntity resEntity = response.getEntity();
                if (CommonUtil.isNotNull(resEntity)) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException:{}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UnsupportedEncodingException:{}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("IOException:{}", e.getMessage());
        } catch (ParseException e) {
            LOGGER.error("ParseException :{}", e.getMessage());
        } catch (KeyManagementException e) {
            LOGGER.error("KeyManagementException :{}", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("NoSuchAlgorithmException :{}", e.getMessage());
        }
        return result;
    }
    public String doPostVi(String url, Map<String, String> map, String charset, boolean isInitFlag) {
        HttpPost httpPost = null;
        String result = null;
        if (CommonUtil.isExistsNull(url, map, isInitFlag)) {
            return null;
        }
        if (!Pattern.matches(CommonConst.HTTP_REG, url)) {
            LOGGER.error("Invalid request...");
            return null;
        }
        try {
            if (isInitFlag) {
                init();
            }
            httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
            // 设置参数
            setParam(map, charset, httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            if (CommonUtil.isNotNull(response)) {
                HttpEntity resEntity = response.getEntity();
                if (CommonUtil.isNotNull(resEntity)) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException:{}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UnsupportedEncodingException:{}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("IOException:{}", e.getMessage());
        } catch (ParseException e) {
            LOGGER.error("ParseException :{}", e.getMessage());
        } catch (KeyManagementException e) {
            LOGGER.error("KeyManagementException :{}", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("NoSuchAlgorithmException :{}", e.getMessage());
        }
        return result;
    }
    /**
     * doPost请求
     *
     * @param url 网络连接
     * @param map 带参Map
     * @param charset 字符编码（UTF-8等）
     * @param isInitFlag 初始化标志
     * @return String
     */
    public String doVedioPost(String url, Map<String, String> map, String charset, boolean isInitFlag) {
        boolean checkSuccess = paramCheck(url, map);
        if (!checkSuccess) {
            LOGGER.error("param Check fail.");
            return null;
        }
        HttpPost httpPost = null;
        String result = null;
        try {
            if (isInitFlag) {
                init();
            }
            httpPost = new HttpPost(url);
            httpPost.setHeader(CommonConst.VIDEOINSIGHTCOLLECTERAUTHCODE, TokenUtil.getInstance().getToken());

            // 设置参数
            setParam(map, charset, httpPost);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (CommonUtil.isNotNull(response)) {
                try {
                    HttpEntity resEntity = response.getEntity();
                    if (CommonUtil.isNotNull(resEntity)) {
                        result = EntityUtils.toString(resEntity, charset);
                    }
                } finally {
                    response.close();
                }
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("VedioClientProtocolException :{}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("VedioUnsupportedEncodingException:{}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("VedioIOException:{}", e.getMessage());
        } catch (ParseException e) {
            LOGGER.error("VedioParseException :{}", e.getMessage());
        } catch (KeyManagementException e) {
            LOGGER.error("VedioKeyManagementException :{}", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("VedioNoSuchAlgorithmException :{}", e.getMessage());
        }
        return result;
    }

    public String getDomainCodePost(String url, Map<String, String> map, String charset, boolean isInitFlag) {
        boolean checkSuccess = paramCheck(url, map);
        if (!checkSuccess) {
            LOGGER.error("param Check fail.");
            return null;
        }
        HttpPost httpPost = null;
        String result = null;
        try {
            if (isInitFlag) {
                init();
            }
            httpPost = new HttpPost(url);
            httpPost.setHeader("Cookie",map.get("Cookie"));

            // 设置参数
            setParam(map, charset, httpPost);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (CommonUtil.isNotNull(response)) {
                try {
                    HttpEntity resEntity = response.getEntity();
                    if (CommonUtil.isNotNull(resEntity)) {
                        result = EntityUtils.toString(resEntity, charset);
                    }
                } finally {
                    response.close();
                }
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("VedioClientProtocolException :{}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("VedioUnsupportedEncodingException:{}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("VedioIOException:{}", e.getMessage());
        } catch (ParseException e) {
            LOGGER.error("VedioParseException :{}", e.getMessage());
        } catch (KeyManagementException e) {
            LOGGER.error("VedioKeyManagementException :{}", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("VedioNoSuchAlgorithmException :{}", e.getMessage());
        }
        return result;
    }


    /**
     * 设置参数
     *
     * @param map map
     * @param charset charset
     * @param httpPost httpPost
     * @throws UnsupportedEncodingException 异常
     */
    private void setParam(Map<String, String> map, String charset, HttpPost httpPost)
        throws UnsupportedEncodingException {
        // 设置参数
        List<NameValuePair> lists = new ArrayList<NameValuePair>();
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> elem = (Entry<String, String>) iterator.next();
            lists.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
        }
        if (lists.size() > 0) {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(lists, charset);
            httpPost.setEntity(entity);
        }
    }

    /**
     * 对入参进行校验
     *
     * @param str 请求链接
     * @param map map入参
     * @return 检查结果 ；true:成功；false:失败
     */
    private boolean paramCheck(String str, Map<String, String> map) {
        String decodeUrl = StringUtil.decodeFromUrl(str);
        if (CommonUtil.isExistsNull(map)) {
            LOGGER.error(" map is null .");
            return false;
        }
        boolean isMatchesUrl = Pattern.matches(CommonConst.HTTP_REG, decodeUrl);
        if (!isMatchesUrl) {
            // 匹配失败，日志记录脱敏结果；
            LOGGER.error("param not matches rule. param : {} ", StringUtil.desensitization(decodeUrl));
        }
        return isMatchesUrl;
    }

    /**
     * 发送请求，获取相应值
     *
     * @param url 连接
     * @return String
     */
    public String doGet(String url) {
        String result = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (CommonUtil.isNotNull(response)) {
                try {
                    HttpEntity resEntity = response.getEntity();
                    if (CommonUtil.isNotNull(resEntity)) {
                        result = EntityUtils.toString(resEntity, CommonConstant.ENCODE);
                    }
                } finally {
                    response.close();
                }
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException :{}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UnsupportedEncodingException:{}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("IOException:{}", e.getMessage());
        }
        return result;
    }

    public String doGet(String url,Map<String,String>map) {
        String result = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type","application/json;charset=UTF-8");
            httpGet.setHeader("Cache-Control","no-cache");
            httpGet.setHeader("Cookie",map.get("cookie"));
            try {
                httpClient = new SslClient();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (CommonUtil.isNotNull(response)) {
                try {
                    HttpEntity resEntity = response.getEntity();
                    if (CommonUtil.isNotNull(resEntity)) {
                        result = EntityUtils.toString(resEntity, CommonConstant.ENCODE);
                    }
                } finally {
                    response.close();
                }
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException :{}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UnsupportedEncodingException:{}", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("IOException:{}", e.getMessage());
        }
        return result;
    }

    /**
     * doPost4Stream 请求
     *
     * @param url 网络连接
     * @param map 带参Map
     * @param charset 字符编码（UTF-8等）
     * @return byte[] 返回二进制编码
     * @throws IOException IO异常
     */
    public byte[] doPost4Stream(String url, Map<String, String> map, String charset) throws IOException {
        HttpPost httpPost = null;
        byte[] istbs = null;
        try {
            init();
            httpPost = new HttpPost(url);

            // 设置参数
            if (!MapUtils.isNull(map)) {
                setParam(map, charset, httpPost);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (CommonUtil.isNotNull(response)) {
                HttpEntity resEntity = response.getEntity();
                if (CommonUtil.isNotNull(resEntity)) {
                    try (InputStream result = resEntity.getContent()) {
                        istbs = inputStreamToByte(result);
                    }
                }
            }
        } catch (KeyManagementException e) {
            LOGGER.error("KeyManagementException :{}", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("NoSuchAlgorithmException :{}", e.getMessage());
        }
        return istbs;
    }

    /**
     * 读取内容转化为byte格式
     *
     * @param inputStrm 字节输入流
     * @return byte[]
     */
    private byte[] inputStreamToByte(InputStream inputStrm) {
        byte[] imgDatas = null;
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            if (CommonUtil.isNotNull(inputStrm)) {
                int ch = inputStrm.read();
                while (ch != FLAG) {
                    byteStream.write(ch);
                    ch = inputStrm.read();
                }
            }
            imgDatas = byteStream.toByteArray();
        } catch (IOException e) {
            LOGGER.error("InputStreamToByte() {}", e.getMessage());
        }
        return imgDatas;
    }

    /**
     * 设置post请求头
     *
     * @param post post请求
     * @param header 请求消息头
     */
    private static void setHeader(HttpPost post, Map<String, String> header) {
        Iterator<?> iterator = header.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry elem = (Entry) iterator.next();
            post.setHeader((String) elem.getKey(), (String) elem.getValue());
        }
    }

    /**
     * 对接总控程序的post请求执行方法
     *
     * @param url 链接地址
     * @param jsonObj 请求数据
     * @param header 请求消息头
     * @return String 返回参数
     */
    public String doPostMasterControl(String url, Object jsonObj, Map<String, String> header) {
        if (StringUtil.isEmpty(url) || CommonUtil.isNull(jsonObj)) {
            LOGGER.error("Invalid request parameter parameter...");
            return null;
        }
        String result = null;
        HttpPost httpPost = new HttpPost(url);

        StringEntity entity = new StringEntity(jsonObj.toString(), CommonConstant.CHARSET_UTF8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        if (MapUtils.isNotEmpty(header)) {
            setHeader(httpPost, header);
        }

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            response = client.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, UtilMethod.ENCODE);
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException :{}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("IOException :{}", e.getMessage());
        } catch (ParseException e) {
            LOGGER.error("ParseException :{}", e.getMessage());
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    public String doPostMasterController(String url, Object jsonObj, Map<String, String> header) {
        if (StringUtil.isEmpty(url) || CommonUtil.isNull(jsonObj)) {
            LOGGER.error("Invalid request parameter parameter...");
            return null;
        }
        String result = null;
        HttpPost httpPost = new HttpPost(url);

        StringEntity entity = new StringEntity(jsonObj.toString(), CommonConstant.CHARSET_UTF8);
        entity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(entity);
        /*
         * 绕过SSL认证
         * */
        CloseableHttpClient client = null;
        try {
            client = getIgnoeSSLClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = response.getHeaders("Set-Cookie")[0].toString();
        int indexEnd = str.indexOf(";");
        str = str.substring(12,indexEnd);
        try {
            response = client.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, UtilMethod.ENCODE);
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("ClientProtocolException :{}", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("IOException :{}", e.getMessage());
        } catch (ParseException e) {
            LOGGER.error("ParseException :{}", e.getMessage());
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result+"&"+str;
    }


    public String getContent(String url, Map<String, String> mapdata) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String content = null;
        String str=null;
        try {
            httpClient = getIgnoeSSLClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 创建httppost
        HttpPost httpPost = new HttpPost(url);
        try {
            // 设置提交方式
            httpPost.addHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            // 添加参数
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            if (mapdata.size() != 0) {
                // 将mapdata中的key存在set集合中，通过迭代器取出所有的key，再获取每一个键对应的值
                Set keySet = mapdata.keySet();
                Iterator it = keySet.iterator();
                while (it.hasNext()) {
                    String k =  it.next().toString();// key
                    String v = mapdata.get(k);// value
                    nameValuePairs.add(new BasicNameValuePair(k, v));
                }
            }
            httpPost.setEntity( new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            // 执行http请求
            response = httpClient.execute(httpPost);
            str = response.getHeaders("Set-Cookie")[0].toString();
            int indexEnd = str.indexOf(";");
            str = str.substring(12,indexEnd);
            // 获得http响应体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 响应的结果
                content = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }



    /**
     * 获取忽略证书验证的client
     *
     * @return
     * @throws Exception
     */
    public CloseableHttpClient getIgnoeSSLClient() throws Exception {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        }).build();
        //创建httpClient
        CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext).
                setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        return client;
    }


}