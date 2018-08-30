package com.zhiyou100.rmlb;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiyou100.rmlb.sbt.emall.dao.TbItemMapper;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItem;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItemExample;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMybatis {
	
	private Logger log = LogManager.getLogger(TestMybatis.class);

	@Autowired
	private TbItemMapper tbItemMapper;

	@Test
	public void testPagesplit() {
		// 设置分页信息
		//		PageHelper.offsetPage(1, 10);
		PageHelper.startPage(1, 20);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
//		System.err.println(list);
		log.info(list);
		// 取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		/*System.err.println(pageInfo.getTotal());
		System.err.println(pageInfo.getPages());
		System.err.println(pageInfo.getPageNum());
		System.err.println(pageInfo.getPageSize());*/
		log.info(pageInfo.getTotal());
		log.info(pageInfo.getPages());
		log.info(pageInfo.getPageNum());
		log.info(pageInfo.getPageSize());
	}
}
