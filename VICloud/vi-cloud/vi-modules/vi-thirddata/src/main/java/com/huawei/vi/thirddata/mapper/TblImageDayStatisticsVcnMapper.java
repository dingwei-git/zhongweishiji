package com.huawei.vi.thirddata.mapper;

import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository(value = "tblImageDayStatisticsVcnMapper")
@DataSourceSelect(DataSourceEnum.VideoinsightCollecter)
public interface TblImageDayStatisticsVcnMapper {

    /*
    *按摄像机维度计算图片总数天
    * @return int
    * @throws DataAccessException sql异常
    * */
    int countPictureByCameraHour(Map<String,Object> map)throws DataAccessException;

    int countPictureByCameraDay(Map<String,Object> map)throws DataAccessException;

    int countPictureByCameraMonth(Map<String,Object> map)throws DataAccessException;

    /*
    * 按域维度计算图片总数
    * */
    int countPictureByRegionDay(Map<String,Object> map)throws DataAccessException;

    int countPictureByRegionHour(Map<String,Object> map)throws DataAccessException;

    int countPictureByRegionMonth(Map<String,Object> map)throws DataAccessException;

}
