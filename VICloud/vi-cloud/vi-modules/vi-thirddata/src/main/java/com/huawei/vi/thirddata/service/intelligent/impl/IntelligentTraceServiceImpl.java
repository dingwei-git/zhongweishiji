package com.huawei.vi.thirddata.service.intelligent.impl;

import com.huawei.vi.entity.po.ImageCountPo;
import com.huawei.vi.entity.vo.ImageCountVO;
import com.huawei.vi.thirddata.mapper.IntelligentTraceMapper;
import com.huawei.vi.thirddata.mapper.TblIpcIpMapper;
import com.huawei.vi.thirddata.service.intelligent.IntelligentTraceService;
import com.jovision.jaws.common.config.i18n.MessageSourceUtil;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.ServiceCommonConst;
import com.jovision.jaws.common.util.TableCommonConstant;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jovision.jaws.common.constant.CommonConst.*;


@Service
public class IntelligentTraceServiceImpl implements IntelligentTraceService {
    @Autowired
    IntelligentTraceMapper intelligentTraceMapper;

    @Autowired
    TblIpcIpMapper tblIpcIpMapper;

    @Autowired
    MessageSourceUtil messageSourceUtil;


    /*
     * 图片溯源按组织查询（新）
     *
     * */
    @Override
    public RestResult newIntelligentTrace(ImageCountVO imageCountVO){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        List<String> listStr = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        String[] organizations = imageCountVO.getOrganization();
        if(organizations == null || organizations.length ==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.organization"));
            return restResult;
        }
        String[] sceneVoList = imageCountVO.getSceneVoList();
        if(sceneVoList == null || sceneVoList.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        List<String> sceneVo = Arrays.asList(sceneVoList);
        List<String> str = new ArrayList<>();
        List<String> organization = Arrays.asList(organizations);
        //开始时间
        String startTime = imageCountVO.getStartTime();
        //结束时间
        String endTime = imageCountVO.getEndTime();
        String[] netWorkVolist = imageCountVO.getNetWorkList();
        if(netWorkVolist == null || netWorkVolist.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.networkElement"));
            return restResult;
        }
        map.put("netWorkVolist",netWorkVolist);
        List<Map> ipList = intelligentTraceMapper.getServerInfo(map);
        if(ipList==null || ipList.size()<=0 || ipList.isEmpty()){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        List<String> netWorkVolists = Arrays.asList(netWorkVolist);
        map.put("startTime",imageCountVO.getStartTime());
        map.put("endTime",imageCountVO.getEndTime());
        map.put("netWorks",netWorkVolists);
        map.put("scenes",sceneVo);
        map.put("tableName",Server_Param_Config);
        String relationShip = tblIpcIpMapper.getRelationShip();
        if(StringUtils.isEmpty(relationShip)){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        map.put("relationShip",relationShip);
        map.put("organization",organization);
        if(imageCountVO.getTimeType().equals("hour")) {
            for (int y = 0; y < organization.size(); y++) {
                Map<String, Object> organMap = new HashMap<>();
                String id = organization.get(y);
                String fileds = null;
                if (id.length() == 3) {
                    map.put("id0", id);
                    fileds = "filed0";
                    map.put("filed0", fileds);
                }
                if (id.length() == 6) {
                    map.put("id1", id);
                    fileds = "filed1";
                    map.put("filed1", fileds);
                }
                if (id.length() == 9) {
                    map.put("id2", id);
                    fileds = "filed2";
                    map.put("filed2", fileds);
                }
                if (id.length() == 12) {
                    map.put("id3", id);
                    fileds = "filed3";
                    map.put("filed3", fileds);
                }
            }
            map.put("ipList", ipList);
            String name = null;
            String ipAddress = null;
            List<String> nameList = new ArrayList<>();
            List<String> ipLists = new ArrayList<>();
            List<Map<String, Object>> maps = tblIpcIpMapper.getlevelNames(map);
            if(maps==null || maps.size()<=0 || maps.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            for (int x = 0; x < maps.size(); x++) {
                name = maps.get(x).get("NAME").toString();
                nameList.add(name);
            }
            map.put("cityLists", nameList);
            for (int i = 0; i < ipList.size(); i++) {
                ipAddress = ipList.get(i).get("SERVICE_IP_ADDRESS").toString();
                ipLists.add(ipAddress);
            }
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime == null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i).substring(0, 19);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }

            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_HOUR_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCount(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListCompare = new ArrayList<>();
            for(int i=0;i<=resultList.size()-1;i++){
                if(i == 0 ){
                    resultListCompare.add(resultList.get(i));
                }else {
                    Map<String,Object> compareMap = new HashMap<>();
                    compareMap.putAll(resultList.get(i));
                    for(int a=0;a<=nameList.size()-1;a++){
                        String column = (maps.get(a).get("NAME").toString()+"_VCM0").toUpperCase();
                        compareMap.put(column,Integer.parseInt(resultList.get(i).get((column)).toString())-Integer.parseInt(resultList.get(i-1).get(column).toString()));
                    }
                    resultListCompare.add(compareMap);
                }
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultListCompare.size(); i++ ) {
                Map<String, Object> mapLists = resultListCompare.get(i);
                String time = null;
                Iterator<String> it = mapLists.keySet().iterator();
                while( it.hasNext() ) {
                    Map<String, Object> newMap = new HashMap<>();
                    String keys = (String) it.next();
                    if (keys.equals("PERIOD_START_TIME")) {
                        time = mapLists.get(keys).toString();
                    }
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM0")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_");
                        String[] ke = null;
                        ke = keys.split("_VCM0");
                        newKey = keys.replaceAll("_VCM0", "");
                        newMap.put("organization",  ke[0]);
                        newMap.put("pictureCount", value);
                        newMap.put("time", time);
                        newMap.put("remark",  "VCM(" + ipLists.get(0) + ")");
                        resultListNew.add(newMap);
                    }
                }
                for(int x=0;x<resultListNew.size();x++){
                    if(resultListNew.get(x).get("time")==null){
                        resultListNew.get(x).put("time",time);
                    }
                }
            }
            Collections.sort(resultListNew,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });
            restResult.setData(resultListNew);
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        }
        if(imageCountVO.getTimeType().equals("day")) {
            for (int y = 0; y < organization.size(); y++) {
                Map<String, Object> organMap = new HashMap<>();
                String id = organization.get(y);
                String fileds = null;
                if (id.length() == 3) {
                    map.put("id0", id);
                    fileds = "filed0";
                    map.put("filed0", fileds);
                }
                if (id.length() == 6) {
                    map.put("id1", id);
                    fileds = "filed1";
                    map.put("filed1", fileds);
                }
                if (id.length() == 9) {
                    map.put("id2", id);
                    fileds = "filed2";
                    map.put("filed2", fileds);
                }
                if (id.length() == 12) {
                    map.put("id3", id);
                    fileds = "filed3";
                    map.put("filed3", fileds);
                }
            }
            map.put("ipList", ipList);
            String name = null;
            String ipAddress = null;
            List<String> nameList = new ArrayList<>();
            List<String> ipLists = new ArrayList<>();
            List<Map<String, Object>> maps = tblIpcIpMapper.getlevelNames(map);
            if(maps==null || maps.size()<=0 || maps.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            for (int x = 0; x < maps.size(); x++) {
                name = maps.get(x).get("NAME").toString();
                nameList.add(name);
            }
            map.put("cityLists", nameList);
            for (int i = 0; i < ipList.size(); i++) {
                ipAddress = ipList.get(i).get("SERVICE_IP_ADDRESS").toString();
                ipLists.add(ipAddress);
            }
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime == null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i).substring(0, 19);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }

            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_DAY_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCountDay(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultList.size(); i++ ) {
                Map<String, Object> mapLists = resultList.get(i);
                String time = null;
                Iterator<String> it = mapLists.keySet().iterator();
                while( it.hasNext() ) {
                    Map<String, Object> newMap = new HashMap<>();
                    String keys = (String) it.next();
                    if (keys.equals("PERIOD_START_TIME")) {
                        time = mapLists.get(keys).toString();
                    }
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM0")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_");
                        newKey = keys.replaceAll("_VCM0", "");
                        newMap.put("organization", keyVal[0]);
                        newMap.put("pictureCount", value);
                        newMap.put("time", time);
                        newMap.put("remark",   "VCM(" + ipLists.get(0) + ")");
                        resultListNew.add(newMap);
                    }
                }
                for(int x=0;x<resultListNew.size();x++){
                    if(resultListNew.get(x).get("time")==null){
                        resultListNew.get(x).put("time",time);
                    }
                }
            }
        Collections.sort(resultListNew,new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                int ret = 0;
                //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                return ret;
            }
        });
        restResult.setData(resultListNew);
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        }
        return restResult;

    }


    /*
    * 按摄像机查询（新）
    * */
    @Override
    public RestResult newIntelligentTraceCamera(ImageCountVO imageCountVO){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        List<String> listStr = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        String[] organizations = imageCountVO.getOrganization();
        if(organizations == null || organizations.length ==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.organization"));
            return restResult;
        }
        String[] sceneVoList = imageCountVO.getSceneVoList();
        if(sceneVoList == null || sceneVoList.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        List<String> sceneVo = Arrays.asList(sceneVoList);
        List<String> str = new ArrayList<>();
        List<String> organization = Arrays.asList(organizations);
        //开始时间
        String startTime = imageCountVO.getStartTime();
        //结束时间
        String endTime = imageCountVO.getEndTime();
        String[] netWorkVolist = imageCountVO.getNetWorkList();
        if(netWorkVolist == null || netWorkVolist.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.networkElement"));
            return restResult;
        }
        map.put("netWorkVolist",netWorkVolist);
        List<Map> ipList = intelligentTraceMapper.getServerInfo(map);
        if(ipList==null || ipList.size()<=0 || ipList.isEmpty()){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        List<String> ipLists = new ArrayList<>();
        if(ipList.size()>0 && ipList!=null) {
            for (int i = 0; i < ipList.size(); i++) {
                String ips = ipList.get(i).get("SERVICE_IP_ADDRESS").toString();
                ipLists.add(ips);
            }
        }
        map.put("ipLists",ipLists);
        List<String> netWorkVolists = Arrays.asList(netWorkVolist);
        map.put("startTime",imageCountVO.getStartTime());
        map.put("endTime",imageCountVO.getEndTime());
        map.put("netWorks",netWorkVolists);
        map.put("scenes",sceneVo);
        map.put("tableName",Server_Param_Config);
        String relationShip = tblIpcIpMapper.getRelationShip();
        map.put("relationShip",relationShip);
        map.put("organization",organization);
        //获取cameraSn
        String maxTime = tblIpcIpMapper.getMaxTime();
        if(StringUtils.isEmpty(maxTime)){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        maxTime = maxTime.replaceAll("-","").replaceAll(" ","").replaceAll(":","");
        map.put("cameraTable","TBL_CAMERA_MANAGER_ORIGINAL_"+maxTime);
        //获取cameraSns
        List<Map<String,Object>> cameraSns = tblIpcIpMapper.getcamSns(map);
        if(cameraSns==null || cameraSns.size()<=0 || cameraSns.isEmpty()){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        List<String> ipListes = new ArrayList<>();
        List<String> camernStrList = new ArrayList<>();
        for(int x=0;x<cameraSns.size();x++){
            ipListes.add(cameraSns.get(x).get("IP").toString());
            camernStrList.add(cameraSns.get(x).get("CAMERA_SN").toString());
        }
        map.put("ipListes",ipListes);
        map.put("camernStrList",camernStrList);
        if(imageCountVO.getTimeType().equals("hour")) {
            map.put("ipList", ipList);
            List<Map<String, Object>> maps = tblIpcIpMapper.getCamernSns(map);
            if(maps==null || maps.size()<=0 || maps.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime==null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            Map<String, Object> map1 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }
            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_HOUR_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCountCamera(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListCompare = new ArrayList<>();
            for(int i=0;i<=resultList.size()-1;i++){
                if(i == 0 ){
                    resultListCompare.add(resultList.get(i));
                }else {
                    Map<String,Object> compareMap = new HashMap<>();
                    compareMap.putAll(resultList.get(i));
                    for(int a=0;a<=cameraSns.size()-1;a++){
                        String column = ("_VCM"+a).toUpperCase();
                        compareMap.put(column,Integer.parseInt(resultList.get(i).get((column)).toString())-Integer.parseInt(resultList.get(i-1).get(column).toString()));
                    }
                    resultListCompare.add(compareMap);
                }
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultListCompare.size(); i++ ) {
                Map<String, Object> mapLists = resultListCompare.get(i);
                String time = null;
                Iterator<String> it = mapLists.keySet().iterator();
                while( it.hasNext() ) {
                    Map<String, Object> newMap = new HashMap<>();
                    String keys = (String) it.next();
                    if (keys.equals("PERIOD_START_TIME")) {
                        time = mapLists.get(keys).toString();
                    }
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCM");
                        Integer count =Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCM"+keyVal[1], ipListes.get(count));
                        newMap.put("organization", newKey);
                        newMap.put("pictureCount", value);
                        newMap.put("time", time);
                        newMap.put("remark", "VCM(" + ipLists.get(count) + ")");
                        resultListNew.add(newMap);
                    }
                    if (keys.contains("_VCN")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCN");
                        Integer count =Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCN"+keyVal[1], organization.get(count));
                        newMap.put("organization", newKey);
                        newMap.put("pictureCount", value);
                        newMap.put("time", time);
                        newMap.put("remark", "VCN(" + ipLists.get(count) + ")");
                        resultListNew.add(newMap);
                    }
                }
                for(int x=0;x<resultListNew.size();x++){
                    if(resultListNew.get(x).get("time")==null){
                        resultListNew.get(x).put("time",time);
                    }
                }
            }
            Collections.sort(resultListNew,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });
            restResult.setData(resultListNew);
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        }
        if(imageCountVO.getTimeType().equals("day")) {
            map.put("ipList", ipList);
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime == null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            Map<String, Object> map1 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i).substring(0, 19);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }

            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_HOUR_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCountCameraDay(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultList.size(); i++ ) {
                Map<String, Object> mapLists = resultList.get(i);
                String time = null;
                Iterator<String> it = mapLists.keySet().iterator();
                while( it.hasNext() ) {
                    Map<String, Object> newMap = new HashMap<>();
                    String keys = (String) it.next();
                    if (keys.equals("PERIOD_START_TIME")) {
                        time = mapLists.get(keys).toString();
                    }
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCM");
                        Integer count =Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCM"+keyVal[1], ipListes.get(count));
                        newMap.put("organization", newKey);
                        newMap.put("pictureCount", value);
                        newMap.put("time", time);
                        newMap.put("remark", "VCM(" + ipLists.get(count) + ")");
                        resultListNew.add(newMap);
                    }
                    if (keys.contains("_VCN")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCN");
                        Integer count =Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCN"+keyVal[1], organization.get(count));
                        newMap.put("organization", newKey);
                        newMap.put("pictureCount", value);
                        newMap.put("time", time);
                        newMap.put("remark", "VCN(" + ipLists.get(count) + ")");
                        resultListNew.add(newMap);
                    }
                }
                for(int x=0;x<resultListNew.size();x++){
                    if(resultListNew.get(x).get("time")==null){
                        resultListNew.get(x).put("time",time);
                    }
                }
            }
            Collections.sort(resultListNew,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });

            restResult.setData(resultListNew);
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        }
        return restResult;
    }


    /*
     * 图片溯源按摄像机查询分页(新)
     * */
    @Override
    public RestResult newIntelligentTraceCameras(ImageCountVO imageCountVO){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        Integer currentPage = imageCountVO.getCurrentPage();
        if ((Object) currentPage == null) {
            currentPage = 0;
        }
        Integer pageSize = imageCountVO.getPageSize();
        if ((Object) pageSize == null) {
            pageSize = 0;
        }
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        List<String> listStr = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        String[] organizations = imageCountVO.getOrganization();
        if(organizations == null || organizations.length ==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.organization"));
            return restResult;
        }
        String[] sceneVoList = imageCountVO.getSceneVoList();
        if(sceneVoList == null || sceneVoList.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        List<String> sceneVo = Arrays.asList(sceneVoList);
        List<String> str = new ArrayList<>();
        List<String> organization = Arrays.asList(organizations);
        //开始时间
        String startTime = imageCountVO.getStartTime();
        //结束时间
        String endTime = imageCountVO.getEndTime();
        String[] netWorkVolist = imageCountVO.getNetWorkList();
        if(netWorkVolist == null || netWorkVolist.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.networkElement"));
            return restResult;
        }
        map.put("netWorkVolist",netWorkVolist);
        List<Map> ipList = intelligentTraceMapper.getServerInfo(map);
        if(ipList==null || ipList.size()<=0 || ipList.isEmpty()){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        List<String> ipLists = new ArrayList<>();
        if(ipList.size()>0 && ipList!=null) {
            for (int i = 0; i < ipList.size(); i++) {
                String ips = ipList.get(i).get("SERVICE_IP_ADDRESS").toString();
                ipLists.add(ips);
            }
        }
        map.put("ipLists",ipLists);
        List<String> netWorkVolists = Arrays.asList(netWorkVolist);
        map.put("startTime",imageCountVO.getStartTime());
        map.put("endTime",imageCountVO.getEndTime());
        map.put("netWorks",netWorkVolists);
        map.put("scenes",sceneVo);
        map.put("tableName",Server_Param_Config);
        String relationShip = tblIpcIpMapper.getRelationShip();
        if(StringUtils.isEmpty(relationShip)){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        map.put("relationShip",relationShip);
        map.put("organization",organization);
        //获取cameraSn
        String maxTime = tblIpcIpMapper.getMaxTime();
        if(StringUtils.isEmpty(maxTime)){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        maxTime = maxTime.replaceAll("-","").replaceAll(" ","").replaceAll(":","");
        map.put("cameraTable","TBL_CAMERA_MANAGER_ORIGINAL_"+maxTime);
        //获取cameraSns
        List<Map<String,Object>> cameraSns = tblIpcIpMapper.getcamSns(map);
        if(cameraSns==null || cameraSns.size()<=0 || cameraSns.isEmpty()){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        List<String> ipListes = new ArrayList<>();
        List<String> camernStrList = new ArrayList<>();
        for(int x=0;x<cameraSns.size();x++){
            ipListes.add(cameraSns.get(x).get("IP").toString());
            camernStrList.add(cameraSns.get(x).get("CAMERA_SN").toString());
        }
        map.put("ipListes",ipListes);
        map.put("camernStrList",camernStrList);
        if(imageCountVO.getTimeType().equals("hour")) {
            map.put("ipList", ipList);
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime==null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }
            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_HOUR_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCountCamera(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListCompare = new ArrayList<>();
            for(int i=0;i<=resultList.size()-1;i++){
                if(i == 0 ){
                    resultListCompare.add(resultList.get(i));
                }else {
                    Map<String,Object> compareMap = new HashMap<>();
                    compareMap.putAll(resultList.get(i));
                    for(int a=0;a<=cameraSns.size()-1;a++){
                        String column = ("_VCM"+a).toUpperCase();
                        compareMap.put(column,Integer.parseInt(resultList.get(i).get((column)).toString())-Integer.parseInt(resultList.get(i-1).get(column).toString()));
                    }
                    resultListCompare.add(compareMap);
                }
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultListCompare.size(); i++ ) {
                Map<String, Object> mapLists = resultListCompare.get(i);
                String time = null;
                Map<String,Object> objectMap = new HashMap<>();
                objectMap.putAll(mapLists);
                Set<String> keySet =  mapLists.keySet();
                for(String key : keySet){
                    Map<String, Object> newMap = new HashMap<>();
                    String keys = key;
                    if (keys.equals("PERIOD_START_TIME")) {
                        time = mapLists.get(keys).toString();
                    }
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCM");
                        Integer count = Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCM"+keyVal[1], ipListes.get(count)+"_VCM(" + ipLists.get(count) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time", mapLists.get("PERIOD_START_TIME"));
                    }
                    if (keys.contains("_VCN")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCN");
                        Integer count = Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCN"+keyVal[1], ipListes.get(count)+"_VCN(" + ipLists.get(count) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time", mapLists.get("PERIOD_START_TIME"));
                    }
                }
                resultListNew.add(objectMap);
            }
            Collections.sort(resultListNew,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });
            List<Map<String,Object>> ma= pageList(resultListNew,pageSize,currentPage);
            Integer num = resultListNew.size();
            Map<String,Object>mas = new HashedMap();
            mas.put("total",num);
            mas.put("list",ma);
            if(resultListNew!=null&&resultListNew.size()>0){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(mas);
                restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
            }else{
                Map<String,Object> listMap = new HashedMap();
                List<String> stringList = new ArrayList<>();
                listMap.put("total",0);
                listMap.put("list",stringList);
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(listMap);
            }
        }
        if(imageCountVO.getTimeType().equals("day")) {
            map.put("ipList", ipList);
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime==null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i).substring(0, 19);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }

            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_HOUR_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCountCameraDay(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultList.size(); i++ ) {
                Map<String, Object> mapLists = resultList.get(i);
                String time = null;
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.putAll(mapLists);
                Set<String> keySet = mapLists.keySet();
                for(String key :keySet){
                    Map<String, Object> newMap = new HashMap<>();
                    String keys =key;
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCM");
                        Integer count = Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCM"+keyVal[1], ipListes.get(count)+"_VCM(" + ipLists.get(count) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time", mapLists.get("PERIOD_START_TIME"));
                    }
                    if (keys.contains("_VCN")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCN");
                        Integer count = Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCN"+keyVal[1], ipListes.get(count)+"_VCN(" + ipLists.get(count) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time", mapLists.get("PERIOD_START_TIME"));
                    }
                }
                resultListNew.add(objectMap);
            }
            Collections.sort(resultListNew,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });
            List<Map<String,Object>> ma= pageList(resultListNew,pageSize,currentPage);
            Integer num = resultListNew.size();
            Map<String,Object>mas = new HashedMap();
            mas.put("total",num);
            mas.put("list",ma);
            if(resultListNew!=null&&resultListNew.size()>0){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(mas);
                restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
            }else{
                Map<String,Object> listMap = new HashedMap();
                List<String> stringList = new ArrayList<>();
                listMap.put("total",0);
                listMap.put("list",stringList);
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(listMap);
            }
        }
        return restResult;
    }


    /*
     * 图片溯源按组织查询分页(新)
     * */
    @Override
    public RestResult intelligentTracePages(ImageCountVO imageCountVO){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        Integer currentPage = imageCountVO.getCurrentPage();
        if((Object) currentPage == null){
            currentPage = 0;
        }
        Integer pageSize = imageCountVO.getPageSize();
        if((Object) pageSize == null){
            pageSize = 0;
        }
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        List<String> listStr = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        String[] organizations = imageCountVO.getOrganization();
        if(organizations == null || organizations.length ==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.organization"));
            return restResult;
        }
        String[] sceneVoList = imageCountVO.getSceneVoList();
        if(sceneVoList == null || sceneVoList.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        List<String> sceneVo = Arrays.asList(sceneVoList);
        List<String> str = new ArrayList<>();
        List<String> organization = Arrays.asList(organizations);
        //开始时间
        String startTime = imageCountVO.getStartTime();
        //结束时间
        String endTime = imageCountVO.getEndTime();
        String[] netWorkVolist = imageCountVO.getNetWorkList();
        if(netWorkVolist == null || netWorkVolist.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.networkElement"));
            return restResult;
        }
        map.put("netWorkVolist",netWorkVolist);
        List<Map> ipList = intelligentTraceMapper.getServerInfo(map);
        if(ipList==null || ipList.size()<=0 || ipList.isEmpty()){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        List<String> netWorkVolists = Arrays.asList(netWorkVolist);
        map.put("startTime",imageCountVO.getStartTime());
        map.put("endTime",imageCountVO.getEndTime());
        map.put("netWorks",netWorkVolists);
        map.put("scenes",sceneVo);
        map.put("tableName",Server_Param_Config);
        String relationShip = tblIpcIpMapper.getRelationShip();
        if(StringUtils.isEmpty(relationShip)){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        map.put("relationShip",relationShip);
        map.put("organization",organization);
        if(imageCountVO.getTimeType().equals("hour")) {
            for (int y = 0; y < organization.size(); y++) {
                Map<String, Object> organMap = new HashMap<>();
                String id = organization.get(y);
                String fileds = null;
                if (id.length() == 3) {
                    map.put("id0", id);
                    fileds = "filed0";
                    map.put("filed0", fileds);
                }
                if (id.length() == 6) {
                    map.put("id1", id);
                    fileds = "filed1";
                    map.put("filed1", fileds);
                }
                if (id.length() == 9) {
                    map.put("id2", id);
                    fileds = "filed2";
                    map.put("filed2", fileds);
                }
                if (id.length() == 12) {
                    map.put("id3", id);
                    fileds = "filed3";
                    map.put("filed3", fileds);
                }
            }
            map.put("ipList", ipList);
            String name = null;
            String ipAddress = null;
            List<String> nameList = new ArrayList<>();
            List<String> ipLists = new ArrayList<>();
            List<Map<String, Object>> maps = tblIpcIpMapper.getlevelNames(map);
            if(maps==null || maps.size()<=0 || maps.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            for (int x = 0; x < maps.size(); x++) {
                name = maps.get(x).get("NAME").toString();
                nameList.add(name);
            }
            map.put("cityLists", nameList);
            for (int i = 0; i < ipList.size(); i++) {
                ipAddress = ipList.get(i).get("SERVICE_IP_ADDRESS").toString();
                ipLists.add(ipAddress);
            }
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime==null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i).substring(0, 19);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }

            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_HOUR_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCount(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListCompare = new ArrayList<>();
            for(int i=0;i<=resultList.size()-1;i++){
                if(i == 0 ){
                    resultListCompare.add(resultList.get(i));
                }else {
                    Map<String,Object> compareMap = new HashMap<>();
                    compareMap.putAll(resultList.get(i));
                    for(int a=0;a<=nameList.size()-1;a++){
                        String column = (maps.get(a).get("NAME").toString()+"_VCM0").toUpperCase();
                        compareMap.put(column,Integer.parseInt(resultList.get(i).get((column)).toString())-Integer.parseInt(resultList.get(i-1).get(column).toString()));
                    }
                    resultListCompare.add(compareMap);
                }
            }

            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultListCompare.size(); i++ ) {
                Map<String, Object> mapLists = resultListCompare.get(i);
                Map<String, Object>  objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.putAll(mapLists);
                String time = null;
                Set<String>keySet = mapLists.keySet();
                /*Iterator<String> it = mapLists.keySet().iterator();
                while( it.hasNext() ) {*/
                for(String key : keySet){
                    Map<String, Object> newMap = new HashMap<>();
                    String keys = key;
                    /*if (keys.equals("PERIOD_START_TIME")) {
                        time = mapLists.get(keys).toString();
                    }*/
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM0")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_");
                        newKey = keys.replaceAll("_VCM0", "_VCM(" + ipLists.get(0) + ")");
                        objectObjectHashMap.put(newKey,value);
                        objectObjectHashMap.put("time",mapLists.get("PERIOD_START_TIME"));
                        /*newMap.put(keyVal[0]+"_VCM(" + ipLists.get(0) + ")",value);
                        newMap.put("time", keyVal[0]+"_VCM(" + ipLists.get(0) + ")");*/
                    }
                }
                resultListNew.add(objectObjectHashMap);
                /*for(int x=0;x<resultListNew.size();x++){
                    if(resultListNew.get(x).get("time")==null){
                        resultListNew.get(x).put("time",time);
                    }
                }*/
            }
            Collections.sort(resultListNew,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });
            List<Map<String,Object>> ma= pageList(resultListNew,pageSize,currentPage);
            Integer num = resultListNew.size();
            Map<String,Object>mas = new HashedMap();
            mas.put("total",num);
            mas.put("list",ma);
            if(resultListNew!=null&&resultListNew.size()>0){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(mas);
                restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
            }else{
                Map<String,Object> listMap = new HashedMap();
                List<String> stringList = new ArrayList<>();
                listMap.put("total",0);
                listMap.put("list",stringList);
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(listMap);
            }
        }
        if(imageCountVO.getTimeType().equals("day")) {
            for (int y = 0; y < organization.size(); y++) {
                Map<String, Object> organMap = new HashMap<>();
                String id = organization.get(y);
                String fileds = null;
                if (id.length() == 3) {
                    map.put("id0", id);
                    fileds = "filed0";
                    map.put("filed0", fileds);
                }
                if (id.length() == 6) {
                    map.put("id1", id);
                    fileds = "filed1";
                    map.put("filed1", fileds);
                }
                if (id.length() == 9) {
                    map.put("id2", id);
                    fileds = "filed2";
                    map.put("filed2", fileds);
                }
                if (id.length() == 12) {
                    map.put("id3", id);
                    fileds = "filed3";
                    map.put("filed3", fileds);
                }
            }
            map.put("ipList", ipList);
            String name = null;
            String ipAddress = null;
            List<String> nameList = new ArrayList<>();
            List<String> ipLists = new ArrayList<>();
            List<Map<String, Object>> maps = tblIpcIpMapper.getlevelNames(map);
            if(maps==null || maps.size()<=0 || maps.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            for (int x = 0; x < maps.size(); x++) {
                name = maps.get(x).get("NAME").toString();
                nameList.add(name);
            }
            map.put("cityLists", nameList);
            for (int i = 0; i < ipList.size(); i++) {
                ipAddress = ipList.get(i).get("SERVICE_IP_ADDRESS").toString();
                ipLists.add(ipAddress);
            }
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime==null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            System.out.println(stringTime);
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i).substring(0, 19);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }

            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            stringTi.sort((String s1, String s2) -> s1.compareTo(s2));
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_DAY_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCountDay(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultList.size(); i++ ) {
                Map<String, Object> mapLists = resultList.get(i);
                Map<String, Object>  objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.putAll(mapLists);
                String time = null;
                Set<String> keySet = mapLists.keySet();
                for(String key :keySet ) {
                    //Map<String, Object> newMap = new HashMap<>();
                    String keys =key;

                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM0")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_");
                        newKey = keys.replaceAll("_VCM0", "_VCM(" + ipLists.get(0) + ")");
                        /*newMap.put("organization", newKey);
                        newMap.put("pictureCount", value);*/
                        objectObjectHashMap.put(newKey,value);
                        /* newMap.put(newKey,value);*/
                        objectObjectHashMap.put("time", mapLists.get("PERIOD_START_TIME"));
                        // newMap.put("remark", keyVal[0] + "_VCM(" + ipLists.get(0) + ")");
                    }
                }
                resultListNew.add(objectObjectHashMap);

            }
            Collections.sort(resultListNew,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });

            List<Map<String,Object>> ma= pageList(resultListNew,pageSize,currentPage);
            Integer num = resultListNew.size();
            Map<String,Object>mas = new HashedMap();
            mas.put("total",num);
            mas.put("list",ma);
            if(resultListNew!=null&&resultListNew.size()>0){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(mas);
                restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
            }else{
                Map<String,Object> listMap = new HashedMap();
                List<String> stringList = new ArrayList<>();
                listMap.put("total",0);
                listMap.put("list",stringList);
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(listMap);
            }
        }
        return restResult;

    }



    /*
    * 图片溯源按组织查询
    * */
    @Override
    public RestResult intelligentTrace(ImageCountVO imageCountVO){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        //开始时间
        String startTime = imageCountVO.getStartTime();
        //结束时间
        String endTime = imageCountVO.getEndTime();
        String[] netWorkVolist = imageCountVO.getNetWorkList();
        if(netWorkVolist == null || netWorkVolist.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.networkElement"));
            return restResult;
        }
        map.put("netWorkVolist",netWorkVolist);
        List<Map> list = intelligentTraceMapper.getServerInfo(map);
        String[] sceneVoList = imageCountVO.getSceneVoList();
        if(sceneVoList == null || sceneVoList.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        String[] organizations = imageCountVO.getOrganization();
        if(organizations == null || organizations.length ==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.organization"));
            return restResult;
        }
        List<String> organization = Arrays.asList(organizations);
        List<String> netWorkVolists = Arrays.asList(netWorkVolist);
        //获取vcn名称
        Map<String,Object> maps = new HashedMap();
        maps.put("serviceName",netWorkVolists);
        List<Map> ivsName = intelligentTraceMapper.getServiceName(maps);
        List<String> sceneVo = Arrays.asList(sceneVoList);
        if(StringUtils.isEmpty(imageCountVO.getStartTime())){
            restResult.setMessage(messageSourceUtil.getMessage("device.startTime"));
            return restResult;
        }
        if(StringUtils.isEmpty(imageCountVO.getEndTime())){
            restResult.setMessage(messageSourceUtil.getMessage("device.endTime"));
            return restResult;
        }
        if(StringUtils.isEmpty(imageCountVO.getTimeType())){
            restResult.setMessage(messageSourceUtil.getMessage("device.TimeDimension"));
            return restResult;
        }
        map.put("startTime",imageCountVO.getStartTime());
        map.put("endTime",imageCountVO.getEndTime());
        map.put("netWorks",netWorkVolists);
        map.put("scenes",sceneVo);
        map.put("tableName",Server_Param_Config);
        String filed = null;
        String organ = "";
        String organs = "";
        if(imageCountVO.getTimeType().equals("day")) {
            map.put("tabName", Image_Day_Statistics_VCN);
            for (int y = 0; y < organization.size(); y++) {
                //天数时间差
                List<String> stringTime =  getBetweenDates(startTime,endTime);
                stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
                Map<String, Object> organMap = new HashMap<>();
                String id = organization.get(y);
                String fileds = null;
                if (id.length() == 3) {
                    organMap.put("id0", id);
                    fileds = "filed0";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 6) {
                    organMap.put("id1", id);
                    fileds = "filed1";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 9) {
                    organMap.put("id2", id);
                    fileds = "filed2";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 12) {
                    organMap.put("id3", id);
                    fileds = "filed3";
                    organMap.put("fileds", fileds);
                }
                List<Map> cameraSn = tblIpcIpMapper.getOrganization(organMap);
                if (cameraSn == null || cameraSn.size() <= 0) {
                    cameraSn.add(null);
                }
                map.put("groupNames",cameraSn);
                //符合条件的所有sn
                imageCountPos = intelligentTraceMapper.getIntelligent(map);
                for(int x=0;x<imageCountPos.size();x++){
                    String dateTime = imageCountPos.get(x).getTime().substring(0,10);
                    imageCountPos.get(x).setTime(dateTime);
                }
                if(imageCountPos ==null || imageCountPos.size()<=0){
                    Map<String,Object> listMap = new HashedMap();
                    List<String> stringList = new ArrayList<>();
                    listMap.put("list",stringList);
                    restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                    restResult.setData(listMap);
                    return restResult;
                }
                List<String> serName = new ArrayList<>();
                for(int t=0;t<ivsName.size();t++){
                    String ivsNames = ivsName.get(t).get("service_name").toString();
                    serName.add(ivsNames);
                }
                filed = cameraSn.get(1).get("fileds").toString();
                    for(int x = 0; x < imageCountPos.size(); x++){
                    imageCountPos.get(x).setOrganization(filed);
                    //满足条件设备的时间
                    String time = imageCountPos.get(x).getTime();
                    for(int z=0;z<stringTime.size();z++){
                      if(stringTime.get(z).equals(time)){
                        for(int j=0;j<ivsName.size();j++){
                            if(!ivsName.get(j).get("service_name").equals(imageCountPos.get(x).getRemark())){
                                ImageCountPo imageCountPo = new ImageCountPo();
                                imageCountPo.setOrganization(filed);
                                imageCountPo.setTime(time);
                                imageCountPo.setPictureCount("0");
                                imageCountPo.setRemark(ivsName.get(j).get("service_name").toString());
                                newImageCountPos.add(imageCountPo);
                            }else {
                                newImageCountPos.add(imageCountPos.get(x));
                                break;
                            }
                        }
                    }else {
                        for(int i=0;i<ivsName.size();i++) {
                            ImageCountPo imageCountPo1 = new ImageCountPo();
                            imageCountPo1.setRemark(ivsName.get(i).get("service_name").toString());
                            imageCountPo1.setPictureCount("0");
                            imageCountPo1.setTime(stringTime.get(z));
                            imageCountPo1.setOrganization(filed);
                            newImageCountPos.add(imageCountPo1);
                        }
                    }
                    }
                }
                List<String> imageCountString = new ArrayList<>();
                for(int z=0;z<newImageCountPos.size();z++){
                    String timeImageCount = newImageCountPos.get(z).getTime();
                    imageCountString.add(timeImageCount);
                }
                LinkedHashSet<String> hashSet = new LinkedHashSet<>(imageCountString);
                //获取a-b的交集
                stringTime.removeAll(hashSet);
                for(int s=0;s<stringTime.size();s++){
                    for(int o=0;o<ivsName.size();o++){
                        ImageCountPo images = new ImageCountPo();
                        images.setRemark(ivsName.get(o).get("service_name").toString());
                        images.setPictureCount("0");
                        images.setTime(stringTime.get(s));
                        images.setOrganization(filed);
                        newImageCountPos.add(images);
                    }
                }
            }
        }
        if(imageCountVO.getTimeType().equals("hour")){
            map.put("tabName",Image_Hour_Statistics_VCN);
            imageCountPos = intelligentTraceMapper.getIntelligentHour(map);
            if(imageCountPos ==null || imageCountPos.size()<=0){
                Map<String,Object> listMap = new HashedMap();
                List<String> stringList = new ArrayList<>();
                listMap.put("list",stringList);
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(listMap);
                return restResult;
            }
            for (int y = 0; y < organization.size(); y++) {
                //小时时间差
                List<String> stringTime =  getBetweenDateHours(startTime,endTime);
                stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
                Map<String, Object> organMap = new HashMap<>();
                String id = organization.get(y);
                String fileds = null;
                if (id.length() == 3) {
                    organMap.put("id0", id);
                    fileds = "filed0";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 6) {
                    organMap.put("id1", id);
                    fileds = "filed1";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 9) {
                    organMap.put("id2", id);
                    fileds = "filed2";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 12) {
                    organMap.put("id3", id);
                    fileds = "filed3";
                    organMap.put("fileds", fileds);
                }
                List<Map> cameraSn = tblIpcIpMapper.getOrganization(organMap);
                if (cameraSn == null || cameraSn.size() <= 0) {
                    cameraSn.add(null);
                }

            map.put("groupNames",cameraSn);
            imageCountPos = intelligentTraceMapper.getIntelligentHour(map);
            for(int t=0;t<imageCountPos.size();t++){
                String dateTime = imageCountPos.get(t).getTime().substring(0,13);
                imageCountPos.get(t).setTime(dateTime);
            }
            if(imageCountPos ==null || imageCountPos.size()<=0){
                Map<String,Object> listMap = new HashedMap();
                List<String> stringList = new ArrayList<>();
                listMap.put("list",stringList);
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(listMap);
                return restResult;
            }
            List<String> serName = new ArrayList<>();
            for(int t=0;t<ivsName.size();t++){
                String ivsNames = ivsName.get(t).get("service_name").toString();
                serName.add(ivsNames);
            }
            filed = cameraSn.get(1).get("fileds").toString();
                for(int x = 0; x < imageCountPos.size(); x++){
                    imageCountPos.get(x).setOrganization(filed);
                    //满足条件设备的时间
                    String time = imageCountPos.get(x).getTime();
                     for(int h=0;h<stringTime.size();h++){
                         if(time.equals(stringTime.get(h))) {
                             for (int j = 0; j < ivsName.size(); j++) {
                                 if (!ivsName.get(j).get("service_name").equals(imageCountPos.get(x).getRemark())) {
                                     ImageCountPo imageCountPo = new ImageCountPo();
                                     imageCountPo.setOrganization(filed);
                                     imageCountPo.setTime(time);
                                     imageCountPo.setPictureCount("0");
                                     imageCountPo.setRemark(ivsName.get(j).get("service_name").toString());
                                     newImageCountPos.add(imageCountPo);
                                 } else {
                                     newImageCountPos.add(imageCountPos.get(x));
                                     break;
                                 }
                             }
                         }else {
                            for(int i=0;i<ivsName.size();i++) {
                                ImageCountPo imageCountPo1 = new ImageCountPo();
                                imageCountPo1.setRemark(ivsName.get(i).get("service_name").toString());
                                imageCountPo1.setPictureCount("0");
                                imageCountPo1.setTime(stringTime.get(h));
                                imageCountPo1.setOrganization(filed);
                                newImageCountPos.add(imageCountPo1);
                         }
                     }
                }
             }
                List<String> imageCountString = new ArrayList<>();
                for(int z=0;z<newImageCountPos.size();z++){
                    String timeImageCount = newImageCountPos.get(z).getTime();
                    imageCountString.add(timeImageCount);
                }
                LinkedHashSet<String> hashSet = new LinkedHashSet<>(imageCountString);
                //获取a-b的交集
                stringTime.removeAll(hashSet);
                for(int s=0;s<stringTime.size();s++){
                    for(int o=0;o<ivsName.size();o++){
                        ImageCountPo images = new ImageCountPo();
                        images.setRemark(ivsName.get(o).get("service_name").toString());
                        images.setPictureCount("0");
                        images.setTime(stringTime.get(s));
                        images.setOrganization(filed);
                        newImageCountPos.add(images);
                    }
                }

            }
        }
        //list集合去重
/*        for  ( int  i  =   0 ; i  <  newImageCountPos.size()  -   1 ; i ++ )  {
            for  ( int  j  =  newImageCountPos.size()  -   1 ; j  >=  i; j -- )  {
                if  ( newImageCountPos.get(j).getTime().equals(newImageCountPos.get(i).getTime())&&
                        newImageCountPos.get(j).getRemark().equals(newImageCountPos.get(i).getRemark())
                        &&newImageCountPos.get(j).getPictureCount().equals("0")
                        &&newImageCountPos.get(j).getOrganization().equals(newImageCountPos.get(i).getOrganization()))  {
                    newImageCountPos.remove(j);
                }
            }
        }*/

        for(int i=newImageCountPos.size()-1;i==0;i--)  {
            for (int j = newImageCountPos.size(); j ==0 ; j--) {
                if (i!=j && newImageCountPos.get(j).getTime().equals(newImageCountPos.get(i).getTime())&&
                        newImageCountPos.get(j).getRemark().equals(newImageCountPos.get(i).getRemark())
                        &&newImageCountPos.get(j).getPictureCount().equals("0")
                        &&newImageCountPos.get(j).getOrganization().equals(newImageCountPos.get(i).getOrganization())){
                    newImageCountPos.remove(j);
                    j--;
                }
            }
        }
        newImageCountPos.sort((ImageCountPo s1, ImageCountPo s2) -> s1.getTime().compareTo(s2.getTime()));
        if(imageCountPos!=null&&imageCountPos.size()>0){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(newImageCountPos);
            restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
       }else{
           restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
           restResult.setMessage(messageSourceUtil.getMessage("device.query"));
       }
       return restResult;
    }


    /*
    * 图片溯源按摄像机查询
    * */
    @Override
    public RestResult intelligentTraceByCamera(ImageCountVO imageCountVO) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> imageCountPoes = new ArrayList<>();
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        //开始时间
        String startTime = imageCountVO.getStartTime();
        //结束时间
        String endTime = imageCountVO.getEndTime();
        Map<String,Object> map = new HashMap<>();
        String[] netWorkVolist = imageCountVO.getNetWorkList();
        if(netWorkVolist == null || netWorkVolist.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.networkElement"));
            return restResult;
        }
        String[] sceneVoList = imageCountVO.getSceneVoList();
        if(sceneVoList == null || sceneVoList.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        String[] organizations = imageCountVO.getOrganization();
        if(organizations == null || organizations.length ==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.equipment"));
            return restResult;
        }
        //拿到给到的ip
        List<String> organization = Arrays.asList(organizations);
        List<Map>cameraSn = new ArrayList<>();
        List<Map>cameraSns = new ArrayList<>();
        //拿到给到的vcn vcm
        List<String> netWorkVolists = Arrays.asList(netWorkVolist);
        Map<String,Object> maps = new HashedMap();
        maps.put("serviceName",netWorkVolists);
        List<Map> ivsName = intelligentTraceMapper.getServiceName(maps);
        List<String> sceneVo = Arrays.asList(sceneVoList);
        if(StringUtils.isEmpty(imageCountVO.getStartTime())){
            restResult.setMessage(messageSourceUtil.getMessage("device.startTime"));
            return restResult;
        }
        if(StringUtils.isEmpty(imageCountVO.getEndTime())){
            restResult.setMessage(messageSourceUtil.getMessage("device.endTime"));
            return restResult;
        }
        if(StringUtils.isEmpty(imageCountVO.getTimeType())){
            restResult.setMessage(messageSourceUtil.getMessage("device.TimeDimension"));
            return restResult;
        }
        map.put("startTime",imageCountVO.getStartTime());
        map.put("endTime",imageCountVO.getEndTime());
        map.put("netWorks",netWorkVolists);
        map.put("scenes",sceneVo);
        map.put("tableName",Server_Param_Config);
        if(imageCountVO.getTimeType().equals("day")){
            //天数时间差
            List<String> stringTime =  getBetweenDates(startTime,endTime);
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            map.put("tabName",Image_Day_Statistics_VCN);
            for(int i=0;i<organization.size();i++) {
                Map<String, Object> organMap = new HashMap<>();
                String ip = organization.get(i);
                organMap.put("ip", ip);
                cameraSn = tblIpcIpMapper.getCameraSnById(organMap);
                if (cameraSn == null || cameraSn.size() <= 0) {
                    cameraSn.add(null);
                }
                cameraSns.addAll(cameraSn);
            }

            map.put("groupNames",cameraSns);
            //符合条件的所有sn
            imageCountPos = intelligentTraceMapper.getIntelligents(map);
            for(int x=0;x<imageCountPos.size();x++){
                String dateTime = imageCountPos.get(x).getTime().substring(0,10);
                imageCountPos.get(x).setTime(dateTime);
            }
            if(imageCountPos ==null || imageCountPos.size()<=0){
                Map<String,Object> listMap = new HashedMap();
                List<String> stringList = new ArrayList<>();
                listMap.put("list",stringList);
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(listMap);
                return restResult;
            }else {
                for(int x = 0; x < imageCountPos.size(); x++) {
                    //满足条件设备的时间
                    String time = imageCountPos.get(x).getTime();

                    for (int h = 0; h < stringTime.size(); h++) {
                        if (time.equals(stringTime.get(h))) {
                            for (int j = 0; j < ivsName.size(); j++) {
                                if (!ivsName.get(j).get("service_name").equals(imageCountPos.get(x).getRemark())) {
                                    ImageCountPo imageCountPo = new ImageCountPo();
                                    imageCountPo.setTime(time);
                                    imageCountPo.setPictureCount("0");
                                    imageCountPo.setOrganization(imageCountPos.get(x).getOrganization());
                                    imageCountPo.setRemark(ivsName.get(j).get("service_name").toString());
                                    newImageCountPos.add(imageCountPo);
                                } else {
                                    newImageCountPos.add(imageCountPos.get(x));
                                    break;
                                }
                            }
                        } else {
                            for (int i = 0; i < ivsName.size(); i++) {
                                ImageCountPo imageCountPo1 = new ImageCountPo();
                                imageCountPo1.setRemark(ivsName.get(i).get("service_name").toString());
                                imageCountPo1.setPictureCount("0");
                                imageCountPo1.setTime(stringTime.get(h));
                                imageCountPo1.setOrganization(imageCountPos.get(x).getOrganization());
                                newImageCountPos.add(imageCountPo1);
                            }
                        }
                    }
                }
                List<String> imageCountString = new ArrayList<>();
                for(int z=0;z<newImageCountPos.size();z++){
                    String timeImageCount = newImageCountPos.get(z).getTime();
                    imageCountString.add(timeImageCount);
                }
                LinkedHashSet<String> hashSet = new LinkedHashSet<>(imageCountString);
                //获取a-b的交集
                stringTime.removeAll(hashSet);
                for(int s=0;s<stringTime.size();s++){
                    for(int o=0;o<ivsName.size();o++){
                        ImageCountPo images = new ImageCountPo();
                        images.setRemark(ivsName.get(o).get("service_name").toString());
                        images.setPictureCount("0");
                        images.setTime(stringTime.get(s));
                        images.setOrganization(imageCountPos.get(o).getOrganization());
                        newImageCountPos.add(images);
                    }
                }

            }
        }
        if(imageCountVO.getTimeType().equals("hour")){
            //天数时间差
            List<String> stringTime =  getBetweenDateHours(startTime,endTime);
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));

            map.put("tabName",Image_Hour_Statistics_VCN);
            for(int i=0;i<organization.size();i++) {
                Map<String, Object> organMap = new HashMap<>();
                String ip = organization.get(i);
                organMap.put("ip", ip);
                cameraSn = tblIpcIpMapper.getCameraSnById(organMap);
                if (cameraSn == null || cameraSn.size() <= 0) {
                    cameraSn.add(null);
                }
                cameraSns.addAll(cameraSn);
            }
                map.put("groupNames",cameraSns);
                imageCountPos = intelligentTraceMapper.getIntelligentHours(map);
                for(int t=0;t<imageCountPos.size();t++){
                    String datetTime = imageCountPos.get(t).getTime().substring(0,13);
                    imageCountPos.get(t).setTime(datetTime);
                }
                if(imageCountPos ==null || imageCountPos.size()<=0){
                    Map<String,Object> listMap = new HashedMap();
                    List<String> stringList = new ArrayList<>();
                    listMap.put("list",stringList);
                    restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                    restResult.setData(listMap);
                    return restResult;
                }else {
                    for(int x = 0; x < imageCountPos.size(); x++) {
                        //满足条件设备的时间
                        String time = imageCountPos.get(x).getTime();
                        for (int h = 0; h < stringTime.size(); h++) {
                            if (time.equals(stringTime.get(h))) {
                                for (int j = 0; j < ivsName.size(); j++) {
                                    if (!ivsName.get(j).get("service_name").equals(imageCountPos.get(x).getRemark())) {
                                        ImageCountPo imageCountPo = new ImageCountPo();
                                        imageCountPo.setTime(time);
                                        imageCountPo.setPictureCount("0");
                                        imageCountPo.setOrganization(imageCountPos.get(x).getOrganization());
                                        imageCountPo.setRemark(ivsName.get(j).get("service_name").toString());
                                        newImageCountPos.add(imageCountPo);
                                    } else {
                                        newImageCountPos.add(imageCountPos.get(x));
                                        break;
                                    }
                                }
                            } else {
                                for (int i = 0; i < ivsName.size(); i++) {
                                    ImageCountPo imageCountPo1 = new ImageCountPo();
                                    imageCountPo1.setRemark(ivsName.get(i).get("service_name").toString());
                                    imageCountPo1.setPictureCount("0");
                                    imageCountPo1.setTime(stringTime.get(h));
                                    imageCountPo1.setOrganization(imageCountPos.get(x).getOrganization());
                                    newImageCountPos.add(imageCountPo1);
                                }
                            }
                        }
                    }
                    List<String> imageCountString = new ArrayList<>();
                    for(int z=0;z<newImageCountPos.size();z++){
                        String timeImageCount = newImageCountPos.get(z).getTime();
                        imageCountString.add(timeImageCount);
                    }
                    LinkedHashSet<String> hashSet = new LinkedHashSet<>(imageCountString);
                    //获取a-b的交集
                    stringTime.removeAll(hashSet);
                    for(int x=0;x<imageCountPos.size();x++) {
                        for (int s = 0; s < stringTime.size(); s++) {
                            for (int o = 0; o < ivsName.size(); o++) {
                                ImageCountPo images = new ImageCountPo();
                                images.setRemark(ivsName.get(o).get("service_name").toString());
                                images.setPictureCount("0");
                                images.setTime(stringTime.get(s));
                                images.setOrganization(imageCountPos.get(x).getOrganization());
                                newImageCountPos.add(images);
                            }
                        }
                    }
            }
        }
        newImageCountPos.sort((ImageCountPo s1, ImageCountPo s2) -> s1.getTime().compareTo(s2.getTime()));
        if(newImageCountPos!=null&&newImageCountPos.size()>0){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(newImageCountPos);
            restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
        }else{
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.query"));
        }
        return restResult;
    }

    /*
    * 图片溯源按组织查询分页
    * */
    @Override
    public RestResult intelligentTracePage(ImageCountVO imageCountVO){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        Integer currentPage = imageCountVO.getCurrentPage();
        if((Object) currentPage == null){
            currentPage = 0;
        }
        Integer pageSize = imageCountVO.getPageSize();
        if((Object) pageSize == null){
            pageSize = 0;
        }
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<String> cityLists = new ArrayList<>();
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        //开始时间
        String startTime = imageCountVO.getStartTime();
        //结束时间
        String endTime = imageCountVO.getEndTime();
        String[] netWorkVolist = imageCountVO.getNetWorkList();
        if(netWorkVolist == null || netWorkVolist.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.networkElement"));
            return restResult;
        }
        String[] sceneVoList = imageCountVO.getSceneVoList();
        if(sceneVoList == null || sceneVoList.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        String[] organizations = imageCountVO.getOrganization();
        if(organizations == null || organizations.length ==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.organization"));
            return restResult;
        }
        List<String> stringTime = new ArrayList<>();
        List<String> organization = Arrays.asList(organizations);
        List<String> netWorkVolists = Arrays.asList(netWorkVolist);
        //获取vcn名称
        Map<String,Object> maps = new HashedMap();
        maps.put("serviceName",netWorkVolists);
        List<Map> ivsName = intelligentTraceMapper.getServiceName(maps);
        List<String> ivsNameStr = new ArrayList<>();
        for(int i=0;i<ivsName.size();i++){
            ivsNameStr.add(ivsName.get(i).get("service_name").toString());
        }
        List<String> sceneVo = Arrays.asList(sceneVoList);
        if(StringUtils.isEmpty(imageCountVO.getStartTime())){
            restResult.setMessage(messageSourceUtil.getMessage("device.startTime"));
            return restResult;
        }
        if(StringUtils.isEmpty(imageCountVO.getEndTime())){
            restResult.setMessage(messageSourceUtil.getMessage("device.endTime"));
            return restResult;
        }
        if(StringUtils.isEmpty(imageCountVO.getTimeType())){
            restResult.setMessage(messageSourceUtil.getMessage("device.TimeDimension"));
            return restResult;
        }
        map.put("startTime",imageCountVO.getStartTime());
        map.put("endTime",imageCountVO.getEndTime());
        map.put("netWorks",netWorkVolists);
        map.put("scenes",sceneVo);
        map.put("tableName",Server_Param_Config);
        String filed = null;
        String organ = "";
        String organs = "";
        if(imageCountVO.getTimeType().equals("day")) {
            //天数时间差
            stringTime =  getBetweenDates(startTime,endTime);
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            map.put("tabName", Image_Day_Statistics_VCN);
            for (int y = 0; y < organization.size(); y++) {
                Map<String, Object> organMap = new HashMap<>();
                String id = organization.get(y);
                String fileds = null;
                if (id.length() == 3) {
                    organMap.put("id0", id);
                    fileds = "filed0";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 6) {
                    organMap.put("id1", id);
                    fileds = "filed1";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 9) {
                    organMap.put("id2", id);
                    fileds = "filed2";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 12) {
                    organMap.put("id3", id);
                    fileds = "filed3";
                    organMap.put("fileds", fileds);
                }
                List<Map> cameraSn = tblIpcIpMapper.getOrganization(organMap);
                if (cameraSn == null || cameraSn.size() <= 0) {
                    cameraSn.add(null);
                }
                //cameraSns.addAll(camerSn);
                map.put("groupNames",cameraSn);
                //符合条件的所有结果
                imageCountPos = intelligentTraceMapper.getIntelligent(map);
                if(imageCountPos ==null || imageCountPos.size()<=0){
                    Map<String,Object> listMap = new HashedMap();
                    List<String> stringList = new ArrayList<>();
                    listMap.put("total",0);
                    listMap.put("list",stringList);
                    restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                    restResult.setData(listMap);
                    return restResult;
                }
                filed = cameraSn.get(1).get("fileds").toString();
                cityLists.add(filed);
                for(int t=0;t<imageCountPos.size();t++){
                    String dateTime = imageCountPos.get(t).getTime().substring(0,10);
                    imageCountPos.get(t).setTime(dateTime);
                    imageCountPos.get(t).setOrganization(filed);
                    newImageCountPos.add(imageCountPos.get(t));
                }

            }
        }
        if(imageCountVO.getTimeType().equals("hour")){
            //小时时间差
            stringTime =  getBetweenDateHours(startTime,endTime);
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            map.put("tabName",Image_Hour_Statistics_VCN);
            //imageCountPos = intelligentTraceMapper.getIntelligentHour(map);
            for (int y = 0; y < organization.size(); y++) {
                Map<String, Object> organMap = new HashMap<>();
                String id = organization.get(y);
                String fileds = null;
                if (id.length() == 3) {
                    organMap.put("id0", id);
                    fileds = "filed0";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 6) {
                    organMap.put("id1", id);
                    fileds = "filed1";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 9) {
                    organMap.put("id2", id);
                    fileds = "filed2";
                    organMap.put("fileds", fileds);
                }
                if (id.length() == 12) {
                    organMap.put("id3", id);
                    fileds = "filed3";
                    organMap.put("fileds", fileds);
                }
                List<Map> cameraSn = tblIpcIpMapper.getOrganization(organMap);
                if (cameraSn == null || cameraSn.size() <= 0) {
                    cameraSn.add(null);
                }

                map.put("groupNames",cameraSn);
                imageCountPos = intelligentTraceMapper.getIntelligentHour(map);
                filed = cameraSn.get(1).get("fileds").toString();
                cityLists.add(filed);
                if(imageCountPos ==null || imageCountPos.size()<=0){
                    Map<String,Object> listMap = new HashedMap();
                    List<String> stringList = new ArrayList<>();
                    listMap.put("total",0);
                    listMap.put("list",stringList);
                    restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                    restResult.setData(listMap);
                    return restResult;
                }
                for(int t=0;t<imageCountPos.size();t++){
                    String dateTime = imageCountPos.get(t).getTime().substring(0,13);
                    imageCountPos.get(t).setTime(dateTime);
                    imageCountPos.get(t).setOrganization(filed);
                    newImageCountPos.add(imageCountPos.get(t));
                }

            }
        }
        newImageCountPos.sort((ImageCountPo s1, ImageCountPo s2) -> s1.getTime().compareTo(s2.getTime()));
        Date data = new Date();
        String tableName = "tmp"+data.getTime();
        intelligentTraceMapper.createTable(tableName);
        intelligentTraceMapper.insert(tableName,newImageCountPos);
        List<Map<String,Object>> list = intelligentTraceMapper.getIvsName(tableName,ivsNameStr,cityLists);
        List<String> date = new ArrayList<>();
        for(int d=0;d<list.size();d++){
            String dates = list.get(d).get("datetime").toString();
            date.add(dates);
        }
        stringTime.removeAll(date);
        for(int p=0;p<stringTime.size();p++){
            Map<String,Object> map1= new HashedMap();
            map1.put("datetime",stringTime.get(p));
            list.add(map1);
        }
        Collections.sort(list,new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                int ret = 0;
                //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                ret = o1.get("datetime").toString().compareTo(o2.get("datetime").toString());//逆序的话就用o2.compareTo(o1)即可
                return ret;
            }
        });
        List<Map<String,Object>> map1= pageList(list,pageSize,currentPage);
        Integer num = list.size();
        Map<String,Object>mapList = new HashedMap();
        mapList.put("total",num);
        mapList.put("list",map1);
        intelligentTraceMapper.deleteByTableName(tableName);
        if(imageCountPos!=null&&imageCountPos.size()>0){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(mapList);
            restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
        }else{
            Map<String,Object> listMap = new HashedMap();
            List<String> stringList = new ArrayList<>();
            listMap.put("total",0);
            listMap.put("list",stringList);
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(listMap);
        }
        return restResult;
    }


    /*
    * 图片溯源按摄像机查询分页
    * */
    @Override
    public RestResult intelligentTraceByCameraPage(ImageCountVO imageCountVO) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE, "", null);
        List<String> cityLists = new ArrayList<>();
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> imageCountPoes = new ArrayList<>();
        Integer currentPage = imageCountVO.getCurrentPage();
        if ((Object) currentPage == null) {
            currentPage = 0;
        }
        Integer pageSize = imageCountVO.getPageSize();
        if ((Object) pageSize == null) {
            pageSize = 0;
        }
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        //开始时间
        String startTime = imageCountVO.getStartTime();
        //结束时间
        String endTime = imageCountVO.getEndTime();
        Map<String, Object> map = new HashMap<>();
        String[] netWorkVolist = imageCountVO.getNetWorkList();
        if (netWorkVolist == null || netWorkVolist.length == 0) {
            restResult.setMessage(messageSourceUtil.getMessage("device.networkElement"));
            return restResult;
        }
        String[] sceneVoList = imageCountVO.getSceneVoList();
        if (sceneVoList == null || sceneVoList.length == 0) {
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        String[] organizations = imageCountVO.getOrganization();
        if (organizations == null || organizations.length == 0) {
            restResult.setMessage(messageSourceUtil.getMessage("device.equipment"));
            return restResult;
        }
        //拿到给到的ip
        List<String> organization = Arrays.asList(organizations);
        List<Map> cameraSn = new ArrayList<>();
        List<Map> cameraSns = new ArrayList<>();
        List<String> stringTime= new ArrayList<>();
        //拿到给到的vcn vcm
        List<String> netWorkVolists = Arrays.asList(netWorkVolist);
        Map<String, Object> maps = new HashedMap();
        maps.put("serviceName", netWorkVolists);
        List<Map> ivsName = intelligentTraceMapper.getServiceName(maps);
        List<String> ivsNameStr = new ArrayList<>();
        for (int i = 0; i < ivsName.size(); i++) {
            ivsNameStr.add(ivsName.get(i).get("service_name").toString());
        }
        List<String> sceneVo = Arrays.asList(sceneVoList);
        if (StringUtils.isEmpty(imageCountVO.getStartTime())) {
            restResult.setMessage(messageSourceUtil.getMessage("device.startTime"));
            return restResult;
        }
        if (StringUtils.isEmpty(imageCountVO.getEndTime())) {
            restResult.setMessage(messageSourceUtil.getMessage("device.endTime"));
            return restResult;
        }
        if (StringUtils.isEmpty(imageCountVO.getTimeType())) {
            restResult.setMessage(messageSourceUtil.getMessage("device.TimeDimension"));
            return restResult;
        }
        map.put("startTime", imageCountVO.getStartTime());
        map.put("endTime", imageCountVO.getEndTime());
        map.put("netWorks", netWorkVolists);
        map.put("scenes", sceneVo);
        map.put("tableName", Server_Param_Config);
        if (imageCountVO.getTimeType().equals("day")) {
            //天数时间差
            stringTime = getBetweenDates(startTime, endTime);
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            map.put("tabName", Image_Day_Statistics_VCN);
            for (int i = 0; i < organization.size(); i++) {
                Map<String, Object> organMap = new HashMap<>();
                String ip = organization.get(i);
                organMap.put("ip", ip);
                cameraSn = tblIpcIpMapper.getCameraSnById(organMap);
                if (cameraSn == null || cameraSn.size() <= 0) {
                    cameraSn.add(null);
                }
                cameraSns.addAll(cameraSn);
            }

            map.put("groupNames", cameraSns);
            //符合条件的所有sn
            imageCountPos = intelligentTraceMapper.getIntelligents(map);
            if (imageCountPos == null || imageCountPos.size() <= 0) {
                Map<String, Object> listMap = new HashedMap();
                List<String> stringList = new ArrayList<>();
                listMap.put("total", 0);
                listMap.put("list", stringList);
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(listMap);
                return restResult;
            }
            for(int t=0;t<imageCountPos.size();t++){
                String dateTime = imageCountPos.get(t).getTime().substring(0,10);
                imageCountPos.get(t).setTime(dateTime);
                newImageCountPos.add(imageCountPos.get(t));
            }


            /*if (imageCountPos == null || imageCountPos.size() <= 0) {
                Map<String, Object> listMap = new HashedMap();
                List<String> stringList = new ArrayList<>();
                listMap.put("total", 0);
                listMap.put("list", stringList);
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(listMap);
                return restResult;
            } else {
                for (int x = 0; x < imageCountPos.size(); x++) {
                    //满足条件设备的时间
                    String time = imageCountPos.get(x).getTime();
                    for (int h = 0; h < stringTime.size(); h++) {
                        if (time.equals(stringTime.get(h))) {
                            for (int j = 0; j < ivsName.size(); j++) {
                                if (!ivsName.get(j).get("service_name").equals(imageCountPos.get(x).getRemark())) {
                                    ImageCountPo imageCountPo = new ImageCountPo();
                                    imageCountPo.setTime(time);
                                    imageCountPo.setPictureCount("0");
                                    imageCountPo.setOrganization(imageCountPos.get(x).getOrganization());
                                    imageCountPo.setRemark(ivsName.get(j).get("service_name").toString());
                                    newImageCountPos.add(imageCountPo);
                                } else {
                                    newImageCountPos.add(imageCountPos.get(x));
                                }
                            }
                        } else {
                            for (int i = 0; i < ivsName.size(); i++) {
                                ImageCountPo imageCountPo1 = new ImageCountPo();
                                imageCountPo1.setRemark(ivsName.get(i).get("service_name").toString());
                                imageCountPo1.setPictureCount("0");
                                imageCountPo1.setTime(stringTime.get(h));
                                imageCountPo1.setOrganization(imageCountPos.get(x).getOrganization());
                                newImageCountPos.add(imageCountPo1);
                            }
                        }
                    }
                }
                List<String> imageCountString = new ArrayList<>();
                for (int z = 0; z < newImageCountPos.size(); z++) {
                    String timeImageCount = newImageCountPos.get(z).getTime();
                    imageCountString.add(timeImageCount);
                }
                LinkedHashSet<String> hashSet = new LinkedHashSet<>(imageCountString);
                //获取a-b的交集
                stringTime.removeAll(hashSet);
                for (int x = 0; x < imageCountPos.size(); x++) {
                    for (int s = 0; s < stringTime.size(); s++) {
                        for (int o = 0; o < ivsName.size(); o++) {
                            ImageCountPo images = new ImageCountPo();
                            images.setRemark(ivsName.get(o).get("service_name").toString());
                            images.setPictureCount("0");
                            images.setTime(stringTime.get(s));
                            images.setOrganization(imageCountPos.get(x).getOrganization());
                            newImageCountPos.add(images);
                        }
                    }
                }
            }*/
        }
        if (imageCountVO.getTimeType().equals("hour")) {
            //天数时间差
            stringTime = getBetweenDateHours(startTime, endTime);
            stringTime.sort((String s1, String s2) -> s1.compareTo(s2));
            map.put("tabName", Image_Hour_Statistics_VCN);
            for (int i = 0; i < organization.size(); i++) {
                Map<String, Object> organMap = new HashMap<>();
                String ip = organization.get(i);
                organMap.put("ip", ip);
                cameraSn = tblIpcIpMapper.getCameraSnById(organMap);
                if (cameraSn == null || cameraSn.size() <= 0) {
                    cameraSn.add(null);
                }
                cameraSns.addAll(cameraSn);
            }
            map.put("groupNames", cameraSns);
            imageCountPos = intelligentTraceMapper.getIntelligentHour(map);
            if (imageCountPos == null || imageCountPos.size() <= 0) {
                Map<String, Object> listMap = new HashedMap();
                List<String> stringList = new ArrayList<>();
                listMap.put("total", 0);
                listMap.put("list", stringList);
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setData(listMap);
                return restResult;
            }
            for(int t=0;t<imageCountPos.size();t++){
                String dateTime = imageCountPos.get(t).getTime().substring(0,13);
                imageCountPos.get(t).setTime(dateTime);
                newImageCountPos.add(imageCountPos.get(t));
            }

           /* else {
                for (int x = 0; x < imageCountPos.size(); x++) {
                    //满足条件设备的时间
                    String time = imageCountPos.get(x).getTime();
                    if (stringTime.contains(time)) {
                        for (int j = 0; j < ivsName.size(); j++) {
                            if (!ivsName.get(j).get("service_name").equals(imageCountPos.get(x).getRemark())) {
                                ImageCountPo imageCountPo = new ImageCountPo();
                                imageCountPo.setTime(time);
                                imageCountPo.setPictureCount("0");
                                imageCountPo.setOrganization(imageCountPos.get(x).getOrganization());
                                imageCountPo.setRemark(ivsName.get(j).get("service_name").toString());
                                newImageCountPos.add(imageCountPo);
                            } else {
                                newImageCountPos.add(imageCountPos.get(x));
                            }
                        }
                    } else {
                        for (int i = 0; i < ivsName.size(); i++) {
                            ImageCountPo imageCountPo1 = new ImageCountPo();
                            imageCountPo1.setRemark(ivsName.get(i).get("service_name").toString());
                            imageCountPo1.setPictureCount("0");
                            imageCountPo1.setTime(time);
                            imageCountPo1.setOrganization(imageCountPos.get(x).getOrganization());
                            newImageCountPos.add(imageCountPo1);
                        }
                    }
                }
                List<String> imageCountString = new ArrayList<>();
                for (int z = 0; z < newImageCountPos.size(); z++) {
                    String timeImageCount = newImageCountPos.get(z).getTime();
                    imageCountString.add(timeImageCount);
                }
                LinkedHashSet<String> hashSet = new LinkedHashSet<>(imageCountString);
                //获取a-b的交集
                stringTime.removeAll(hashSet);
                for (int x = 0; x < imageCountPos.size(); x++) {
                    for (int s = 0; s < stringTime.size(); s++) {
                        for (int o = 0; o < ivsName.size(); o++) {
                            ImageCountPo images = new ImageCountPo();
                            images.setRemark(ivsName.get(o).get("service_name").toString());
                            images.setPictureCount("0");
                            images.setTime(stringTime.get(s));
                            images.setOrganization(imageCountPos.get(x).getOrganization());
                            newImageCountPos.add(images);
                        }
                    }
                }
            }*/
        }
        //list集合去重
   /*     for (int i = newImageCountPos.size() - 1; i == 0; i--) {
            for (int j = newImageCountPos.size(); j == 0; j--) {
                if (i != j && newImageCountPos.get(j).getTime().equals(newImageCountPos.get(i).getTime()) &&
                        newImageCountPos.get(j).getRemark().equals(newImageCountPos.get(i).getRemark())
                        && newImageCountPos.get(j).getPictureCount().equals("0")) {
                    newImageCountPos.remove(j);
                    j--;
                }
            }
        }*/
        /*
        for  ( int  i  =   0 ; i  <  newImageCountPos.size()  -   1 ; i ++ )  {
            for  ( int  j  =  newImageCountPos.size()  -   1 ; j  >=  i; j -- )  {
                if  ( newImageCountPos.get(j).getTime().equals(newImageCountPos.get(i).getTime())&&
                        newImageCountPos.get(j).getRemark().equals(newImageCountPos.get(i).getRemark())
                        &&newImageCountPos.get(j).getPictureCount().equals("0")
                        &&newImageCountPos.get(j).getOrganization().equals(newImageCountPos.get(i).getOrganization()))  {
                    newImageCountPos.remove(j);
                }
            }
        }*/
        newImageCountPos.sort((ImageCountPo s1, ImageCountPo s2) -> s1.getTime().compareTo(s2.getTime()));
        Date data = new Date();
        String tableName = "tmp" + data.getTime();
        intelligentTraceMapper.createTable(tableName);
        intelligentTraceMapper.insert(tableName, newImageCountPos);
        String str = null;
        List<String> string = new ArrayList<>();
        for (int i = 0; i < organization.size(); i++) {
            if (organization.get(i).contains(".")) {
                str = organization.get(i).replace(".", "*");
                string.add(str);
            }
        }
        List<Map<String, Object>> list = intelligentTraceMapper.getIvsNames(tableName, ivsNameStr, string);
        List<Map<String, Object>> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> mapList = list.get(i);
            Map<String, Object> keyMap = new HashedMap();
            Iterator<Map.Entry<String, Object>> iterator = mapList.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String key = entry.getKey();
                String value = entry.getValue().toString();
                if (!key.equals("dateTime")) {
                    String newKey = key.replaceAll("\\*", "\\.");
                    keyMap.put(newKey, value);
                } else {
                    keyMap.put("dateTime", value);
                }
            }
            lists.add(keyMap);
        }
        List<String> date = new ArrayList<>();
        for(int d=0;d<lists.size();d++){
            String dates = lists.get(d).get("datetime").toString();
            date.add(dates);
        }
        stringTime.removeAll(date);
        for(int p=0;p<stringTime.size();p++){
            Map<String,Object> map1= new HashedMap();
            map1.put("datetime",stringTime.get(p));
            lists.add(map1);
        }
        Collections.sort(lists,new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                int ret = 0;
                //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                ret = o1.get("datetime").toString().compareTo(o2.get("datetime").toString());//逆序的话就用o2.compareTo(o1)即可
                return ret;
            }
        });
        List<Map<String,Object>> map1= pageList(lists,pageSize,currentPage);
        Integer num = lists.size();
        Map<String,Object>mapList = new HashedMap();
        mapList.put("total",num);
        mapList.put("list",map1);
        intelligentTraceMapper.deleteByTableName(tableName);
        if(imageCountPos!=null&&imageCountPos.size()>0){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(mapList);
            restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
        }else{
            Map<String,Object> listMap = new HashedMap();
            List<String> stringList = new ArrayList<>();
            listMap.put("total",0);
            listMap.put("list",stringList);
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(listMap);
        }
        return restResult;
    }


    /*
    * 分页方法
    * */
    public static List<ImageCountPo> page(List<ImageCountPo> dataList, int pageSize, int currentPage) {
        List<ImageCountPo> currentPageList = new ArrayList<>();
        if (dataList != null && dataList.size() > 0) {
            int currIdx = (currentPage > 1 ? (currentPage - 1) * pageSize : 0);
            for (int i = 0; i < pageSize && i < dataList.size() - currIdx; i++) {
                ImageCountPo data = dataList.get(currIdx + i);
                currentPageList.add(data);
            }
        }
        return currentPageList;
    }


    public List<Map<String, Object>> pageList(List<Map<String, Object>> list,int pageIndex,int pageSize){
        int startIndex = (pageIndex - 1) * pageSize;
        int lastIndex = pageIndex * pageSize;
        int count = list.size();
        if (lastIndex >= count) {
            lastIndex = count;
        }
        return list.subList(startIndex, lastIndex);
    }


    /*
    * 获取两个时间之间的日期
    * */
    private List<String> getBetweenDates(String start, String end) {
        List<String> result = new ArrayList<String>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start_date = sdf.parse(start);
            Date end_date = sdf.parse(end);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start_date);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end_date);
            while (tempStart.before(tempEnd)||tempStart.equals(tempEnd)) {
                result.add(sdf.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) {

            e.printStackTrace();
        }
        Collections.reverse(result);
        return result;
    }
    private List<String> getBet(String start, String end) {
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(start));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), min.get(Calendar.DATE),0,0,0);
        try {
            max.setTime(sdf.parse(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), max.get(Calendar.DATE),23,59,59);
        Calendar curr = min;
        while (curr.before(max)) {
            list.add(sdf.format(curr.getTime()));
            curr.add(Calendar.DATE, 1);
        }
        return list;
    }

    /*
     * 获取一天之间的所有小时
     * */
    private List<String> getBetweenDateHours(String start, String end) {
        List<String> result = new ArrayList<String>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            Date start_date = sdf.parse(start);
            Date end_date = sdf.parse(end);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start_date);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end_date);
            while (tempStart.before(tempEnd)||tempStart.equals(tempEnd)) {
                result.add(sdf.format(tempStart.getTime()));
                tempStart.add(Calendar.HOUR_OF_DAY, 1);
            }
        } catch (ParseException e) {

            e.printStackTrace();
        }
        Collections.reverse(result);
        return result;
    }




    @Override
    public RestResult gitintelligentTraceList() {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,messageSourceUtil.getMessage("device.configurationNotFound"),null);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put(TableCommonConstant.SQL_TABLENAM,TableCommonConstant.TBL_SERVER_PARAM_CONFIG);
        String intelligenttrace = "1,2";
        if(StringUtils.isEmpty(intelligenttrace)){
            return restResult;
        }
        String[] intelligenttraceList = intelligenttrace.split(",");
        map.put("intelligenttraceList",intelligenttraceList);
        DataSourceEnum videoinsightCollecter = DataSourceEnum.VideoinsightCollecter;
        DataSourceEnum videoinsight = DataSourceEnum.VideoInsight;
        List<Map<String,String>> list =intelligentTraceMapper.gitintelligentTraceList(map);
        if(list!=null&&list.size()>0){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(list);
            restResult.setMessage("");
        }else{
            restResult.setMessage(messageSourceUtil.getMessage("device.result"));
        }
        return restResult;
    }

    @Override
    public RestResult getNetWorkTraces(ImageCountVO imageCountVO){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        List<String> listStr = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        Integer currentPage = imageCountVO.getCurrentPage();
        if((Object) currentPage == null){
            currentPage = 0;
        }
        Integer pageSize = imageCountVO.getPageSize();
        if((Object) pageSize == null){
            pageSize = 0;
        }
        String[] sceneVoList = imageCountVO.getSceneVoList();
        if(sceneVoList == null || sceneVoList.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        List<String> sceneVo = Arrays.asList(sceneVoList);
        String[] netWorkVolist = imageCountVO.getNetWorkList();
        if(netWorkVolist == null || netWorkVolist.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.networkElement"));
            return restResult;
        }
        map.put("netWorkVolist",netWorkVolist);
        List<Map> ipList = intelligentTraceMapper.getServerInfo(map);
        if(ipList==null || ipList.size()<=0 || ipList.isEmpty()){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        List<String> ipLists = new ArrayList<>();
        if(ipList.size()>0 && ipList!=null) {
            for (int i = 0; i < ipList.size(); i++) {
                String ipAddress = ipList.get(i).get("SERVICE_IP_ADDRESS").toString();
                ipLists.add(ipAddress);
            }
        }
        List<String> netWorkVolists = Arrays.asList(netWorkVolist);
        map.put("startTime",imageCountVO.getStartTime());
        map.put("endTime",imageCountVO.getEndTime());
        map.put("netWorks",netWorkVolists);
        map.put("scenes",sceneVo);
        map.put("tableName",Server_Param_Config);
        String relationShip = tblIpcIpMapper.getRelationShip();
        map.put("relationShip",relationShip);
        if(imageCountVO.getTimeType().equals("hour")) {
            map.put("ipList", ipList);
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime == null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            Map<String, Object> map1 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i).substring(0, 19);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }
            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi==null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_HOUR_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCountHour(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultList.size(); i++ ) {
                Map<String, Object> mapLists = resultList.get(i);
                String time = null;
                Map<String,Object> objectMap = new HashMap<>();
                objectMap.putAll(mapLists);
                Set<String> keySet = mapLists.keySet();
                for(String key :keySet){
                    Map<String, Object> newMap = new HashMap<>();
                    String keys = key;
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCM");
                        Integer count = Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCM"+keyVal[1], "VCM(" + ipLists.get(count) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time",mapLists.get("PERIOD_START_TIME"));
                    }
                    if (keys.contains("_VCN")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCN");
                        Integer count = Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCN"+keyVal[1], "VCN(" + ipLists.get(count) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time",mapLists.get("PERIOD_START_TIME"));
                    }
                }
                resultListNew.add(objectMap);
            }
            List<Map<String,Object>> ma = new ArrayList<>();
            if(currentPage>0||pageSize>0){
                ma= pageList(resultListNew,pageSize,currentPage);
            }else{
                ma = resultListNew;
            }
            Collections.sort(ma,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });
            Integer num = resultListNew.size();
            Map<String,Object>maps= new HashedMap();
            maps.put("total",num);
            maps.put("list",ma);
            restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(maps);
        }
        if(imageCountVO.getTimeType().equals("day")) {
            map.put("ipList", ipList);
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime==null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            Map<String, Object> map1 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i).substring(0, 19);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }
            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_DAY_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCountWD(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultList.size(); i++ ) {
                Map<String, Object> mapLists = resultList.get(i);
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.putAll(mapLists);
                Set<String> keySet = mapLists.keySet();
                for(String key :keySet){
                    Map<String, Object> newMap = new HashMap<>();
                    String keys = key;
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCM");
                        Integer count = Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCM"+keyVal[1], "VCM(" + ipLists.get(count) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time",mapLists.get("PERIOD_START_TIME"));
                    }
                    if (keys.contains("_VCN")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCN");
                        Integer count = Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCN"+keyVal[1], "VCN(" + ipLists.get(count) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time",mapLists.get("PERIOD_START_TIME"));
                    }
                }
                resultListNew.add(objectMap);
            }
            List<Map<String,Object>> ma = new ArrayList<>();
            if(currentPage>0||pageSize>0){
                ma= pageList(resultListNew,pageSize,currentPage);
            }else{
                ma = resultListNew;
            }
            Collections.sort(ma,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });
            Integer num = resultListNew.size();
            Map<String,Object>maps= new HashedMap();
            maps.put("total",num);
            maps.put("list",ma);
            restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(maps);
        }
        return restResult;

    }


    @Override
    public RestResult getNetWorkTraceByHours(ImageCountVO imageCountVO){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        List<String> listStr = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        Integer currentPage = imageCountVO.getCurrentPage();
        if((Object) currentPage == null){
            currentPage = 0;
        }
        Integer pageSize = imageCountVO.getPageSize();
        if((Object) pageSize == null){
            pageSize = 0;
        }
        String[] sceneVoList = imageCountVO.getSceneVoList();
        if(sceneVoList == null || sceneVoList.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        List<String> sceneVo = Arrays.asList(sceneVoList);
        List<String> str = new ArrayList<>();
        //开始时间
        String startTime = imageCountVO.getStartTime();
        //结束时间
        String endTime = imageCountVO.getEndTime();
        String[] netWorkVolist = imageCountVO.getNetWorkList();
        if(netWorkVolist == null || netWorkVolist.length ==0 ){
            restResult.setMessage(messageSourceUtil.getMessage("device.networkElement"));
            return restResult;
        }
        map.put("netWorkVolist",netWorkVolist);
        List<Map> ipList = intelligentTraceMapper.getServerInfo(map);
        if(ipList==null || ipList.size()<=0 || ipList.isEmpty()){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        List<String> ipLists = new ArrayList<>();
        for (int i = 0; i < ipList.size(); i++) {
            String ipAddress = ipList.get(i).get("SERVICE_IP_ADDRESS").toString();
            ipLists.add(ipAddress);
        }
        List<String> netWorkVolists = Arrays.asList(netWorkVolist);
        map.put("startTime",imageCountVO.getStartTime());
        map.put("endTime",imageCountVO.getEndTime());
        map.put("netWorks",netWorkVolists);
        map.put("scenes",sceneVo);
        map.put("tableName",Server_Param_Config);
        String relationShip = tblIpcIpMapper.getRelationShip();
        if(StringUtils.isEmpty(relationShip)){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
            return restResult;
        }
        map.put("relationShip",relationShip);
        if(imageCountVO.getTimeType().equals("hour")) {
            map.put("ipList", ipList);
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime==null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            Map<String, Object> map1 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i).substring(0, 19);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }
            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_HOUR_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCountHour(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListCompare = new ArrayList<>();
            for(int i=0;i<=resultList.size()-1;i++){
                if(i == 0 ){
                    resultListCompare.add(resultList.get(i));
                }else {
                    Map<String,Object> compareMap = new HashMap<>();
                    compareMap.putAll(resultList.get(i));
                    for(int a=0;a<=netWorkVolists.size()-1;a++){
                        String column = ("_VCM0").toUpperCase();
                        compareMap.put(column,Integer.parseInt(resultList.get(i).get((column)).toString())-Integer.parseInt(resultList.get(i-1).get(column).toString()));
                    }
                    resultListCompare.add(compareMap);
                }
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultListCompare.size(); i++ ) {
                Map<String, Object> mapLists = resultListCompare.get(i);
                String time = null;
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.putAll(mapLists);
                Set<String> keySet = mapLists.keySet();
                for(String key :keySet){
                    Map<String, Object> newMap = new HashMap<>();
                    String keys = key;
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCM");
                        Integer count = Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCM"+keyVal[1], "VCM(" + ipLists.get(count) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time",mapLists.get("PERIOD_START_TIME"));
                    }
                    if (keys.contains("_VCN")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_VCN");
                        Integer count = Integer.parseInt(keyVal[1]);
                        newKey = keys.replaceAll("_VCN"+keyVal[1], "VCN(" + ipLists.get(count) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time",mapLists.get("PERIOD_START_TIME"));
                    }
                }
                resultListNew.add(objectMap);
            }
            List<Map<String,Object>> ma = new ArrayList<>();
            if(currentPage>0||pageSize>0){
                ma= pageList(resultListNew,pageSize,currentPage);
            }else{
                ma = resultListNew;
            }
            Collections.sort(ma,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });
            Integer num = resultListNew.size();
            Map<String,Object>maps= new HashedMap();
            maps.put("total",num);
            maps.put("list",ma);
            restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(maps);
        }
        if(imageCountVO.getTimeType().equals("day")) {
            map.put("ipList", ipList);
            map.put("ivsNameStr", ipLists);
            List<String> stringTime = tblIpcIpMapper.getPeriodTime(map);
            if(stringTime==null || stringTime.size()<=0 || stringTime.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            Map<String, Object> map1 = new HashMap<>();
            map1.put("key1", stringTime);
            List<Map<String, String>> mapList = new ArrayList<>();
            for (int i = 0; i < stringTime.size(); i++) {
                Map<String, String> map4 = new HashMap<>();
                String oldString = stringTime.get(i).substring(0, 19);
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                map4.put("key1", oldString);
                map4.put("key2", newString);
                mapList.add(map4);
            }
            map.put("periodList", mapList);
            List<String> stringTi = tblIpcIpMapper.getTimes(map);
            if(stringTi == null || stringTi.size()<=0 || stringTi.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warning"));
                return restResult;
            }
            List<String> strs = new ArrayList<>();
            String newDate = null;
            String newData = null;
            for (int i = 0; i < stringTi.size(); i++) {
                newData = stringTi.get(i).replaceAll("-", "");
                newDate = "TBL_IMAGE_DAY_STATISTICS_" + newData;
                strs.add(newDate);
            }
            map.put("dayList", strs);
            List<String> cameraManagerList = tblIpcIpMapper.qryCameraManagerOriList(map);
            if(cameraManagerList==null || cameraManagerList.size()<=0 || cameraManagerList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String, String>> cameraList = new ArrayList<>();
            for (int i = 0; i < cameraManagerList.size(); i++) {
                Map<String, String> mapCamera = new HashMap<>();
                String oldString = cameraManagerList.get(i).substring(28, cameraManagerList.get(i).length());
                StringBuilder sb = new StringBuilder(oldString);
                sb.insert(4,"-");
                sb.insert(7,"-");
                sb.insert(10," ");
                sb.insert(13,":");
                sb.insert(16,":");
                oldString = sb.toString();
                String newString = oldString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                mapCamera.put("key1", oldString);
                mapCamera.put("key2", newString);
                cameraList.add(mapCamera);
            }
            map.put("cameraList", cameraList);
            List<Map<String,Object>> resultList = tblIpcIpMapper.getPicturesCountWD(map);
            if(resultList==null || resultList.size()<=0 || resultList.isEmpty()){
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                restResult.setMessage(messageSourceUtil.getMessage("device.warnings"));
                return restResult;
            }
            List<Map<String,Object>> resultListNew = new ArrayList<>();
            for( int i = 0; i < resultList.size(); i++ ) {
                Map<String, Object> mapLists = resultList.get(i);
                String time = null;
                Map<String,Object> objectMap = new HashMap<>();
                objectMap.putAll(mapLists);
                Set<String> keySet = mapLists.keySet();
                for(String key :keySet){
                    Map<String, Object> newMap = new HashMap<>();
                    String keys = key;
                    String[] keyVal = null;
                    String newKey = null;
                    if (keys.contains("_VCM0")) {
                        String value = mapLists.get(keys).toString();
                        keyVal = keys.split("_");
                        newKey = keys.replaceAll("_VCM0", "VCM(" + ipLists.get(0) + ")");
                        objectMap.put(newKey,value);
                        objectMap.put("time",mapLists.get("PERIOD_START_TIME"));
                    }
                }
                resultListNew.add(objectMap);
            }
            List<Map<String,Object>> ma = new ArrayList<>();
            if(currentPage>0||pageSize>0){
                ma= pageList(resultListNew,pageSize,currentPage);
            }else{
                ma = resultListNew;
            }
            Collections.sort(ma,new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o1.get("time").toString().compareTo(o2.get("time").toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });
            Integer num = resultListNew.size();
            Map<String,Object>maps= new HashedMap();
            maps.put("total",num);
            maps.put("list",ma);
            restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(maps);
        }
        return restResult;

    }

    @Override
    public RestResult getNetWorkTrace(ImageCountVO imageCountVO) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        Map<String,Object> map = new HashedMap();
        Integer currentPage = imageCountVO.getCurrentPage();
        if((Object) currentPage == null){
           currentPage = 0;
        }
        Integer pageSize = imageCountVO.getPageSize();
        if((Object) pageSize == null){
            pageSize = 0;
        }
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> imageCountPosList = new ArrayList<>();
        //元素维度
        String[]netWorkVoList = imageCountVO.getNetWorkList();
        if(netWorkVoList == null || netWorkVoList.length==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.elementDimension"));
            return restResult;
        }
        //指示场景
        String[]sceneVolist = imageCountVO.getSceneVoList();
        if(sceneVolist == null || sceneVolist.length==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        //日期：开始时间
        String startTime = imageCountVO.getStartTime();
        if(StringUtils.isEmpty(startTime)){
            restResult.setMessage(messageSourceUtil.getMessage("device.startTime"));
            return restResult;
        }
        //日期：结束时间
        String endTime = imageCountVO.getEndTime();
        if(StringUtils.isEmpty(endTime)){
            restResult.setMessage(messageSourceUtil.getMessage("device.endTime"));
            return restResult;
        }
        List<String> sceneVoLists = Arrays.asList(sceneVolist);
        List<String> netWorkVoLists = Arrays.asList(netWorkVoList);
        Map<String,Object> maps = new HashedMap();
        map.put("scenes",sceneVoLists);
        map.put("netWorks",netWorkVoLists);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        maps.put("netWorks",netWorkVoLists);
        //获取两个时间之间的值
        List<String> timeList = getBetweenDates(startTime,endTime);
        timeList.sort((String s1,String s2)->s1.compareTo(s2));
        //获取网元维度字符串数组
        List<Map> ivsName = intelligentTraceMapper.getServiceNames(maps);
        List<String> ivsNameStr = new ArrayList<>();
        for(int i=0;i<ivsName.size();i++){
            ivsNameStr.add(ivsName.get(i).get("service_name").toString());
        }
        imageCountPos = intelligentTraceMapper.getNetWorkTraceByDay(map);
        if(imageCountPos ==null || imageCountPos.size()<=0){
            Map<String,Object> listMap = new HashedMap();
            List<String> stringList = new ArrayList<>();
            listMap.put("total",0);
            listMap.put("list",stringList);
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(listMap);
            return restResult;
        }
        for(int t=0;t<imageCountPos.size();t++){
            String dateTime = imageCountPos.get(t).getTime().substring(0,10);
            imageCountPos.get(t).setTime(dateTime);
        }
        for(int x = 0; x < imageCountPos.size(); x++){
            //满足条件设备的时间
            String time = imageCountPos.get(x).getTime();
            if(timeList.contains(time)){
                for(int j=0;j<ivsName.size();j++){
                    if(!ivsName.get(j).get("service_name").equals(imageCountPos.get(x).getRemark())){
                        ImageCountPo imageCountPo = new ImageCountPo();
                        imageCountPo.setTime(time);
                        imageCountPo.setPictureCount("0");
                        imageCountPo.setRemark(ivsName.get(j).get("service_name").toString());
                        newImageCountPos.add(imageCountPo);
                    }else {
                        newImageCountPos.add(imageCountPos.get(x));
                    }
                }
            }else {
                for(int i=0;i<ivsName.size();i++) {
                    ImageCountPo imageCountPo1 = new ImageCountPo();
                    imageCountPo1.setRemark(ivsName.get(i).get("service_name").toString());
                    imageCountPo1.setPictureCount("0");
                    imageCountPo1.setTime(time);
                    newImageCountPos.add(imageCountPo1);
                }
            }
        }
        List<String> imageCountString= new ArrayList<>();
        for(int z=0;z<newImageCountPos.size();z++){
            String timeImageCount = newImageCountPos.get(z).getTime();
            imageCountString.add(timeImageCount);
        }
        LinkedHashSet<String> hashSets = new LinkedHashSet<>(imageCountString);
        //获取a-b的交集
        timeList.removeAll(hashSets);
        for(int s=0;s<timeList.size();s++){
            for(int o=0;o<ivsName.size();o++){
                ImageCountPo images = new ImageCountPo();
                images.setRemark(ivsName.get(o).get("service_name").toString());
                images.setPictureCount("0");
                images.setTime(timeList.get(s));
                newImageCountPos.add(images);
            }
        }
        //list集合去重
        for(int i=0;i<newImageCountPos.size()-1;i++)  {
            for (int j = 0; j < newImageCountPos.size(); j++) {
                if (i!=j && newImageCountPos.get(j).getTime().equals(newImageCountPos.get(i).getTime())&&
                        newImageCountPos.get(j).getRemark().equals(newImageCountPos.get(i).getRemark())
                        &&newImageCountPos.get(j).getPictureCount().equals("0")){
                    newImageCountPos.remove(j);
                    j--;
                }
            }
        }
        newImageCountPos.sort((ImageCountPo s1, ImageCountPo s2) -> s1.getTime().compareTo(s2.getTime()));
        Date data = new Date();
        String tableName = "tmp"+data.getTime();
        intelligentTraceMapper.createTable(tableName);
        intelligentTraceMapper.insert(tableName,newImageCountPos);
        List<String> cityLists = new ArrayList<>();
        cityLists.add("");
        List<Map<String,Object>> list = intelligentTraceMapper.getIvs(tableName,ivsNameStr,cityLists);


        List<Map<String,Object>> map1 = new ArrayList<>();
        if(currentPage>0||pageSize>0){
            map1= pageList(list,pageSize,currentPage);
        }else{
            map1 = list;
        }
        Collections.sort(map1,new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                int ret = 0;
                //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                ret = o1.get("datetime").toString().compareTo(o2.get("datetime").toString());//逆序的话就用o2.compareTo(o1)即可
                return ret;
            }
        });
        Integer num = list.size();
        Map<String,Object>mapList = new HashedMap();
        mapList.put("total",num);
        mapList.put("list",map1);
        intelligentTraceMapper.deleteByTableName(tableName);
        restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setData(mapList);
        return restResult;
    }

    @Override
    public RestResult getNetWorkTraceByHour(ImageCountVO imageCountVO) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        Integer currentPage = imageCountVO.getCurrentPage();
        if((Object) currentPage == null){
            currentPage = 0;
        }
        Integer pageSize = imageCountVO.getPageSize();
        if((Object) pageSize == null){
            pageSize = 0;
        }
        Map<String,Object> map = new HashedMap();
        List<ImageCountPo> imageCountPos = new ArrayList<>();
        List<ImageCountPo> imageCountPosList = new ArrayList<>();
        List<ImageCountPo> newImageCountPos = new ArrayList<>();
        //元素维度
        String[]netWorkVoList = imageCountVO.getNetWorkList();
        if(netWorkVoList == null || netWorkVoList.length==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.elementDimension"));
            return restResult;
        }
        //指示场景
        String[]sceneVolist = imageCountVO.getSceneVoList();
        if(sceneVolist == null || sceneVolist.length==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.indicatorScenario"));
            return restResult;
        }
        //日期：开始时间
        String startTime = imageCountVO.getStartTime();
        if(StringUtils.isEmpty(startTime)){
            restResult.setMessage(messageSourceUtil.getMessage("device.startTime"));
            return restResult;
        }
        //日期：结束时间
        String endTime = imageCountVO.getEndTime();
        if(StringUtils.isEmpty(endTime)){
            restResult.setMessage(messageSourceUtil.getMessage("device.endTime"));
            return restResult;
        }

        List<String> sceneVoLists = Arrays.asList(sceneVolist);
        List<String> netWorkVoLists = Arrays.asList(netWorkVoList);
        //获取两个时间的差值
        List<String> timeList = getBetweenDateHours(startTime,endTime);
        timeList.sort((String s1,String s2)->s1.compareTo(s2));
        Map<String,Object> maps = new HashedMap();
        maps.put("netWorks",netWorkVoLists);
        map.put("scenes",sceneVoLists);
        map.put("netWorks",netWorkVoLists);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        //获取网元维度字符串数组
        List<Map> ivsName = intelligentTraceMapper.getServiceName(maps);
        List<String> ivsNameStr = new ArrayList<>();
        for(int i=0;i<ivsName.size();i++){
            ivsNameStr.add(ivsName.get(i).get("service_name").toString());
        }
        imageCountPos = intelligentTraceMapper.getNetWorkTraceByHour(map);
        if(imageCountPos ==null || imageCountPos.size()<=0){
            Map<String,Object> listMap = new HashedMap();
            List<String> stringList = new ArrayList<>();
            listMap.put("total",0);
            listMap.put("list",stringList);
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(listMap);
            return restResult;
        }
        for(int t=0;t<imageCountPos.size();t++){
            String dateTime = imageCountPos.get(t).getTime().substring(0,13);
            imageCountPos.get(t).setTime(dateTime);
        }
        for(int x = 0; x < imageCountPos.size(); x++) {
            //满足条件设备的时间
            String time = imageCountPos.get(x).getTime();
            for (int h = 0; h < timeList.size(); h++) {
                if (time.equals(timeList.get(h))) {
                    if (timeList.contains(time)) {
                        for (int j = 0; j < ivsName.size(); j++) {
                            if (!ivsName.get(j).get("service_name").equals(imageCountPos.get(x).getRemark())) {
                                ImageCountPo imageCountPo = new ImageCountPo();
                                imageCountPo.setTime(time);
                                imageCountPo.setPictureCount("0");
                                imageCountPo.setRemark(ivsName.get(j).get("service_name").toString());
                                newImageCountPos.add(imageCountPo);
                            } else {
                                newImageCountPos.add(imageCountPos.get(x));
                            }
                        }
                    } else {
                        for (int i = 0; i < ivsName.size(); i++) {
                            ImageCountPo imageCountPo1 = new ImageCountPo();
                            imageCountPo1.setRemark(ivsName.get(i).get("service_name").toString());
                            imageCountPo1.setPictureCount("0");
                            imageCountPo1.setTime(time);
                            newImageCountPos.add(imageCountPo1);
                        }
                    }
                }
            }
        }
        List<String> imageCountString= new ArrayList<>();
        for(int z=0;z<newImageCountPos.size();z++){
            String timeImageCount = newImageCountPos.get(z).getTime();
            imageCountString.add(timeImageCount);
        }
        LinkedHashSet<String> hashSets = new LinkedHashSet<>(imageCountString);
        //获取a-b的交集
        timeList.removeAll(hashSets);
        for(int s=0;s<timeList.size();s++){
            for(int o=0;o<ivsName.size();o++){
                ImageCountPo images = new ImageCountPo();
                images.setRemark(ivsName.get(o).get("service_name").toString());
                images.setPictureCount("0");
                images.setTime(timeList.get(s));
                newImageCountPos.add(images);
            }
        }
        //list集合去重
        for(int i=0;i<newImageCountPos.size()-1;i++)  {
            for (int j = 0; j < newImageCountPos.size(); j++) {
                if (i!=j && newImageCountPos.get(j).getTime().equals(newImageCountPos.get(i).getTime())&&
                        newImageCountPos.get(j).getRemark().equals(newImageCountPos.get(i).getRemark())
                        &&newImageCountPos.get(j).getPictureCount().equals("0")){
                    newImageCountPos.remove(j);
                    j--;
                }
            }
        }
        newImageCountPos.sort((ImageCountPo s1, ImageCountPo s2) -> s1.getTime().compareTo(s2.getTime()));
        Date data = new Date();
        String tableName = "tmp"+data.getTime();
        intelligentTraceMapper.createTable(tableName);
        intelligentTraceMapper.insert(tableName,newImageCountPos);
        List<String> cityLists = new ArrayList<>();
        cityLists.add("");
        List<Map<String,Object>> list = intelligentTraceMapper.getIvs(tableName,ivsNameStr,cityLists);
        List<Map<String,Object>> map1 = new ArrayList<>();
        if(currentPage>0||pageSize>0){
             map1= pageList(list,pageSize,currentPage);
        }else{
            map1 = list;
        }
        Collections.sort(map1,new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                int ret = 0;
                //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                ret = o1.get("datetime").toString().compareTo(o2.get("datetime").toString());//逆序的话就用o2.compareTo(o1)即可
                return ret;
            }
        });
        Integer num = list.size();
        Map<String,Object>mapList = new HashedMap();
        mapList.put("total",num);
        mapList.put("list",map1);
        intelligentTraceMapper.deleteByTableName(tableName);
        restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setData(mapList);
        return restResult;
    }



}
