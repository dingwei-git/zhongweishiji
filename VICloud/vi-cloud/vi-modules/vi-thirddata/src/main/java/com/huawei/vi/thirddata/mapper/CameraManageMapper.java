package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.entity.po.CameraManagePo;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface CameraManageMapper {

    List<CameraManagePo> getCameraPage(Map<String, Object> map) throws DataAccessException;

    List<CameraManagePo> getManyCameraPage(Map<String, Object> map) throws DataAccessException;

}
