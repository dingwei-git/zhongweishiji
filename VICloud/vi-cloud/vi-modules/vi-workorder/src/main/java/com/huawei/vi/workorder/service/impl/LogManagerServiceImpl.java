package com.huawei.vi.workorder.service.impl;

import com.huawei.vi.entity.po.LogManagerPO;
import com.huawei.vi.entity.po.UserPO;
import com.huawei.vi.workorder.mapper.LogManagerMapper;
import com.huawei.vi.workorder.mapper.UserMapper;
import com.huawei.vi.workorder.service.LogManagerService;
import com.jovision.jaws.common.constant.LogOperateTypeEnum;
import com.jovision.jaws.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogManagerServiceImpl implements LogManagerService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LogManagerMapper logManagerMapper;
    @Override
    public List queryLog(String orderCode) {
        List<LogManagerPO> logList = logManagerMapper.queryLog(orderCode);
        List<String> listStatus = LogOperateTypeEnum.toList();
        List resultList = convertToFormat(logList);
        return resultList;
    }
    @Override
    public int insertLog(LogManagerPO logManagerPO) {
        return logManagerMapper.insertLog(logManagerPO);
    }
    @Override
    public List<LogManagerPO> queryForback(Map map) {
        return logManagerMapper.queryForback(map);
    }
    public List convertToFormat(List<LogManagerPO> logList){
        List resultList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (LogManagerPO logManagerPO:logList){
            StringBuffer str = new StringBuffer();
            switch(logManagerPO.getOperateType()){
                case "工单创建完成":
                    str.append("工单单号").append(logManagerPO.getCode()).append("由").append(logManagerPO.getOperateName()).append("创建.");
                    break;
                case "建单审核通过":
                    str.append(logManagerPO.getOperateName()).append(logManagerPO.getOperateType());
                    break;
                case "工单已派发":
                    //根据id查询用户信息
                    if(StringUtils.isNotEmpty(logManagerPO.getAssign())){
                        UserPO userPO = userMapper.getInfo(logManagerPO.getAssign());
                        if(userPO!=null){
                            str.append(logManagerPO.getOperateName()).append("将工单派发给").append(userPO.getUserTblName());
                        }else{
                            str.append(logManagerPO.getOperateName()).append("将工单派发给").append("");
                        }
                    }else{
                        str.append(logManagerPO.getOperateName()).append("将工单派发给").append("");
                    }
                    break;
                case "工单已接收":
                    str.append(logManagerPO.getOperateName()).append("接收了工单");
                    break;
                case "工单已分发":
                    //根据id查询用户信息
                    if(StringUtils.isNotEmpty(logManagerPO.getAssign())){
                        UserPO userPO = userMapper.getInfo(logManagerPO.getAssign());
                        if(userPO!=null){
                            str.append(logManagerPO.getOperateName()).append("将工单分发给了").append(userPO.getUserTblName());
                        }else{
                            str.append(logManagerPO.getOperateName()).append("将工单分发给了").append("");
                        }
                    }else{
                        str.append(logManagerPO.getOperateName()).append("将工单分发给了").append("");
                    }
                    break;
                default :
                    str.append(logManagerPO.getOperateName()).append(logManagerPO.getOperateType());
                    break;
            }
            Map map= new HashMap();
            map.put("logContent",str.toString());

            map.put("datetime",sdf.format(logManagerPO.getDatetime()));
            resultList.add(map);
        }
        return resultList;
        }

    }
//    CREATE_FINISH_ORDER("工单创建完成"),
//    CREATE_APPROVAL("建单审核通过"),
//    ORDER_DISPATCHED("工单已派发"),
//    ORDER_ACCEPTED("工单已接收"),
//    ORDER_DISTRIBUTED("工单已分发"),
//    APPLY_DELAYED_COMMIT("延期申请已提交"),
//    APPLY_DELAYED_REJECTED("延期申请初审驳回"),
//    APPLY_DELAYED_PROCESS("延期申请初审通过"),
//    MAINTAINED_FINISH_COMMIT("维修完成申请已提交"),
//    MAINTAINED_FIRST_APPROVAL("维修完成申请初审通过"),
//    MAINTAINED_FINALLY_APPROVAL("维修完成终审通过");


