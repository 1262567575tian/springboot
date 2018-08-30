package com.zhiyou100.rmlb.sbt.emall.cat.dubbo.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyou100.rmlb.sbt.emall.cat.dubbo.CartService;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.common.utils.JsonUtils;
import com.zhiyou100.rmlb.sbt.emall.dao.TbItemMapper;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItem;

import redis.clients.jedis.JedisCluster;

/**
 * 添加购物车服务处理
 */
@Service(version="1.0.0")
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisCluster jedisCluster;
	@Autowired
	private TbItemMapper tbItemMapper;

	@Override
	public N3Result addCart(long userId, long itemId, int num) {
		// 向redis中添加购物车
		// 数据类型是hash key：用户id field：商品id value：商品信息
		// 判断商品是否存在
		Boolean hexists = jedisCluster.hexists("CART:" + userId, itemId + "");
		// 如果存在数量增加
		if (hexists) {
			String json = jedisCluster.hget("CART:" + userId, itemId + "");
			// 把json转换成TbItem
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum() + num);
			jedisCluster.hset("CART:" + userId, itemId + "", JsonUtils.objectToJson(item));
			return N3Result.ok();
		}
		// 如果不存在，根据商品id取商品信息
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		// 设置购物车数量
		tbItem.setNum(num);
		// 取一张图片
		String image = tbItem.getImage();
		if (StringUtils.isNoneBlank(image)) {
			tbItem.setImage(image.split(",")[0]);
		}
		// 添加到购物车列表
		jedisCluster.hset("CART:" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return N3Result.ok();
	}

	@Override
	public N3Result mergeCart(long userId, List<TbItem> list) {
		//遍历商品列表
		//把列表添加到购物车
		//判断购物车是否有此商品
		//如果有，数量相加
		//如果没有添加新商品
		for (TbItem tbItem : list) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return N3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		//根据用户id去redis里面的value
		List<String> hvals = jedisCluster.hvals("CART:" + userId);
		List<TbItem> list=new ArrayList<>();
		for (String string : hvals) {
			//创建一个Tbitem对象
			TbItem item=JsonUtils.jsonToPojo(string, TbItem.class);
			list.add(item);
		}
		return list;
	}

	@Override
	public N3Result updateCartNum(long userId, long itemId, int num) {
		//从redis中取商品信息
		String json=jedisCluster.hget("CART:" + userId, itemId + "");
		TbItem item=JsonUtils.jsonToPojo(json, TbItem.class);
		//更新商品数量
		item.setNum(num);
		//写入redis
		jedisCluster.hset("CART:" + userId, itemId + "", JsonUtils.objectToJson(item));
		return N3Result.ok();
	}

	@Override
	public N3Result deleteCart(long userId, long itemId) {
		//删除购物车商品
		jedisCluster.hdel("CART:" + userId, itemId + "");
		return N3Result.ok();
	}

	@Override
	public N3Result clearCartItem(Long userId) {
		jedisCluster.del("CART:" + userId);
		return N3Result.ok();
	}

	

}
