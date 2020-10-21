package com.jovision.jaws.common.util;

import com.alibaba.fastjson.JSONObject;
import com.jovision.jaws.common.config.i18n.MessageSourceUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 遍历对象，返回json
 */
public class TraverseObjectUtil {

    private static String positiveInteger = "^[1-9]\\d*$";

    public static JSONObject traverseCameraBriefInfoExes(Object obj){
        Field[] fields=obj.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        JSONObject json = new JSONObject();
        for(int i=0;i<fields.length;i++){
            String name = fields[i].getName();
            String code = name.substring(0,1).toUpperCase()+name.substring(1); //将属性的首字符大写，方便构造get，set方法
            fieldNames[i]=fields[i].getName();
            Object value = getFieldValueByName(code,obj);
            json.put(name,value);
        }
        return json;
    }
    /* 根据属性名获取属性值
     * */
    public static Object getFieldValueByName(String fieldName, Object model) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = model.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(model, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    public static String newTrim(Object obj) {
        if(obj==null || "".equals(obj)){
            return "-";
        }
        return String.valueOf(obj);
    }

    public static String trim(Object obj) {
        if(obj==null || "".equals(obj)){
            return "";
        }
        return String.valueOf(obj);
    }

    /**
     * 校验是否为正整数(不包括0)
     * @param str
     * @return
     */
    public static boolean checkPositiveInteger(String str){
        Pattern pattern = Pattern.compile(positiveInteger);
        boolean flag = pattern.matcher(str).matches();
        return flag;
    }

    public static List<String> checkOther(List<String> list,String str){
        List<String> resuleList = new ArrayList<String>();
        list.forEach(k->{
            if(str.equals(k)){
                k = "-";
            }
            resuleList.add(k);
        });
        return resuleList;
    }

    public static RestResult checkGinsengRequest(MessageSourceUtil messageSourceUtil,HttpServletRequest request, String ... strs){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        for(String str:strs){
            if(StringUtils.isEmpty(request.getParameter(str))){
                restResult.setMessage(str+messageSourceUtil.getMessage("isEmpt"));
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                return restResult;
            }
        }
        return restResult;
    }
}
