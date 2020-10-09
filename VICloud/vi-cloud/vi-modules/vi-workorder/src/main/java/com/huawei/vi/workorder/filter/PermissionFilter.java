package com.huawei.vi.workorder.filter;

import com.alibaba.fastjson.JSONObject;
import com.huawei.vi.workorder.mapper.UserMapper;
import com.jovision.jaws.common.constant.AppResultEnum;
import com.jovision.jaws.common.constant.NumConstant;
import com.jovision.jaws.common.exception.BusinessErrorEnum;
import com.jovision.jaws.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 权限校验
 * */
@Slf4j
@Component
@WebFilter(urlPatterns = "*")
@Order(0)
public class PermissionFilter extends OncePerRequestFilter {
    @Autowired
    private UserMapper userMapper;
    private static int[] permission_id ={1813};
    private static Map condition = new HashMap();
    static {
        condition.put("roleId", NumConstant.NUM_4);
        condition.put("permission_id",permission_id);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String url = request.getRequestURI();
            if (url.indexOf("/order/")>=0){
                List list = userMapper.queryPermission(condition);
                if(list!=null&&list.size()>0){
                    //filterChain.doFilter(request, response);
                }else{
                    throw new BusinessException(BusinessErrorEnum.NO_PERMISSION);
                }
            }
        } catch (Exception ex) {
            response.setContentType("text/html;charset=UTF-8");
            JSONObject tmp = new JSONObject();
            tmp.put("message", AppResultEnum.NO_PERMISSION.getMessage());
            tmp.put("code",AppResultEnum.NO_PERMISSION.getCode());
            response.getOutputStream().write(tmp.toJSONString().getBytes("UTF-8"));
            response.getOutputStream().close();
            return;
        }
        filterChain.doFilter(request, response);
    }
}