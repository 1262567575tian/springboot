package com.zhiyou100.rmlb.sbt.emall.order.dubbo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.cat.dubbo.CartService;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.common.utils.CookieUtils;
import com.zhiyou100.rmlb.sbt.emall.common.utils.JsonUtils;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItem;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUser;
import com.zhiyou100.rmlb.sbt.emall.sso.dubbo.TokenService;


/**
 * 用户登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

	
	
	private String SSO_URL="http://localhost:8031";
	
	@Reference(version="1.0.0")
	private TokenService tokenService;
	
	@Reference(version="1.0.0")
	private CartService cartService;
	
	/**
	 * 前处理，执行handler之前执行此方法 返回true:放行, false:拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//从cookie中取token
		String token = CookieUtils.getCookieValue(request, "sso-token");
		//判断token是否存在
		if(StringUtils.isBlank(token)) {
			//如果token不存在，未登录状态，跳转到sso系统的登录页面。用户登录成功后跳转到当前请求的url
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL()); 
			//拦截
			return false;
		}
		//如果token存在，需要用sso系统的服务，根据token取用户信息
		N3Result e3Result = tokenService.getUserByToken(token);
		//如果取不到，登录已经过期，需要登录
		if(e3Result.getStatus() != 200) {
			//如果token不存在，未登录状态，跳转到sso系统的登录页面。用户登录成功后跳转到当前请求的url
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL()); 
			//拦截
			return false;
		}
		//如果取到用户信息，是登录状态，需要把用户信息写入request
		TbUser user = (TbUser) e3Result.getData();
		System.out.println("--------------"+user.getId());
		request.setAttribute("user", user);
		//判断cookie中是否有购物车数据，如果有就合并到服务端
		String jsonCartList = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isNoneBlank(jsonCartList)) {
			//合并购物车
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartList, TbItem.class));
		}
		//放行
		return true;
	}

	/**
	 * handler执行之后，返回ModelAndView之前
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 返回ModelAndView之后，可以在此处理异常
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
