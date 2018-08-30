package com.zhiyou100.rmlb.emall.sbt.manager.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUITreeNode;
import com.zhiyou100.rmlb.sbt.emall.dubbo.ItemCatDubboService;

/** 商品类目控制器 */
@Controller
public class ItemCatController {

	@Reference(version="1.0.0")
	private ItemCatDubboService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(value="id", defaultValue="0") Long parentId) {
		return itemCatService.getCatList(parentId);
	}
}
