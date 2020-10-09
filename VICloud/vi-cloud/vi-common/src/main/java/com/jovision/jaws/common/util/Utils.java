package com.jovision.jaws.common.util;

public class Utils {

    public static String getWand(String str,String wand,int webLoginLimitExpire){
        StringBuffer stringBuffer = new StringBuffer();
        if("addAll".equals(str)){
            for(int i=0;i<webLoginLimitExpire;i++){
                stringBuffer.append("0");
            }
        }else if("add".equals(str)){
            int index = wand.indexOf("0");
            stringBuffer.append(wand);
            stringBuffer.replace(index,index+1,"1");
        }else if("delete".equals(str)){
            int index = wand.indexOf("1");
            stringBuffer.append(wand);
            stringBuffer.replace(index,index+1,"0");
        }
        return String.valueOf(stringBuffer);
    }

}
