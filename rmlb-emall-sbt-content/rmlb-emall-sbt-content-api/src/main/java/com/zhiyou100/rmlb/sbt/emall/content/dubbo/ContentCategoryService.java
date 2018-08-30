package com.zhiyou100.rmlb.sbt.emall.content.dubbo;

import java.util.List;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUITreeNode;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;

public interface ContentCategoryService {
	List<EasyUITreeNode> getContentCatgory(long parentId);
	N3Result addContentcate(long parentId,String name);
	N3Result updateContentcate(long id,String name);
	N3Result deleteContentcate(long id);
}
