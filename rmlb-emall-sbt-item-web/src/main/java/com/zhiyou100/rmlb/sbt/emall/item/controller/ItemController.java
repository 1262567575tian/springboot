package com.zhiyou100.rmlb.sbt.emall.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.dubbo.ItemDubboService;
import com.zhiyou100.rmlb.sbt.emall.item.pojo.Item;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItem;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItemDesc;



@Controller
public class ItemController {
	
	@Reference(version="1.0.0")
	private ItemDubboService itemService;
	
	@RequestMapping("/item/{id}")
	public String getItem(@PathVariable long id ,Model model) {
		TbItem tbItem=itemService.getItemById(id);
		Item item=new Item(tbItem);
		TbItemDesc itemDesc=itemService.getItemDescById_1_1(id);
		model.addAttribute("item",item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
