package com.huawei.vi.thirddata.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.vi.entity.model.*;
import com.huawei.vi.entity.model.RestResult;
import com.huawei.vi.entity.po.GBsyncData;
import com.huawei.vi.thirddata.mapper.TblIpcIpMapper;
import com.huawei.vi.thirddata.mapper.TblIpcIpSyncLogMapper;
import com.huawei.vi.thirddata.mapper.TblIpcIpSyncRecordMapper;
import com.huawei.vi.thirddata.mapper.TblIpcIpSyncRecordOperatorMapper;
import com.huawei.vi.thirddata.service.GBsyncDataService;
import com.huawei.vi.thirddata.service.ivsserver.common.Result;
import com.jovision.jaws.common.config.i18n.MessageSourceUtil;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.constant.Constants;
import com.jovision.jaws.common.constant.GB28181Constants;
import com.jovision.jaws.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GBsyncDataServiceImpl implements GBsyncDataService {

    @Value("${vi.url}")
    private String viURL;

    @Value("${dbtype}")
    private String dbtype;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TblIpcIpMapper tblIpcIpMapper;

    @Autowired
    private TblIpcIpSyncRecordMapper ipcIpSyncRecordMapper;

    @Autowired
    private TblIpcIpSyncLogMapper tblIpcIpSyncLogMapper;

    @Autowired
    private TblIpcIpSyncRecordOperatorMapper operatorMapper;

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 功能描述: <br> 同步GB28181设备信息
     *
     * @Param: [gBsyncData]
     * @Return: java.util.List<com.alibaba.fastjson.JSONObject>
     * @Author: pangxh
     * @Date: 2020/8/13 21:04
     */
    @Override
    public List<JSONObject> syncData(GBsyncData gBsyncData) {
        List<JSONObject> cameraInfos = GBsyncDataUtil.syncData(gBsyncData);
        return cameraInfos;
    }

    /**
     * 功能描述: 处理获取到的数据，返回同步到的数量，其中需要修改的设备数量，需要新增的设备数量
     *
     * @Param: [gBsyncData]
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/13 22:10
     */
    @Override
    public RestResult syncDataAndReturnDealInfo(GBsyncData gBsyncData) {
        FutureTask<List<JSONObject>> task = new FutureTask<>(new CallSyncData(gBsyncData));
        new Thread(task, "syncDataAndReturnDealInfo").start();
        List<JSONObject> ipcList = null;
        try {
            ipcList = task.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            task.cancel(true);
        }
        //todo 取出所有的设备包含层级关系 供插入到tbl_ipc_ip_sync_record表中使用
        if (ipcList == null || ipcList.size() == 0) {
            return RestResult.generateRestResult(NumConstant.NUM_1000,"success", null);
        }
        Map<String, Object> resultMap = new HashMap<>();
        //同步到所有设备树形数据
        List<TblIpcIpSyncRecord> syncRecords = getLevelIpInfo(ipcList, 0);
        //同步到所有设备树形数据(不包含已经存在的)
        List<TblIpcIpSyncRecord> syncRecordsWithoutExist = getLevelIpInfo(ipcList, 1);
        //给前端返回不包含已存在设备的树形结构数据
        resultMap.put("originalTreeData", syncRecordsWithoutExist);
        //同步到所有的设备（单条数据）
        List<TblIpcIpSyncRecord> recordWitheLevel = getRecordWitheLevel(syncRecords);
        //已经在ipc_ip_tmp表中的设备
        List<Map<String, Object>> allIPC = tblIpcIpMapper.queryAllIPC();
        int deleteCount = deleteCount(allIPC,recordWitheLevel);
        resultMap.put("deleteCount",deleteCount);
        //同步到的所有设备数量
        resultMap.put("totalCount", recordWitheLevel.size());
        //获取需要更新和需要新增的设备,并把数据写入到tbl_ipc_ip_sync_record和tbl_ipc_ip_record_operator
        splitExistAndNotExistIpc(resultMap, recordWitheLevel);

        //ipcIpSyncRecordMapper.insertBatch(recordWitheLevel);
        //todo 获取原有组织结构树形数据


        return RestResult.generateRestResult(NumConstant.NUM_1000,"success", resultMap);
    }

    private int deleteCount(List<Map<String, Object>> allIPC, List<TblIpcIpSyncRecord> recordWitheLevel) {
        StringBuilder recordBuild = new StringBuilder();
        recordWitheLevel.stream().map(TblIpcIpSyncRecord::getDeviceid).collect(Collectors.toList()).forEach(ipc->recordBuild.append(ipc).append(","));
        List<Map<String, Object>> list = allIPC.stream().filter(map -> !recordBuild.toString().contains(String.valueOf(map.get("cameraNum")))).collect(Collectors.toList());
        if(list!=null){
            return list.size();
        }else{
            return 0;
        }

    }

    @Override
    public RestResult SyncDataDataGrid(String taskid, String offset, String rows, String status) {
        Map<String, Object> param = new HashMap<>();
        param.put("taskid", taskid);
        param.put("pageindex", (Integer.valueOf(offset) - 1) * Integer.valueOf(rows));
        param.put("rows", Integer.valueOf(rows));
        param.put("status", status);

        List<TblIpcIpSyncRecordOperator> operators = operatorMapper.selectByTaskId(param);

        operators.stream().forEach(operator -> {
            if (StringUtils.isNotEmpty(operator.getRawlevels())) {
                operator.setRawlevels(operator.getRawlevels().replace("[", "").replace("]", ""));
            }
            if (StringUtils.isNotEmpty(operator.getCurrentlevels())) {
                StringBuilder builder = new StringBuilder();
                String[] levelAndId = operator.getCurrentlevels().split("#");
                for (String li : levelAndId) {
                    builder.append(li.split(":")[0]).append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                operator.setCurrentlevels(builder.toString());
            }
        });

        return RestResult.generateRestResult(NumConstant.NUM_1000,"success", operators);
    }

    @Override
    public RestResult oneKeyFillTheGap(String taskid) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put(CommonConst.DEVICE_SYNCDATA_TASKID, taskid);
            param.put(CommonConst.DEVICE_SYNCDATA_ADD, CommonConst.DEVICE_SYNCDATA_ADD);
            List<TblIpcIpSyncRecordOperator> operators = operatorMapper.selectByTaskId(param);
            //int maxCopoleCode = tblIpcIpMapper.maxCopoleCode();
            Integer maxIp = tblIpcIpMapper.queryMaxIp();
            if(maxIp == null){
                maxIp = Integer.valueOf(0);
            }
            for (int index = 0; index < operators.size(); index++) {
                //maxCopoleCode = maxCopoleCode + 1;
                TblIpcIpSyncRecordOperator operator = operators.get(index);
                operator.setCopolecode(operator.getCameranum());
                operator.setIp(IpUtils.num2ip(maxIp +index+1));
                operator.setCameraid(operator.getCameranum());
                //operator.setCameraName(operator.getCameranum());
                operatorMapper.updateByPrimaryKeySelective(operator);
            }
            return RestResult.generateRestResult(NumConstant.NUM_1000,"success",null);
        } catch (Exception e) {
            log.error("oneKeyFillTheGap====exception:", e);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败",null);
        }
    }

    /**
     * 功能描述: 弹窗编辑组织
     *
     * @Param: [data] Map 中的key[taskid(任务id),cameraid(摄像机编码),currentlevels(level:levelid#level2:level2id)]
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/20 16:00
     */
    @Override
    public RestResult editLevels(List<Map<String, String>> data) {
        log.info("editLevels===={}",JSON.toJSONString(data));
        operatorMapper.updateCurrentLevelsForEmpty(data.get(0).get("taskid"));
        data.stream().forEach(map -> {
            operatorMapper.updateCurrentLevels(map);
        });

        return RestResult.generateRestResult(NumConstant.NUM_1000,"success", null);
    }

    @Override
    public RestResult getIpcWithLatestLevel(String taskid) {
        List<Map<String, Object>> ipcWithLatestLevel = operatorMapper.getIpcWithLatestLevel(taskid);
        log.info("getIpcWithLatestLevel====>{}",JSON.toJSONString(ipcWithLatestLevel));
        ipcWithLatestLevel.stream().forEach(map -> {
            log.info("getIpcWithLatestLevel====>singleMap:{}",JSON.toJSONString(map));
            String currentlevels = String.valueOf(map.get(CommonConst.DEVICE_SYNCDATA_CURRENTLEVEL));
            log.info("getIpcWithLatestLevel====>singlecurrentlevels:{}",currentlevels);
            String[] levels = currentlevels.split("#");
            map.put(CommonConst.DEVICE_SYNCDATA_CURRENTLEVEL, levels[levels.length - 1]);
            log.info("getIpcWithLatestLevel====>resultMap:{}",map);
        });
        return RestResult.generateRestResult(NumConstant.NUM_1000,"success",ipcWithLatestLevel);
    }

    @Override
    public RestResult comfirOperator(String taskid,String token,String userId,String userName) {
        if (StringUtils.isEmpty(taskid)) {
            log.info("参数错误taskid：{}", taskid);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败",null);
        }

        Map<String, Object> param = new HashMap<>();
        param.put(CommonConst.DEVICE_SYNCDATA_TASKID, taskid);
        param.put("status",CommonConst.DEVICE_SYNCDATA_ADD);
        List<TblIpcIpSyncRecordOperator> operators = operatorMapper.selectByTaskId(param);
        List<TblIpcIpSyncRecordOperator> operatorsWithoutCurrentLevels = operators.stream().filter(op -> StringUtils.isEmpty(op.getCurrentlevels())).collect(Collectors.toList());
        if (operatorsWithoutCurrentLevels != null && operatorsWithoutCurrentLevels.size() > 0) {
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "有设备未设置组织关系", null);
        }

        List<Map<String, Object>> collect = operators.stream().map(dev -> {
            Map<String, Object> map = new HashMap<>();
            String[] levels = dev.getCurrentlevels().split("#");
            for (int index = 0; index < levels.length; index++) {
                String[] level = levels[index].split(":");
                map.put("filed" + index, level[0]);
                map.put("id" + index, level[1]);

            }
            map.put("copoleCode", dev.getCopolecode());
            map.put("camera_name", dev.getCameraName());
            map.put("ip", dev.getIp());
            map.put("ipCode", dev.getCameranum());
            map.put("cameraId", dev.getCameraid());
            map.put("ipType", dev.getIptype());
            map.put("ipName", dev.getCameraName());
            map.put("longitude", dev.getLongitude());
            map.put("latitude", dev.getLatitude());
            map.put("platformIp", dev.getPlatformip());
            map.put("cameraSn", dev.getCameraSn());
            map.put("groupName", dev.getGroupName());
            map.put("isCheck", dev.getCheckflag());
            map.put("hd", dev.getHd());
            map.put("manufacturer", dev.getManufacturer());
            map.put("model",  dev.getModel());
            return map;
        }).collect(Collectors.toList());
        if(CommonConst.DBTYPE_MYSQL.equals(dbtype)) {
            tblIpcIpMapper.insertBatchIpcTable(collect);
        }
        if(CommonConst.DBTYPE_GAUSS.equals(dbtype)) {
            for(Map<String, Object> map:collect) {
                tblIpcIpMapper.insertBatchIpcSelective(map);
            }
        }
        // ipc_ip_tmp数据有变更时写入redis，在下次分析时同步ipc_ip表数据
        redisUtil.save("period_refresh","1");
        visitVi(token);
        return RestResult.generateRestResult(NumConstant.NUM_1000,"success", null);
    }

    private void visitVi(String token){
        try{

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("token",token);
            log.info("visitVi======>token:{}",token);
            HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<MultiValueMap<String, String>>(body,headers);

            ResponseEntity<String> restResultResponseEntity = restTemplate.postForEntity(viURL + "/oneKeyForImportNetworkRelationship.do", multiValueMapHttpEntity, String.class);
            log.info("jsonObjectResponseEntity=====>{}",restResultResponseEntity.getBody());
        }catch (Exception e){
            log.error("jsonObjectResponseEntity=====>{}",e);
        }

    }

    /**
     * 功能描述: 国际化标题
     *
     * @Param: []
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/22 14:24
     */
    @Override
    public RestResult internationalizationTitle() {

        List<Map<String, String>> resultList = new ArrayList<>();

        //原始层级
        String originalLevel = messageSourceUtil.getMessage("device.originalLevel");
        Map<String, String> originalLevelmap = new HashMap<>();
        originalLevelmap.put("field", "rawlevels");
        originalLevelmap.put("name", originalLevel);
        resultList.add(originalLevelmap);

        //共杆编码
        String commonRodCode = messageSourceUtil.getMessage("device.commonRodCode");
        Map<String, String> commonRodCodelmap = new HashMap<>();
        commonRodCodelmap.put("field", "copolecode");
        commonRodCodelmap.put("name", commonRodCode);
        resultList.add(commonRodCodelmap);

        //设备编码
        String deviceCode = messageSourceUtil.getMessage("device.deviceCode");
        Map<String, String> deviceCodemap = new HashMap<>();
        deviceCodemap.put("field", "cameranum");
        deviceCodemap.put("name", deviceCode);
        resultList.add(deviceCodemap);


        //cameraid
        String cameraid = messageSourceUtil.getMessage("device.cameraid");
        Map<String, String> cameraidMap = new HashMap<>();
        cameraidMap.put("field", "cameraid");
        cameraidMap.put("name", cameraid);
        resultList.add(cameraidMap);


        //设备类型
        String deviceType = messageSourceUtil.getMessage("device.deviceType");
        Map<String, String> deviceTypemap = new HashMap<>();
        deviceTypemap.put("field", "iptype");
        deviceTypemap.put("name", deviceType);
        resultList.add(deviceTypemap);

        //设备名称
        String deviceName = messageSourceUtil.getMessage("device.deviceName");
        Map<String, String> deviceNamemap = new HashMap<>();
        deviceNamemap.put("field", "cameraName");
        deviceNamemap.put("name", deviceName);
        resultList.add(deviceNamemap);

        //设备ip
        String deviceIp = messageSourceUtil.getMessage("device.deviceIp");
        Map<String, String> deviceIpmap = new HashMap<>();
        deviceIpmap.put("field", "ip");
        deviceIpmap.put("name", deviceIp);
        resultList.add(deviceIpmap);

        //经度
        String longitude = messageSourceUtil.getMessage("device.longitude");
        Map<String, String> longitudemap = new HashMap<>();
        longitudemap.put("field", "longitude");
        longitudemap.put("name", longitude);
        resultList.add(longitudemap);

        //纬度
        String latitude = messageSourceUtil.getMessage("device.latitude");
        Map<String, String> latitudemap = new HashMap<>();
        latitudemap.put("field", "latitude");
        latitudemap.put("name", latitude);
        resultList.add(latitudemap);


        //存储平台IP地址
        String platformid = messageSourceUtil.getMessage("device.platformid");
        Map<String, String> platformidmap = new HashMap<>();
        platformidmap.put("field", "platformip");
        platformidmap.put("name", platformid);
        resultList.add(platformidmap);


        // 分组名称
        String groupname = messageSourceUtil.getMessage("device.groupname");
        Map<String, String> groupnamemap = new HashMap<>();
        groupnamemap.put("field", "groupName");
        groupnamemap.put("name", groupname);
        resultList.add(groupnamemap);

        //厂家
        String factory = messageSourceUtil.getMessage("device.factory");
        Map<String, String> factorymap = new HashMap<>();
        factorymap.put("field", "manufacturer");
        factorymap.put("name", factory);
        resultList.add(factorymap);

        //分辨率
//        String resolution = messageSourceUtil.getMessage("device.resolution");
//        Map<String, String> resolutionmap = new HashMap<>();
//        resolutionmap.put("field", "hd");
//        resolutionmap.put("name", resolution);
//        resultList.add(resolutionmap);

        //型号
        String model = messageSourceUtil.getMessage("device.model");
        Map<String, String> modelmap = new HashMap<>();
        modelmap.put("field", "model");
        modelmap.put("name", model);
        resultList.add(modelmap);


        //目标层级
        String targetLevel = messageSourceUtil.getMessage("device.targetLevel");
        Map<String, String> targetLevelmap = new HashMap<>();
        targetLevelmap.put("field", "currentlevels");
        targetLevelmap.put("name", targetLevel);
        resultList.add(targetLevelmap);

        return RestResult.generateRestResult(NumConstant.NUM_1000,"success",resultList);
    }

    @Override
    public RestResult ipcipdatagird(Integer levelCount, String levelid, String offset, String rows) {
        Map<String, Object> param = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (levelCount != null && levelCount.intValue() > 0) {
            for (int index = 0; index < levelCount; index++) {
                builder.append("(").append("id").append(index).append("=").append("'").append(levelid).append("'").append(")").append(" ").append("or").append(" ");
            }
            builder.delete(builder.lastIndexOf("or"),builder.length()-1);
            param.put("levelid",builder.toString());
        }
        StringBuilder cloumnBuilder = new StringBuilder();
        if(CommonConst.DBTYPE_GAUSS.equals(dbtype)){
            for (int index = 0; index < levelCount.intValue(); index++) {
                cloumnBuilder.append("filed" + index).append(" AS `filed"+index+"`").append(",");
                cloumnBuilder.append("id" + index).append(" AS `id"+index+"`").append(",");
            }
            cloumnBuilder.append("group_name").append(" AS `group_name`").append(",");
            cloumnBuilder.append("camera_name").append(" AS `camera_name`").append(",");
            cloumnBuilder.append("ip").append(" AS `ip`").append(",");
            cloumnBuilder.append("latitude").append(" AS `latitude`").append(",");
            cloumnBuilder.append("camera_sn").append(" AS `camera_sn`").append(",");
            cloumnBuilder.append("manufacturer").append(" AS `manufacturer`").append(",");
            cloumnBuilder.append("copoleCode").append(" AS `copoleCode`").append(",");
            cloumnBuilder.append("cameraId").append(" AS `cameraId`").append(",");
            cloumnBuilder.append("lastOnlineTime").append(" AS `lastOnlineTime`").append(",");
            cloumnBuilder.append("model").append(" AS `model`").append(",");
            cloumnBuilder.append("cameraNum").append(" AS `cameraNum`").append(",");
            cloumnBuilder.append("camera_name").append(" AS `camera_name`").append(",");
            cloumnBuilder.append("hd").append(" AS `hd`").append(",");
            cloumnBuilder.append("checkflag").append(" AS `checkflag`").append(",");
            cloumnBuilder.append("ipType").append(" AS `ipType`").append(",");
            cloumnBuilder.append("referTime").append(" AS `referTime`").append(",");
            cloumnBuilder.append("longitude").append(" AS `longitude`").append(",");
            cloumnBuilder.append("platformIp").append(" AS `platformIp`");

        }else{
            cloumnBuilder.append("*");
        }
        param.put("filds",cloumnBuilder.toString());
        param.put("pageindex", (Integer.valueOf(offset) - 1) * Integer.valueOf(rows));
        param.put("rows", Integer.valueOf(rows));
        Integer total = tblIpcIpMapper.queryIpcByParamsCount(param);
        List<Map<String, Object>> maps = tblIpcIpMapper.queryIpcByParams(param);
        Map<String,Object> result = new HashMap<>();
        result.put("totalCount",total);
        result.put("rows",maps);
        return RestResult.generateRestResult(NumConstant.NUM_1000,"success",result);
    }

    @Override
    public RestResult editipc(Map<String, Object> param) {
        int i = tblIpcIpMapper.updateIpcByCameraNum(param);
        if(i>0){
            // ipc_ip_tmp数据有变更时写入redis，在下次分析时同步ipc_ip表数据
            redisUtil.save("period_refresh","1");
            return RestResult.generateRestResult(NumConstant.NUM_1000,"success",null);
        }else{
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败",null);
        }
    }

    /**
     * 功能描述: <br>分理处已经在ipc表中和未在表中的数据
     *
     * @Param: [resultMap, recordWitheLevel]
     * @Return: void
     * @Author: pangxh
     * @Date: 2020/8/14 19:56
     */
    private void splitExistAndNotExistIpc(Map<String, Object> resultMap, List<TblIpcIpSyncRecord> recordWitheLevel) {
        List<TblIpcIpSyncRecord> recordWitheLevelTmp = new ArrayList<>();
        recordWitheLevelTmp.addAll(recordWitheLevel);
        List<String> deviceIds = recordWitheLevel.stream().map(TblIpcIpSyncRecord::getDeviceid).collect(Collectors.toList());
        List<String> cameraNums = tblIpcIpMapper.queryIpcExist(deviceIds);
        List<TblIpcIpSyncRecord> hasExistIps = new ArrayList<>();
        List<TblIpcIpSyncRecord> toAddIps = new ArrayList<>();
        for (String cameraNum : cameraNums) {
            for (TblIpcIpSyncRecord tblIpcIpSyncRecord : recordWitheLevelTmp) {
                if (cameraNum.equals(tblIpcIpSyncRecord.getDeviceid())) {
                    hasExistIps.add(tblIpcIpSyncRecord);
                }
            }
        }
        recordWitheLevel.removeAll(hasExistIps);
        toAddIps.addAll(recordWitheLevel);

        //插入同步记录表
        String taskId = UUID.randomUUID().toString();

        // 本次同步数据的任务ID返回给前端
        resultMap.put("currentTaskid", taskId);

        Date operatorTime = new Date();

        List<TblIpcIpSyncLog> syncLogs = new ArrayList<>();
        List<TblIpcIpSyncRecordOperator> operators = new ArrayList<>();

        //新增设备处理
        toAddIps.stream().forEach(dev -> {
            insetIpcSyncRecord(dev, syncLogs, operators, "add", taskId, operatorTime);
        });

        //已存在设备处理
        hasExistIps.stream().forEach(dev -> {
            insetIpcSyncRecord(dev, syncLogs, operators, "update", taskId, operatorTime);
        });

        tblIpcIpSyncLogMapper.insertBatch(syncLogs);

        //返回给前端需要新增的数量
        resultMap.put("updateCount", hasExistIps.size());
        //resultMap.put("updateIps", hasExistIps);
        // 返回给前端需要新增的数量
        resultMap.put("needToAddCount", toAddIps.size());
        //resultMap.put("needToAddIps", toAddIps);

        //todo 插入到tbl_ipc_ip_record_operator表中
        if(CommonConst.DBTYPE_MYSQL.equals(dbtype)) {
            operatorMapper.insertBatch(operators);
        }

        if(CommonConst.DBTYPE_GAUSS.equals(dbtype)) {
            for(TblIpcIpSyncRecordOperator recordOperator:operators) {
                operatorMapper.insertSelective(recordOperator);
            }
        }


        //暂时注释掉，表格数据另外一个接口返回
//        Collection<TblIpcIpSyncRecordOperator> needtoaddList = operators.stream().filter(dev -> CommonConst.DEVICE_SYNCDATA_ADD.equals(dev.getStatus())).collect(Collectors.toList());
//        resultMap.put("dataGride",needtoaddList);

    }

    private void insetIpcSyncRecord(TblIpcIpSyncRecord dev, List<TblIpcIpSyncLog> syncLogs, List<TblIpcIpSyncRecordOperator> operators, String status, String taskid, Date operatorTime) {
        dev.setTaskid(taskid);
        dev.setOperatetime(operatorTime);
        dev.setSynctype(status);

        TblIpcIpSyncLog log = new TblIpcIpSyncLog();
        log.setId(UUID.randomUUID().toString());
        log.setCameraid(dev.getDeviceid());
        log.setCreatetime(operatorTime);
        log.setOperator("");
        log.setTaskid(taskid);
        log.setStatus(status);
        log.setDetails(JSON.toJSONString(dev));
        syncLogs.add(log);
        //插入设备同步数据记录表
        ipcIpSyncRecordMapper.insertSelective(dev);

        TblIpcIpSyncRecordOperator recordOperator = new TblIpcIpSyncRecordOperator();
        transferToTblIpcIpSyncRecordOperator(dev, recordOperator);
        recordOperator.setIp("-");
        recordOperator.setCameraid("-");
        operators.add(recordOperator);
    }


    private void transferToTblIpcIpSyncRecordOperator(TblIpcIpSyncRecord dev, TblIpcIpSyncRecordOperator recordOperator) {
        //todo 把record表的数据转化成TblIpcIpSyncRecordOperator数据
        recordOperator.setId(UUID.randomUUID().toString());
        recordOperator.setTaskid(dev.getTaskid());
        recordOperator.setIp(dev.getIpaddress());
        recordOperator.setCameranum(dev.getDeviceid());
        recordOperator.setCameraName(dev.getName());
        recordOperator.setCameraid(dev.getDeviceid());
        recordOperator.setCheckflag("1");
        recordOperator.setIptype(IpcTypeEnum.getTypeName(dev.getInfo()));
        recordOperator.setLongitude(dev.getLongitude() == null ? "-" : dev.getLongitude());
        recordOperator.setLatitude(dev.getLatitude() == null ? "-" : dev.getLatitude());
        recordOperator.setPlatformip("1.1.1.1");
        recordOperator.setCameraSn(GB28181Constants.GB28181_SUBTRACTION_SYMBOL);
        recordOperator.setGroupName(GB28181Constants.GB28181_SUBTRACTION_SYMBOL);
        recordOperator.setLastonlinetime(GB28181Constants.GB28181_SUBTRACTION_SYMBOL);
        recordOperator.setRefertime(GB28181Constants.GB28181_SUBTRACTION_SYMBOL);
        recordOperator.setRawlevels(String.valueOf(dev.getLevels()));
        recordOperator.setStatus(dev.getSynctype());
        recordOperator.setManufacturer(dev.getManufacturer());
        recordOperator.setModel(dev.getModel());
        recordOperator.setHd(dev.getHd());
        recordOperator.setCreatetime(dev.getOperatetime());

    }

    /**
     * 功能描述: <br>
     *
     * @Param: [ipcList, removeExist:是否去除已存在的ipc，0不去除，1去除]
     * @Return: java.util.List<com.huawei.vi.entity.model.TblIpcIpSyncRecord>
     * @Author: pangxh
     * @Date: 2020/8/14 21:23
     */
    private List<TblIpcIpSyncRecord> getLevelIpInfo(List<JSONObject> ipcList, int removeExist) {
        //顶级parentid
        List<TblIpcIpSyncRecord> syncRecords = new ArrayList<>();
        for (JSONObject ipc : ipcList) {
            TblIpcIpSyncRecord tblIpcIpSyncRecord = new TblIpcIpSyncRecord();
            String topParentId = ipc.getString("DeviceID");
            JSONObject deviceListObject = ipc.getJSONObject("DeviceList");
            //DeviceList中设备的数量
            Integer deviceNum = deviceListObject.getInteger("@Num");
            JSONArray devices = new JSONArray();
            try {
                devices.addAll(deviceListObject.getJSONArray("Item"));
            } catch (Exception e) {
                JSONObject devObject = deviceListObject.getJSONObject("Item");
                devices.add(devObject);
            }
            //原始完整数据的树形接口数据
            if (removeExist == 0) {
                dealLevelIpInfo(topParentId, devices, tblIpcIpSyncRecord);
            }
            //去除已存在设备的树形结构
            if (removeExist == 1) {
                JSONArray objects = new JSONArray(devices);
                removeExitIpc(objects);
                if (objects.size() > 0) {
                    dealLevelIpInfo(topParentId, objects, tblIpcIpSyncRecord);
                } else {
                    tblIpcIpSyncRecord = null;
                }
            }
            if (tblIpcIpSyncRecord != null) {
                syncRecords.add(tblIpcIpSyncRecord);
            }
        }
        return syncRecords;
    }

    private void removeExitIpc(JSONArray objects) {
        List<String> deviceIds = objects.stream().map(dev -> {
            JSONObject object = (JSONObject) dev;
            return object;
        }).collect(Collectors.toList()).stream().map(dev -> dev.getString(GB28181Constants.GB28181_DEVICEID)).collect(Collectors.toList());

        List<String> cameraNums = tblIpcIpMapper.queryIpcExist(deviceIds);
        ArrayList<Object> objects1 = new ArrayList<>(objects);
        for (String cameraNum : cameraNums) {
            for (Object tblIpcIpSyncRecord : objects1) {
                JSONObject object = (JSONObject) tblIpcIpSyncRecord;
                if (cameraNum.equals(object.getString(GB28181Constants.GB28181_DEVICEID))) {
                    objects.remove(tblIpcIpSyncRecord);
                }
            }
        }
    }

    private void dealLevelIpInfo(String topParentId, JSONArray devices, TblIpcIpSyncRecord tblIpcIpSyncRecord) {
        //获取第一个层级的信息
        devices.stream().map(dev -> {
            JSONObject object = (JSONObject) dev;
            return object;
        }).filter(dev -> topParentId.equals(dev.getString("ParentID"))).collect(Collectors.toList())
                //组合数据
                .stream().forEach(levelInfo -> {
            try {
                transferToTblIpcIpSyncRecord(levelInfo, tblIpcIpSyncRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getLevelIpInfoChild(tblIpcIpSyncRecord, devices);

        });


    }

    private List<TblIpcIpSyncRecord> getLevelIpInfoChild(TblIpcIpSyncRecord tblIpcIpSyncRecord, JSONArray devices) {
        String parentid = tblIpcIpSyncRecord.getDeviceid();
        List<TblIpcIpSyncRecord> children = new ArrayList<>();
        tblIpcIpSyncRecord.setChild(children);
        for (int index = 0; index < devices.size(); index++) {
            JSONObject devices1 = (JSONObject) devices.get(index);
            TblIpcIpSyncRecord child = new TblIpcIpSyncRecord();
            if (parentid.equals(devices1.getString(GB28181Constants.GB28181_PARENTID))) {
                try {
                    //先把上一层级所有的父级加进来
                    child.getLevels().addAll(tblIpcIpSyncRecord.getLevels());
                    //把上一层级的名字加进来
                    child.getLevels().add(tblIpcIpSyncRecord.getName());
                    transferToTblIpcIpSyncRecord(devices1, child);
                    children.add(child);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for (TblIpcIpSyncRecord record : children) {
            record.setChild(getLevelIpInfoChild(record, devices));
        }

        if (children.size() == 0) {
            return null;
        }
        return children;
    }

    private void transferToTblIpcIpSyncRecord(JSONObject levelInfo, TblIpcIpSyncRecord tblIpcIpSyncRecord) throws Exception {
        tblIpcIpSyncRecord.setName(levelInfo.getString(GB28181Constants.GB28181_NAME));
        tblIpcIpSyncRecord.setDeviceid(levelInfo.getString(GB28181Constants.GB28181_DEVICEID));
        tblIpcIpSyncRecord.setAddress(levelInfo.getString(GB28181Constants.GB28181_ADDRESS));
        tblIpcIpSyncRecord.setChannelno(levelInfo.getString(GB28181Constants.GB28181_CHANNELNO));
        tblIpcIpSyncRecord.setCivilcode(levelInfo.getString(GB28181Constants.GB28181_CIVILCODE));
        tblIpcIpSyncRecord.setInfo(levelInfo.getJSONObject(GB28181Constants.GB28181_INFO) != null ? levelInfo.getJSONObject(GB28181Constants.GB28181_INFO).getString(GB28181Constants.GB28181_PTZTYPE) : null);
        tblIpcIpSyncRecord.setIpaddress(levelInfo.getString(GB28181Constants.GB28181_IPADDRESS));
        tblIpcIpSyncRecord.setLatitude(levelInfo.getString(GB28181Constants.GB28181_LATITUDE));
        tblIpcIpSyncRecord.setLongitude(levelInfo.getString(GB28181Constants.GB28181_LONGITUDE));
        tblIpcIpSyncRecord.setLoginname(levelInfo.getString(GB28181Constants.GB28181_LOGINNAME));
        tblIpcIpSyncRecord.setManufacturer(levelInfo.getString(GB28181Constants.GB28181_MANUFACTURER));
        tblIpcIpSyncRecord.setParentid(levelInfo.getString(GB28181Constants.GB28181_PARENTID));
        tblIpcIpSyncRecord.setModel(levelInfo.getString(GB28181Constants.GB28181_MODEL));
        tblIpcIpSyncRecord.setOwner(levelInfo.getString(GB28181Constants.GB28181_OWNER));
        tblIpcIpSyncRecord.setStatus(levelInfo.getString(GB28181Constants.GB28181_STATUS));
        tblIpcIpSyncRecord.setSecrecy(levelInfo.getString(GB28181Constants.GB28181_SECRECY));
        tblIpcIpSyncRecord.setSafetyway(levelInfo.getString(GB28181Constants.GB28181_SAFETYWAY));
        tblIpcIpSyncRecord.setRegisterway(levelInfo.getString(GB28181Constants.GB28181_REGISTERWAY));
        tblIpcIpSyncRecord.setPort(levelInfo.getString(GB28181Constants.GB28181_PORT));
        tblIpcIpSyncRecord.setPassword(levelInfo.getString(GB28181Constants.GB28181_PASSWORD));
        tblIpcIpSyncRecord.setParental(levelInfo.getString(GB28181Constants.GB28181_PARENTAL));
    }

    /**
     * 功能描述: 获取所有设备并且添加上层级
     *
     * @Param: [syncRecords]
     * @Return: java.util.List<com.huawei.vi.entity.model.TblIpcIpSyncRecord>
     * @Author: pangxh
     * @Date: 2020/8/14 17:16
     */
    private List<TblIpcIpSyncRecord> getRecordWitheLevel(List<TblIpcIpSyncRecord> syncRecords) {
        List<TblIpcIpSyncRecord> recordsWitheLevel = new ArrayList<>();
        syncRecords.stream().forEach(record -> {
            leftNode(record, recordsWitheLevel);
        });
        return recordsWitheLevel;
    }

    private void leftNode(TblIpcIpSyncRecord syncRecords, List<TblIpcIpSyncRecord> recordsWitheLevel) {
        if (syncRecords.getChild() != null && syncRecords.getChild().size() > 0) {
            for (TblIpcIpSyncRecord tblIpcIpSyncRecord : syncRecords.getChild()) {
                leftNode(tblIpcIpSyncRecord, recordsWitheLevel);
            }
        } else {
            setTblIpcIpSyncRecord(syncRecords);
            recordsWitheLevel.add(syncRecords);
        }
    }

    private void setTblIpcIpSyncRecord(TblIpcIpSyncRecord syncRecords) {
        Class<? extends TblIpcIpSyncRecord> aClass = syncRecords.getClass();
        List<String> levels = syncRecords.getLevels();
        for (int index = 0; index < levels.size(); index++) {
            try {
                Method declaredMethod = aClass.getDeclaredMethod("setLevel" + (index + 1), String.class);
                declaredMethod.invoke(syncRecords, levels.get(index));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }


    class CallSyncData implements Callable<List<JSONObject>> {

        GBsyncData gBsyncData;

        public CallSyncData(GBsyncData gBsyncData) {
            this.gBsyncData = gBsyncData;
        }

        @Override
        public List<JSONObject> call() throws Exception {
            //有时候请求获取不到数据 记录请求的的次数
            int count = 0;
            while ((syncData(gBsyncData) == null || syncData(gBsyncData).size() == 0) && count < 5) {
                count++;
            }
            return syncData(gBsyncData);
        }
    }

}
