package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.entity.vo.Dictionary;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface DictionaryMapper {

    public List<Dictionary> selectByCondition(Map<String, Object> params);
}
