package com.huawei.vi.workorder.controller;

import com.huawei.vi.entity.vo.OrderOperateVo;
import com.huawei.vi.entity.vo.RepairVo;
import com.huawei.vi.feign.config.client.UserClient;
import com.huawei.vi.workorder.service.OrderService;
import com.jovision.jaws.common.constant.AppResultEnum;
import com.jovision.jaws.common.constant.HeaderParamEnum;
import com.jovision.jaws.common.controller.BaseController;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/order/")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 列表页：1、代办工单列表、2、进行中工单列表、3、已完成工单列表
     * */
    @GetMapping("list/{orderStatus}")
    public RestResult queryList(@PathVariable String orderStatus,
                                @RequestParam(defaultValue = "1") int currPage,
                                @RequestParam(defaultValue = "20") int pageSize){
        if(StringUtils.isEmpty(getUserIdFromAttribute())){
            return RestResult.generateRestResult(AppResultEnum.NOT_FIND_USER.getCode(), AppResultEnum.NOT_FIND_USER.getMessage(),null);
        }
        Map condition = new HashMap();
        condition.put("orderStatus",orderStatus);
        condition.put("userId",getUserIdFromAttribute());
        condition.put("currPage",currPage);
        condition.put("pageSize",pageSize);
        return orderService.queryOrderList(condition);
    }
    /**
     * 查询工单详情
     * */
    @GetMapping("info/{orderCode}")
    public RestResult getOrderInfo(@PathVariable String orderCode){
       return  RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),orderService.getOrderInfo(orderCode));
    }
    /**
     *  工单管理页面接口，查询待做、进行中、已完成工单的数量
     *  typecode=todoing,process,finish
     * */
    @GetMapping("count/orderstatus")
    public RestResult getCountByStatus(){
        if(StringUtils.isEmpty(getUserIdFromAttribute())){
            return RestResult.generateRestResult(AppResultEnum.NOT_FIND_USER.getCode(),AppResultEnum.NOT_FIND_USER.getMessage(),null);
        }
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),orderService.getCountByStatus(getUserIdFromAttribute()));
    }
    /**
     * 维修工单操作接口：
     * @param
     * */
    @PostMapping("update/repair")
    public RestResult updateRepair(HttpServletRequest request, @RequestParam(value= "filename",required=false) MultipartFile[] files, RepairVo repairVo, String recheck){
        RestResult restResult = repeat("app:repair:"+repairVo.getOrderCode());
        if(!(restResult.getCode()==AppResultEnum.SUCCESS.getCode())){
            return restResult;
        }
        String token = request.getHeader(HeaderParamEnum.AUTHORIZATION.getTitle()).substring(7);
        if(StringUtils.isEmpty(getUserNameFromAttribute())){
            return RestResult.generateRestResult(AppResultEnum.NOT_FIND_USER.getCode(),AppResultEnum.NOT_FIND_USER.getMessage(),null);
        }
        return orderService.updateRepair(files,repairVo,getUserNameFromAttribute(),recheck,token);
    }
    /**
     * 关闭工单操作接口：
     * @param
     * */
    @PostMapping("update/feedback")
    public RestResult updateClose(@RequestBody OrderOperateVo operateVo){
        RestResult restResult = repeat("app:feedback:"+operateVo.getOrderCode());
        if(!(restResult.getCode()==AppResultEnum.SUCCESS.getCode())){
            return restResult;
        }
        if(StringUtils.isEmpty(getUserNameFromAttribute())){
            return RestResult.generateRestResult(AppResultEnum.NOT_FIND_USER.getCode(),AppResultEnum.NOT_FIND_USER.getMessage(),null);
        }
        // 一级工单
//            if (!StringNumConstant.STRING_NUM_1.equals(GlobalConfigProperty.getGlobalMap()
//                    .get(PropertiesConfigCommonConst.IS_HAVE_FIELD_ORGANIZATION_MANAGER))) {
//                return workOrderReceiveService.feedbackOrder(orderFeedbackList, userName, multipartFile);
//            }
        // 二级工单
        return orderService.updateRepairFeedBack(operateVo, getUserNameFromAttribute());
    }

    private RestResult repeat(String key){
        long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            redisTemplate.expire(key, 5, TimeUnit.SECONDS);
        }
        if (count > 1) {
            return RestResult.generateRestResult(AppResultEnum.REPEAT_COMMIT.getCode(),AppResultEnum.REPEAT_COMMIT.getMessage(),null);
        }
        return RestResult.generateRestResult(AppResultEnum.SUCCESS.getCode(),AppResultEnum.SUCCESS.getMessage(),null);
    }
}
