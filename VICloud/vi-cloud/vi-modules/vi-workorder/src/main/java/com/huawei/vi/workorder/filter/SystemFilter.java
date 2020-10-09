package com.huawei.vi.workorder.filter;

import com.alibaba.fastjson.JSONObject;
import com.jovision.jaws.common.config.redis.RedisOperatingService;
import com.jovision.jaws.common.constant.HeaderParamEnum;
import com.jovision.jaws.common.constant.TokenConstant;
import com.jovision.jaws.common.exception.BusinessErrorEnum;
import com.jovision.jaws.common.exception.BusinessException;
import com.jovision.jaws.common.util.IpUtils;
import com.jovision.jaws.common.vo.ExecuteResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.UUID;
/**
 * 日志和鉴权
 *
 * @Author: ABug
 * @Date: 2020/1/9 19:57
 * @Version V1.0.0
 **/
@Slf4j
@Component
@WebFilter(urlPatterns = "*")
@Order(-1)
public class SystemFilter extends OncePerRequestFilter {
    @Autowired
    private RedisOperatingService redisOperatingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String url = request.getRequestURI();
        String ip = IpUtils.getIpAddr(request);
        String method = request.getMethod();
        String uuid = UUID.randomUUID().toString();
        String result = null;
        StringBuilder sb = new StringBuilder();
        try {
            RequestWrapper requestWrapper = null;
            if (url.indexOf("/order/") >= 0||
                    url.indexOf("/ipc/") >= 0||
                    url.indexOf("/forback/") >= 0||
                    url.indexOf("/fault/") >= 0||
                    url.indexOf("/alarm/") >= 0) {
                try {
                    String token = request.getHeader(HeaderParamEnum.AUTHORIZATION.getTitle()).substring(7);
                    String subToken = token.substring(token.indexOf(TokenConstant.TOKEN)+11);
                    Claims claims = parseJWT(subToken, TokenConstant.TOKEN_SECRET);
                    if (claims == null) {
                        log.info(BusinessErrorEnum.PARSE_TOKEN_FAIL.getMsg());
                        throw new BusinessException(BusinessErrorEnum.PARSE_TOKEN_FAIL);
                    }
                    long exp = Long.parseLong(claims.get("expireDate").toString());

                    //校验有效性
                    //DateTime dateTime = DateTime.parse(exp);
                    if ((exp - System.currentTimeMillis()) < 0) {
                        log.info(BusinessErrorEnum.TOKEN_OVER_LOCAL.getMsg());
                        throw new BusinessException(BusinessErrorEnum.TOKEN_OVER_LOCAL);
                    }
                    String userId = claims.get("user_id")==null?"":claims.get("user_id").toString();
                    if(StringUtils.isEmpty(userId)){
                        log.info(BusinessErrorEnum.TOKEN_USER_NOT_FOUND.getMsg());
                        throw new BusinessException(BusinessErrorEnum.TOKEN_USER_NOT_FOUND);
                    }
                    String userName = claims.get("user_name")==null?"":claims.get("user_name").toString();
                    if(StringUtils.isEmpty(userName)){
                        log.info(BusinessErrorEnum.TOKEN_USER_NOT_FOUND.getMsg());
                        throw new BusinessException(BusinessErrorEnum.TOKEN_USER_NOT_FOUND);
                    }
//                    Cookie cookie=new Cookie("user_id",userId);
//                    Cookie userNameCookie=new Cookie("user_name",userName);
//                    response.addCookie(cookie);//通过response保存cookie对象
//                    response.addCookie(userNameCookie);
                    request.setAttribute("userId",userId);
                    request.setAttribute("userName",userName);
                    String tokenRedis = redisOperatingService.getTokenByToken(token);
                    if(StringUtils.isEmpty(tokenRedis)){
                        log.info(BusinessErrorEnum.TOKEN_NOT_FOUND.getMsg());
                        throw new BusinessException(BusinessErrorEnum.TOKEN_NOT_FOUND);
                    }
                    if(!tokenRedis.equals("1")){
                        log.info(BusinessErrorEnum.TOKEN_NOT_SAME.getMsg());
                        throw new BusinessException(BusinessErrorEnum.TOKEN_NOT_SAME);
                    }
                }
//                catch (BusinessException b){
//                    log.error(BusinessErrorEnum.TOKEN_NOT_SAME.getMsg());
//                    response.setContentType("text/html;charset=UTF-8");
//                    JSONObject tmp = new JSONObject();
//                    tmp.put("message", b.getMsg());
//                    tmp.put("code",b.getCode());
//                    response.getOutputStream().write(tmp.toJSONString().getBytes("UTF-8"));
//                    response.getOutputStream().close();
//                    return;
//                }
                catch (Exception ex) {
                    log.error("SystemFilter=",ex);
                    //String responseString = new BusinessException(BusinessErrorEnum.UMS_TOKEN_OVERDUE).toString();
                    response.setStatus(301);
                    response.setContentType("text/html;charset=UTF-8");
                    response.getOutputStream().write("{message=token失效, code=1111}".getBytes("UTF-8"));
                    response.getOutputStream().close();
                    return;
                }
            }

            ResponseWrapper responseWrapper = new ResponseWrapper(response);

            if (null == requestWrapper) {
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(requestWrapper, responseWrapper);
            }

//            int responseStatus = response.getStatus();
//            String responseContentType = response.getContentType();
//            result = new String(responseWrapper.getResponseData());
//
//            //log.info("[UUID={}][RESPONSE][URL={}][HTTP_METHOD={}][IP={}][responseStatus={}][ContentType={}][BODY={}][user_id={}]", uuid, url, method, ip, responseStatus, responseContentType, LogEncodeUtils.encodeInfo(result), userId);
//            ServletOutputStream outputStream = response.getOutputStream();
//            //outputStream.write(result.getBytes());
//            outputStream.flush();
//            outputStream.close();
        } catch (Exception ex) {
            log.info("SystemFilter:", ex.getMessage(), ex);
            ExecuteResult executeResult = new ExecuteResult();
            executeResult.setError(BusinessErrorEnum.BASE_REQUEST_ILLEGAL.getCode(), BusinessErrorEnum.BASE_REQUEST_ILLEGAL.getMsg());
            ServletOutputStream outputStream = response.getOutputStream();

            result = JSONObject.toJSONString(executeResult);

            outputStream.write(result.getBytes());
            outputStream.flush();
            outputStream.close();
        }finally {
            //logAudit(userId , sb.toString() , ip , url , result,umsUserTemp);
        }
    }


    class ResponseWrapper extends HttpServletResponseWrapper {
        /**
         * This class implements an output stream in which the data is written into a byte array.
         * The buffer automatically grows as data is written to it. The data can be retrieved using toByteArray() and toString().
         * Closing a ByteArrayOutputStream has no effect. The methods in this class can be called after the stream has been closed without generating an IOException.
         */
        private ByteArrayOutputStream buffer = null;//输出到byte array
        private ServletOutputStream out = null;
        private PrintWriter writer = null;

        public ResponseWrapper(HttpServletResponse resp) throws IOException {
            super(resp);
            buffer = new ByteArrayOutputStream();// 真正存储数据的流
            out = new WapperedOutputStream(buffer);
            writer = new PrintWriter(new OutputStreamWriter(buffer, this.getCharacterEncoding()));
        }

        /**
         * 重载父类获取outputstream的方法
         */
        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return out;
        }

        /**
         * 重载父类获取writer的方法
         */
        @Override
        public PrintWriter getWriter() throws UnsupportedEncodingException {
            return writer;
        }

        /**
         * 重载父类获取flushBuffer的方法
         */
        @Override
        public void flushBuffer() throws IOException {
            if (out != null) {
                out.flush();
            }
            if (writer != null) {
                writer.flush();
            }
        }

        @Override
        public void reset() {
            buffer.reset();
        }

        /**
         * 将out、writer中的数据强制输出到WapperedResponse的buffer里面，否则取不到数据
         */
        public byte[] getResponseData() throws IOException {
            flushBuffer();
            return buffer.toByteArray();
        }

        /**
         * 内部类，对ServletOutputStream进行包装
         */
        private class WapperedOutputStream extends ServletOutputStream {
            private ByteArrayOutputStream bos = null;

            public WapperedOutputStream(ByteArrayOutputStream stream) throws IOException {
                bos = stream;
            }

            @Override
            public void write(int b) throws IOException {
                bos.write(b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                bos.write(b, 0, b.length);
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {

            }
        }
    }

    class RequestWrapper extends HttpServletRequestWrapper {
        private final byte[] body;

        /**
         * 这个必须加,复制request中的bufferedReader中的值
         *
         * @param request
         * @throws IOException
         */
        public RequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            body = getBodyString(request);
        }

        /**
         * 获取请求Body
         *
         * @param request
         * @return
         */
        public byte[] getBodyString(final ServletRequest request) throws IOException {
            String contentType = request.getContentType();
            String bodyString = "";

            if (StringUtils.isNotBlank(contentType) && (contentType.contains("multipart/form-data") || contentType.contains("x-www-form-urlencoded"))) {

                Enumeration<String> pars = request.getParameterNames();

                while (pars.hasMoreElements()) {

                    String n = pars.nextElement();

                    bodyString += n + "=" + request.getParameter(n) + "&";

                }

                bodyString = bodyString.endsWith("&") ? bodyString.substring(0, bodyString.length() - 1) : bodyString;

                return bodyString.getBytes(Charset.forName("UTF-8"));

            } else {

                //return StreamUtil.readBytes(request.getReader(), "UTF-8");
                return null;

            }
        }


        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream bais = new ByteArrayInputStream(body);
            return new ServletInputStream() {

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener listener) {

                }

                @Override
                public int read() throws IOException {
                    return bais.read();
                }
            };
        }
    }

    private Claims parseJWT(String token, String secret) {
        //签名秘钥，和生成的签名的秘钥一模一样
        //得到DefaultJwtParser
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(secret.getBytes())
                    //设置需要解析的jwt
                    .parseClaimsJws(token).getBody();
        } catch (Exception ex) {
            log.error("jwt解密失败",ex);
            return null;
        }
        return claims;
    }

}


