package com.huawei.vi.thirddata.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.utils.CollectionUtil;
import com.huawei.vi.entity.vo.*;
import com.huawei.vi.entity.vo.Dictionary;
import com.huawei.vi.thirddata.mapper.DictionaryMapper;
import com.huawei.vi.thirddata.mapper.EquipmentMapper;
import com.huawei.vi.thirddata.service.EquipmentService;
import com.huawei.vi.thirddata.service.userIpresorce.UserIpResourceService;
import com.jovision.jaws.common.config.i18n.MessageSourceUtil;
import com.jovision.jaws.common.constant.CommonConst;
import com.jovision.jaws.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    EquipmentMapper equipmentMapper;
    @Autowired
    UserIpResourceService userIpResourceService;
    @Autowired
    DictionaryMapper dictionaryMapper;
    @Autowired
    private RedisUtil redis;
    @Autowired
    private MessageSourceUtil messageSourceUtil;

    private Lock lock = new ReentrantLock();
    private static String typeId = "100002001";
    private static String model = "model";// 类型
    private static String ipType = "ipType";// 设备
    private String[] organizeNames = {messageSourceUtil.getMessage("hierarchy1"),messageSourceUtil.getMessage("hierarchy2"),
            messageSourceUtil.getMessage("hierarchy3"),messageSourceUtil.getMessage("hierarchy4")};

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentServiceImpl.class);

    private HttpClientUtil httpClientUtil = new HttpClientUtil();

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${childequipment.url}")
    private String queryChildEquipmentUrl;
    @Value("${childequipment.everyTimeCount}")
    private int everyTimeCount = 1;// 每次请求的数量
    @Value("${childequipment.requestCount}")
    private int requestCount = 1;// 重新请求次数
    private static Date date = null;

    /**
     * 采用为是的字段
     */
    private static final String[] isUse = {"code","domainCode","vendorType","type","cameraLocation","netType","deviceCreateTime","longitude",
            "latitude","height","origDevCode","connectCode","supportGA1400"};

    @Override
    public RestResult getEquipmentStatistical(HttpServletRequest request) {
        RestResult restResult = TraverseObjectUtil.checkGinsengRequest(messageSourceUtil,request,"token","data","pageNum","pageSize");
        if(restResult.getCode()==ServiceCommonConst.CODE_SUCCESS){
            restResult.setCode(ServiceCommonConst.CODE_FAILURE);
            return restResult;
        }
        String level = redis.getValue("ipclLevel");
        if(StringUtils.isEmpty(level)){
            restResult.setMessage(messageSourceUtil.getMessage("levelIsEmpt"));
            restResult.setCode(ServiceCommonConst.CODE_FAILURE);
            return restResult;
        }
        int levelInt = Integer.parseInt(level);
        Map<String, Object> map = getConditions(request,levelInt);// 查询条件封装
        if("false".equals(map.get("flag"))){
            restResult.setCode(ServiceCommonConst.CODE_FAILURE);
            restResult.setMessage(messageSourceUtil.getMessage("resultIsEmpt"));
            return restResult;
        }
        String pageNum = request.getParameter("pageNum");// 页码
        String pageSize = request.getParameter("pageSize");// 每页条数
        List<EquipmentVo> list = equipmentMapper.getEquipmentStatistical(map);
        if(list!=null&&list.size()>0){
            List<EquipmentVo> data =
                    CollectionUtil.getPagedList(list, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
            Map<String, Object> maps = new HashMap<>();
            maps.put("list", data);
            maps.put("totalItems", list.size());
            maps.put("menuList", map.get("menuList"));
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage("");
            restResult.setData(maps);
        }
        restResult.setCode(ServiceCommonConst.CODE_FAILURE);
        restResult.setMessage(messageSourceUtil.getMessage("resultIsEmpt"));
        return restResult;
    }

    private Map<String, Object> getConditions(HttpServletRequest request,int level){
        int menuId = level;
        List<Menu> menuList = new ArrayList<Menu>();// 菜单
        Menu menu = null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("zero", NumConstant.NUM_0);
        map.put("three",NumConstant.NUM_3);
        map.put("five",NumConstant.NUM_5);
        map.put("ten",NumConstant.NUM_10);
        List<String> list = new ArrayList<String>();
        for(int i=0;i<level;i++){
            list.add("views.id"+i);
            menu = getMuse(i,organizeNames[i],"filed"+i);
            menuList.add(menu);
        }
        JSONObject json = JSONObject.parseObject(request.getParameter("data"));
        List<String> modelList = new ArrayList<String>();
        List<String> ipTypeList = new ArrayList<String>();
        List<String> shelveNameList = new ArrayList<String>();
        List<String> id = new ArrayList<String>();
        if(json!=null){
            // 厂家
            JSONArray shelveName = json.getJSONArray("shelveName");
            if(shelveName!=null&&shelveName.size()>0){
                list.add("views.shelveName");
                shelveNameList = shelveName.toJavaList(String.class);
                menuId = menuId+1;
                menu = getMuse(menuId,"厂家","sheLveName");
                menuList.add(menu);
            }
            // 类型
            JSONArray ipType = json.getJSONArray("ipType");
            if(ipType!=null&&ipType.size()>0){
                list.add("views.ipType");
                ipTypeList = ipType.toJavaList(String.class);
                menuId = menuId+1;
                menu = getMuse(menuId,"类型","ipType");
                menuList.add(menu);
            }
            // 型号
            JSONArray model = json.getJSONArray("model");
            if(model!=null&&model.size()>0){
                list.add("views.model");
                modelList = model.toJavaList(String.class);
                menuId = menuId+1;
                menu = getMuse(menuId,"型号","model");
                menuList.add(menu);
            }
            // 组织
            JSONArray ids = json.getJSONArray("id");
            if(ids!=null&&ids.size()>0){
                id = ids.toJavaList(String.class);
                int idSubscript = level-1;
                map.put("ida","id"+idSubscript);// 分组条件
            }
        }
        menuId = menuId+1;
        menu = getMuse(menuId,"0-3年","zeroToThree");
        menuList.add(menu);
        menuId = menuId+1;
        menu = getMuse(menuId,"3-5年","threeToFive");
        menuList.add(menu);
        menuId = menuId+1;
        menu = getMuse(menuId,"5-10年","fiveToTen");
        menuList.add(menu);
        menuId = menuId+1;
        menu = getMuse(menuId,"未知年限","unknown");
        menuList.add(menu);
        map.put("list",list);// 分组条件
        map.put("id", CommonConst.ARCHIVESID);// 摄像机档案id
        map.put("id1", CommonConst.ARCHIVESID1);// 基础设施档案id
        map.put("name",CommonConst.MANUFACTURERID);// 厂家
        map.put("name1",CommonConst.MANUFACTURERID1);// 厂家
        map.put("time",CommonConst.DATEID);// 日期
        map.put("time1",CommonConst.DATEID1);// 日期
        map.put("modelList",modelList);// 型号
        map.put("ipTypeList",ipTypeList);// 类型
        map.put("shelveNameList",shelveNameList);// 厂家
        map.put("ids",id);// 厂家
        map.put("menuList",menuList);// 菜单
        String token = request.getParameter("token");
        map.put("flag","true");//
        if(StringUtils.isNotEmpty(token)){
            String userId = token.split("_")[0];// 1为admin用户
            if(!"1".equals(userId)){
                List<String> userIp = equipmentMapper.getUserEquipmentIp(userId);
                if(userIp!=null&&userIp.size()>0){
                    map.put("userIp",userIp);
                    map.put("flag","true");//
                }else{
                    map.put("flag","false");//
                }
            }
        }
        map.put("dossierStatus","已审核");
        return map;
    }

    private Menu getMuse(int id,String title,String key){
        Menu menu = new Menu();
        menu.setId(id);
        menu.setKey(key);
        menu.setTitle(title);
        return menu;
    }

    @Override
    public RestResult queryEquipmentList(HttpServletRequest request) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        String cameraId = request.getParameter("cameraNum");
        String userName = request.getParameter("userName");
        if(StringUtils.isEmpty(userName)){
            restResult.setMessage(messageSourceUtil.getMessage("nameIsEmpt"));
            return restResult;
        }
        String isDefault = "0";
        if(CommonConst.ADMIN_US_NAME.equals(userName)){
            isDefault = "1";
        }
        String id = CommonConst.ARCHIVESID;
        String id1 = CommonConst.ARCHIVESID1;
        Map<String,String> map = new HashMap<String,String>();
        map.put("cameraId",cameraId);
        map.put("id",id);
        map.put("id1",id1);
        map.put("userName",userName);
        map.put("isDefault",isDefault);
        List<String> list = equipmentMapper.queryEquipmentListByUser(map);
        if(list!=null&&list.size()>0){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setData(list);
            restResult.setMessage("");
        }
        restResult.setMessage(messageSourceUtil.getMessage("resultIsEmpt"));
        return restResult;
    }

    @Override
    public RestResult queryChildEquipmentByVCN(HttpServletRequest request) {
        RestResult restResult = TraverseObjectUtil.checkGinsengRequest(messageSourceUtil,request,"deviceType","cookie");
        if(restResult.getCode()==ServiceCommonConst.CODE_SUCCESS){
            restResult.setCode(ServiceCommonConst.CODE_FAILURE);
            return restResult;
        }
        String cookie = request.getParameter("cookie");
        String getUrlData = "deviceType="+request.getParameter("deviceType")+"&";
        addVCNInformation(getUrlData,cookie,restResult);
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setMessage("");
        return restResult;
    }

    @Override
    public RestResult queryConditions(HttpServletRequest request) {
        RestResult restResult = TraverseObjectUtil.checkGinsengRequest(messageSourceUtil,request,"loginName");
        if(restResult.getCode()==ServiceCommonConst.CODE_SUCCESS){
            restResult.setMessage(messageSourceUtil.getMessage("nameIsEmpt"));
            restResult.setCode(ServiceCommonConst.CODE_FAILURE);
            return restResult;
        }
        String loginName = request.getParameter("loginName");
        String level = redis.getValue("ipclLevel");
        if(StringUtils.isEmpty(level)){
            restResult.setMessage(messageSourceUtil.getMessage("levelIsEmpt"));
            restResult.setCode(ServiceCommonConst.CODE_FAILURE);
            return restResult;
        }
        int levelInt = Integer.parseInt(level);
        Map<String, Object> map = getOrganiseInfo(loginName,levelInt);// 查询组织层级
        List<Dictionary> dictionary = getDicByPid(typeId);// 查询厂家
        List<String> modelList= equipmentMapper.queryEquipment(model);// 查询类型
        List<String> ipTypeList= equipmentMapper.queryEquipment(ipType);// 查询设备
        JSONObject json = new JSONObject();
        json.put("netWork",map);
        json.put("dictionary",dictionary);
        json.put("modelList",modelList);
        json.put("ipTypeList",ipTypeList);
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        restResult.setData(json);
        return restResult;
    }

    @Override
    public RestResult exportEquipment(HttpServletRequest request,HttpServletResponse response) {
        RestResult restResult = TraverseObjectUtil.checkGinsengRequest(messageSourceUtil,request);
        if(restResult.getCode()==ServiceCommonConst.CODE_SUCCESS){
            return restResult;
        }
        String level = redis.getValue("ipclLevel");
        if(StringUtils.isEmpty(level)){
            restResult.setMessage(messageSourceUtil.getMessage("levelIsEmpt"));
            restResult.setCode(ServiceCommonConst.CODE_FAILURE);
            return restResult;
        }
        int levelInt = Integer.parseInt(level);
        Map<String, Object> map = getConditions(request,levelInt);// 查询条件封装
        List<Menu> menuList  = (List<Menu>)map.get("menuList");
        List<String> sheetNameList = new ArrayList<String>();
        sheetNameList.add("设备使用年限统计");
        List dataList = equipmentMapper.getEquipmentStatistical(map);
        Map menuMap = new HashMap();
        menuMap.put("sheet1",menuList);
        Map sheetNameMap = new HashMap();
        sheetNameMap.put("sheet1",sheetNameList);
        Map dataMap = new HashMap();
        dataMap.put("sheet1",dataList);
        ExcelUtils.createExcelFile(menuMap,sheetNameMap, dataMap,response);
        return restResult;
    }

    private List<Dictionary> getDicByPid(String pid) {
        Map<String, Object> params = new HashMap<>(com.huawei.utils.NumConstant.NUM_2);
        params.put("pid", pid);
        return dictionaryMapper.selectByCondition(params);
    }

    /**
     * 获取组织信息
     *
     * @param userName 用户名
     * @return 组织信息
     */
    private Map<String, Object> getOrganiseInfo(String userName,int level) {
        Map<String, Object> result = new HashMap<>(com.huawei.utils.NumConstant.NUM_4);
        result.putAll(userIpResourceService.getOrganizationsByUser(userName, null, CommonConst.TBL_IPC_IP_TMP));
        result.put("ipcLevelNum", level);

        List<String> list = new ArrayList<String>();
        for(int i=0;i<level;i++){
            list.add(organizeNames[i]);
        }
        result.put("organizeNames", list);
        return result;
    }

    private RestResult addVCNInformation(String getUrlData,String cookie,RestResult restResult){
        date = new Date();// 处理数据开始时间
        int total = 0;
        int fromIndex = 1;// 开始索引
        int toIndex = everyTimeCount;// 结束索引
        int totalPage = 1;
        for(int i=1;i<10000;i++){
            getUrlData+="fromIndex="+fromIndex+"&";
            getUrlData+="toIndex="+toIndex;
            String url = queryChildEquipmentUrl+getUrlData;
            LOGGER.info("queryChildEquipmentUrl-->"+url);
            String str = getDataByUrl(url,cookie);
            fromIndex = fromIndex + everyTimeCount;
            toIndex = toIndex + everyTimeCount;
            if(StringUtils.isEmpty(str)){
                LOGGER.error("addVCNInformation-->"+"第"+i+"次处理数据为空,接口数据总数据为"+total);
                if(i==1){
                    break;
                }else{
                    continue;
                }
            }
            VCNEquipmentInformationVo equipmentInformationVo = JSONObject.parseObject(str, VCNEquipmentInformationVo.class);
            if(!"0".equals(equipmentInformationVo.getResultCode())){
//                restResult.setMessage("处理失败,获取VCN接口错误码为："+equipmentInformationVo.getResultCode());
                LOGGER.error("addVCNInformation-->"+"第"+i+"次处理数据为空,接口数据总数据为"+total);
                if(i==1){
                    break;
                }else{
                    continue;
                }
            }
            CameraBriefExInfos cameraBriefExInfos = equipmentInformationVo.getCameraBriefExInfos();
            if(cameraBriefExInfos==null){
                LOGGER.error("addVCNInformation-->"+"第"+i+"次处理数据为空,接口数据总数据为"+total);
                if(i==1){
                    break;
                }else{
                    continue;
                }
            }
            ThreadPoolExecutor singleThreadExecutor = new ThreadPoolExecutor();
            int finalI = i;
            singleThreadExecutor.getSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        putInStorage(cameraBriefExInfos,date, finalI);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            if(i==1){
                total = cameraBriefExInfos.getTotal();
                if(total%everyTimeCount>0){
                    totalPage = total/everyTimeCount+1;
                }else {
                    totalPage = total/everyTimeCount;
                }
            }
            if(i==totalPage){
                break;
            }
        }
        restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
        JSONObject resJson = new JSONObject();
//        resJson.put("insertCount",insertCount/30);// 处理数据新增成功条数
        resJson.put("total",total);// 接口返回数据总条数
        restResult.setData(resJson);
        restResult.setMessage("");
        return restResult;
    }

    private void putInStorage(CameraBriefExInfos cameraBriefExInfos,Date date,int i){
        Map<String, List<CameraInformationVo>> map = processData(cameraBriefExInfos,date);
        List<CameraInformationVo> insertList = map.get("insertList");
        if(insertList==null||insertList.size()==0){
            LOGGER.error("addVCNInformation-->"+"第"+i+"次处理数据为空或没有需要新增的数据");
        }else{
            try {
                if(lock.tryLock()){
                    for(CameraInformationVo cameraInformationVo:insertList){
                        equipmentMapper.insertChildEquipmentInfo(cameraInformationVo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }

    private String getDataByUrl(String url,String cookie){
        Map<String, String> getMap = new HashMap<String, String>();
        getMap.put("Cookie",cookie);
        String str = httpClientUtil.doGet(url,getMap);
        if(StringUtils.isEmpty(str)){
            for(int i=0;i<requestCount;i++){
                str = httpClientUtil.doGet(url,getMap);
                if(StringUtils.isNotEmpty(str)){
                    break;
                }
            }
        }
        LOGGER.info("str============================"+str);
        return str;
    }

    /**
     * 解析接口返回数据并封装入库集合
     * @param cameraBriefExInfos
     * @return List<CameraInformationVo>
     */
    private Map<String, List<CameraInformationVo>> processData(CameraBriefExInfos cameraBriefExInfos, Date date){
        Map<String, List<CameraInformationVo>> map = new HashMap<String, List<CameraInformationVo>>();
        List<CameraInformationVo> insertList = new ArrayList<CameraInformationVo>();// 新增集合
        CameraBriefInfoExList cameraBriefInfoExList = cameraBriefExInfos.getCameraBriefInfoExList();
        if(cameraBriefInfoExList==null){
            return map;
        }
        List<CameraBriefInfoExes> cameraBriefInfoExesList = cameraBriefInfoExList.getCameraBriefInfoExes();
        if(cameraBriefInfoExesList==null||cameraBriefInfoExesList.size()==0){
            return map;
        }
        for(int i=0;i<cameraBriefInfoExesList.size();i++){
            CameraBriefInfoExes cameraBriefInfoExes = cameraBriefInfoExesList.get(i);
            if(cameraBriefInfoExes!=null){
                JSONObject json = TraverseObjectUtil.traverseCameraBriefInfoExes(cameraBriefInfoExes);
                encapsulationData(insertList,json,date);
            }
        }
        map.put("insertList",insertList);
        return map;
    }

    /**
     * 遍历json对象，封装参数
     * @param insertList
     * @param json
     * @param date
     */
    private void encapsulationData(List<CameraInformationVo> insertList,JSONObject json,Date date){
        String cameraSn = TraverseObjectUtil.newTrim(json.getString("code"));
        Boolean insertFlag = equipmentMapper.checkEquipment(cameraSn);
        if(insertFlag){
            //fastjson解析方法
            for (Map.Entry<String, Object> entry : json.entrySet()) {
                CameraInformationVo cameraInformationVo = new CameraInformationVo();
                cameraInformationVo.setCameraSn(cameraSn);// 设备编码
                cameraInformationVo.setCameraType(entry.getKey());// 字段
                cameraInformationVo.setCameraValue(TraverseObjectUtil.newTrim(entry.getValue()));// 字段值
                // 是否采用
                if(Arrays.asList(isUse).contains(entry.getKey())){
                    cameraInformationVo.setIsUse("1");
                }else{
                    cameraInformationVo.setIsUse("0");
                }
                cameraInformationVo.setCreateTime(date);// 创建时间
                cameraInformationVo.setUpdateTime(date);// 修改时间
                insertList.add(cameraInformationVo);
            }
        }else{
            LOGGER.error("encapsulationData-->当前时间"+sdf.format(date)+"此设备号不在设备表中存在"+cameraSn);
        }
    }

}
