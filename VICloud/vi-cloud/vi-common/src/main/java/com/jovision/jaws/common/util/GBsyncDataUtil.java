package com.jovision.jaws.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.vi.entity.po.GBsyncData;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class GBsyncDataUtil {

    public static List<JSONObject> syncData(GBsyncData gBsyncData) {
        List<JSONObject> result = new ArrayList<>();
        getData(result,gBsyncData);
        //getDataFromXml(result, gBsyncData);
        return result;
    }


    private static void getData(List<JSONObject> result, GBsyncData gBsyncData) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("platformid", gBsyncData.getPlatformId());
        String body = JSON.toJSONString(bodyMap);
        int len = body.getBytes().length;
        Socket socket = null;
        try {
            socket = new Socket(gBsyncData.getIp(), gBsyncData.getPort());
            OutputStream os = socket.getOutputStream();
            os.write((byte) 6);
            os.write((byte) 0);
            os.write((byte) 0);
            os.write((byte) 0);
            os.write(int_byte(len));
            os.write(int_byte(0));
            os.write(new byte[20], 0, 20);
            os.write(body.getBytes(), 0, len);
            log.info("=============给服务端发送数据：{}");
            os.flush();
            Thread.sleep(500);
            socket.shutdownOutput();
            log.info("=============给服务端发送完数据");
            log.info("=============客户端开始接受数据");
            InputStream inputStream = socket.getInputStream();
            log.info("=============客户端开始接受到数据大小：{}", inputStream.available());
            byte[] bytes = new byte[1024];
            int length;
            StringBuilder sb = new StringBuilder();
            if (inputStream.available() <= 32) {
                result = new ArrayList<>();
                return;
            }
            while ((length = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, length, "UTF-8"));
            }
            String[] split = new String(sb.toString().getBytes("utf-8")).split("</Response>");
            List<String> response = new ArrayList<>();
            List<String> collect = Stream.of(split).map(element -> element + "</Response>").collect(Collectors.toList());
            collect.remove(collect.size() - 1);
            List<String> res = collect.stream().map(element -> {
                String[] elements = element.split("<Response>");
                return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response>" + elements[1];
            }).collect(Collectors.toList());
            List<JSONObject> objects = res.stream().map(element -> {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = XmlTool.xml2Json(element);
                    log.info("设备信息======》{}", jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;
            }).collect(Collectors.toList());

            result.addAll(objects);
            inputStream.close();
            os.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void getDataFromXml(List<JSONObject> result, GBsyncData gBsyncData){
        String xmlStr= null;
        try {
            xmlStr = XmlTool.readFile("D:/device.xml");
            Document doc = DocumentHelper.parseText(xmlStr);
            JSONObject json=new JSONObject();
            XmlTool.dom4j2Json(doc.getRootElement(),json);
            result.add(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private static byte[] int_byte(int id) {
        //int是32位   4个字节  创建length为4的byte数组
        byte[] arr = new byte[4];
        arr[0] = (byte) ((id >> 0 * 8) & 0xff);
        arr[1] = (byte) ((id >> 1 * 8) & 0xff);
        arr[2] = (byte) ((id >> 2 * 8) & 0xff);
        arr[3] = (byte) ((id >> 3 * 8) & 0xff);
        return arr;
    }
}
