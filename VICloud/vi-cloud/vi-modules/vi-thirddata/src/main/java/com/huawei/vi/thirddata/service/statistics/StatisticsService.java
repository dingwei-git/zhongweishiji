package com.huawei.vi.thirddata.service.statistics;

import com.huawei.vi.entity.vo.DossierVo;
import com.jovision.jaws.common.util.RestResult;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface StatisticsService {
    public RestResult getStatisticsDossier(DossierVo dossierVo);

    Map<String, Object> queryPublicInfo(String userName, String tableNamePrefix);

    public RestResult getTableHeader(String leves);

    public RestResult dossierExport(DossierVo dossierVo, HttpServletResponse response);





}
