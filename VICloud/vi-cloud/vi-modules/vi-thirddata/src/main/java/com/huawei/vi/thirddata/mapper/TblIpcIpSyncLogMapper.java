package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.entity.model.TblIpcIpSyncLog;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface TblIpcIpSyncLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(TblIpcIpSyncLog record);

    int insertSelective(TblIpcIpSyncLog record);

    TblIpcIpSyncLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TblIpcIpSyncLog record);

    int updateByPrimaryKey(TblIpcIpSyncLog record);

    int insertBatch(List<TblIpcIpSyncLog> list);
}