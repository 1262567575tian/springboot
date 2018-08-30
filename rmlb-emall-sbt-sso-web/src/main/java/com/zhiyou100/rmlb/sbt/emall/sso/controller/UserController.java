package com.zhiyou100.rmlb.sbt.emall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUser;
import com.zhiyou100.rmlb.sbt.emall.sso.dubbo.RegisterService;



@Controller
public class UserController {
	
	@Reference(version="1.0.0")
	private RegisterService registerService;
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public N3Result checkData(@PathVariable String param,@PathVariable int type) {
		return registerService.userchick(param, type);
	}
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public N3Result userRegis(TbUser tbUser) {
		return registerService.UserRegis(tbUser);
	}
}
