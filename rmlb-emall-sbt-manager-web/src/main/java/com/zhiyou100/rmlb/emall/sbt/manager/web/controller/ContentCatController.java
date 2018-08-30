package com.zhiyou100.rmlb.emall.sbt.manager.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUITreeNode;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.content.dubbo.ContentCategoryService;



/**
 * 后台内容管理类目
 * */
@Controller
public class ContentCatController {
	@Reference(version="1.0.0")
	private ContentCategoryService categoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategory(@RequestParam(name="id",defaultValue="0")long parentId) {
		return categoryService.getContentCatgory(parentId);
	}
	
	/**
	 * 类目子节点添加
	 * */
	
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public N3Result AddContentCate(long parentId ,String name) {
		return categoryService.addContentcate(parentId, name);
	}
	
	/**
	 * 类目子节点修改
	 * */
	@RequestMapping(value="/content/category/update",method=RequestMethod.POST)
	@ResponseBody
	public N3Result updateContentCate(long id,String name) {
		return categoryService.updateContentcate(id, name);
	}
	
	/**
	 * 类目节点删除
	 * */
	@RequestMapping(value="/content/category/delete/",method=RequestMethod.POST)
	@ResponseBody
	public N3Result deleteContentCate(long id) {
		return categoryService.deleteContentcate(id);
	}
	
}
