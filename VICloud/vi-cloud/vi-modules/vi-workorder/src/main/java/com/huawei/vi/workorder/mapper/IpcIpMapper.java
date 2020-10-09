package com.huawei.vi.workorder.mapper;

import com.huawei.vi.entity.po.FaultTypePO;
import com.huawei.vi.entity.po.TblIpcIp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface IpcIpMapper {

    Map getIpcInfo(String cameraId);

}
