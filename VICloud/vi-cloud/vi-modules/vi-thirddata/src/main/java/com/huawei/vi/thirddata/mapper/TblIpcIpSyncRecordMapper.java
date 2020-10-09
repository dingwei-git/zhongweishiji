package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.entity.model.TblIpcIpSyncRecord;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface TblIpcIpSyncRecordMapper {

    int deleteByPrimaryKey(TblIpcIpSyncRecord key);

    int insert(TblIpcIpSyncRecord record);

    int insertSelective(TblIpcIpSyncRecord record);

    TblIpcIpSyncRecord selectByPrimaryKey(TblIpcIpSyncRecord key);

    int updateByPrimaryKeySelective(TblIpcIpSyncRecord record);

    int updateByPrimaryKey(TblIpcIpSyncRecord record);

    int insertBatch(List<TblIpcIpSyncRecord> list);
}