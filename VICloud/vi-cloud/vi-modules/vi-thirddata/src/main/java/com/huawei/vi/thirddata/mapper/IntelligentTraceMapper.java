package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.entity.po.ImageCountPo;
import com.huawei.vi.entity.vo.ImageCountVO;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@DataSourceSelect(DataSourceEnum.VideoinsightCollecter)
public interface IntelligentTraceMapper extends IbaseDao<ImageCountVO, String>{

    public List<Map<String,String>> gitintelligentTraceList(Map<String,Object> map);

    public List<ImageCountPo> getIntelligent(Map<String, Object> map) throws DataAccessException;

    public List<ImageCountPo> getIntelligents(Map<String, Object> map) throws DataAccessException;

    public List<ImageCountPo> getIntelligentHour(Map<String, Object> map) throws DataAccessException;

    public List<ImageCountPo> getIntelligentHours(Map<String, Object> map) throws DataAccessException;

    public List<ImageCountPo> getNetWorkTraceByDay(Map<String,Object> map)throws DataAccessException;

    public List<ImageCountPo> getNetWorkTraceByHour(Map<String,Object> map)throws DataAccessException;

    public List<Map> getServiceName(Map<String,Object> map)throws DataAccessException;

    public List<Map> getServiceNames(Map<String,Object> map)throws DataAccessException;

    int insert(@Param("tableName") String tableName, @Param("relists") List<ImageCountPo> record)
            throws DataAccessException;

    int createTable(@Param("tableName") String tableName) throws DataAccessException;

    public List<Map<String,Object>> getIvsName(@Param("tableName") String tableName, @Param("ivsNameStr") List<String> stringList,@Param("cityLists") List<String> cityList)throws DataAccessException;

    public List<Map<String,Object>> getIvsNames(@Param("tableName") String tableName, @Param("ivsNameStr") List<String> stringList,@Param("cityLists") List<String> cityList)throws DataAccessException;

    public List<Map<String,Object>> getIvs(@Param("tableName") String tableName, @Param("ivsNameStr") List<String> stringList,@Param("cityLists") List<String> cityList)throws DataAccessException;

    int deleteByTableName(@Param("tableName") String tableName) throws DataAccessException;






}
