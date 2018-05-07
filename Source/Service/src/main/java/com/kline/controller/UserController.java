package com.kline.controller;

import com.kline.pojo.JsonResult;
import com.kline.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUser")
    public User getUser() {
        User user = new User();

        user.setName("kline150");
        user.setAge(30);
        user.setBirthday(new Date());
        user.setPassword("kline");
        user.setDesc("hello hello hello");

        return user;
    }

    @RequestMapping("/getUserJson")
    public JsonResult getUserJson() {
        User user = new User();

        user.setName("kline100");
        user.setAge(18);
        user.setBirthday(new Date());
        user.setPassword("kline6564");
        user.setDesc("hello kline");

        return JsonResult.ok(user);
    }
}
