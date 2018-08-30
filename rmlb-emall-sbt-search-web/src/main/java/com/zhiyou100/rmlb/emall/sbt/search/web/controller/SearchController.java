package com.zhiyou100.rmlb.emall.sbt.search.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.SearchResult;
import com.zhiyou100.rmlb.sbt.emall.search.dubbo.SearchService;



@Controller
public class SearchController {
	
	@Reference(version="1.0.0")
	private SearchService searchSerice;
	
	@RequestMapping(value="/search",method=RequestMethod.POST)
	public String searchItemList(String keyword,@RequestParam(defaultValue="1")Integer page,Model model) throws Exception {
		//keyword=new String(keyword.getBytes("iso-8859-1"), "utf-8");
		//查询商品列表
		System.out.println("*******"+keyword);
		SearchResult result=searchSerice.search(keyword, page, 60);
		//把结果传递给页面
		model.addAttribute("query",keyword);
		model.addAttribute("totalPages", result.getTotalPages());
		model.addAttribute("page",page);
		model.addAttribute("recourdCount", result.getRecourdCount());
		model.addAttribute("itemList", result.getItemList());
		return "search";
	}
}
