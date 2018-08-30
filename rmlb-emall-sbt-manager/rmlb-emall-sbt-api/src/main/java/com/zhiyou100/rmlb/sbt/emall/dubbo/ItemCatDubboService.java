package com.zhiyou100.rmlb.sbt.emall.dubbo;

import java.util.List;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUITreeNode;

/** 商品类目  */
public interface ItemCatDubboService {

	/** 获取类目的列表 */
	List<EasyUITreeNode> getCatList(long parentId);
}
