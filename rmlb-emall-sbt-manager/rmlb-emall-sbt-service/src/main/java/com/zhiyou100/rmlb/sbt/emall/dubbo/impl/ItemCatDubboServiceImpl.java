package com.zhiyou100.rmlb.sbt.emall.dubbo.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUITreeNode;
import com.zhiyou100.rmlb.sbt.emall.dao.TbItemCatMapper;
import com.zhiyou100.rmlb.sbt.emall.dubbo.ItemCatDubboService;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItemCat;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItemCatExample;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItemCatExample.Criteria;

@Service(version="1.0.0" )
public class ItemCatDubboServiceImpl implements ItemCatDubboService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getCatList(long parentId) {
		// 1. 根据parentId查询节点列表
		TbItemCatExample example = new TbItemCatExample();
		// 设置查询条件  where parentId = ?
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		// 2. 将list转换为EasyUITreeNode
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent() ? "closed" : "open");
			
			resultList.add(node);
		}
		// 3. 返回
		return resultList;
	}

}
