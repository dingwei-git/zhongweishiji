package com.huawei.vi.workorder.service.impl;

import com.huawei.vi.workorder.mapper.IpcIpMapper;
import com.huawei.vi.workorder.service.IpcIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class IpcIpServiceImpl implements IpcIpService {

    @Autowired
    private IpcIpMapper ipcIpMapper;

    @Override
    public Map getIpcInfo(String cameraId) {
        Map<String,Object> map = ipcIpMapper.getIpcInfo(cameraId);

        List valList = new ArrayList<>();
        for(int i=0;i<10;i++){
            if(map.containsKey("filed"+i)){
                valList.add(map.get("filed"+i));
            }
        }
        map.put("filedList",valList);
        return map;
    }
}
