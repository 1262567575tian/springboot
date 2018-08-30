package com.zhiyou100.rmlb.emall.sbt.manager.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	
	@RequestMapping("/test")
	public String test() {
		return "test";
	}
}
