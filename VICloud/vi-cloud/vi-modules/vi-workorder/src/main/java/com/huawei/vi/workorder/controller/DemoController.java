package com.huawei.vi.workorder.controller;

public class DemoController {
    public static void main(String[] args) {
        String str= "103_app_token_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiMTAzIiwidXNlcl9uYW1lIjoibGNoX3dlaXhpdSIsImV4cGlyZURhdGUiOjE2MDEyNjAyODAyODQsInRva2VuRXhwaXJlIjo2MDAsImlhdCI6MTYwMTI1OTY4MCwic3ViIjoiMTAzIn0.XkIcsvDaMBM_qG356I6iaBGXrTZYQECsOO6DOSGVPTw";
        String str1 = str.substring(0,str.indexOf("_app_token_"));
        System.out.println(str1);
    }
}
