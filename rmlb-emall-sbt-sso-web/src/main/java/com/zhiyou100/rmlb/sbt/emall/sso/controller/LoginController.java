package com.zhiyou100.rmlb.sbt.emall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.common.utils.CookieUtils;
import com.zhiyou100.rmlb.sbt.emall.sso.dubbo.UserLoginService;



@Controller
public class LoginController {
	
	@Reference(version="1.0.0")
	private UserLoginService loginService;
	
	@RequestMapping("/page/login")
	public String login(String redirect,Model model) {
		model.addAttribute("redirect",redirect);
		return "login";
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public N3Result login(String username,String password,HttpServletRequest request,HttpServletResponse response) {
		N3Result N3Result=loginService.UserLogin(username, password);
		//判断是否登录成功
		if(N3Result.getStatus()==200) {
			String token=N3Result.getData().toString();
			//如果登录成功把token写入cookie
			CookieUtils.setCookie(request, response, "sso-token", token);
		}
		return N3Result;
	}
	
}
