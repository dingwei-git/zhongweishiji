package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.entity.vo.CameraInformationVo;
import com.huawei.vi.entity.vo.EquipmentVo;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface EquipmentMapper {

    List<EquipmentVo> getEquipmentStatistical(Map<String, Object> map) throws DataAccessException;

    List<String> queryEquipmentListByUser(Map<String,String> map);

    int insertChildEquipmentInfo(@Param("cameraInformationVo") CameraInformationVo cameraInformationVo);

    int updateChildEquipmentInfo(@Param("list") List<CameraInformationVo> list);

    Boolean checkEquipment(@Param("cameraSn") String cameraSn);

    List<String> queryEquipment(@Param("type") String type,@Param("other") String other);

    List<String> getUserEquipmentIp(@Param("userId") String userId);
}
