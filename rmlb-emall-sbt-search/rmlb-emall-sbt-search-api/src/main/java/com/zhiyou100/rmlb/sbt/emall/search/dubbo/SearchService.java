package com.zhiyou100.rmlb.sbt.emall.search.dubbo;


import com.zhiyou100.rmlb.sbt.emall.common.pojo.SearchResult;

public interface SearchService {
	SearchResult search(String keyword, int page, int rows) throws Exception;
}
