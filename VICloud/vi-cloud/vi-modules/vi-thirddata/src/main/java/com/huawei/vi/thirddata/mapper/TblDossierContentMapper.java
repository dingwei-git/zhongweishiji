package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.entity.po.TblIpcIp;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface TblDossierContentMapper extends IbaseDao<TblIpcIp, String>{

    List<Map> getDossier(Map<String, Object> dossierMap) throws DataAccessException;//获得所有的id value getDossierContent

    List<Map> getDossierContent() throws DataAccessException;

    List<Map> getDossierMap(@Param("teamLists") List<String> list)throws DataAccessException;

    int getDossierCount()throws DataAccessException;

}
