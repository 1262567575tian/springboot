package com.zhiyou100.rmlb.sbt.emall.order.dubbo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	@RequestMapping("/order")
	public String order() {
		return "order-cart";
	}
	
}
