package com.zhiyou100.rmlb.sbt.emall.content.dubbo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUITreeNode;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.content.dubbo.ContentCategoryService;
import com.zhiyou100.rmlb.sbt.emall.dao.TbContentCategoryMapper;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbContentCategory;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbContentCategoryExample;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbContentCategoryExample.Criteria;



/**
 * 内容类目管理
 */
@Service(version="1.0.0")
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCatgory(long parentId) {
		// 根据parentId查询子节点类目
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		// 转换成EasyUITreeNodeUtil的列表
		List<EasyUITreeNode> easyUITreeNodelist = new ArrayList<>();
		for (TbContentCategory category : list) {
			EasyUITreeNode vo = new EasyUITreeNode();
			vo.setId(category.getId());
			vo.setText(category.getName());
			vo.setState(category.getIsParent() ? "closed" : "open");
			// 添加到列表
			easyUITreeNodelist.add(vo);
		}
		return easyUITreeNodelist;
	}

	@Override
	public N3Result addContentcate(long parentId, String name) {
		// 设置一个tb_content_category表对应的pojo对象
		TbContentCategory category = new TbContentCategory();
		// 设置pojo对象的属性
		category.setParentId(parentId);
		category.setName(name);
		// 1.正常、2.下架、3.删除
		category.setStatus(1);
		category.setSortOrder(1);
		category.setIsParent(false);
		category.setCreated(new Date());
		category.setUpdated(new Date());
		// 插入到数据库
		contentCategoryMapper.insert(category);
		// 判断父节点isparent的属性，如果不是true改为true
		TbContentCategory vo = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!vo.getIsParent()) {
			vo.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(vo);
		}
		// 返回结果，返回N3Result包含pojo
		return N3Result.ok(category);
	}

	@Override
	public N3Result updateContentcate(long id, String name) {
		TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
		category.setName(name);
		contentCategoryMapper.updateByPrimaryKey(category);
		return N3Result.ok();
	}

	@Override
	public N3Result deleteContentcate(long id) {
		// 根据id获取对象
		TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
		long parentId=category.getParentId();
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		// 判断是否为父节点，如果是不允许删除
		if (category.getIsParent()) {
			return N3Result.build(1, "该节点下面还有其他节点，删除失败");
		} else {
			// 删除子节点
			contentCategoryMapper.deleteByPrimaryKey(id);
			// 根据对象的parentId求出父节点下的子类目数量
				TbContentCategoryExample example=new TbContentCategoryExample();
		        Criteria Criteria = example.createCriteria();
		        Criteria.andParentIdEqualTo(parentId);
			long count=contentCategoryMapper.countByExample(example);
			// 判断如果为true修改父节点的isparent为false
			if (count == 0) {
					contentCategory.setIsParent(false);
					contentCategoryMapper.updateByPrimaryKey(contentCategory);
			}
			
		}

		return N3Result.ok();
	}

}
