package com.zhiyou100.rmlb.emall.sbt.content.web.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.content.dubbo.ContentService;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbContent;


/**
 * 首页展示Controller
 * */

@Controller
public class IndexController {
	@Value("${CONTENT_LUNBO_ID}")
	private long CONTENT_LUNBO_ID;
	
	@Reference(version="1.0.0")
	private ContentService contentService;
	
	
	@RequestMapping("/")
	public String showIndex(Model model) {
		List<TbContent> ad1List=contentService.contentList(CONTENT_LUNBO_ID);
		model.addAttribute("ad1List", ad1List);
		return "index";
	}
}
