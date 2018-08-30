package com.zhiyou100.rmlb.sbt.emall.order.dubbo;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.pojo.OrderInfo;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbOrder;

public interface OrderService {
	
	N3Result createOrder(OrderInfo orderInfo);
	TbOrder getOrder(String orderId);

}
