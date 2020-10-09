package com.huawei.vi.thirddata.service;

import com.alibaba.fastjson.JSONObject;
import com.huawei.vi.entity.vo.ImageDayStatisticsVcnVO;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;
import com.jovision.jaws.common.util.RestResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface MonitorService {

    public void getCookie(Map<String,String> map, HttpServletResponse response,HttpServletRequest request);

    public JSONObject getDomiancode(HttpServletResponse response);

    public RestResult getCountPicture(ImageDayStatisticsVcnVO imageDayStatisticsVcnVO, HttpServletRequest request, HttpServletResponse response);

    public RestResult getCountPictureByRegion(ImageDayStatisticsVcnVO imageDayStatisticsVcnVO,HttpServletRequest request,HttpServletResponse response);

    public List<TblServerParamConfig> getAddressIp(ImageDayStatisticsVcnVO imageDayStatisticsVcn);

}
