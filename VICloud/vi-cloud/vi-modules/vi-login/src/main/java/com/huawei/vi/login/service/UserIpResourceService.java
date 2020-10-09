package com.huawei.vi.login.service;

import lombok.NonNull;

import java.util.List;
import java.util.Map;

public interface UserIpResourceService {

    List<String> getIpcIpByUserOrId(@NonNull String loginUserName, List<String> id, @NonNull String tableName);

    public List<Map<String, Object>> getIpcsByUser(@NonNull String loginUserName, List<String> id,
                                                   @NonNull String tableName);
}
