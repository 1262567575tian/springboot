package com.zhiyou100.rmlb.sbt.emall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.sso.dubbo.TokenService;



@Controller
public class TokenController {
	@Reference(version="1.0.0")
	private TokenService service;
	
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback) {
		N3Result result=service.getUserByToken(token);
		//响应结果之前判断是否为jsonp请求
		if(StringUtils.isNoneBlank(callback)) {
			//把结果封装成一个js语句响应
			MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction((callback));
			return mappingJacksonValue;
		}
		
		
		
		return result;
	}
}
