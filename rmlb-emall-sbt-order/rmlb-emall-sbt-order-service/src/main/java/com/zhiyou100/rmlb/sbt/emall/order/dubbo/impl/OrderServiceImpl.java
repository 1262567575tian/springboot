package com.zhiyou100.rmlb.sbt.emall.order.dubbo.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.dao.TbOrderItemMapper;
import com.zhiyou100.rmlb.sbt.emall.dao.TbOrderMapper;
import com.zhiyou100.rmlb.sbt.emall.dao.TbOrderShippingMapper;
import com.zhiyou100.rmlb.sbt.emall.order.dubbo.OrderService;
import com.zhiyou100.rmlb.sbt.emall.pojo.OrderInfo;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbOrder;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbOrderItem;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbOrderShipping;

import redis.clients.jedis.JedisCluster;



/**
 * 订单处理服务
 */
@Service(version="1.0.0")
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Autowired
	private JedisCluster jedisClient;
	

	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;

	@Override
	public N3Result createOrder(OrderInfo orderInfo) {
		// 生成订单号。使用redis的incr生成
		// 如果redis不存在ORDER_ID_GEN_KEY
		if (!jedisClient.exists("ORDER_ID_GEN")) {
			// 创建一个key为ORDER_ID_GEN_KEY 值为ORDER_ID_START的数据
			jedisClient.set("ORDER_ID_GEN", ORDER_ID_START);
		}
		String orderId = jedisClient.incr("ORDER_ID_GEN").toString();
		System.out.println(orderId);
		// 补全TBorder属性
		orderInfo.setOrderId(orderId);
		// 1.未付款2.已付款3.未发货4.已发货5.交易成功6.交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		// 插入订单表
		orderMapper.insert(orderInfo);
		// 向订单明细表插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			String detailId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			// 补全pojo属性
			tbOrderItem.setId(detailId);
			tbOrderItem.setOrderId(orderId);
			// 插入明细表
			orderItemMapper.insert(tbOrderItem);
		}
		// 向订单物流表插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		
		/** 清空购物车放在表现层做可以降低耦合度 */
		
		// 返回E3Result，包含订单号
		return N3Result.ok(orderId);
	}

	@Override
	public TbOrder getOrder(String orderId) {
		
		return orderMapper.selectByPrimaryKey(orderId);
	}

}
