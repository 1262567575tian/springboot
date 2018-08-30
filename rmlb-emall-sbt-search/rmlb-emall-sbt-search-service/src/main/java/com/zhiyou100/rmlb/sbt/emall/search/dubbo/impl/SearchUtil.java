package com.zhiyou100.rmlb.sbt.emall.search.dubbo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.SearchItem;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.SearchResult;


@Repository
public class SearchUtil {
	
	@Autowired
	private SolrClient cloudSolrClient;
	
	public SearchResult search(SolrQuery query) throws Exception {
		//根据条件查询索引库
		QueryResponse response=cloudSolrClient.query(query);
		//取查询结果总记录数
		SolrDocumentList documentList=response.getResults();
		System.out.println("**********"+documentList.size());
		long numFound=documentList.getNumFound();
		//创建一个返回结果对象
		SearchResult searchResult=new SearchResult();
		searchResult.setRecourdCount((int) numFound);
		//创建一个商品列表对象
		List<SearchItem> itemList=new ArrayList<>();
		//取商品列表及高亮结果
		Map<String,Map<String,List<String>>> highlighting=response.getHighlighting();
		for (SolrDocument document : documentList) {
			//商品信息
			SearchItem item=new SearchItem();
			//System.out.println("********"+Long.parseLong(document.get("item_price").toString()));
			//System.out.println("**********"+document.get("item_category_name"));
			//item.setCategoryName(document.get("category_name").toString());
			item.setId( document.get("id").toString());
			item.setImage( document.get("item_image").toString());
			item.setPrice(Long.parseLong(document.get("item_price").toString()));
			//item.setSellPoint(document.get("sell_point").toString());
			//高亮结果
			List<String> list=highlighting.get(document.get("id")).get("item_title");
			String itemTitle="";
			if(list!=null && list.size()>0) {
				itemTitle=list.get(0);
			}else {
				itemTitle=(String) document.get("item_title");
			}
			item.setTitle(itemTitle);
			//添加到商品列表
			itemList.add(item);
		}
		//把列表添加到返回结果中
		searchResult.setItemList(itemList);
		return searchResult;
	}
}
