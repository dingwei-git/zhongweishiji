package com.huawei.vi.thirddata.controller;

import com.huawei.vi.entity.vo.DossierVo;
import com.huawei.vi.thirddata.service.statistics.StatisticsService;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.controller.BaseController;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.ServiceCommonConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class StatisticsController extends BaseController {

    @Autowired
    StatisticsService statisticsService;

    /*
    * 档案统计
    * */
    @RequestMapping(value = "/statisticsDossier.do",method = {RequestMethod.POST})
    public RestResult statisticsDossier(DossierVo dossierVo){
        RestResult restResult = statisticsService.getStatisticsDossier(dossierVo);
        return restResult;
    }


    /*
    * 联动级联接口
    * */
    @RequestMapping(value = "/getLinkageCascade.do", method = RequestMethod.POST)
    @ResponseBody
    public com.huawei.cn.components.common.RestResult getLinkageCascade(HttpServletRequest request){
        String userName = getUserName();
        Map<String, Object> returnMap = statisticsService.queryPublicInfo(userName, CommonConst.TBL_FAULT_STATISTICAL_FOR_RM);
        return com.huawei.cn.components.common.RestResult.generateRestResult(ServiceCommonConst.CODE_SUCCESS,"联动级联效果",returnMap);

    }

    /*
    * 档案统计获取表头
    * */
    @RequestMapping(value = "/getTableHeader.do", method = RequestMethod.POST)
    @ResponseBody
    public RestResult getTableHeader(String leves ){
        RestResult restResult = statisticsService.getTableHeader(leves);
        return restResult;
    }


    /*
    * 档案统计导出
    * */
    @ResponseBody
    @RequestMapping(value = "/dossierExport.do",method = {RequestMethod.POST})
    public RestResult dossierExportExcel(DossierVo dossierVo, HttpServletResponse response){
       RestResult restResult = statisticsService.dossierExport(dossierVo,response);
       return restResult;
    }





}
