package com.zhiyou100.rmlb.sbt.emall.content.dubbo.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUIDataGridResult;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.content.dubbo.ContentService;
import com.zhiyou100.rmlb.sbt.emall.dao.TbContentMapper;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbContent;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbContentExample;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbContentExample.Criteria;



/**
 * 内容管理
 */
@Service(version="1.0.0")
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	
	@Override
	@CachePut(value="content", key="\"CONTENT\" + #content.id + \"BASE\"")
	public N3Result addContent(TbContent content) {
		// 补全对象属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		// 插入数据
		contentMapper.insert(content);
		
		// 返回结果
		return N3Result.ok();
	}

	@Override
	public EasyUIDataGridResult contentList(long categoryId, int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbContentExample contentExample = new TbContentExample();
		Criteria criteria = contentExample.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> contents = contentMapper.selectByExample(contentExample);
		// 创建返回值对象
		EasyUIDataGridResult gridUtil = new EasyUIDataGridResult();
		gridUtil.setRows(contents);
		// 取分页结果
		PageInfo<TbContent> pageInfo = new PageInfo<>(contents);
		gridUtil.setTotal(pageInfo.getTotal());
		return gridUtil;
	}

	@Override
	@Cacheable(value="content", key="\"CONTENT\" + #content.getId() + \"BASE\"" )
	public N3Result updateContent(TbContent content) {
		// 根据id获取需要修改的对象
		TbContent vo = contentMapper.selectByPrimaryKey(content.getId());
		// 补全对象属性、
		content.setCreated(vo.getCreated());
		content.setUpdated(new Date());
		// 执行更新
		contentMapper.updateByPrimaryKeySelective(content);
		// 同步缓存
		//jedis.hdel(CONTENT_REDIS_LIST, content.getCategoryId().toString());
		// 返回结果
		return N3Result.ok();
	}

	@Override
	//@CacheEvict(key="\"CONTENT\" + #ids + \"BASE\"" )
	public N3Result deleteContent(String ids) {
		// 分割ids
		String[] split = ids.split(",");
		// 循环遍历删除
		for (String id : split) {
			TbContent content=contentMapper.selectByPrimaryKey(Long.valueOf(id));
			// 同步缓存
			//jedis.hdel(CONTENT_REDIS_LIST, content.getCategoryId().toString());
			contentMapper.deleteByPrimaryKey(Long.valueOf(id));
			
			
			
		}
		
		return N3Result.ok();
	}

	@Override
	@Cacheable(value="content", key="\"CONTENT\" + #cid + \"BASE\"" )
	public List<TbContent> contentList(long cid) {
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> contentList = contentMapper.selectByExampleWithBLOBs(example);
		// 查询结果插入到redis
		
		return contentList;
	}

}
