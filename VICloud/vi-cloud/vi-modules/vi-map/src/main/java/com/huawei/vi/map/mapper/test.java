package com.huawei.vi.map.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface test {
    public List<Map<String,String>> selectAll();
}
