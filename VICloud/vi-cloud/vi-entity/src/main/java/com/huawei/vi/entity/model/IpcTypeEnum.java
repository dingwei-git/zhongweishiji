package com.huawei.vi.entity.model;

/**
 * @ClassName : IpcTypeEnum
 * @Description : 摄像机类型枚举类
 * @Author : pangxh
 * @Date: 2020-08-18 20:19
 */
public enum  IpcTypeEnum {
    // 摄像机类型，1球机，2半球，3固定枪机，4遥控枪机
    BALL("1","球机"),
    HALFBALL("2","半球"),
    FIXEDGUN("3","固定枪机"),
    REMOTECONTROLGUNMACHINE("4","遥控枪机");

    private String code;
    private String typeName;

    IpcTypeEnum(String code, String typeName) {
        this.code = code;
        this.typeName = typeName;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static String getTypeName(String codeStr){
        IpcTypeEnum[] values = IpcTypeEnum.values();
        for(IpcTypeEnum typeEnum:values){
            if(typeEnum.getCode().equals(codeStr)){
                return typeEnum.getTypeName();
            }
        }
        return null;
    }

}
