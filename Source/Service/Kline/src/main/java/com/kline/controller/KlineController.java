package com.kline.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KlineController {
	
	@RequestMapping("/hello")
	public Object hello() {
		return "kline hello";
	}

}
