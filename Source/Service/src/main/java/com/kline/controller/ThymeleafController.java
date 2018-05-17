package com.kline.controller;

import com.kline.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("th")
public class ThymeleafController {

    @RequestMapping("/index")
    public String index(ModelMap map) {
        map.addAttribute("name", "thymeleaf-kline");
        return "thymeleaf/index";
    }
    @RequestMapping("center")
    public String center() {
        return "thymeleaf/center/center";
    }

//    @RequestMapping("/test")
//    public String test(ModelMap map) {
//
//        User user = new User();
//
//        return
//    }
}
