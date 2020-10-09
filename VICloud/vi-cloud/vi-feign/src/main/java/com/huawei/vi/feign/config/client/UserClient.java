package com.huawei.vi.feign.config.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "vi-login",configuration = LoginInter.class)
public interface UserClient {



//    @RequestMapping(value="/user/test", method= RequestMethod.GET,consumes = "application/json",
//            produces = "application/json" )
    @GetMapping("/user/test")
    String test(@RequestParam("user_id") String user_id);
}
