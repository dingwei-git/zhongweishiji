package com.huawei.vi.thirddata.service.cameramanage.impl;

import com.huawei.vi.entity.po.CameraManagePo;
import com.huawei.vi.entity.vo.CameraPageVo;
import com.huawei.vi.entity.vo.CameraVo;
import com.huawei.vi.thirddata.mapper.CameraManageMapper;
import com.huawei.vi.thirddata.service.cameramanage.CameraMangeService;
import com.jovision.jaws.common.config.i18n.MessageSourceUtil;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.ServiceCommonConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jovision.jaws.common.util.TableCommonConstant.TBL_IPC_IP_TMP;

@Service
public class CameraManagesServiceImpl  implements CameraMangeService {

    private static final Logger LOG = LoggerFactory.getLogger(CameraManagesServiceImpl.class);

    @Autowired
    CameraManageMapper cameraManageMapper;

    @Autowired
    MessageSourceUtil messageSourceUtil;


    @Override
    public RestResult getCameraPage(CameraVo cameraVo) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        Map<String,Object> map = new HashMap<>();
        String cameraName = cameraVo.getCameraName();
        if(StringUtils.isEmpty(cameraName)){
            LOG.error(messageSourceUtil.getMessage("device.equipment"));
            restResult.setMessage(messageSourceUtil.getMessage("device.equipment"));
            return restResult;
        }
        Integer pageNum = cameraVo.getPageNum();//页码
        Integer rowsPage = cameraVo.getRowsPerPage();//每页行数
        Integer start = (pageNum-1)*rowsPage;
        map.put("tblName",TBL_IPC_IP_TMP);
        map.put("cameraName",cameraName);
        map.put("rowsPage",rowsPage);
        map.put("start",start);
        List<CameraManagePo> cameraManagePos = cameraManageMapper.getCameraPage(map);
        if(cameraManagePos!=null&&cameraManagePos.size()>0){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
            restResult.setData(cameraManagePos);
        }else {
            restResult.setMessage(messageSourceUtil.getMessage("device.result"));
        }
        return restResult;
    }

    @Override
    public RestResult getManyCameraPage(CameraPageVo cameraPageVo) {
        RestResult restResult = RestResult.generateRestResult(ServiceCommonConst.CODE_FAILURE,"",null);
        Map<String,Object> map = new HashMap<>();
        String cameraName = cameraPageVo.getCameraName();
        String cameraIp = cameraPageVo.getCameraIp();
        String cameraId = cameraPageVo.getCameraId();
        Integer pageNum = cameraPageVo.getPageNum();//页码
        Integer rowsPage = cameraPageVo.getRowsPerPage();//每页行数
        Integer start = (pageNum-1)*rowsPage;
        map.put("tblName",TBL_IPC_IP_TMP);
        map.put("cameraName",cameraName);
        map.put("rowsPage",rowsPage);
        map.put("start",start);
        map.put("cameraIp",cameraIp);
        map.put("cameraId",cameraId);
        List<CameraManagePo> cameraManagePos = cameraManageMapper.getManyCameraPage(map);
        if(cameraManagePos!=null&&cameraManagePos.size()>0){
            restResult.setCode(ServiceCommonConst.CODE_SUCCESS);
            restResult.setMessage(messageSourceUtil.getMessage("device.querySuccess"));
            restResult.setData(cameraManagePos);
        }else {
            restResult.setMessage(messageSourceUtil.getMessage("device.result"));
        }
        return restResult;
    }
}
