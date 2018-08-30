package com.zhiyou100.rmlb.sbt.emall.cat.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.common.utils.CookieUtils;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUser;
import com.zhiyou100.rmlb.sbt.emall.sso.dubbo.TokenService;



/**
 * 添加购物车拦截器
 * */
@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Reference(version="1.0.0")
	private TokenService tokenService;
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//前处理，执行handler之前执行方法
		//从cookie中取token
		String token=CookieUtils.getCookieValue(request, "sso-token");
		//如果没有token，未登录状态，直接放行
		if(StringUtils.isBlank(token)) {
			return true;
		}
		//取到token，需要调用sso系统的服务，根据token取用户信息
		N3Result e3Result=tokenService.getUserByToken(token);
		//没有取到用户信息，登录过期，直接放行
		if(e3Result.getStatus() != 200) {
			return true;
		}
		//取到用户信息，登录状态
		TbUser tbUser=(TbUser) e3Result.getData();
		//把用户信息放到request中国，只需要在Controller中判断是否包含user信息。放行
		request.setAttribute("user", tbUser);
		return true;
	}

}
