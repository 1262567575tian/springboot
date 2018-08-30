package com.zhiyou100.rmlb.sbt.emall.dao;

import java.util.List;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.SearchItem;




  
public interface SearchItemMapper {
	
	List<SearchItem> getItemByList();
	SearchItem getItemById(long id);
}
