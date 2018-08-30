package com.zhiyou100.rmlb.emall.sbt.manager.web.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUIDataGridResult;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.content.dubbo.ContentService;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbContent;



@Controller
public class ContentController {
	Logger log = Logger.getLogger(ContentController.class);
	@Reference(version="1.0.0")
	private ContentService contentService;
	
	/**
	 * 内容增加
	 * */
	@RequestMapping("/content/save")
	@ResponseBody
	public N3Result addContent(TbContent content) {
		log.info("save  content======"+content.getId());
		N3Result result=contentService.addContent(content);
		log.info("save  content======finish");
		return result;
	}
	
	/**
	 * 内容列表
	 * */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult contentList( long categoryId,int page,int rows) {
		return contentService.contentList(categoryId, page, rows);
	}
	
	/**
	 * 内容修改
	 * */
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public N3Result updateContent(TbContent content) {
		return contentService.updateContent(content);
	}
	
	/**
	 * 内容删除
	 * */
	@RequestMapping("/content/delete")
	@ResponseBody
	public N3Result deleteContent(String ids) {
		return contentService.deleteContent(ids);
	}
}
