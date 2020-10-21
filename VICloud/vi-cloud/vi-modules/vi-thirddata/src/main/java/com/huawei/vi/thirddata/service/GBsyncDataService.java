package com.huawei.vi.thirddata.service;

import com.alibaba.fastjson.JSONObject;
import com.huawei.vi.entity.model.RestResult;
import com.huawei.vi.entity.po.GBsyncData;

import java.util.List;
import java.util.Map;

public interface GBsyncDataService {

    List<JSONObject> syncData(GBsyncData gBsyncData);

    RestResult syncDataAndReturnDealInfo(GBsyncData gBsyncData);

    RestResult SyncDataDataGrid(String taskid,String offset,String rows,String status);

    RestResult oneKeyFillTheGap(String taskid);

    RestResult  editLevels(List<Map<String,String>> data);

    RestResult  getIpcWithLatestLevel(String taskid);

    RestResult comfirOperator(String taskid,String token,String userId,String userName);

    RestResult internationalizationTitle();

    RestResult ipcipdatagird(Integer levelCount,String levelid,String offset,String rows,String condition);

    RestResult editipc(Map<String,Object> param);
}
