package com.huawei.vi.login.controller;

import com.huawei.vi.entity.po.UserPO;
import com.huawei.vi.login.service.UserService;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.vo.ExecuteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 获取用户基本信息
     * */
    @GetMapping("get_user")
    public RestResult getOwnUser(@RequestParam String user_id) {
        return userService.getUser(user_id);
    }

//    /**
//     * 获取用户基本信息
//     * */
//    @GetMapping("info")
//    public UserPO getInfo(@RequestParam("user_id") String user_id) {
//        UserPO userPO =  userService.getInfo(user_id);
//        return userPO;
//    }

//    /**
//     * 获取用户基本信息
//     * */
//    @GetMapping("test")
//    public String test(@RequestParam("user_id") String user_id) {
//
//        return "feign test success";
//    }

}
