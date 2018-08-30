package com.zhiyou100.rmlb.sbt.emall.dubbo.impl;

import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;

//import javax.jms.Destination;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.Session;

//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.core.MessageCreator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
//import com.zhiyou100.rmlb.sbt.emall.common.jedis.JedisClient;
//import com.zhiyou100.rmlb.sbt.emall.common.utils.JsonUtils;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.common.utils.IDUtils;
import com.zhiyou100.rmlb.sbt.emall.common.utils.JsonUtils;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUIDataGridResult;
import com.zhiyou100.rmlb.sbt.emall.dao.TbItemDescMapper;
import com.zhiyou100.rmlb.sbt.emall.dao.TbItemMapper;
import com.zhiyou100.rmlb.sbt.emall.dubbo.ItemDubboService;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItem;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItemDesc;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItemExample;

/**
 * 商品管理服务
 */
@Service(version="1.0.0" )
public class ItemDubboServiceImpl implements ItemDubboService {
//	Logger log = Logger.getLogger(ItemDubboServiceImpl.class);
	private Logger log = LogManager.getLogger(ItemDubboServiceImpl.class);

//	@Value("${REDIS_ITEM_PRE}")
//	private String REDIS_ITEM_PRE;
//
//	@Value("${ITEM_CACHE_EXPIRE}")
//	private Integer ITEM_CACHE_EXPIRE;

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemDescMapper itemDescMapper;

//	@Autowired
//	private JedisClient jedisClient;
//
	@Autowired
	private JmsTemplate jmsTemplate;// activemq中其中一个接口
//
/*	@Autowired
	private Topic topic;*/
	
//	@Autowired
//	private Destination topicDestination;// activemq创建一个添加Destination
//
//	@Autowired
//	private Destination deleteTopicDestination;// activemq创建一个删除Destination
//
//	@Autowired
//	private Destination updateTopicDestination;// activemq创建一个修改Destination

//	@Resource
//	private RedisAtomicLong redisAtomicLong;
//	@Cacheable(value = "user", key = "'user'.concat(#id)")
	@Override
	@Cacheable(value="item", key="\"item\" + #id + \"BASE\"" )
	public TbItem getItemById(long id) {
		TbItem item = itemMapper.selectByPrimaryKey(id);
		return item;
	}

	@Override
	public TbItem getItemById_1_1(long itemId) {
		// 查询缓存
		/*try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + "BASE");
			if (StringUtils.isNoneBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		// 根据主键查询
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		/*
		 * TbItemExample example = new TbItemExample(); Criteria createCriteria
		 * =example.createCriteria(); // 设置查询条件
		 * createCriteria.andIdEqualTo(itemId); // 执行查询 List<TbItem> items =
		 * (List<TbItem>) itemMapper.selectByExample(example); if (items != null
		 * && items.size() > 0) { return items.get(0); }
		 */
		/*try {
			// 把查询结果添加到缓存中
			jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + "BASE", JsonUtils.objectToJson(item));
			// 设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + "BASE", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return item;
	}

	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);

		// 创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);

		return result;
	}

	@Override
	public N3Result addItem(TbItem item, String desc) {
		// 1. 生成商品id
		long itemId = IDUtils.genItemId();

		// 2. 补全TbItem对象属性
		item.setId(itemId);
		// 商品状态：1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		// 3. 向商品表tb_item插入数据
		itemMapper.insert(item);
		// 4. 创建一个TbItemDesc对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 5. 补全TbItemDesc的属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		// 6. 向商品描述表tb_item_desc添加数据
		itemDescMapper.insert(itemDesc);
		// MQ发送一个商品添加消息
		sendMessageAddItem(itemId);
		// 7. NJResult.ok();
		return N3Result.ok();
	}

	@Override
	public TbItemDesc getItemDescById_1_1(long itemId) {
		log.info("====getItemDescById_1_1商品描述的查询START====" + itemId);
		// // 查询缓存
		/*try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + "DESC");
			if (StringUtils.isNoneBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				log.info("========商品描述的缓存查询内容========" + tbItemDesc);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		// 从数据库中查询tbItemDesc
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		log.info("========商品描述的数据库查询内容========" + tbItemDesc);
		/*try {
			// 如果缓存没有添加到缓存中
			jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + "DESC", JsonUtils.objectToJson(tbItemDesc));
			// 设置缓存内容过期时间
			jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + "DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		log.info("====getItemDescById_1_1商品描述的查询END====");
		return tbItemDesc;
	}

	@Override
	public N3Result deleteItem(long[] ids) {
		log.info("====deleteItem删除商品START====" + ids);
		// 删除商品信息
		itemMapper.deleteItem(ids);
		// 删除描述
		int result = itemDescMapper.deleteItemDesc(ids);
		log.info("========删除商品描述的结果========" + result);
		// MQ发送一组删除信息
		sendMessageDeleteItem(ids);
		// 查询缓存 如果有缓存删除缓存
		log.info("========删除商品缓存开始========");
		deleteItemCache(ids);
		log.info("========删除商品缓存结束========");

		log.info("====deleteItem删除商品END====");
		return N3Result.ok();
	}

	@Override
	public N3Result shelvesItem(long[] ids) {
		itemMapper.shelvesItem(ids);
		return N3Result.ok();
	}

	@Override
	public N3Result offShelfItem(long[] ids) {
		itemMapper.offShelfItem(ids);
		return N3Result.ok();
	}

	@Override
	public N3Result getItemDescById_1_0(long id) {
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(id);
		return N3Result.ok(itemDesc);
	}

	@Override
	public N3Result updateItem(TbItem tbItem, String desc) {
		log.info("====updateItem商品更新START====" + tbItem + desc);
		// 补全item的属性
		tbItem.setStatus((byte) 1);// 1-正常，2-下架，3-删除
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		// 向商品表插入数据
		itemMapper.updateByPrimaryKey(tbItem);
		// 创建商品表描述表对应的pojo对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		// 补全desc的属性
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setItemId(tbItem.getId());
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		// 向商品描述表插入数据
		int result = itemDescMapper.updateByPrimaryKeyWithBLOBs(tbItemDesc);
		log.info("========updateItem向商品描述表插入数据========" + result);
		// MQ发送更改消息
		sendMessageItemUpdate(tbItem.getId());
		log.info("========updateItem_CACHE_START========");
		// 查询缓存 如果商品更改信息，更新redis的缓存
		updateItemCache(tbItem.getId());
		log.info("========updateItem_CACHE_END========");

		log.info("====updateItem商品更新END====");
		// 返回成功
		return N3Result.ok();
	}

	
	@Override
	public N3Result getItemDescById_1_2(long itemId) {
		log.info("====getItemDescById_1_2商品描述的查询START====" + itemId);
		// 查询缓存
		/*try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + "DESC");
			if (StringUtils.isNoneBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				log.info("========商品描述的缓存查询内容========" + tbItemDesc);
				return N3Result.ok(tbItemDesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		// 从数据库中查询tbItemDesc
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		log.info("========商品描述的数据库查询内容========" + tbItemDesc);
		getItemDescById_1_2Cache(itemId, tbItemDesc);
		log.info("====getItemDescById_1_2商品描述的查询END====");
		return N3Result.ok(tbItemDesc);
	}

	/** 添加缓存：商品描述 */
	private void getItemDescById_1_2Cache(long itemId, TbItemDesc tbItemDesc) {
		/*try {
			// 如过缓存没有添加到缓存中
			log.info("========商品描述缓存对象========: " + jedisClient + " :====" + REDIS_ITEM_PRE);
			jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + "DESC", JsonUtils.objectToJson(tbItemDesc));
			// 设置缓存内容过期时间
			jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + "DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	/** 缓存处理：deleteItem */
	private void deleteItemCache(long[] ids) {
		/*for (long id : ids) {
			try {
				String json = jedisClient.get(REDIS_ITEM_PRE + ":" + id + "BASE");
				String json2 = jedisClient.get(REDIS_ITEM_PRE + ":" + id + "DESC");
				if (StringUtils.isNoneBlank(json)) {
					jedisClient.del(REDIS_ITEM_PRE + ":" + id + "BASE");
				}
				if (StringUtils.isNoneBlank(json2)) {
					jedisClient.del(REDIS_ITEM_PRE + ":" + id + "DESC");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}

	/** 缓存处理：updateItem */
	private void updateItemCache(long itemId) {
		/*try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + "BASE");
			if (StringUtils.isNoneBlank(json)) {
				// 根据主键查询
				TbItem item = itemMapper.selectByPrimaryKey(itemId);
				// 从数据库中查询tbItemDesc
				TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
				// 把最新数据添加到缓存中
				jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + "BASE", JsonUtils.objectToJson(item));
				jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + "DESC", JsonUtils.objectToJson(itemDesc));
				// 设置过期时间
				jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + "BASE", ITEM_CACHE_EXPIRE);
				jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + "DESC", ITEM_CACHE_EXPIRE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	
	/** MQ发送商品添加消息 */
	private void sendMessageAddItem(long itemId) {
		jmsTemplate.send("topicDestination" , new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				log.info("========发送一个商品添加消息内容========" + itemId);
				return session.createTextMessage(itemId + "");
			}
		});
	}

	/** MQ发送删除信息 */
	private void sendMessageDeleteItem(long[] ids) {
		/*jmsTemplate.send(deleteTopicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				String id = JsonUtils.objectToJson(ids);
				log.info("========发送一个商品删除消息内容========" + id);
				return session.createTextMessage(id);
			}
		});*/
	}
	
	/** 发送更改消息 */
	private void sendMessageItemUpdate(long id) {
		/*jmsTemplate.send(updateTopicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				log.info("========发送一个商品更新消息内容========" + id);
				return session.createTextMessage(id + "");
			}
		});*/
	}
}
