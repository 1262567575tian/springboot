package com.zhiyou100.rmlb.sbt.emall.search.dubbo.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.SearchResult;
import com.zhiyou100.rmlb.sbt.emall.search.dubbo.SearchService;




@Service(version="1.0.0")
public class SearchServiceImpl implements SearchService{

	@Autowired
	private SearchUtil search;
	
	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		//创建一个SolrQuery对象
		System.out.println(keyword);
		SolrQuery query=new SolrQuery();
		//设置查询条件
		query.setQuery(keyword);
		//设置分页条件
		if(page<=0)page=1;
		query.setStart((page-1)*rows);
		//设置rows
		query.setRows(rows);
		//设置默认搜索域
		query.set("df", "item_title");
		//开启高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		//调用util查询
		SearchResult result=search.search(query);
		System.out.println("***********"+result);
		//计算总页数
		long recordCount=result.getRecourdCount();
		int totalPage=(int) (recordCount/rows);
		if(recordCount%rows>0) {
			totalPage++;
		}
		//添加到返回结果
		result.setTotalPages(totalPage);
		return result;
	}

}
