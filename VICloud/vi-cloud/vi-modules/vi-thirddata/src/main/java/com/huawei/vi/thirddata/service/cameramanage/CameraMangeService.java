package com.huawei.vi.thirddata.service.cameramanage;

import com.huawei.vi.entity.vo.CameraPageVo;
import com.huawei.vi.entity.vo.CameraVo;
import com.jovision.jaws.common.util.RestResult;

public interface CameraMangeService {

    RestResult getCameraPage(CameraVo cameraVo);

    RestResult getManyCameraPage(CameraPageVo cameraPageVo);

}
