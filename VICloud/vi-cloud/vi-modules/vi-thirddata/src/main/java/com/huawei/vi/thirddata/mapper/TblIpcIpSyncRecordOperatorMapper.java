package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.entity.model.TblIpcIpSyncRecordOperator;
import com.huawei.vi.entity.model.TblIpcIpSyncRecordOperator;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TblIpcIpSyncRecordOperatorMapper {
    int deleteByPrimaryKey(String taskid);

    int insert(TblIpcIpSyncRecordOperator record);

    int insertSelective(TblIpcIpSyncRecordOperator record);

    TblIpcIpSyncRecordOperator selectByPrimaryKey(String taskid);

    int updateByPrimaryKeySelective(TblIpcIpSyncRecordOperator record);

    int updateByPrimaryKey(TblIpcIpSyncRecordOperator record);

    int insertBatch(List<TblIpcIpSyncRecordOperator> list);

    int updateByTaskid(TblIpcIpSyncRecordOperator record);

    List<TblIpcIpSyncRecordOperator> selectByTaskId(Map<String,Object> param);

    int updateCurrentLevels(Map<String,String> param);

    int updateCurrentLevelsForEmpty(@Param("taskid") String taskid);

    List<Map<String,Object>> getIpcWithLatestLevel(String taskid);


}