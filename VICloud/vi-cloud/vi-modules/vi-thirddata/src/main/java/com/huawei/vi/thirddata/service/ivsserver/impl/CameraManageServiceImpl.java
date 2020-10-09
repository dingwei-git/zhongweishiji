/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.ivsserver.impl;

import com.alibaba.fastjson.JSONObject;
import com.huawei.cn.components.util.XmlParserUtil;
import com.huawei.utils.CheckUtils;
import com.huawei.utils.CollectionUtil;
import com.huawei.utils.CommonUtil;
import com.huawei.utils.NumConstant;
import com.huawei.vi.entity.vo.ProgressInfo;
import com.huawei.vi.thirddata.mapper.TblCameraManageMapper;
import com.huawei.vi.thirddata.mapper.TblCameraManageOnceMapper;
import com.huawei.vi.thirddata.mapper.TblServerParamConfigMapper;
import com.huawei.vi.thirddata.service.baseserv.impl.BaseServImpl;
import com.jovision.jaws.common.util.ProtocolConfig;
import com.huawei.vi.thirddata.service.binary.pojo.TblCameraDetailOriginalData;
import com.huawei.vi.thirddata.service.binary.pojo.TblServerParamConfig;
import com.huawei.vi.thirddata.service.ivsserver.CameraManageService;
import com.huawei.vi.thirddata.service.ivsserver.ServiceCommonConst;
import com.jovision.jaws.common.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * CameraManageServiceImpl
 *
 * @since 2019-08-06
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class CameraManageServiceImpl extends BaseServImpl<TblCameraDetailOriginalData, Integer>
    implements CameraManageService {
    /**
     * 公共日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraManageServiceImpl.class);

    private static final String SQL_TABNAME = "tabName";

    private static final String CHAR_SET = "UTF-8";

    private static final String LANGU_TYPE = "zh-CN";

    private static final String IVS_TOTAL = "total";

    private static final String NUMER0 = "0";

    // 每页最大支持数
    private static final int LIMIT = 5000;

    @Resource(name = "cameraManageOnceDao")
    private TblCameraManageOnceMapper cameraManageOnceDao;

    @Resource(name = "cameraManageDao")
    private TblCameraManageMapper cameraManageDao;

    @Resource(name = "serverParamConfigDao")
    private TblServerParamConfigMapper serverParamConfigDao;

    @Override
    public int saveCameraIntersection(String tableName) {
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(SQL_TABNAME, tableName);
        cameraManageOnceDaoDropTable(keyMap);
        cameraManageOnceDaoCreateTblOriginalData(keyMap);
        List<TblCameraDetailOriginalData> lists = cameraManageOnceDaoGetIntersection(keyMap);
        if (CollectionUtil.isEmpty(lists)) {
            LOGGER.error(" list is null or empty");
            return 0;
        }
        cameraManageDaoDropTable(keyMap);
        cameraManageDaoCreateTblOriginalData(keyMap);
        return cameraManageDaoInsert(tableName, lists);
    }

    private List<TblCameraDetailOriginalData> cameraManageOnceDaoGetIntersection(Map<String, Object> keyMap) {
        try {
            return cameraManageOnceDao.getIntersection(keyMap);
        } catch (DataAccessException e) {
            LOGGER.error("getIntersection DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    private int cameraManageDaoInsert(String tableName, List<TblCameraDetailOriginalData> lists) {
        try {
            return cameraManageDao.insert(tableName, lists);
        } catch (DataAccessException e) {
            LOGGER.error("insert DataAccessException {} ", e.getMessage());
        }
        return 0;
    }

    private void cameraManageOnceDaoCreateTblOriginalData(Map<String, Object> keyMap) {
        try {
            cameraManageOnceDao.createTblOriginalData(keyMap);
        } catch (DataAccessException e) {
            LOGGER.error("createTblOriginalData DataAccessException {} ", e.getMessage());
        }
    }

    @Override
    protected Map<String, Object> getMap(TblCameraDetailOriginalData t, Map<String, Object> condition) {
        return null;
    }

    @Override
    public boolean isLoginIvs(String ipAddress, String account, String secretCode) {
        Map<String, String> parmMap = new HashMap<String, String>();
        String url =
            ProtocolConfig.getParameterConfig().getIvsCollectUrl().replaceFirst(ServiceCommonConst.HOST, ipAddress);
        String loginUrl = url + "users/login/v1.1";
        parmMap.put("account", account);
        parmMap.put("pwd", secretCode);
        Object loginObj = new HttpClientUtil().doPost(loginUrl, parmMap, CHAR_SET, true);
        if (loginObj != null) {
            String returnStr = loginObj.toString();
            if (returnStr.indexOf("Success") >= 0) {
                LOGGER.info("Login Success!");
                return true;
            }
        }
        LOGGER.error("Login Failed!");
        return false;
    }





    private JSONObject toJsonObj(Map<String, String> map, JSONObject resultJson) {
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            resultJson.put(key, map.get(key));
        }
        return resultJson;
    }

    private static JSONObject doPost(String httpUrl, JSONObject json) {
        String param = json.toJSONString();
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        JSONObject obj = new JSONObject();
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Cache-Control", "no-cache");
            String cookie = connection.getHeaderField("JSESSIONID");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                obj.put("cookie",cookie);
                obj.put("result",result);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return obj;
    }





    @Override
    public void logOutIvs(String ipAddress) {
        Map<String, String> parmMap = new HashMap<String, String>();
        String url =
            ProtocolConfig.getParameterConfig().getIvsCollectUrl().replaceFirst(ServiceCommonConst.HOST, ipAddress);
        String logoutUrl = url + "management/application-logout";
        Object logoutObj = new HttpClientUtil().doPost(logoutUrl, parmMap, CHAR_SET, false);
        if (logoutObj != null) {
            String returnStr = logoutObj.toString();
            if (returnStr.indexOf("Success") >= 0) {
                LOGGER.info("Logout Success!");
                return;
            }
        }
        LOGGER.error("Logout Failed!");
    }

    /**
     * 修改服务器登录状态
     *
     * @param isLogin 是否登录成功
     * @param tspc 服务器参数配置实体对象
     */
    @Override
    public void updateServerConnectStatus(boolean isLogin, TblServerParamConfig tspc) {
        LOGGER.info("updateServerConnectStatus start.");
        if (CommonUtil.isNull(tspc)) {
            return;
        }
        if (isLogin) {
            if (LANGU_TYPE.equals(ProgressInfo.getLanguageTyle())) {
                tspc.setMessage(ProtocolConfig.getGlobalMap().get("connectSucessCn"));
            } else {
                tspc.setMessage(ProtocolConfig.getGlobalMap().get("connectSucessEn"));
            }
        } else {
            if (LANGU_TYPE.equals(ProgressInfo.getLanguageTyle())) {
                tspc.setMessage(ProtocolConfig.getGlobalMap().get("connectFailedCn"));
            } else {
                tspc.setMessage(ProtocolConfig.getGlobalMap().get("connectFailedEn"));
            }
        }
        tspc.setServiceStatus(isLogin ? NUMER0 : "1");
        serverParamConfigDaoUpdateByPrimaryKeySelective(tspc); // 修改服务器状态
        LOGGER.info("updateServerConnectStatus end.");
    }

    private void serverParamConfigDaoUpdateByPrimaryKeySelective(TblServerParamConfig tspc) {
        try {
            serverParamConfigDao.updateByPrimaryKeySelective(tspc);
        } catch (DataAccessException e) {
            LOGGER.error("updateByPrimaryKeySelective DataAccessException {} ", e.getMessage());
        }
    }

    @Override
    public List<TblCameraDetailOriginalData> getCameraIntersection(String tabName) {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put(SQL_TABNAME, tabName);
        try {
            return cameraManageDao.getValidateCameraList(maps);
        } catch (DataAccessException e) {
            LOGGER.error("getValidateCameraList DataAccessException {} ", e.getMessage());
        }
        return null;
    }

    @Override
    public int saveCameraOriginal(String ipAddress, String account, String secretCode) {
        if (!isLoginIvs(ipAddress, account, secretCode)) {
            return 0;
        }

        // 公共解析入库方法 进来先取第一页
        Map<String, String> countMap = saveCameraOriginalByPage(ipAddress, 1);

        // 获取总条数计算还剩多少页
        int total = Integer.parseInt(countMap.get(IVS_TOTAL));
        if (total > LIMIT) {
            int pages = (total + LIMIT - 1) / LIMIT;
            for (int pageNum = 1; pageNum < pages; pageNum++) {
                saveCameraOriginalByPage(ipAddress, pageNum + 1);
            }
        }
        return total;
    }

    /**
     * 解析IVS返回的摄像机详细信息数据结构
     *
     * @param page 第几页
     * @return 控制用数据载体
     */
    private Map<String, String> saveCameraOriginalByPage(String ipAddress, int page) {
        Document document = null;
        Map<String, String> countMap = new HashMap<String, String>();
        List<TblCameraDetailOriginalData> cameraDetails = new ArrayList<TblCameraDetailOriginalData>();
        try {
            String url =
                ProtocolConfig.getParameterConfig().getIvsCollectUrl().replaceFirst(ServiceCommonConst.HOST, ipAddress);
            StringBuilder stringBuilder = new StringBuilder(NumConstant.EXPECTED_BUFFER_DATA).append(url)
                .append("cameras/v1.1?page=")
                .append(page)
                .append("&limit=")
                .append(LIMIT);
            String camerasUrl = stringBuilder.toString();
            Object logoutObj = new HttpClientUtil().doGet(camerasUrl);
            if (logoutObj == null) {
                LOGGER.error("Get cameraDetail is null.");
                countMap.put(IVS_TOTAL, NUMER0);
                return countMap;
            }
            SAXReader saxReader = XmlParserUtil.createSaxReader();
            InputStream inputStream = new ByteArrayInputStream(logoutObj.toString().getBytes(StandardCharsets.UTF_8));
            document = saxReader.read(inputStream);
            Element root = document.getRootElement();
            Element resultElement = root.element("result");
            String code = resultElement.element("code").getStringValue();
            if (!NUMER0.equals(code)) {
                LOGGER.error("Get cameraDetail failed.");
                countMap.put(IVS_TOTAL, NUMER0);
                return countMap;
            }
            countMap.put(IVS_TOTAL, root.element("total-count").getStringValue());

            // 解析摄像机列表
            Element cameraListElement = root.element("camera-list");
            for (Iterator<?> itemCameras = cameraListElement.elementIterator(); itemCameras.hasNext();) {
                TblCameraDetailOriginalData cameraDetail = getTblCameraDetailOriginalData(itemCameras);
                cameraDetail.setIpIvs(ipAddress);
                cameraDetails.add(cameraDetail);
            }
        } catch (DocumentException e) {
            LOGGER.error("parsingCondition failed.");
        }
        cameraManageOnceDaoInsert(cameraDetails);
        return countMap;
    }

    private void cameraManageOnceDaoInsert(List<TblCameraDetailOriginalData> cameraDetails) {
        try {
            cameraManageOnceDao.insert(cameraDetails);
        } catch (DataAccessException e) {
            LOGGER.error("insert DataAccessException {} ", e.getMessage());
        }
    }

    private static TblCameraDetailOriginalData getTblCameraDetailOriginalData(Iterator<?> itemCameras) {
        TblCameraDetailOriginalData cameraDetail = new TblCameraDetailOriginalData();
        Element itCamera = (Element) itemCameras.next();
        String cameraState = itCamera.element("camera-state").getStringValue();
        if (StringUtils.isNumeric(cameraState)) {
            cameraDetail.setCameraState(Integer.parseInt(cameraState));
        }
        cameraDetail.setCameraType(itCamera.element("camera-type").getStringValue());
        cameraDetail.setDirection(itCamera.element("direction").getStringValue());
        cameraDetail.setCameraId(itCamera.element("id").getStringValue());
        cameraDetail.setLatitude(itCamera.element("latitude").getStringValue());
        cameraDetail.setLongitude(itCamera.element("longitude").getStringValue());
        cameraDetail.setCameraName(itCamera.element("name").getStringValue());
        cameraDetail.setCameraSn(itCamera.element("sn").getStringValue());
        cameraDetail.setAddress(itCamera.element("address").getStringValue());
        String analysisType = itCamera.element("analysisList").getStringValue();
        if (!CheckUtils.isNullOrBlank(analysisType)) {
            cameraDetail.setAnalysisType(analysisType.trim());
        }
        cameraDetail.setCameraFeature(itCamera.element("camera-feature").getStringValue());
        cameraDetail.setCameraUse(itCamera.element("camera-use").getStringValue());
        cameraDetail.setCompass(itCamera.element("compass").getStringValue());
        cameraDetail.setFieldNo(itCamera.element("field-no").getStringValue());
        cameraDetail.setGroupId(itCamera.element("groupId").getStringValue());
        cameraDetail.setLane(itCamera.element("lane").getStringValue());
        cameraDetail.setMountHeight(itCamera.element("mount-height").getStringValue());
        cameraDetail.setNvr(itCamera.element("nvr").getStringValue());
        cameraDetail.setPlatName(itCamera.element("plat-name").getStringValue());
        cameraDetail.setResolutionType(itCamera.element("resolution-type").getStringValue());
        cameraDetail.setStreamUrl(itCamera.element("stream-url").getStringValue());
        cameraDetail.setSubstatus(itCamera.element("sub-status").getStringValue());
        String taskType = itCamera.element("taskTypeList").getStringValue();
        if (StringUtils.isNumeric(taskType)) {
            cameraDetail.setTaskType(Integer.parseInt(taskType.trim()));
        }
        cameraDetail.setVcnCameraUse(itCamera.element("vcnCameraUse").getStringValue());
        return cameraDetail;
    }

    @Override
    public void truncateCameraOriginal(String tableName) {
        cameraManageOnceDaoDelTblOriginalData(new HashMap<String, Object>());
    }

    private void cameraManageOnceDaoDelTblOriginalData(Map<String, Object> conditions) {
        try {
            cameraManageOnceDao.delTblOriginalData(conditions);
        } catch (DataAccessException e) {
            LOGGER.error("showTableByname DataAccessException {} ", e.getMessage());
        }
    }

    private void cameraManageDaoDropTable(Map<String, Object> conditions) {
        try {
            cameraManageDao.dropTable(conditions);
        } catch (DataAccessException e) {
            LOGGER.error("dropTable DataAccessException {} ", e.getMessage());
        }
    }

    private void cameraManageOnceDaoDropTable(Map<String, Object> conditions) {
        try {
            cameraManageOnceDao.dropTable(conditions);
        } catch (DataAccessException e) {
            LOGGER.error("dropTable DataAccessException {} ", e.getMessage());
        }
    }

    private void cameraManageDaoCreateTblOriginalData(Map<String, Object> conditions) {
        try {
            cameraManageDao.createTblOriginalData(conditions);
        } catch (DataAccessException e) {
            LOGGER.error("createTblOriginalData DataAccessException {} ", e.getMessage());
        }
    }

}
