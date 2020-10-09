package com.huawei.vi.workorder.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.huawei.vi.entity.po.*;
import com.huawei.vi.entity.vo.*;
import com.huawei.vi.feign.config.client.UserClient;
import com.huawei.vi.workorder.mapper.OrderMapper;
import com.huawei.vi.workorder.service.*;
import com.jovision.jaws.common.constant.*;
import com.jovision.jaws.common.util.HttpClientUtil;
import com.jovision.jaws.common.util.PageResult;
import com.jovision.jaws.common.util.RestResult;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MaintenanceInfoService maintenanceInfoService;
    @Autowired
    private WorkOrderPictureService workOrderPictureService;
    @Autowired
    private LogManagerService logManagerService;
    @Autowired
    private AlarmService alarmService;

    @Autowired
    private UserClient userClient;

    @Value("${path.images}")
    private String imagesPath ;
    @Value("${path.onsite_picture_url}")
    private String onsite_picture_url ;
    @Value("${path.recheck_picture_url}")
    private String recheck_picture_url ;
    @Value("${path.fault_picture_url}")
    private String fault_picture_url ;

    @Value("${reviewUrl}")
    private String reviewUrl;
    /**
     * 根据工单状态和维修人员查询工单列表
     * */
    @Override
    public RestResult queryOrderList(Map condition) {
        String status = (String) condition.get("orderStatus");
        List list = new ArrayList();
        if(status.equals("2")){
            list.add(OrderStatusEnum.APPLY_FOR_COMPLETED.getValue());
            list.add(OrderStatusEnum.APPLY_FOR_DELAY.getValue());
            list.add(OrderStatusEnum.APPLY_FOR_CLOSED.getValue());
            list.add(OrderStatusEnum.APPLY_FOR_SUSPENSION.getValue());
            list.add(OrderStatusEnum.DELAYED_LEVEL_ONE.getValue());
            list.add(OrderStatusEnum.PENDING_FORCIBLE_CLOSURE_APPROVAL.getValue());
            list.add(OrderStatusEnum.PENDING_DELAY_APPROVAL.getValue());
            list.add(OrderStatusEnum.SUSPENDED_LEVEL_ONE.getValue());
            list.add(OrderStatusEnum.PENDING_SUSPENSION_APPROVAL.getValue());
            list.add(OrderStatusEnum.PENDING_CLOSURE_APPROVAL.getValue());
        }else if(StringNumConstant.STRING_NUM_1.equals(status)){
            list.add(OrderStatusEnum.TO_BE_MAINTAINED.getValue());
            list.add(OrderStatusEnum.REJECTED_LEVEL_TWO.getValue());
            list.add(OrderStatusEnum.REJECTED_LEVEL_ONE.getValue());
            list.add(OrderStatusEnum.DELAYED_LEVEL_TWO.getValue());
            list.add(OrderStatusEnum.SUSPENDED_LEVEL_TWO.getValue());
        }else if(StringNumConstant.STRING_NUM_3.equals(status)){
            list.add(OrderStatusEnum.COMPLETED.getValue());
            list.add(OrderStatusEnum.CLOSED.getValue());
        }
        condition.put("orderStatus",list);
        int currPage = Integer.parseInt(condition.get(PageResult.CURRPAGE).toString());
        int pageSize = Integer.parseInt(condition.get(PageResult.PAGESIZE).toString());
        condition.put("startLimit",(currPage-1)*pageSize);
        condition.put("endLimit",currPage*pageSize);
        //分页查询
        List<OrderListVO> result = orderMapper.queryOrderList(condition);
        int total = orderMapper.queryOrderListCount(condition);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(result!=null&&result.size()>0){
            for(OrderListVO orderListVO:result){
                if(orderListVO.getDelay()!=null){
                    long delay = Long.parseLong(orderListVO.getDelay());
                    try {
                        Date date = sdf.parse(orderListVO.getCreatTime().toString());
                        long newDate = date.getTime()+delay*60*60*1000;
                        orderListVO.setCreatTime(sdf.format(new Date(newDate)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),PageResult.success(total, currPage, pageSize, result));
    }
    /**
     *根据工单id，查询工单详情
     * */
    @Override
    public OrderDetailVO getOrderInfo(String orderCode) {
        OrderDetailVO orderDetailVO = orderMapper.getOrderInfo(orderCode);
        if(orderDetailVO==null){
            return null;
        }
        //查询反馈信息
        String[] operateType ={HandlePhaseEnum.EXTENSION_SUBMITTED.getValue(),HandlePhaseEnum.HANGUP_SUBMITTED.getValue(),HandlePhaseEnum.CLOSE_SUBMITTED.getValue(),HandlePhaseEnum.REPAIR_COMPLETED_SUBMITTED.getValue()};
        Map map =new HashMap();
        map.put("operateType",operateType);
        map.put("orderCode",orderCode);
        List<LogManagerPO> list= logManagerService.queryForback(map);
        if(list!=null&&list.size()>0){
            LogManagerPO logManagerPO = list.get(0);
            orderDetailVO.setForbackDetails(logManagerPO.getDetails());
        }
        //查询报警编号列表
        //TODO
        List<String> alarmList = alarmService.queryAlarmCode(orderCode);
        orderDetailVO.setAlarmList(alarmList);
        String faultPic = orderDetailVO.getFaultPictureUrl();
        String onsitePic= orderDetailVO.getOnsitePictureUrl();
        String recheckPic = orderDetailVO.getRecheckPictureUrl();
        if(!StringUtils.isNullOrEmpty(faultPic)){
            String[] str = faultPic.split(";");
            List<String> faultPicList = new ArrayList<>();
            for (String s:str){
                //s=fault_picture_url+s;
                faultPicList.add(s);
            }
            orderDetailVO.setFaultPictureUrlList(faultPicList);
        }
        if(!StringUtils.isNullOrEmpty(onsitePic)){
            String str[] = onsitePic.split(";");
            List<String> onsitePicList = new ArrayList<>();
            for (String s:str){
                //s=onsite_picture_url+s;
                onsitePicList.add(s);
            }
            orderDetailVO.setOnsitePictureUrlList(onsitePicList);
        }
        if(!StringUtils.isNullOrEmpty(recheckPic)){
            String str[] = recheckPic.split(";");
            List<String> recheckPicList = new ArrayList<>();
            for (String s:str){
                //s=recheck_picture_url+s;
                recheckPicList.add(s);
            }
            orderDetailVO.setRecheckPictureUrlList(recheckPicList);
        }
        //延期时间处理
        if(orderDetailVO.getDelay()!=0){
            int delay = orderDetailVO.getDelay();
            long newDate = orderDetailVO.getDemandTime().getTime()+delay*60*60*1000;
            orderDetailVO.setDemandTime(new Date(newDate));
        }

        return orderDetailVO;
    }
    /**
     * 工单管理页面接口，查询待做、进行中、已完成工单的数量
     * */
    @Override
    public List getCountByStatus(String userId) {
        List<Map> list = orderMapper.getCountByStatus(userId);
        List<Map> resultList = new ArrayList<>();
        long processCount = 0;
        long finishCount = 0;
        long toBeMaintainedCount = 0;
        Map waitMap = new HashMap();
        waitMap.put("order_status", StringNumConstant.STRING_NUM_1);
        waitMap.put("count",0);
        Map processMap = new HashMap();
        processMap.put("order_status", StringNumConstant.STRING_NUM_2);
        processMap.put("count",0);
        Map finishMap = new HashMap();
        finishMap.put("order_status", StringNumConstant.STRING_NUM_3);
        finishMap.put("count",0);
        if(list!=null&&list.size()>0){
            for (Map map:list){
                String status = (String) map.get("order_status");
                if(OrderStatusEnum.TO_BE_MAINTAINED.getValue().equals(status)||
                        OrderStatusEnum.REJECTED_LEVEL_TWO.getValue().equals(status)||
                        OrderStatusEnum.REJECTED_LEVEL_ONE.getValue().equals(status)||
                        OrderStatusEnum.DELAYED_LEVEL_TWO.getValue().equals(status)||
                        OrderStatusEnum.SUSPENDED_LEVEL_TWO.getValue().equals(status)){
                    long count = (long) map.get("count");
                    toBeMaintainedCount= toBeMaintainedCount+count;
                    waitMap.put("order_status", StringNumConstant.STRING_NUM_1);
                    waitMap.put("count",toBeMaintainedCount);
                }else if(OrderStatusEnum.COMPLETED.getValue().equals(status)||
                        OrderStatusEnum.CLOSED.getValue().equals(status)){
                    long count = (long) map.get("count");
                    finishCount= finishCount+count;
                    finishMap.put("order_status", StringNumConstant.STRING_NUM_3);
                    finishMap.put("count",finishCount);
                }else{
                    long count = (long) map.get("count");
                    processCount =processCount+count;
                    processMap.put("order_status", StringNumConstant.STRING_NUM_2);
                    processMap.put("count",processCount);
                }
            }
        }
        resultList.add(waitMap);
        resultList.add(processMap);
        resultList.add(finishMap);
        return resultList;
    }

    /**
     * 工单维修操作
     *
     * @return*/
    @Override
    @Transactional
    public RestResult updateRepair(MultipartFile[] files, RepairVo repairVo,String userName,String recheck,String token){
         if(repairVo==null){
             return RestResult.generateRestResult(AppResultEnum.PARAM_IS_NULL.getCode(),AppResultEnum.PARAM_IS_NULL.getMessage(),null);
         }
         if(org.apache.commons.lang3.StringUtils.isEmpty(repairVo.getForbackContent())){
             return RestResult.generateRestResult(AppResultEnum.FORBACKCONTENT_IS_NULL.getCode(),AppResultEnum.FORBACKCONTENT_IS_NULL.getMessage(),null);
         }
         if(files==null||files.length==0){
             return RestResult.generateRestResult(AppResultEnum.PICTURE_IS_NULL.getCode(),AppResultEnum.PICTURE_IS_NULL.getMessage(),null);
         }

        //TODO,判断当前订单状态是否能执行当前操作
        String orderCode = repairVo.getOrderCode();
        if(StringUtils.isNullOrEmpty(orderCode)){
            return RestResult.generateRestResult(AppResultEnum.ERROE.getCode(),AppResultEnum.ERROE.getMessage(),null);
        }
        String operateType = repairVo.getOperateType();
        String orderStatus = orderMapper.findByOrderCode(orderCode);
        if(StringUtils.isNullOrEmpty(orderStatus)){
            return RestResult.generateRestResult(AppResultEnum.ERROE.getCode(),AppResultEnum.ERROE.getMessage(),null);
        }
        if(!orderStatus.equals(OrderStatusEnum.TO_BE_MAINTAINED.getValue())&&
                !orderStatus.equals(OrderStatusEnum.REJECTED_LEVEL_ONE.getValue())&&
                !orderStatus.equals(OrderStatusEnum.DELAYED_LEVEL_TWO.getValue())&&
                !orderStatus.equals(OrderStatusEnum.SUSPENDED_LEVEL_TWO.getValue())&&
                !orderStatus.equals(OrderStatusEnum.REJECTED_LEVEL_TWO.getValue())){
            return RestResult.generateRestResult(AppResultEnum.NOT_SUPPORT_OPERTION.getCode(), AppResultEnum.NOT_SUPPORT_OPERTION.getMessage(),null);
        }
        log.info("=======复检开始："+recheck);
        //复检
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(recheck)&&recheck.equals(StringNumConstant.STRING_NUM_1)){
            boolean flag=checkParamForRecheck(orderCode);
            if (!flag) {
                log.error("=======复检校验失败");
                return RestResult.generateRestResult(AppResultEnum.REVIEW_IS_FAULT.getCode(), AppResultEnum.REVIEW_IS_FAULT.getMessage(),null);
            }
            //判断工单是否可以复检
            //TODO
            List<String> alarmCodeList = alarmService.queryAlarmCode(orderCode);
            if(alarmCodeList!=null&&alarmCodeList.size()>0){
                String alarmCode = alarmCodeList.get(0);
                RestResult restResult = alarmService.queryAlarmInfo(alarmCode,orderCode);
                if(restResult.getCode()==AppResultEnum.SUCCESS.getCode()){
                    AlarmPO alarmPO = (AlarmPO) restResult.getData();
                    //[{"cameraId":"03902423291316412249","faultType":"015","monitorType":2,"taskId":"28_1_mutiptzimgtaskinfo_1599799997835_1"}]
                    List data = new ArrayList();
                    JSONObject json = new JSONObject();
                    json.put("cameraId",alarmPO.getCameraId());
                    json.put("faultTypes",alarmPO.getFaultTypeId());
                    //json.put("monitorType",alarmPO.getMonitorTypeId());
                   // json.put("taskId",alarmPO.getTaskId());
                    json.put("orderCode",orderCode);
                    data.add(json);
                    HttpClientUtil clientUtil = new HttpClientUtil();
                    Map condition = new HashMap();
                    condition.put("data",data.toString());
                    condition.put("token",token);
                    log.info("=======复检请求参数："+condition.toString());
                    String str = clientUtil.doPostVi(reviewUrl,condition,"UTF-8",true);
                    if(org.apache.commons.lang3.StringUtils.isEmpty(str)||str.equals("isLoginFlag")){
                        return RestResult.generateRestResult(AppResultEnum.TOKEN_TIME_OUT.getCode(),AppResultEnum.TOKEN_TIME_OUT.getMessage(),null);
                    }
                    log.info("=======复检请求返回值："+str);
                    JSONObject jsonObject = JSONObject.parseObject(str);
                    if(jsonObject!=null&&jsonObject.get("code")!=null&&jsonObject.getInteger("code")==0){
                        log.info("=======复检成功"+str);
                    }else{
                        return RestResult.generateRestResult(AppResultEnum.REVIEW_IS_FAULT.getCode(),AppResultEnum.REVIEW_IS_FAULT.getMessage(),null);
                    }
                    log.info("=======复检请求返回值："+str);
                }else{
                    log.info("=======复检：复检失败，获取报警信息失败,"+orderCode);
                    return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.REVIEW_IS_FAULT.getMessage(),null);
                }
            }else{
                log.info("=======复检：复检失败，获取报警编号失败,"+orderCode);
                return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.REVIEW_IS_FAULT.getMessage(),null);
            }
        }
        log.info("=======复检结束");


        //上传图片
        String path = imagesPath;
        File picFile = Paths.get(path).toFile();
        if (!picFile.exists()) {
            picFile.mkdirs();
        }
        List resultList = new ArrayList();
        StringBuffer allPath = new StringBuffer();
        if(files!=null&&files.length>0){
            for (MultipartFile multipartFile:files){
                //1、上传的图像文件重新命名
                String originFileName = multipartFile.getOriginalFilename();
                String fileName = UUID.randomUUID().toString().replace("-","")
                        + originFileName.substring(originFileName.lastIndexOf("."));
                String picRelativePath =fileName;
                File dir = new File(path, fileName);
                File filepath = new File(path);
                if(!filepath.exists()){
                    filepath.mkdirs();
                }
                try {
                    multipartFile.transferTo(dir);
                    Map<String, String> map = new HashMap<>();
                    map.put("filePath", path);
                    map.put("fileName", fileName);
                    resultList.add(map);
                    String converFile = onsite_picture_url+fileName;
                    if(allPath!=null&&allPath.length()>0){
                        allPath.append(";").append(converFile);
                    }else{
                        allPath.append(converFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //将图片信息保存到数据库
        WorkOrderPicturePO orderPicture = new WorkOrderPicturePO();
        orderPicture.setOrderCode(repairVo.getOrderCode());
        orderPicture.setOnsitePictureUrl(allPath.toString());
        workOrderPictureService.updateOrderPic(orderPicture);

        //更新工单状态
        OrderEntity order = new OrderEntity();
        order.setOrderStatus(OrderStatusEnum.APPLY_FOR_COMPLETED.getValue());
        order.setOrderCode(repairVo.getOrderCode());
        orderMapper.updateOrderStatus(order);
        //更新维修信息
        MaintenanceInfoEntity maintenanceInfoEntity = new MaintenanceInfoEntity();
        maintenanceInfoEntity.setFeedbackTime(new Timestamp(new Date().getTime()));
        maintenanceInfoEntity.setDoneTime(new Timestamp(new Date().getTime()));
        maintenanceInfoEntity.setOrderCode(repairVo.getOrderCode());
        maintenanceInfoService.updateRepairInfo(maintenanceInfoEntity);
        //更新日志
        LogManagerPO logManagerPO = new LogManagerPO();
        logManagerPO.setOperateName(userName);
        logManagerPO.setDatetime(new Date());
        logManagerPO.setOperateType(HandlePhaseEnum.REPAIR_COMPLETED_SUBMITTED.getValue());
        logManagerPO.setCode(repairVo.getOrderCode());
        logManagerPO.setDetails(repairVo.getForbackContent());
        logManagerPO.setLogType(LogTypeEnum.WORK_ORDER_LOG.getStyle());
        logManagerService.insertLog(logManagerPO);

        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),null);
    }
    /**
     * 校验复检工单是否存在手动建单和复检中
     * @return boolean 是否通过校验
     */
    private boolean checkParamForRecheck(String orderCode) {
        OrderDetailVO orderDetailVO = orderMapper.getOrderInfo(orderCode);
        if (orderDetailVO==null) {
            return false;
        }
        // 手动建单和复检中的工单不能发送复检任务
        boolean checkResult = orderDetailVO.getCreationType().equals("手动创建")
                || orderDetailVO.getReviewStatus().equals("复检中")||orderDetailVO.getReviewStatus().equals("已复检");
        return !checkResult;
    }
    @Override
    @Transactional
    public RestResult updateRepairFeedBack(OrderOperateVo operateVo, String userName) {
        if (operateVo==null) {
            return RestResult.generateRestResult(AppResultEnum.PARAM_IS_NULL.getCode(), AppResultEnum.PARAM_IS_NULL.getMessage(), null);
        }
        if (StringUtils.isNullOrEmpty(operateVo.getOrderCode())) {
            return RestResult.generateRestResult(AppResultEnum.ORDERCODE_PARAM_IS_NULL.getCode(), AppResultEnum.ORDERCODE_PARAM_IS_NULL.getMessage(), null);
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(operateVo.getOperateContent())){
            return RestResult.generateRestResult(AppResultEnum.FORBACKCONTENT_IS_NULL.getCode(),AppResultEnum.FORBACKCONTENT_IS_NULL.getMessage(),null);
        }
        String orderStatus = orderMapper.findByOrderCode(operateVo.getOrderCode());
        if(StringUtils.isNullOrEmpty(orderStatus)){
            return RestResult.generateRestResult(AppResultEnum.DATA_NOT_EXIT.getCode(),AppResultEnum.DATA_NOT_EXIT.getMessage(),null);
        }
        // 校验状态和操作是否匹配
        if (!checkStatus(operateVo)) {
            return RestResult.generateRestResult(AppResultEnum.NOT_SUPPORT_OPERTION.getCode(), AppResultEnum.NOT_SUPPORT_OPERTION.getMessage(), null);
        }
        List<OrderEntity> workOrderList = new ArrayList<>(); // for feedback time
        MaintenanceInfoEntity maintenanceInfoEntity = new MaintenanceInfoEntity();
        maintenanceInfoEntity.setOrderCode(operateVo.getOrderCode());
        // 申请完成时，更新实际完成时间，判断是否超时
        if (operateVo.getOperateType().equals(OrderStatusEnum.APPLY_FOR_COMPLETED.getValue())) {
            // 实际完成时间
            maintenanceInfoEntity.setDoneTime(new Timestamp(new Date().getTime()));
            //TODO，判断是否超时
            maintenanceInfoEntity.setTimeout((new Timestamp(new Date().getTime())).after(maintenanceInfoService.selectByOrderCode(operateVo.getOrderCode()).getDemandTime()));
        }
        //申请延期
        if (operateVo.getOperateType().equals(OrderStatusEnum.APPLY_FOR_DELAY.getValue())) {
            int delay =12;//默认延长时间为12小时
            if(operateVo.getDelayTime()!=null){
                Timestamp delayTime = Timestamp.valueOf(operateVo.getDelayTime());
                MaintenanceInfoEntity maintenanceInfoEntity1 = maintenanceInfoService.selectByOrderCode(operateVo.getOrderCode());
                Timestamp demandTime = maintenanceInfoEntity1.getDemandTime();
                //计算延长时间差
                delay = getTimeDifference(delayTime,demandTime);
                if(delay<=0){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(demandTime.getTime());
                    String dateString = dateFormat.format(date);
                    return RestResult.generateRestResult(AppResultEnum.DELAY_TIME_SHORT.getCode(),MessageFormat.format(AppResultEnum.DELAY_TIME_SHORT.getMessage(),dateString),null);
                }
            }
            maintenanceInfoEntity.setDelay(delay);
        }
        OrderEntity workOrder = new OrderEntity();
        workOrder.setOrderCode(operateVo.getOrderCode());
        workOrder.setOrderStatus(operateVo.getOperateType());
        //workOrder.setFeedbackTime(new Date());
        orderMapper.updateOrderStatus(workOrder);
        maintenanceInfoEntity.setFeedbackTime(new Timestamp(new Date().getTime()));
        maintenanceInfoService.updateByOrderCode(maintenanceInfoEntity);
        // log 向tbl_log_manager表中添加反馈信息
        String operateType = getOperateType(operateVo.getOperateType());
        LogManagerPO logManagerPO = new LogManagerPO();
        logManagerPO.setOperateName(userName);
        logManagerPO.setDatetime(new Date());
        logManagerPO.setOperateType(operateType);
        logManagerPO.setCode(operateVo.getOrderCode());
        logManagerPO.setDetails(operateVo.getOperateContent());
        logManagerPO.setLogType(LogTypeEnum.WORK_ORDER_LOG.getStyle());
        logManagerService.insertLog(logManagerPO);
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),null);
    }
    public  int getTimeDifference(Timestamp formatTime1, Timestamp formatTime2) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        long t1 = formatTime1.getTime();
        long t2 = formatTime2.getTime();
        int hours=(int) ((t1 - t2)/(1000*60*60));
        return hours;
    }
    private boolean checkStatus(OrderOperateVo operateVo) {
//        // 查询用户选择工单对应的所有工单状态
//        List<String> orderStatusList = orderMapper.selectStatusByOrderCode(operateVo.getOrderCode());
//
//        String feedbackType = operateVo.getOperateType();
//
//        // 与反馈操作匹配的工单状态
//        Map<String, String> orderStatusMap = new HashMap<>();
//        orderStatusMap.put(OrderStatusEnum.TO_BE_MAINTAINED.getValue(), "");
//
//        // 当前状态全为待维修时，可以进行申请延期、申请挂起操作
//        if (OrderStatusEnum.APPLY_FOR_DELAY.getValue().equals(feedbackType)
//                || OrderStatusEnum.APPLY_FOR_SUSPENSION.getValue().equals(feedbackType)) {
//            return orderStatusList.stream().allMatch(status -> "".equals(orderStatusMap.get(status)));
//        }
//        orderStatusMap.put(OrderStatusEnum.DELAYED_LEVEL_TWO.getValue(), "");
//        orderStatusMap.put(OrderStatusEnum.SUSPENDED_LEVEL_TWO.getValue(), "");
//
//        // 当前状态全为待维修/已延期（二级）/已挂起（二级）时，可以进行申请关闭操作
//        if (OrderStatusEnum.APPLY_FOR_CLOSED.getValue().equals(feedbackType)) {
//            return orderStatusList.stream().allMatch(status -> "".equals(orderStatusMap.get(status)));
//        }
//        orderStatusMap.put(OrderStatusEnum.REJECTED_LEVEL_TWO.getValue(), "");
//
//        // 当前状态全为待维修/已延期（二级）/已挂起（二级）/已驳回（二级）时，可以进行申请完成操作
//        if (OrderStatusEnum.APPLY_FOR_COMPLETED.getValue().equals(feedbackType)) {
//            return orderStatusList.stream().allMatch(status -> "".equals(orderStatusMap.get(status)));
//        }

        //去日志中查询
        String[] operateType ={HandlePhaseEnum.REPAIR_COMPLETED_FINAL_REVIEW_REJECT.getValue(),
                HandlePhaseEnum.CLOSE_FINAL_REVIEW_REJECT.getValue(),
                HandlePhaseEnum.HANGUP_FINAL_REVIEW_REJECT.getValue(),
                HandlePhaseEnum.EXTENSION_FINAL_REVIEW_REJECT.getValue()};
        Map map =new HashMap();
        map.put("operateType",operateType);
        map.put("orderCode",operateVo.getOrderCode());
        List<LogManagerPO> logManagerPOList = logManagerService.queryForback(map);
        if(logManagerPOList!=null&&logManagerPOList.size()>0){
            String feedbackType = operateVo.getOperateType();
            if(OrderStatusEnum.APPLY_FOR_DELAY.getValue().equals(feedbackType)
                    || OrderStatusEnum.APPLY_FOR_SUSPENSION.getValue().equals(feedbackType)
                    ||OrderStatusEnum.APPLY_FOR_CLOSED.getValue().equals(feedbackType)){
                return false;
            }
        }
        return true;
    }
    private String getOperateType(String type) {
        String operateType = HandlePhaseEnum.CLOSE_SUBMITTED.getValue();
        if (type.equals(OrderStatusEnum.APPLY_FOR_COMPLETED.getValue())) {
            operateType = HandlePhaseEnum.REPAIR_COMPLETED_SUBMITTED.getValue();
        } else if (type.equals(OrderStatusEnum.APPLY_FOR_DELAY.getValue())) {
            operateType = HandlePhaseEnum.EXTENSION_SUBMITTED.getValue();
        } else if (type.equals(OrderStatusEnum.APPLY_FOR_SUSPENSION.getValue())) {
            operateType = HandlePhaseEnum.HANGUP_SUBMITTED.getValue();
        }
        return operateType;
    }
}
