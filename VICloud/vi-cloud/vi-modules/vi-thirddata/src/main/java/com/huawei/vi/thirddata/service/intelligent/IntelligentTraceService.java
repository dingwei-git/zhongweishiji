package com.huawei.vi.thirddata.service.intelligent;

import com.huawei.vi.entity.vo.ImageCountVO;
import com.jovision.jaws.common.util.RestResult;

public interface IntelligentTraceService {

    public RestResult intelligentTrace(ImageCountVO imageCountVO);

    public RestResult intelligentTraceByCamera(ImageCountVO imageCountVO);

    public RestResult gitintelligentTraceList();

    public RestResult getNetWorkTrace(ImageCountVO imageCountVO);

    public RestResult getNetWorkTraceByHour(ImageCountVO imageCountVO);

    public RestResult intelligentTracePage(ImageCountVO imageCountVO);

    public RestResult intelligentTraceByCameraPage(ImageCountVO imageCountVO);



}
