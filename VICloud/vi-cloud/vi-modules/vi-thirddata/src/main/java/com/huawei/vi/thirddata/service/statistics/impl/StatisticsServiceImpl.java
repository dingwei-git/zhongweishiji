package com.huawei.vi.thirddata.service.statistics.impl;

import com.huawei.utils.NumConstant;
import com.huawei.vi.entity.po.DossierStatistics;
import com.huawei.vi.entity.po.TableHeaderInfor;
import com.huawei.vi.entity.vo.DossierVo;
import com.huawei.vi.entity.vo.ProgressInfo;
import com.huawei.vi.thirddata.mapper.TblDossierContentMapper;
import com.huawei.vi.thirddata.mapper.TblIpcIpMapper;
import com.huawei.vi.thirddata.service.statistics.StatisticsService;
import com.huawei.vi.thirddata.service.userIpresorce.UserIpResourceService;
import com.jovision.jaws.common.config.i18n.MessageSourceUtil;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.ServiceCommonConst;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    TblIpcIpMapper tblIpcIpMapper;

    @Autowired
    TblDossierContentMapper tblDossierContentMapper;

    @Resource(name = "userIpResourceService")
    private UserIpResourceService userIpResourceService;

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @Override
    public RestResult getStatisticsDossier(DossierVo dossierVo) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE, "", null);
        int pageSize = dossierVo.getPageSize();
        int currentPage = dossierVo.getCurrentPage();
        String[] organ = dossierVo.getOrganization();
        if (organ == null || organ.length == 0) {
            restResult.setMessage("device.level");
            return restResult;
        }
        List<Map> camerId = new ArrayList<>();
        List<String> dossierIdList = new ArrayList<>();
        Map<String, Object> dossierMap = new HashedMap();
        dossierMap.put("categoryId", 100);
        dossierMap.put("attributeId", 100001);
        dossierMap.put("typeId", 100001001);
        int total = 0;
        List<DossierStatistics> dossierStatistice = new ArrayList<>();
        List<DossierStatistics> dossierStatistices = new ArrayList<>();
        DossierStatistics dossierStatistics = new DossierStatistics();
        String filed0 = null;
        String filed1 = null;
        String filed2 = null;
        String filed3 = null;
        List<String> stringList = Arrays.asList(organ);
        Map<String,Object> maps = new HashedMap();
        for(int x=0;x<stringList.size();x++) {
            String organList = stringList.get(x);
            String id = organList;
            Map<String, Object> map = new HashedMap();
            if (id.length() == 3) {
                map.put("id0", id);
                filed0 = "filed0";
                map.put("filed0", filed0);
                maps.put("ipcLevel",1);
            }
            if (id.length() == 6) {
                map.put("id1", id);
                filed0 = "filed0";
                map.put("filed0", filed0);
                filed1 = "filed1";
                map.put("filed1", filed1);
                maps.put("ipcLevel",2);
            }
            if (id.length() == 9) {
                map.put("id2", id);
                filed0 = "filed0";
                map.put("filed0", filed0);
                filed1 = "filed1";
                map.put("filed1", filed1);
                filed2 = "filed2";
                map.put("filed2", filed2);
                maps.put("ipcLevel",3);
            }
            if (id.length() == 12) {
                map.put("id3", id);
                filed0 = "filed0";
                map.put("filed0", filed0);
                filed1 = "filed1";
                map.put("filed1", filed1);
                filed2 = "filed2";
                map.put("filed2", filed2);
                filed3 = "filed3";
                map.put("filed3", filed3);
                maps.put("ipcLevel",4);
            }
            //获取所有的cameraId
            camerId = tblIpcIpMapper.getCameraIds(map);
            if (camerId == null || camerId.size() == 0) {
                restResult.setMessage(messageSourceUtil.getMessage("device.query"));
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                return restResult;
            }
            List<String> cameraIdList = new ArrayList<>();
            for(int i=0;i<camerId.size();i++){
                cameraIdList.add(camerId.get(i).get("cameraId").toString());
            }
            dossierMap.put("cameraIdList",cameraIdList);
            List<Map> mapList = tblDossierContentMapper.getDossier(dossierMap);//获得所有的id value
            List<String> cameraList = new ArrayList<>();
            if (mapList == null || mapList.size() == 0) {
                restResult.setMessage(messageSourceUtil.getMessage("device.query"));
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                return restResult;
            }
            for(int i=0;i<mapList.size();i++){
                dossierIdList.add(mapList.get(i).get("id").toString());
            }
            String organization0 = null;
            String organization1 = null;
            String organization2 = null;
            String organization3 = null;
            if(camerId.get(0).get("filed0")!=null){
                organization0 = camerId.get(0).get("filed0").toString();
            }
            if(camerId.get(0).get("filed1")!=null){
                organization1 = camerId.get(0).get("filed1").toString();
            }
            if(camerId.get(0).get("filed2")!=null){
                organization2 = camerId.get(0).get("filed2").toString();
            }
            if(camerId.get(0).get("filed3")!=null){
                organization3 = camerId.get(0).get("filed3").toString();
            }
            //设备总数：
            int cameraCount = cameraIdList.size();
            //将id放
            List<Map> list = tblDossierContentMapper.getDossierMap(dossierIdList);
            if (list == null || list.size() == 0) {
                restResult.setMessage(messageSourceUtil.getMessage("device.query"));
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                return restResult;
            }
            int dessCount = tblDossierContentMapper.getDossierCount();
            if (dessCount == 0) {
                restResult.setMessage(messageSourceUtil.getMessage("device.query"));
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                return restResult;
            }
            String dshCount = null;
            String yshCount = null;
            String jdsCount = null;
            for (int k = 0; k < list.size(); k++) {
                Map<String, Object> mapDc = list.get(k);
                if (mapDc.get("dossiersStatus")!=null && mapDc.get("dossiersStatus").toString().equals(messageSourceUtil.getMessage("device.toBeReviewed"))) {
                    //提交数  待审核
                    dshCount = mapDc.get("count(dossiers_status)").toString();
                }
                if (mapDc.get("dossiersStatus")!=null&&mapDc.get("dossiersStatus").toString().equals(messageSourceUtil.getMessage("device.reviewed"))) {
                    //审核数 已审核
                    yshCount = mapDc.get("count(dossiers_status)").toString();
                }
                if (mapDc.get("dossiersStatus")!=null&&mapDc.get("dossiersStatus").toString().equals(messageSourceUtil.getMessage("device.submitted"))) {
                    //已提交
                    jdsCount = mapDc.get("count(dossiers_status)").toString();
                }
            }
            //审核通过率
            if (StringUtils.isEmpty(dshCount)) {
                dshCount = "0";
            }
            if (StringUtils.isEmpty(yshCount)) {
                yshCount = "0";
            }
            if (StringUtils.isEmpty(jdsCount)) {
                jdsCount = "0";
            }
            //device.numberOfFiles：  建档数=待审核+已审核+已提交
            Integer dossierInteger = Integer.parseInt(dshCount) + Integer.parseInt(yshCount) + Integer.parseInt(jdsCount);
            double passPerccent = Integer.parseInt(yshCount);
            double passPerccentes = dossierInteger;
            //审核通过率
            double passPerccents = passPerccent / passPerccentes;
            String passPer = passPerccents * 100 + "";
            if (passPer.length() <= 4) {
                passPer = passPer + "00";
            }
            passPer = passPer.substring(0, 5) + "%";
            //建档率
            double cameraCounts = cameraCount;
            double creatDossierPercent = dossierInteger / cameraCounts;
            String creatDossierPer = creatDossierPercent * 100 + "";
            if (creatDossierPer.length() <= 4) {
                creatDossierPer = creatDossierPer + "00";
            }
            creatDossierPer = creatDossierPer.substring(0, 5) + "%";
            dossierStatistics.setOrganization1(organization0);
            dossierStatistics.setOrganization2(organization1);
            dossierStatistics.setOrganization3(organization2);
            dossierStatistics.setOrganization4(organization3);
            dossierStatistics.setSubNUmber(dshCount);
            dossierStatistics.setAuditsNumber(yshCount);
            dossierStatistics.setDossier(dossierInteger.toString());
            dossierStatistics.setTotalNumber(String.valueOf(cameraCount));
            dossierStatistics.setDossierPercent(creatDossierPer);
            dossierStatistics.setPassPercent(passPer);
            dossierStatistices.add(dossierStatistics);
        }
        total = dossierStatistices.size();
        List<DossierStatistics> dossierStatisticsLists = page(dossierStatistices,pageSize,currentPage);
        maps.put("total",total);
        maps.put("dossierStatisticsLists",dossierStatisticsLists);
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setData(maps);
        return restResult;
    }


    public RestResult getStatisticsDossies(DossierVo dossierVo) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE, "", null);
        String[] organ = dossierVo.getOrganization();
        if (organ == null || organ.length == 0) {
            restResult.setMessage("device.level");
            return restResult;
        }
        List<Map> camerId = new ArrayList<>();
        List<String> dossierIdList = new ArrayList<>();
        Map<String, Object> dossierMap = new HashedMap();
        dossierMap.put("categoryId", 100);
        dossierMap.put("attributeId", 100001);
        dossierMap.put("typeId", 100001001);
        int total = 0;
        List<DossierStatistics> dossierStatistice = new ArrayList<>();
        List<DossierStatistics> dossierStatistices = new ArrayList<>();
        DossierStatistics dossierStatistics = new DossierStatistics();
        String filed0 = null;
        String filed1 = null;
        String filed2 = null;
        String filed3 = null;
        List<String> stringList = Arrays.asList(organ);
        for(int x=0;x<stringList.size();x++) {
            String organList = stringList.get(x);
            String id = organList;
            Map<String, Object> map = new HashedMap();
            if (id.length() == 3) {
                map.put("id0", id);
                filed0 = "filed0";
                map.put("filed0", filed0);
            }
            if (id.length() == 6) {
                map.put("id1", id);
                filed0 = "filed0";
                map.put("filed0", filed0);
                filed1 = "filed1";
                map.put("filed1", filed1);
            }
            if (id.length() == 9) {
                map.put("id2", id);
                filed0 = "filed0";
                map.put("filed0", filed0);
                filed1 = "filed1";
                map.put("filed1", filed1);
                filed2 = "filed2";
                map.put("filed2", filed2);
            }
            if (id.length() == 12) {
                map.put("id3", id);
                filed0 = "filed0";
                map.put("filed0", filed0);
                filed1 = "filed1";
                map.put("filed1", filed1);
                filed2 = "filed2";
                map.put("filed2", filed2);
                filed3 = "filed3";
                map.put("filed3", filed3);
            }
            //获取所有的cameraId
            camerId = tblIpcIpMapper.getCameraIds(map);
            if (camerId == null || camerId.size() == 0) {
                restResult.setMessage(messageSourceUtil.getMessage("device.query"));
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                return restResult;
            }
            List<String> cameraIdList = new ArrayList<>();
            for(int i=0;i<camerId.size();i++){
                cameraIdList.add(camerId.get(i).get("cameraId").toString());
            }
            dossierMap.put("cameraIdList",cameraIdList);
            List<Map> mapList = tblDossierContentMapper.getDossier(dossierMap);//获得所有的id value
            List<String> cameraList = new ArrayList<>();
            if (mapList == null || mapList.size() == 0) {
                restResult.setMessage(messageSourceUtil.getMessage("device.query"));
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                return restResult;
            }
            for(int i=0;i<mapList.size();i++){
                dossierIdList.add(mapList.get(i).get("id").toString());
            }
            String organization0 = null;
            String organization1 = null;
            String organization2 = null;
            String organization3 = null;
            if(camerId.get(0).get("filed0")!=null){
                organization0 = camerId.get(0).get("filed0").toString();
            }
            if(camerId.get(0).get("filed1")!=null){
                organization1 = camerId.get(0).get("filed1").toString();
            }
            if(camerId.get(0).get("filed2")!=null){
                organization2 = camerId.get(0).get("filed2").toString();
            }
            if(camerId.get(0).get("filed3")!=null){
                organization3 = camerId.get(0).get("filed3").toString();
            }
            //设备总数：
            int cameraCount = cameraIdList.size();
            //将id放
            List<Map> list = tblDossierContentMapper.getDossierMap(dossierIdList);
            if (list == null || list.size() == 0) {
                restResult.setMessage(messageSourceUtil.getMessage("device.query"));
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                return restResult;
            }
            int dessCount = tblDossierContentMapper.getDossierCount();
            if (dessCount == 0) {
                restResult.setMessage(messageSourceUtil.getMessage("device.query"));
                restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                return restResult;
            }
            String dshCount = null;
            String yshCount = null;
            String jdsCount = null;
            for (int k = 0; k < list.size(); k++) {
                Map<String, Object> maps = list.get(k);
                if (maps.get("dossiersStatus")!=null && maps.get("dossiersStatus").toString().equals(messageSourceUtil.getMessage("device.toBeReviewed"))) {
                    //提交数
                    dshCount = maps.get("count(dossiers_status)").toString();
                }
                if (maps.get("dossiersStatus")!=null&&maps.get("dossiersStatus").toString().equals(messageSourceUtil.getMessage("device.reviewed"))) {
                    //审核数
                    yshCount = maps.get("count(dossiers_status)").toString();
                }
                if (maps.get("dossiersStatus")!=null&&maps.get("dossiersStatus").toString().equals(messageSourceUtil.getMessage("device.submitted"))) {
                    //审核数
                    jdsCount = maps.get("count(dossiers_status)").toString();
                }
            }
            //审核通过率
            if (StringUtils.isEmpty(dshCount)) {
                dshCount = "0";
            }
            if (StringUtils.isEmpty(yshCount)) {
                yshCount = "0";
            }
            if (StringUtils.isEmpty(jdsCount)) {
                jdsCount = "0";
            }
            //device.numberOfFiles：
            Integer dossierInteger = Integer.parseInt(dshCount) + Integer.parseInt(yshCount) + Integer.parseInt(jdsCount);
            double passPerccent = Integer.parseInt(yshCount);
            double passPerccentes = dossierInteger;
            double passPerccents = passPerccent / passPerccentes;
            String passPer = passPerccents * 100 + "";
            if (passPer.length() <= 4) {
                passPer = passPer + "00";
            }
            passPer = passPer.substring(0, 5) + "%";
            //建档率
            double creatDossier = Integer.parseInt(yshCount);
            double cameraCounts = cameraCount;
            double creatDossierPercent = dossierInteger / cameraCounts;
            String creatDossierPer = creatDossierPercent * 100 + "";
            if (creatDossierPer.length() <= 4) {
                creatDossierPer = creatDossierPer + "00";
            }
            creatDossierPer = creatDossierPer.substring(0, 5) + "%";
            dossierStatistics.setOrganization1(organization0);
            dossierStatistics.setOrganization2(organization1);
            dossierStatistics.setOrganization3(organization2);
            dossierStatistics.setOrganization4(organization3);
            dossierStatistics.setSubNUmber(dshCount);
            dossierStatistics.setAuditsNumber(yshCount);
            dossierStatistics.setDossier(dossierInteger.toString());
            dossierStatistics.setTotalNumber(String.valueOf(cameraCount));
            dossierStatistics.setDossierPercent(creatDossierPer);
            dossierStatistics.setPassPercent(passPer);
            dossierStatistices.add(dossierStatistics);
        }
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setData(dossierStatistices);
        return restResult;
    }

    public RestResult getStatisticsDossiers(DossierVo dossierVo) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE, "", null);
        String[] organ = dossierVo.getOrganization();
        if (organ == null || organ.length == 0) {
            restResult.setMessage("device.level");
            return restResult;
        }
        String organList = organ[0];
        List<Map> camerId = new ArrayList<>();
        List<String> dossierIdList = new ArrayList<>();
        Map<String, Object> dossierMap = new HashedMap();
        dossierMap.put("categoryId", 100);
        dossierMap.put("attributeId", 100001);
        dossierMap.put("typeId", 100001001);
        int total = 0;
        List<DossierStatistics> dossierStatistice = new ArrayList<>();
        List<DossierStatistics> dossierStatistices = new ArrayList<>();
        DossierStatistics dossierStatistics = new DossierStatistics();
        String id = organList;
        String filed0 = null;
        String filed1 = null;
        String filed2 = null;
        String filed3 = null;
        Map<String, Object> map = new HashedMap();
        if (id.length() == 3) {
            map.put("id0", id);
            filed0 = "filed0";
            map.put("filed0", filed0);
        }
        if (id.length() == 6) {
            map.put("id1", id);
            filed0 = "filed0";
            map.put("filed0", filed0);
            filed1 = "filed1";
            map.put("filed1", filed1);
        }
        if (id.length() == 9) {
            map.put("id2", id);
            filed0 = "filed0";
            map.put("filed0", filed0);
            filed1 = "filed1";
            map.put("filed1", filed1);
            filed2 = "filed2";
            map.put("filed2", filed2);
        }
        if (id.length() == 12) {
            map.put("id3", id);
            filed0 = "filed0";
            map.put("filed0", filed0);
            filed1 = "filed1";
            map.put("filed1", filed1);
            filed2 = "filed2";
            map.put("filed2", filed2);
            filed3 = "filed3";
            map.put("filed3", filed3);
        }
        //获取所有的cameraId
        camerId = tblIpcIpMapper.getCameraIds(map);
        if(camerId == null || camerId.size() ==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.query"));
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            return restResult;
        }
        List<Map> mapList = tblDossierContentMapper.getDossier(dossierMap);//获得所有的id value
        if(mapList == null || mapList.size() ==0){
            restResult.setMessage(messageSourceUtil.getMessage("device.query"));
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            return restResult;
        }
        //将cameraId中与value 对比，拿到对应的id
        String organization0=null;
        String organization1=null;
        String organization2=null;
        String organization3=null;
        for (int j = 0; j < camerId.size(); j++) {
            String cameraId = camerId.get(j).get("cameraId").toString();
            for (int y = 0; y < mapList.size(); y++) {
                String strValue = mapList.get(y).get("value").toString();
                if (cameraId.equals(strValue)) {
                    String dossierId = mapList.get(y).get("id").toString();
                    if (!"".equals(camerId.get(j).get("filed0")) && camerId.get(j).get("filed0") != null) {
                        organization0 = camerId.get(j).get("filed0").toString();
                        dossierStatistics.setOrganization1(organization0);
                    }
                    if (!"".equals(camerId.get(j).get("filed1")) && camerId.get(j).get("filed1") != null) {
                        organization1 = camerId.get(j).get("filed1").toString();
                        dossierStatistics.setOrganization2(organization1);
                    }
                    if (!"".equals(camerId.get(j).get("filed2")) && camerId.get(j).get("filed2") != null) {
                        organization2 = camerId.get(j).get("filed2").toString();
                        dossierStatistics.setOrganization3(organization2);
                    }
                    if (!"".equals(camerId.get(j).get("filed3")) && camerId.get(j).get("filed3") != null) {
                        organization3 = camerId.get(j).get("filed3").toString();
                        dossierStatistics.setOrganization4(organization3);
                    }
                    dossierIdList.add(dossierId);
                    dossierStatistice.add(dossierStatistics);

                    //设备总数：
                    int cameraCount = dossierIdList.size();
                    //将id放
                    List<Map> list = tblDossierContentMapper.getDossierMap(dossierIdList);
                    if (list == null || list.size() == 0) {
                        restResult.setMessage(messageSourceUtil.getMessage("device.query"));
                        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                        return restResult;
                    }
                    int dessCount = tblDossierContentMapper.getDossierCount();
                    if (dessCount == 0) {
                        restResult.setMessage(messageSourceUtil.getMessage("device.query"));
                        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
                        return restResult;
                    }
                    String dshCount = null;
                    String yshCount = null;
                    String jdsCount = null;
                    for (int k = 0; k < list.size(); k++) {
                        Map<String, Object> maps = list.get(k);
                        if (maps.get("dossiersStatus").toString().equals(messageSourceUtil.getMessage("device.toBeReviewed"))) {
                            //提交数
                            dshCount = maps.get("count(dossiers_status)").toString();
                        }
                        if (maps.get("dossiersStatus").toString().equals(messageSourceUtil.getMessage("device.reviewed"))) {
                            //审核数
                            yshCount = maps.get("count(dossiers_status)").toString();
                        }
                        if (maps.get("dossiersStatus").toString().equals(messageSourceUtil.getMessage("device.submitted"))) {
                            //审核数
                            jdsCount = maps.get("count(dossiers_status)").toString();
                        }
                    }

                    //审核通过率
                    if (StringUtils.isEmpty(dshCount)) {
                        dshCount = "0";
                    }
                    if (StringUtils.isEmpty(yshCount)) {
                        yshCount = "0";
                    }
                    if (StringUtils.isEmpty(jdsCount)) {
                        jdsCount = "0";
                    }
                    //device.numberOfFiles：
                    Integer dossierInteger = Integer.parseInt(dshCount) + Integer.parseInt(yshCount) + Integer.parseInt(jdsCount);
                    double passPerccent = Integer.parseInt(yshCount);
                    double passPerccents = passPerccent / cameraCount;
                    String passPer = passPerccents * 100 + "";
                    if (passPer.length() <= 4) {
                        passPer = passPer + "00";
                    }
                    passPer = passPer.substring(0, 5) + "%";
                    //建档率
                    double creatDossier = Integer.parseInt(yshCount);
                    double creatDossierPercent = creatDossier / cameraCount;
                    String creatDossierPer = creatDossierPercent * 100 + "";
                    if (creatDossierPer.length() <= 4) {
                        creatDossierPer = creatDossierPer + "00";
                    }
                    creatDossierPer = creatDossierPer.substring(0, 5) + "%";
                    dossierStatistics.setSubNUmber(dshCount);
                    dossierStatistics.setAuditsNumber(yshCount);
                    dossierStatistics.setDossier(dossierInteger.toString());
                    dossierStatistics.setTotalNumber(String.valueOf(cameraCount));
                    dossierStatistics.setDossierPercent(creatDossierPer);
                    dossierStatistics.setPassPercent(passPer);
                    dossierStatistices.add(dossierStatistics);
                }
            }
        }
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setData(dossierStatistices);
        return restResult;
    }


    public static List<DossierStatistics> page(List<DossierStatistics> dataList, int pageSize, int currentPage) {
        List<DossierStatistics> currentPageList = new ArrayList<>();
        if (dataList != null && dataList.size() > 0) {
            int currIdx = (currentPage > 1 ? (currentPage - 1) * pageSize : 0);
            for (int i = 0; i < pageSize && i < dataList.size() - currIdx; i++) {
                DossierStatistics data = dataList.get(currIdx + i);
                currentPageList.add(data);
            }
        }
        return currentPageList;
    }

    /**
     * 刚进入页面初始化时，加载的动态字段信息（可选时间段，可选的第一层组织层级
     *
     * @param userName 用户名
     * @param tableNamePrefix 表名前缀
     * @return map 要展示的数据map
     */
    @Override
    public Map<String, Object> queryPublicInfo(String userName, String tableNamePrefix) {
        Map<String, Object> returnMap = new HashMap<>();

        // 1 根据用户名获取用户权限，查询可选的织层级信息
        Map<String, List<Map<String, String>>> orgInfo = userIpResourceService.getOrganizationsByUser(userName, null, CommonConst.TABLE_IPC_IP);
        returnMap.putAll(orgInfo);
        returnMap.put("ipcLevelNum", ProgressInfo.getInstance().getLevel());

        // 2 查询组织名
        int level = ProgressInfo.getInstance().getLevel();
        String[] titleFields = ProgressInfo.getInstance().getTitleFields();
        if (titleFields != null) {
            List<String> orgName = Arrays.asList(titleFields).subList(NumConstant.NUM_0, level);
            returnMap.put(CommonConst.ORGANIZATION, orgName);
        }
        return returnMap;
    }

    /*
    * 获取表头
    * */
    @Override
    public RestResult getTableHeader(String leves){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        if(StringUtils.isEmpty(leves)){
            restResult.setCode(ServiceCommonConst.CODE_FAILURE);
            restResult.setMessage("device.smallLevel");
            return restResult;
        }
        List<Map> list = new ArrayList<>();
        List<TableHeaderInfor> tableHeaderInforList = new ArrayList<>();
        Map<String,Object> map = new HashedMap();
        DossierStatistics dossierStatistics = new DossierStatistics();
        //TableHeaderInfor tableHeaderInfor= new TableHeaderInfor();
        if(leves.length()==3){
            //组织
            map.put("organization1",messageSourceUtil.getMessage("device.levelOne"));
            //device.numberOfEquipment
            map.put("totalNumber",messageSourceUtil.getMessage("device.numberOfEquipment"));
            //device.passNum
            map.put("auditsNumber",messageSourceUtil.getMessage("device.passNum"));
            //device.numberOfFiles
            map.put("dossier",messageSourceUtil.getMessage("device.numberOfFiles"));
            //device.passRateOfAudit
            map.put("passPercent",messageSourceUtil.getMessage("device.passRateOfAudit"));
            //device.filingRate
            map.put("dossierPercent",messageSourceUtil.getMessage("device.filingRate"));
            int i=1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                TableHeaderInfor tableHeaderInfor= new TableHeaderInfor();
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelOne"))){
                    tableHeaderInfor.setIndex(1);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfEquipment"))){
                    tableHeaderInfor.setIndex(2);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passNum"))){
                    tableHeaderInfor.setIndex(3);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfFiles"))){
                    tableHeaderInfor.setIndex(4);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passRateOfAudit"))){
                    tableHeaderInfor.setIndex(5);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.filingRate"))){
                    tableHeaderInfor.setIndex(6);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                tableHeaderInforList.sort((TableHeaderInfor s1, TableHeaderInfor s2) -> s1.getIndex().compareTo(s2.getIndex()));
            }
        }
        if(leves.length()==6){
            //组织
            map.put("organization1",messageSourceUtil.getMessage("device.levelOne"));
            map.put("organization2",messageSourceUtil.getMessage("device.levelTwo"));
            //device.numberOfEquipment
            map.put("totalNumber",messageSourceUtil.getMessage("device.numberOfEquipment"));
            //device.passNum
            map.put("auditsNumber",messageSourceUtil.getMessage("device.passNum"));
            //device.numberOfFiles
            map.put("dossier",messageSourceUtil.getMessage("device.numberOfFiles"));
            //device.passRateOfAudit
            map.put("passPercent",messageSourceUtil.getMessage("device.passRateOfAudit"));
            //device.filingRate
            map.put("dossierPercent",messageSourceUtil.getMessage("device.filingRate"));
            int i=1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                TableHeaderInfor tableHeaderInfor= new TableHeaderInfor();
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelOne"))){
                    tableHeaderInfor.setIndex(1);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelTwo"))){
                    tableHeaderInfor.setIndex(2);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfEquipment"))){
                    tableHeaderInfor.setIndex(3);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passNum"))){
                    tableHeaderInfor.setIndex(4);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfFiles"))){
                    tableHeaderInfor.setIndex(5);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passRateOfAudit"))){
                    tableHeaderInfor.setIndex(6);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.filingRate"))){
                    tableHeaderInfor.setIndex(7);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                tableHeaderInforList.sort((TableHeaderInfor s1, TableHeaderInfor s2) -> s1.getIndex().compareTo(s2.getIndex()));
            }
        }
        if(leves.length()==9){
            //组织
            map.put("organization1",messageSourceUtil.getMessage("device.levelOne"));
            map.put("organization2",messageSourceUtil.getMessage("device.levelTwo"));
            map.put("organization3",messageSourceUtil.getMessage("device.levelThird"));
            //device.numberOfEquipment
            map.put("totalNumber",messageSourceUtil.getMessage("device.numberOfEquipment"));
            //device.passNum
            map.put("auditsNumber",messageSourceUtil.getMessage("device.passNum"));
            //device.numberOfFiles
            map.put("dossier",messageSourceUtil.getMessage("device.numberOfFiles"));
            //device.passRateOfAudit
            map.put("passPercent",messageSourceUtil.getMessage("device.passRateOfAudit"));
            //device.filingRate
            map.put("dossierPercent",messageSourceUtil.getMessage("device.filingRate"));
            int i=1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                TableHeaderInfor tableHeaderInfor= new TableHeaderInfor();
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelOne"))){
                    tableHeaderInfor.setIndex(1);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelTwo"))){
                    tableHeaderInfor.setIndex(2);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelThird"))){
                    tableHeaderInfor.setIndex(3);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfEquipment"))){
                    tableHeaderInfor.setIndex(4);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passNum"))){
                    tableHeaderInfor.setIndex(5);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfFiles"))){
                    tableHeaderInfor.setIndex(6);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passRateOfAudit"))){
                    tableHeaderInfor.setIndex(7);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.filingRate"))){
                    tableHeaderInfor.setIndex(8);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                tableHeaderInforList.sort((TableHeaderInfor s1, TableHeaderInfor s2) -> s1.getIndex().compareTo(s2.getIndex()));
            }
        }
        if(leves.length()==12){
            //组织
            map.put("organization1",messageSourceUtil.getMessage("device.levelOne"));
            map.put("organization2",messageSourceUtil.getMessage("device.levelTwo"));
            map.put("organization3",messageSourceUtil.getMessage("device.levelThird"));
            map.put("organization4",messageSourceUtil.getMessage("device.levelFourth"));
            //device.numberOfEquipment
            map.put("totalNumber",messageSourceUtil.getMessage("device.numberOfEquipment"));
            //device.passNum
            map.put("auditsNumber",messageSourceUtil.getMessage("device.passNum"));
            //device.numberOfFiles
            map.put("dossier",messageSourceUtil.getMessage("device.numberOfFiles"));
            //device.passRateOfAudit
            map.put("passPercent",messageSourceUtil.getMessage("device.passRateOfAudit"));
            //device.filingRate
            map.put("dossierPercent",messageSourceUtil.getMessage("device.filingRate"));
            int i=1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                TableHeaderInfor tableHeaderInfor= new TableHeaderInfor();
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelOne"))){
                    tableHeaderInfor.setIndex(1);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelTwo"))){
                    tableHeaderInfor.setIndex(2);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelThird"))){
                    tableHeaderInfor.setIndex(3);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelFourth"))){
                    tableHeaderInfor.setIndex(4);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfEquipment"))){
                    tableHeaderInfor.setIndex(5);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passNum"))){
                    tableHeaderInfor.setIndex(6);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfFiles"))){
                    tableHeaderInfor.setIndex(7);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passRateOfAudit"))){
                    tableHeaderInfor.setIndex(8);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.filingRate"))){
                    tableHeaderInfor.setIndex(9);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                tableHeaderInforList.sort((TableHeaderInfor s1, TableHeaderInfor s2) -> s1.getIndex().compareTo(s2.getIndex()));
            }
        }
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setData(tableHeaderInforList);
        return restResult;
    }

    public RestResult getTableHeaders(DossierVo dossierVo){
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        String leves = dossierVo.getLeves();
        if(StringUtils.isEmpty(leves)){
            restResult.setCode(ServiceCommonConst.CODE_FAILURE);
            restResult.setMessage(messageSourceUtil.getMessage("device.smallLevel"));
            return restResult;
        }
        List<Map> list = new ArrayList<>();
        List<TableHeaderInfor> tableHeaderInforList = new ArrayList<>();
        Map<String,Object> map = new HashedMap();
        DossierStatistics dossierStatistics = new DossierStatistics();
        //TableHeaderInfor tableHeaderInfor= new TableHeaderInfor();
        if(leves.length()==3){
            //组织
            map.put("organization1",messageSourceUtil.getMessage("device.levelOne"));
            //device.numberOfEquipment
            map.put("totalNumber",messageSourceUtil.getMessage("device.numberOfEquipment"));
            //device.passNum
            map.put("auditsNumber",messageSourceUtil.getMessage("device.passNum"));
            //device.numberOfFiles
            map.put("dossier",messageSourceUtil.getMessage("device.numberOfFiles"));
            //device.passRateOfAudit
            map.put("passPercent",messageSourceUtil.getMessage("device.passRateOfAudit"));
            //device.filingRate
            map.put("dossierPercent",messageSourceUtil.getMessage("device.filingRate"));
            int i=1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                TableHeaderInfor tableHeaderInfor= new TableHeaderInfor();
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelOne"))){
                    tableHeaderInfor.setIndex(1);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfEquipment"))){
                    tableHeaderInfor.setIndex(2);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passNum"))){
                    tableHeaderInfor.setIndex(3);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfFiles"))){
                    tableHeaderInfor.setIndex(4);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passRateOfAudit"))){
                    tableHeaderInfor.setIndex(5);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.filingRate"))){
                    tableHeaderInfor.setIndex(6);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                tableHeaderInforList.sort((TableHeaderInfor s1, TableHeaderInfor s2) -> s1.getIndex().compareTo(s2.getIndex()));
            }
        }
        if(leves.length()==6){
            //组织
            map.put("organization1",messageSourceUtil.getMessage("device.levelOne"));
            map.put("organization2",messageSourceUtil.getMessage("device.levelTwo"));
            //device.numberOfEquipment
            map.put("totalNumber",messageSourceUtil.getMessage("device.numberOfEquipment"));
            //device.passNum
            map.put("auditsNumber",messageSourceUtil.getMessage("device.passNum"));
            //device.numberOfFiles
            map.put("dossier",messageSourceUtil.getMessage("device.numberOfFiles"));
            //device.passRateOfAudit
            map.put("passPercent",messageSourceUtil.getMessage("device.passRateOfAudit"));
            //device.filingRate
            map.put("dossierPercent",messageSourceUtil.getMessage("device.filingRate"));
            int i=1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                TableHeaderInfor tableHeaderInfor= new TableHeaderInfor();
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelOne"))){
                    tableHeaderInfor.setIndex(1);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelTwo"))){
                    tableHeaderInfor.setIndex(2);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfEquipment"))){
                    tableHeaderInfor.setIndex(3);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passNum"))){
                    tableHeaderInfor.setIndex(4);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfFiles"))){
                    tableHeaderInfor.setIndex(5);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passRateOfAudit"))){
                    tableHeaderInfor.setIndex(6);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.filingRate"))){
                    tableHeaderInfor.setIndex(7);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                tableHeaderInforList.sort((TableHeaderInfor s1, TableHeaderInfor s2) -> s1.getIndex().compareTo(s2.getIndex()));
            }
        }
        if(leves.length()==9){
            //组织
            map.put("organization1",messageSourceUtil.getMessage("device.levelOne"));
            map.put("organization2",messageSourceUtil.getMessage("device.levelTwo"));
            map.put("organization3",messageSourceUtil.getMessage("device.levelThird"));
            //device.numberOfEquipment
            map.put("totalNumber",messageSourceUtil.getMessage("device.numberOfEquipment"));
            //device.passNum
            map.put("auditsNumber",messageSourceUtil.getMessage("device.passNum"));
            //device.numberOfFiles
            map.put("dossier",messageSourceUtil.getMessage("device.numberOfFiles"));
            //device.passRateOfAudit
            map.put("passPercent",messageSourceUtil.getMessage("device.passRateOfAudit"));
            //device.filingRate
            map.put("dossierPercent",messageSourceUtil.getMessage("device.filingRate"));
            int i=1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                TableHeaderInfor tableHeaderInfor= new TableHeaderInfor();
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelOne"))){
                    tableHeaderInfor.setIndex(1);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelTwo"))){
                    tableHeaderInfor.setIndex(2);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelThird"))){
                    tableHeaderInfor.setIndex(3);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfEquipment"))){
                    tableHeaderInfor.setIndex(4);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passNum"))){
                    tableHeaderInfor.setIndex(5);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfFiles"))){
                    tableHeaderInfor.setIndex(6);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passRateOfAudit"))){
                    tableHeaderInfor.setIndex(7);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.filingRate"))){
                    tableHeaderInfor.setIndex(8);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                tableHeaderInforList.sort((TableHeaderInfor s1, TableHeaderInfor s2) -> s1.getIndex().compareTo(s2.getIndex()));
            }
        }
        if(leves.length()==12){
            //组织
            map.put("organization1",messageSourceUtil.getMessage("device.levelOne"));
            map.put("organization2",messageSourceUtil.getMessage("device.levelTwo"));
            map.put("organization3",messageSourceUtil.getMessage("device.levelThird"));
            map.put("organization4",messageSourceUtil.getMessage("device.levelFourth"));
            //device.numberOfEquipment
            map.put("totalNumber",messageSourceUtil.getMessage("device.numberOfEquipment"));
            //device.passNum
            map.put("auditsNumber",messageSourceUtil.getMessage("device.passNum"));
            //device.numberOfFiles
            map.put("dossier",messageSourceUtil.getMessage("device.numberOfFiles"));
            //device.passRateOfAudit
            map.put("passPercent",messageSourceUtil.getMessage("device.passRateOfAudit"));
            //device.filingRate
            map.put("dossierPercent",messageSourceUtil.getMessage("device.filingRate"));
            int i=1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                TableHeaderInfor tableHeaderInfor= new TableHeaderInfor();
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelOne"))){
                    tableHeaderInfor.setIndex(1);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelTwo"))){
                    tableHeaderInfor.setIndex(2);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelThird"))){
                    tableHeaderInfor.setIndex(3);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.levelFourth"))){
                    tableHeaderInfor.setIndex(4);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfEquipment"))){
                    tableHeaderInfor.setIndex(5);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passNum"))){
                    tableHeaderInfor.setIndex(6);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.numberOfFiles"))){
                    tableHeaderInfor.setIndex(7);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.passRateOfAudit"))){
                    tableHeaderInfor.setIndex(8);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                if(entry.getValue().toString().equals(messageSourceUtil.getMessage("device.filingRate"))){
                    tableHeaderInfor.setIndex(9);
                    tableHeaderInfor.setChName(entry.getValue().toString());
                    tableHeaderInfor.setKey(entry.getKey());
                    tableHeaderInfor.setShowName(entry.getValue().toString());
                    tableHeaderInforList.add(tableHeaderInfor);
                }
                tableHeaderInforList.sort((TableHeaderInfor s1, TableHeaderInfor s2) -> s1.getIndex().compareTo(s2.getIndex()));
            }
        }
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setData(tableHeaderInforList);
        return restResult;
    }

    @Override
    public RestResult dossierExport(DossierVo dossierVo, HttpServletResponse response) {
        RestResult restResults = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        RestResult resultHeader = new RestResult();//表头
        RestResult resultContent = new RestResult();//档案数据
        resultHeader =  getTableHeaders(dossierVo);
        List<TableHeaderInfor> list = (List<TableHeaderInfor>) resultHeader.getData();
        resultContent = getStatisticsDossies(dossierVo);
        List<DossierStatistics> lists = (List<DossierStatistics>) resultContent.getData();
        List<String> listStr = new ArrayList<>();
        //定义表头
        for(int i=0;i<list.size();i++){
            String name = list.get(i).getChName();
            listStr.add(name);
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        HSSFRow row = sheet.createRow(0);
        HSSFCell hssfCell = null;
        //插入第一行的表头
        for(int x=0;x<listStr.size();x++){
            hssfCell = row.createCell(x);
            hssfCell.setCellValue(listStr.get(x));
        }
        //第二行写入数据
        for(int y=0;y<lists.size();y++){
            HSSFRow nrow = sheet.createRow(y+1);
            for(int j=0;j<list.size();j++){
                HSSFCell ncell = nrow.createCell(j);
                getCon(ncell,lists.get(y),j);
            }

        }
        File file=new File("/tmp/dossier.xls");
        String path = file.getPath();
        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= FileUtils.openOutputStream(file);
            workbook.write(stream);
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode("dossier.xls", "utf-8"));

            // 2.下载
            OutputStream out = null;
            out = response.getOutputStream();
            // inputStream：读文件，前提是这个文件必须存在，要不就会报错
            InputStream is = new FileInputStream(path);
            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            stream.close();
            out.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(file.exists()){
                file.delete();
            }
        }
        restResults.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResults.setMessage(messageSourceUtil.getMessage("device.downloadSuccessful"));
        return restResults;
    }

    private void getCon(HSSFCell ncell, DossierStatistics dossierStatistics, int j){
        if(StringUtils.isNotEmpty(dossierStatistics.getOrganization1())&& StringUtils.isNotEmpty(dossierStatistics.getOrganization2())
                &&StringUtils.isNotEmpty(dossierStatistics.getOrganization3())&&StringUtils.isNotEmpty(dossierStatistics.getOrganization4())
        ) {

            if (j == 0) {
                if (StringUtils.isNotEmpty(dossierStatistics.getOrganization1())) {
                    ncell.setCellValue(dossierStatistics.getOrganization1());
                }
            }
            if (j == 1) {
                if (StringUtils.isNotEmpty(dossierStatistics.getOrganization2())) {
                    ncell.setCellValue(dossierStatistics.getOrganization2());
                }
            }
            if (j == 2) {
                if (StringUtils.isNotEmpty(dossierStatistics.getOrganization3())) {
                    ncell.setCellValue(dossierStatistics.getOrganization3());
                }
            }
            if (j == 3) {
                if (StringUtils.isNotEmpty(dossierStatistics.getOrganization4())) {
                    ncell.setCellValue(dossierStatistics.getOrganization4());
                }
            }
            if (j == 4) {
                if (StringUtils.isNotEmpty(dossierStatistics.getTotalNumber())) {
                    ncell.setCellValue(dossierStatistics.getTotalNumber());
                }
            }
            if (j == 5) {
                if (StringUtils.isNotEmpty(dossierStatistics.getAuditsNumber())) {
                    ncell.setCellValue(dossierStatistics.getAuditsNumber());
                }
            }
            if (j == 6) {
                if (StringUtils.isNotEmpty(dossierStatistics.getDossier())) {
                    ncell.setCellValue(dossierStatistics.getDossier());
                }
            }
            if (j == 7) {
                if (StringUtils.isNotEmpty(dossierStatistics.getPassPercent())) {
                    ncell.setCellValue(dossierStatistics.getPassPercent());
                }
            }
            if (j == 8) {
                if (StringUtils.isNotEmpty(dossierStatistics.getDossierPercent())) {
                    ncell.setCellValue(dossierStatistics.getDossierPercent());
                }
            }
        }

        if(StringUtils.isNotEmpty(dossierStatistics.getOrganization1())&& StringUtils.isNotEmpty(dossierStatistics.getOrganization2())
                &&StringUtils.isNotEmpty(dossierStatistics.getOrganization3())&&StringUtils.isEmpty(dossierStatistics.getOrganization4())){
            if (j == 0) {
                if (StringUtils.isNotEmpty(dossierStatistics.getOrganization1())) {
                    ncell.setCellValue(dossierStatistics.getOrganization1());
                }
            }
            if (j == 1) {
                if (StringUtils.isNotEmpty(dossierStatistics.getOrganization2())) {
                    ncell.setCellValue(dossierStatistics.getOrganization2());
                }
            }
            if (j == 2) {
                if (StringUtils.isNotEmpty(dossierStatistics.getOrganization3())) {
                    ncell.setCellValue(dossierStatistics.getOrganization3());
                }
            }
            if (j == 3) {
                if (StringUtils.isNotEmpty(dossierStatistics.getTotalNumber())) {
                    ncell.setCellValue(dossierStatistics.getTotalNumber());
                }
            }
            if (j == 4) {
                if (StringUtils.isNotEmpty(dossierStatistics.getAuditsNumber())) {
                    ncell.setCellValue(dossierStatistics.getAuditsNumber());
                }
            }
            if (j == 5) {
                if (StringUtils.isNotEmpty(dossierStatistics.getDossier())) {
                    ncell.setCellValue(dossierStatistics.getDossier());
                }
            }
            if (j == 6) {
                if (StringUtils.isNotEmpty(dossierStatistics.getPassPercent())) {
                    ncell.setCellValue(dossierStatistics.getPassPercent());
                }
            }
            if (j == 7) {
                if (StringUtils.isNotEmpty(dossierStatistics.getDossierPercent())) {
                    ncell.setCellValue(dossierStatistics.getDossierPercent());
                }
            }
        }

        if(StringUtils.isNotEmpty(dossierStatistics.getOrganization1())&& StringUtils.isNotEmpty(dossierStatistics.getOrganization2())
                && StringUtils.isEmpty(dossierStatistics.getOrganization3())&& StringUtils.isEmpty(dossierStatistics.getOrganization4())
        ){
            if (j == 0) {
                if (StringUtils.isNotEmpty(dossierStatistics.getOrganization1())) {
                    ncell.setCellValue(dossierStatistics.getOrganization1());
                }
            }
            if (j == 1) {
                if (StringUtils.isNotEmpty(dossierStatistics.getOrganization2())) {
                    ncell.setCellValue(dossierStatistics.getOrganization2());
                }
            }
            if (j == 2) {
                if (StringUtils.isNotEmpty(dossierStatistics.getTotalNumber())) {
                    ncell.setCellValue(dossierStatistics.getTotalNumber());
                }
            }
            if (j == 3) {
                if (StringUtils.isNotEmpty(dossierStatistics.getAuditsNumber())) {
                    ncell.setCellValue(dossierStatistics.getAuditsNumber());
                }
            }
            if (j == 4) {
                if (StringUtils.isNotEmpty(dossierStatistics.getDossier())) {
                    ncell.setCellValue(dossierStatistics.getDossier());
                }
            }
            if (j == 5) {
                if (StringUtils.isNotEmpty(dossierStatistics.getPassPercent())) {
                    ncell.setCellValue(dossierStatistics.getPassPercent());
                }
            }
            if (j == 6) {
                if (StringUtils.isNotEmpty(dossierStatistics.getDossierPercent())) {
                    ncell.setCellValue(dossierStatistics.getDossierPercent());
                }
            }
        }
    }
}






















