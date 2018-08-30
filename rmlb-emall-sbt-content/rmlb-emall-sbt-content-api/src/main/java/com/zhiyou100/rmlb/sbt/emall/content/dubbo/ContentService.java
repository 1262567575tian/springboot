package com.zhiyou100.rmlb.sbt.emall.content.dubbo;

import java.util.List;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUIDataGridResult;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbContent;



public interface ContentService {
	N3Result addContent(TbContent content);
	EasyUIDataGridResult contentList(long categoryId,int page,int rows);
	N3Result updateContent(TbContent content);
	N3Result deleteContent(String ids);
	List<TbContent> contentList(long cid);
}
