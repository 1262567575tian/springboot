package com.zhiyou100.rmlb.sbt.emall.search.listener;



import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.SearchItem;
import com.zhiyou100.rmlb.sbt.emall.dao.SearchItemMapper;





@Component
public class ItemChangeListener implements MessageListener{
	
	@Autowired
	private SearchItemMapper searchMapper;
	@Autowired
	private SolrClient solrClient;
	@Override
	@JmsListener(destination="topicDestination")
	public void onMessage(Message message) {
		try {
			//从消息中取商品id
			TextMessage textMessage=(TextMessage) message;
			String text=textMessage.getText();
			Long id=new Long(text);
			//根据商品id查询商品信息
			SearchItem searchItem=searchMapper.getItemById(id);
			//创建文档对象
			SolrInputDocument document=new SolrInputDocument();
			//向文档中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSellPoint());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategoryName());
			//写入索引库
			solrClient.add(document);
			//提交
			solrClient.commit();
		} catch (Exception e) {
		e.printStackTrace();
		}
	}

}
