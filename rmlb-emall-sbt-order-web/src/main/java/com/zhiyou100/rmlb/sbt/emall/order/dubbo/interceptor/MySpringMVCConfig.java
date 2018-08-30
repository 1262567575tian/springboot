package com.zhiyou100.rmlb.sbt.emall.order.dubbo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@SpringBootConfiguration
public class MySpringMVCConfig implements WebMvcConfigurer{

	@Autowired
	private LoginInterceptor interceptor;
	
	/**
	 * 自定义拦截器
	 * 继承WebMvcConfigurationSupport，重写addInterceptors方法
	 * */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//String[] excludes = new String[] {"/static/**"} ;
		registry.addInterceptor(interceptor).addPathPatterns("/order/**");
	}
	

/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}*/
}
