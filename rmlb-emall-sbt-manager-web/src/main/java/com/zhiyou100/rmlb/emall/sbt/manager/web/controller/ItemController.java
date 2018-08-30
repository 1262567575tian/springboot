package com.zhiyou100.rmlb.emall.sbt.manager.web.controller;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUIDataGridResult;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.dubbo.ItemDubboService;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItem;
import com.zhiyou100.rmlb.sbt.emall.search.dubbo.SearchItemService;

@Controller
public class ItemController {
	Logger log = Logger.getLogger(ItemController.class);

	@Reference(version="1.0.0")
	private ItemDubboService itemService;
	
	@Reference(version="1.0.0")
	private SearchItemService searchService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	private TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		return itemService.getItemList(page, rows);
	}
	
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	public N3Result saveItem(TbItem item, String desc) {
		log.info("save  item======");
		N3Result result = itemService.addItem(item, desc);
		log.info("save  item======  finish======");
		return result;
	}
	
	/** 商品删除 */
	@RequestMapping(value="/rest/item/delete",method=RequestMethod.POST)
	@ResponseBody
	public N3Result deleteItem(long[] ids) {
		System.out.println(ids);
		N3Result result = itemService.deleteItem(ids);
		return result;
	}
	
	/** 商品下架 */
	@RequestMapping(value="/rest/item/instock",method=RequestMethod.POST)
	@ResponseBody
	public N3Result shelfItem(long[] ids) {
		N3Result result = itemService.offShelfItem(ids);
		return result;
	}
	
	/** 商品上架 */
	@RequestMapping(value="/rest/item/reshelf",method=RequestMethod.POST)
	@ResponseBody
	public N3Result shelvesItem(long[] ids) {
		N3Result result = itemService.shelvesItem(ids);
		return result;
	}
	
	/** 商品修改 */
	@RequestMapping(value="/rest/item/update",method=RequestMethod.POST)
	@ResponseBody
	public N3Result updateItem(TbItem item , String desc) {
		
		return itemService.updateItem(item,desc);
	}
	
	/** 商品回显跳转编辑页面 */
	@RequestMapping(value="/rest/page/item-edit")
	public String display() {

		return "item-edit";
	}
	
	/** 根据id查商品描述  */
	@RequestMapping(value="/rest/item/query/item/desc/{id}")
	@ResponseBody
	public N3Result selectDescById(@PathVariable long id) {
		 N3Result result = itemService.getItemDescById_1_2(id);
		 return result;
	}
	
	/**
	 * 商品导入索引库
	 * */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public N3Result importItem() {
		return searchService.importItem();
	}
}
