package com.zhiyou100.rmlb.sbt.emall.search.dubbo.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.SearchItem;
import com.zhiyou100.rmlb.sbt.emall.dao.SearchItemMapper;
import com.zhiyou100.rmlb.sbt.emall.search.dubbo.SearchItemService;



@Service(version="1.0.0")
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper seachMapper;
	
	@Autowired
	private SolrClient solrClient;

	@Override
	public N3Result importItem() {
		try {
			//查询商品列表
			List<SearchItem> list=seachMapper.getItemByList();
			
			//遍历列表
			for (SearchItem searchItem : list) {
				//创建文档对象
				SolrInputDocument document=new SolrInputDocument();
				//向文档中添加域
				System.out.println("-------------"+searchItem.toString());
				System.out.println("---------"+searchItem.getCategoryName());
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSellPoint());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategoryName());
				//写入索引库
				solrClient.add(document);
			}
			//提交
			solrClient.commit();
			//返回结果
			return N3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return N3Result.build(500, "商品导入失败");
		}
	}

	}
	

	
