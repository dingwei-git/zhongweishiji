package com.huawei.vi.thirddata.controller;

import com.huawei.vi.entity.vo.ImageCountVO;
import com.huawei.vi.thirddata.service.intelligent.IntelligentTraceService;
import com.jovision.jaws.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class IntelligentTraceController {

    @Autowired
    IntelligentTraceService intelligentTraceService;

    /*
    * 图片溯源按组织查询
    * */
    @RequestMapping(value = "/getIntelligent.do",method = {RequestMethod.POST})
    public RestResult intelligentTrace(@RequestBody ImageCountVO imageCountVO){
         RestResult restResult = intelligentTraceService.intelligentTrace(imageCountVO);
        return restResult;
    }

    /*
    * 图片溯源按摄像机查询
    * */
    @RequestMapping(value = "/getIntelligentByCamera.do",method = {RequestMethod.POST})
    public RestResult intelligentTraceByCamera(@RequestBody ImageCountVO imageCountVO){
        RestResult restResult = intelligentTraceService.intelligentTraceByCamera(imageCountVO);
        return restResult;
    }


    /*
    * 图片溯源按组织查询分页
    * */
    @RequestMapping(value = "/getPageIntelligent.do",method = {RequestMethod.POST})
    public RestResult intelligentTracePage(@RequestBody ImageCountVO imageCountVO){
        RestResult restResult = intelligentTraceService.intelligentTracePage(imageCountVO);
        return restResult;
    }

    /*
    * 图片溯源按摄像机查询分页
    * */
    @RequestMapping(value = "/getPageIntelligentByCamera.do",method = {RequestMethod.POST})
    public RestResult intelligentTraceByCameraPage(@RequestBody ImageCountVO imageCountVO){
        RestResult restResult = intelligentTraceService.intelligentTraceByCameraPage(imageCountVO);
        return restResult;
    }


    /**
     * 元素纬度查询
     * @param
     * @return RestResult
     */
    @RequestMapping(value = "/gitintelligentTrace.do",method = RequestMethod.POST)
    public RestResult gitintelligentTrace(){
        RestResult restResult = intelligentTraceService.gitintelligentTraceList();
        return restResult;
    }

    /*
    * 网元维度图片溯源按天统计
    * */
    @RequestMapping(value = "/getNetWorkTrace.do",method={RequestMethod.POST})
    public RestResult netWorkTrace(@RequestBody ImageCountVO imageCountVO){
        RestResult restResult = intelligentTraceService.getNetWorkTrace(imageCountVO);
        return restResult;
    }

    /*
    * 网元维度图片溯源按小时统计
    * */
    @RequestMapping(value = "/getNetWorkTraceByHour.do",method = {RequestMethod.POST})
    public RestResult netWorkTraceByHour(@RequestBody ImageCountVO imageCountVO){
        RestResult restResult = intelligentTraceService.getNetWorkTraceByHour(imageCountVO);
        return restResult;
    }


}
