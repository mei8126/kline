package com.kline.controller;

import com.kline.pojo.JsonResult;
import com.kline.pojo.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private Resource resource;

    @RequestMapping("/hello")
    public Object hello() {
        return "Kline Hello ~~";
    }

    @RequestMapping("/getResource")
    public JsonResult getResource() {
        Resource bean = new Resource();
        BeanUtils.copyProperties(resource, bean);

        return JsonResult.ok(bean);
    }
}
