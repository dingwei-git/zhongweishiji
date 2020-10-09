
package com.huawei.vi.thirddata.mapper;

import com.huawei.vi.thirddata.service.binary.pojo.DzOriginalData;
import com.jovision.jaws.common.datasource.DataSourceEnum;
import com.jovision.jaws.common.datasource.DataSourceSelect;
import org.springframework.stereotype.Repository;

/**
 * 获取东智诊断模块处理后的数据Dao
 *
 * @since 2019-11-12
 */
@Repository(value = "TblDzDiagnosisDataDao")
@DataSourceSelect(DataSourceEnum.VideoInsight)
public interface TblDzDiagnosisDataMapper extends IbaseDao<DzOriginalData, String> {
}
