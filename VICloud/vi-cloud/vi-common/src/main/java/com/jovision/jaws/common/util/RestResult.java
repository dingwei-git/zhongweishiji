package com.jovision.jaws.common.util;

import java.io.Serializable;

public class RestResult implements Serializable {

    private static final long serialVersionUID = -8847558410121701020L;
    private int code;
    private String message = "";
    private Object data = null;

    public RestResult() {
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public static RestResult success() {
        RestResult result = new RestResult();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    public static RestResult success(Object data) {
        RestResult result = new RestResult();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static RestResult success(ResultCode resultCode, Object data) {
        RestResult result = new RestResult();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    public static RestResult failure() {
        RestResult result = new RestResult();
        result.setResultCode(ResultCode.FAILURE);
        return result;
    }

    public static RestResult failure(ResultCode resultCode) {
        RestResult result = new RestResult();
        result.setResultCode(resultCode);
        return result;
    }

    public static RestResult failure(ResultCode resultCode, Object data) {
        RestResult result = new RestResult();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    public static RestResult generateRestResult(int code, String msg, Object data) {
        RestResult result = new RestResult();
        result.setCode(code);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }
}
