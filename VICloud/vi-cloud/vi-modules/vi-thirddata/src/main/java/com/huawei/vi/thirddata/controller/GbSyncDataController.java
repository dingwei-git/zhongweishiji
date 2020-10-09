package com.huawei.vi.thirddata.controller;

import com.huawei.vi.entity.model.RestResult;
import com.huawei.vi.entity.model.ResultCode;
import com.huawei.vi.entity.po.GBsyncData;
import com.huawei.vi.thirddata.mapper.TblServerParamConfigMapper;
import com.huawei.vi.thirddata.service.GBsyncDataService;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.controller.BaseController;
import com.jovision.jaws.common.util.NumConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName : GbSyncDataController
 * @Description : 同步国标设备信息
 * @Author : pangxh
 * @Date: 2020-08-13 20:48
 */
@Slf4j
@RestController
@RequestMapping("/thirddata/gbsyncdata")
public class GbSyncDataController extends BaseController {

    @Value("${gbsyncdata.ip}")
    private String ip;

    @Value("${gbsyncdata.port}")
    private int port;

    @Value("${gbsyncdata.platformId}")
    private String platformId;


    @Autowired
    private GBsyncDataService gBsyncDataService;

    @Autowired
    private TblServerParamConfigMapper tblServerParamConfigMapper;

    /**
     * 功能描述: 同步GB28181设备信息
     *
     * @Param: [gBsyncData]
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/13 21:01
     */
    @PostMapping("/syncdata")
    public RestResult syncData() {
        try {
            //todo 获取dz的ip
            Map<String,Object> param = new HashMap<>();
            //todo 获取dz的ip
            param.put("serviceFlag","2");
            param.put("serviceSubType","2");
            param.put("serviceSubId","500001");
            List<Map<String, Object>> iplist = tblServerParamConfigMapper.selectIpOrPlatformid(param);
            log.info("GbSyncDataController===syncData->iplist{}",iplist);

            //todo 获取dz的platformid
            param.put("serviceFlag","2");
            param.put("serviceSubType","3");
            param.put("serviceSubId","600001");
            List<Map<String, Object>> platformidlist = tblServerParamConfigMapper.selectIpOrPlatformid(param);
            log.info("GbSyncDataController===syncData->platformidlist{}",platformidlist);
            if(iplist == null||iplist.size()==0){
                return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "没有同步到设备",null);
            }

            GBsyncData gBsyncData = new GBsyncData(String.valueOf(iplist.get(0).get("ip")), port, String.valueOf(platformidlist.get(0).get("platformid")));
            return gBsyncDataService.syncDataAndReturnDealInfo(gBsyncData);
        } catch (Exception e) {
            log.error("syncdata====exception:", e);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败",null);
        }

    }

    /**
     * 功能描述: 功能描述: 同步设备的列表
     *
     * @Param: [taskid, rows, offset, status]
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/20 10:22
     */
    @PostMapping("/syncdatagrid")
    public RestResult syncdatagrid(@RequestParam(CommonConst.DEVICE_SYNCDATA_TASKID) String taskid,
                                   @RequestParam(CommonConst.DEVICE_SYNCDATA_PAGE_ROWS) String rows,
                                   @RequestParam(CommonConst.DEVICE_SYNCDATA_PAGE_OFFSET) String offset,
                                   @RequestParam(value = CommonConst.DEVICE_SYNCDATA_ADD, required = false) String status
    ) {
        RestResult restResult = null;

        try {
            String stat = StringUtils.isNotEmpty(status) ? status : CommonConst.DEVICE_SYNCDATA_ADD;
            return gBsyncDataService.SyncDataDataGrid(taskid, offset, rows, stat);
        }catch (Exception e){
            log.error("syncdatagrid====exception:", e);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败",null);
        }
    }

    /**
     * 功能描述: 一键补缺
     *
     * @Param: [taskid]
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/20 10:25
     */
    @PostMapping("/onekeyfillthegap")
    public RestResult oneKeyFillTheGap(@RequestParam(CommonConst.DEVICE_SYNCDATA_TASKID) String taskid) {
        try {
            return gBsyncDataService.oneKeyFillTheGap(taskid);
        } catch (Exception e) {
            log.error("oneKeyFillTheGap====exception:", e);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败", null);
        }
    }


    /**
     * 功能描述: 弹窗编辑组织后保存
     *
     * @Param: [data] Map 中的key[taskid(任务id),cameraid(摄像机编码),currentlevels(level:levelid#level2:level2id)]
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/20 16:00
     */
    @PostMapping("/editLevels")
    public RestResult editLevels(@RequestBody List<Map<String, String>> data) {

        try {
            return gBsyncDataService.editLevels(data);
        } catch (Exception e) {
            log.error("editLevels====exception:", e);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败", null);
        }
    }


    /**
     * 功能描述: 获取最后一级层级和对应的设备
     *
     * @Param: [taskid]
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/20 18:06
     */
    @PostMapping("/getipcwithlatestlevel")
    public RestResult getIpcWithLatestLevel(@RequestParam(CommonConst.DEVICE_SYNCDATA_TASKID) String taskid) {
        try {
            return gBsyncDataService.getIpcWithLatestLevel(taskid);
        } catch (Exception e) {
            log.error("getipcwithlatestlevel====exception:", e);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败", null);
        }
    }

    /**
     * 功能描述: 最终确认按钮
     *
     * @Param: [taskid]
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/22 11:33
     */
    @PostMapping("/comfiroperator")
    public RestResult comfirOperator(@RequestParam(CommonConst.DEVICE_SYNCDATA_TASKID) String taskid) {
        try {
            return gBsyncDataService.comfirOperator(taskid,getToken(),getUserId(),getUserName());
        } catch (Exception e) {
            log.error("comfirOperator====exception:", e);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败", null);
        }
    }

    /**
     * 功能描述: 国际化标题接口
     * @Param: []
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/22 18:16
     */
    @PostMapping("/internationalizationtitle")
    public RestResult internationalizationtitle() {
        try {
            return gBsyncDataService.internationalizationTitle();
        } catch (Exception e) {
            log.error("internationalizationtitle====exception:", e);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败", null);
        }
    }


    /**
     * 功能描述: ipc列表
     * @Param: [levelCount, levelid, rows, offset]
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/24 15:58
     */
    @PostMapping("/ipcipdatagird")
    public RestResult ipcipDatagird(
                                    @RequestParam(value = "levelCount",required = false) Integer levelCount,
                                    @RequestParam(value=CommonConst.DEVICE_SYNCDATA_LEVELID,required = false) String levelid,
                                    @RequestParam(CommonConst.DEVICE_SYNCDATA_PAGE_ROWS) String rows,
                                    @RequestParam(CommonConst.DEVICE_SYNCDATA_PAGE_OFFSET) String offset){

        try {
            return gBsyncDataService.ipcipdatagird(levelCount,levelid,offset,rows);
        } catch (Exception e) {
            log.error("ipcipDatagird====exception:", e);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败",null);
        }
    }


    /**
     * 功能描述: 修改和删除设备
     * @Param: [param]
     * @Return: com.huawei.vi.entity.model.RestResult
     * @Author: pangxh
     * @Date: 2020/8/24 16:08
     */
    @PostMapping("/editipc")
    public RestResult editipc(@RequestBody Map<String,Object> param){
        try {
            return gBsyncDataService.editipc(param);
        } catch (Exception e) {
            log.error("ipcipDatagird====exception:", e);
            return RestResult.generateRestResult(NumConstant.NUM_1_NEGATIVE, "操作失败", null);
        }
    }
}
