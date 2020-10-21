package com.huawei.vi.workorder.service.impl;

import com.huawei.vi.workorder.mapper.IpcIpMapper;
import com.huawei.vi.workorder.service.IpcIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IpcIpServiceImpl implements IpcIpService {

    @Autowired
    private IpcIpMapper ipcIpMapper;
    @Resource(name = "redisUmsTemplate")
    private RedisTemplate redisTemplate;
    @Override
    public Map getIpcInfo(String cameraId) {

        Object obj = redisTemplate.opsForValue().get("ipclLevel");
        int ipclLevel = obj==null?0:Integer.parseInt(obj.toString());
        StringBuffer fileds = new StringBuffer();
        if(ipclLevel>0){
            for(int i=0;i<ipclLevel;i++){
                if(fileds!=null&&fileds.length()>0){
                    fileds.append(",");
                }
                fileds.append("filed").append(i).append(" AS ").append("`").append("filed").append(i).append("`");
            }
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("fileds",fileds);
        param.put("cameraId",cameraId);
        Map<String,Object> map = ipcIpMapper.getIpcInfo(param);

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
