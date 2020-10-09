package com.huawei.vi.workorder.service;


import com.huawei.vi.entity.po.LogManagerPO;

import java.util.List;
import java.util.Map;

public interface LogManagerService {

   List queryLog(String orderCode);

   int insertLog(LogManagerPO logManagerPO);

   List<LogManagerPO> queryForback(Map map);
}
