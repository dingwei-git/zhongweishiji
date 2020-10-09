package com.huawei.vi.thirddata.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.vi.entity.po.TblIpcIp;
import com.huawei.vi.entity.vo.ImageDayStatisticsVcnVO;
import com.huawei.vi.thirddata.mapper.TblImageDayStatisticsVcnMapper;
import com.huawei.vi.thirddata.mapper.TblIpcIpMapper;
import com.huawei.vi.thirddata.mapper.TblServerParamConfigMapper;
import com.huawei.vi.thirddata.service.MonitorService;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.util.HttpClientUtil;
import com.jovision.jaws.common.util.NumConstant;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.ServiceCommonConst;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huawei.vi.thirddata.service.ServiceCommonConst.HTTPS;

@Service
public class MonitorServiceImpl implements MonitorService {


    private HttpClientUtil httpClientUtil = new HttpClientUtil();

    private static final Logger LOG = LoggerFactory.getLogger(MonitorServiceImpl.class);

    @Resource(name = "tblImageDayStatisticsVcnMapper")
    private TblImageDayStatisticsVcnMapper tblImageDayStatisticsVcnMapper;

    @Autowired
    private TblServerParamConfigMapper tblServerParamConfigMapper;

    @Autowired
    private TblIpcIpMapper ipcIpMapper;
             

    /*
    * 获得cookie
    * */
    @Override
    public void getCookie(Map<String,String> maps, HttpServletResponse response,HttpServletRequest request) {
        Map<String,String> map = new HashMap<>();
        Map<String,String> requestMap = new HashMap<>();
        map.put("serviceSubType", NumConstant.NUM_3+"");
        //获取ipAddress
        List<TblServerParamConfig> tblServerParamConfigs = tblServerParamConfigMapper.selectIpAndRemark(map);
        String addressIp = "";
        String url="";
        for(int i=0;i<tblServerParamConfigs.size();i++){
            if(tblServerParamConfigs.get(i).getRemark().toLowerCase().equals("vcn")){
                addressIp = tblServerParamConfigs.get(i).getServiceIpAddress();
                url=HTTPS+addressIp+":18531/loginInfo/login/V1.0";
            }
            if(tblServerParamConfigs.get(i).getRemark().toLowerCase().equals("vcm1")){
                addressIp = tblServerParamConfigs.get(i).getServiceIpAddress();
                //url=HTTPS+addressIp+":443/loginInfo/login/V1.0";
                url = "https://115.236.55.211:14456/sdk_service/rest/users/login/v1.1";
            }
        }
        JSONObject json = new JSONObject();
        requestMap.put("Content-Type","application/x-www-form-urlencoded");
        //String str = httpClientUtil.doPostMasterController(url,json,requestMap);
        Map<String,String>mapStr = new HashedMap();
        String str = httpClientUtil.getContent(url,mapStr);
        response.setHeader("cookies",str);
    }



    /*
     * 获取domaincode
     * */
    @Override
    public JSONObject getDomiancode(HttpServletResponse response) {
//        String cookie = response.getHeader("Cookie");
        String cookie = response.getHeader("cookies");
        Map<String,String> maps = new HashMap<>();
        //获取表中ip,remark 以及serviceipAddress
        Map<String,Object> map = new HashMap<>();
        map.put("serviceSubType",NumConstant.NUM_3);
        List<TblServerParamConfig> tblServerParamConfigs = tblServerParamConfigMapper.selectIpAndRemark(map);
        if(tblServerParamConfigs==null || tblServerParamConfigs.isEmpty()){
            LOG.error("获取设备信息失败");
            return null;
        }
        String connectionUrl = null;
        StringBuilder loginUrl = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA);
        String jsonResult = null;
        JSONObject jsonObject = new JSONObject();
        String con = null;
        try{
            //String str = HTTPS+loginUrl.append(connectionUrl).toString();
            String str= "";
            JSONObject obj = new JSONObject();
            maps.put("cookie",cookie);
            jsonResult = httpClientUtil.doGet(str,maps);
            if (StringUtils.isNotEmpty(jsonResult)) {
                obj = getResultJsonObject(jsonResult);//得到json对象
            }
            JSONObject jsonObj = obj.getJSONObject("domainRouteInfos").getJSONObject("domainRouteList");
            JSONArray jsonArray = jsonObj.getJSONArray("domianRoute");
            for(int i=0;i<jsonArray.size();i++){
                if(StringUtils.isEmpty(jsonArray.getJSONObject(i).getString("superDomain"))){
                    con = jsonArray.getJSONObject(i).getString("domainCode");
                    jsonObj.put("domainCode",con);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public RestResult getCountPicture(ImageDayStatisticsVcnVO imageDayStatisticsVcnVO, HttpServletRequest request, HttpServletResponse response) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        JSONObject jsonObject = getDomiancode(response);
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> mapVo = new HashMap<>();
        if(jsonObject.isEmpty()){
            LOG.error("domainCode is not empty...");
            restResult.setMessage("domainCode is not empty...");
            return restResult;
        }
        if(StringUtils.isEmpty(imageDayStatisticsVcnVO.getStartTime())){
            LOG.error("开始时间不能为空");
            restResult.setMessage("开始时间不能为空");
            return restResult;
        }

        if(StringUtils.isEmpty(imageDayStatisticsVcnVO.getEndTime())){
            LOG.error("结束时间不能为空");
            restResult.setMessage("结束时间不能为空");
            return restResult;
        }
        if(imageDayStatisticsVcnVO.getType() == null){
            LOG.error("图片类型不能为空");
            restResult.setMessage("图片类型不能为空");
            return restResult;

        }
        if(imageDayStatisticsVcnVO.getTranType() == null){
            LOG.error("图片传输统计类型不能为空");
            restResult.setMessage("图片传输统计类型不能为空");
            return restResult;

        }
        if(imageDayStatisticsVcnVO.getPicType() == null){
            LOG.error("图片大小不能为空");
            restResult.setMessage("图片大小不能为空");
            return restResult;
        }
        if(StringUtils.isEmpty(imageDayStatisticsVcnVO.getCameraSn())){
            LOG.error("相机编码不能为空");
            restResult.setMessage("相机编码不能为空");
            return restResult;
        }
        map.put("domainCode",jsonObject.get("domainCode"));
        map.put("startTime",imageDayStatisticsVcnVO.getStartTime());
        map.put("endTime",imageDayStatisticsVcnVO.getEndTime());
        map.put("type",imageDayStatisticsVcnVO.getType());
        map.put("picType",imageDayStatisticsVcnVO.getPicType());
        map.put("tranType",imageDayStatisticsVcnVO.getTranType());
        map.put("tblName", CommonConst.Image_Day_Statistics_VCN);
        String cameraId = imageDayStatisticsVcnVO.getCameraSn();
        mapVo.put("cameraId",cameraId);
        List<TblIpcIp> tblIpcIps = ipcIpMapper.getCameraSn(mapVo);
        if(tblIpcIps==null || tblIpcIps.isEmpty()){
            LOG.error("获取设备编码失败");
            return null;
        }
        String cameraSn = "";
        for(int i=0;i<tblIpcIps.size();i++){
            cameraSn = tblIpcIps.get(i).getCameraCode()+",";
            if(i == tblIpcIps.size()-1){
                cameraSn = cameraSn.substring(0,cameraSn.length()-1);
            }
            map.put("cameraSn",cameraSn);
        }
        int sum = 0;
        if(imageDayStatisticsVcnVO.getStartTime().length()==10){//以小时
           sum = tblImageDayStatisticsVcnMapper.countPictureByCameraHour(map);
        }
        if(imageDayStatisticsVcnVO.getStartTime().length()==8){//以天
            sum =tblImageDayStatisticsVcnMapper.countPictureByCameraDay(map);
        }
        if(imageDayStatisticsVcnVO.getStartTime().length()==6){//以月
            sum = tblImageDayStatisticsVcnMapper.countPictureByCameraMonth(map);
        }
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setData(sum);
        return restResult;
    }

    @Override
    public RestResult getCountPictureByRegion(ImageDayStatisticsVcnVO imageDayStatisticsVcnVO,HttpServletRequest request,HttpServletResponse response) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        JSONObject jsonObject = getDomiancode(response);
        Map<String,Object> map = new HashMap<>();
        JSONObject job = new JSONObject();
        String source = imageDayStatisticsVcnVO.getSource();
        if(StringUtils.isEmpty(imageDayStatisticsVcnVO.getSource())){
            LOG.error("来源不能为空");
            restResult.setMessage("来源不能为空");
            return restResult;
        }
        if(StringUtils.isEmpty(imageDayStatisticsVcnVO.getRemark())){
            LOG.error("备注不能为空");
            restResult.setMessage("备注不能为空");
            return restResult;
        }
        if((imageDayStatisticsVcnVO.getType()==null || imageDayStatisticsVcnVO.getType()==0)){
            LOG.error("类型不能为空");
            restResult.setMessage("类型不能为空");
            return restResult;
        }
        if(imageDayStatisticsVcnVO.getPicType()==null || imageDayStatisticsVcnVO.getPicType()==0){
            LOG.error("大小图片类型不能为空");
            restResult.setMessage("大小图片类型不能为空");
            return restResult;
        }
        if(imageDayStatisticsVcnVO.getTranType()==null || imageDayStatisticsVcnVO.getTranType()==0){
            LOG.error("图片传输统计类型不能为空");
            restResult.setMessage("图片传输统计类型不能为空");
            return restResult;
        }
        if(StringUtils.isEmpty(imageDayStatisticsVcnVO.getStartTime())){
            LOG.error("开始时间不能为空");
            restResult.setMessage("开始时间不能为空");
            return restResult;
        }
        if(StringUtils.isEmpty(imageDayStatisticsVcnVO.getEndTime())){
            LOG.error("结束时间不能为空");
            restResult.setMessage("结束时间不能为空");
            return restResult;
        }
        map.put("source",source);
        map.put("remark",imageDayStatisticsVcnVO.getRemark());
        map.put("type",imageDayStatisticsVcnVO.getType());
        map.put("picType",imageDayStatisticsVcnVO.getPicType());
        map.put("tranType",imageDayStatisticsVcnVO.getTranType());
        map.put("startTime",imageDayStatisticsVcnVO.getStartTime());
        map.put("endTime",imageDayStatisticsVcnVO.getEndTime());
        map.put("tblName",CommonConst.Image_Day_Statistics_VCN);
        map.put("tableNmae",CommonConst.Server_Param_Config);
        job.put("picCount",map);
        int sum = 0;
        if(imageDayStatisticsVcnVO.getStartTime().length()==10){//以小时
            sum = tblImageDayStatisticsVcnMapper.countPictureByRegionHour(map);
        }
        if(imageDayStatisticsVcnVO.getStartTime().length()==8){//以天
            sum = tblImageDayStatisticsVcnMapper.countPictureByRegionDay(map);
        }
        if(imageDayStatisticsVcnVO.getStartTime().length()==6){//以月
           sum =  tblImageDayStatisticsVcnMapper.countPictureByRegionMonth(map);
        }
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setData(sum);
        return restResult;
    }

    @Override
    public List<TblServerParamConfig> getAddressIp(ImageDayStatisticsVcnVO imageDayStatisticsVcn) {
        Map<String,Object> map = new HashMap<>();
        map.put("serviceSubType", NumConstant.NUM_3+"");
        //获取ipAddress
        List<TblServerParamConfig> tblServerParamConfigs = tblServerParamConfigMapper.selectIpAndRemark(map);
        return tblServerParamConfigs;
    }


    /**
     * String转换为JSONObject对象
     * @param returnJson json
     * @return jsonObject jsonObject
     */
    private JSONObject getResultJsonObject(String returnJson) {
        String jsonStr = returnJson.substring(returnJson.indexOf("{"), returnJson.lastIndexOf("}") + 1);
        return JSON.parseObject(jsonStr);
    }





}
