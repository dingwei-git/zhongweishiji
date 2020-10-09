package com.huawei.vi.workorder.service;

import java.util.List;

public interface FaultTypeService {

    List queryAll();

    List queryFaultInfo(String orderCode);
}
