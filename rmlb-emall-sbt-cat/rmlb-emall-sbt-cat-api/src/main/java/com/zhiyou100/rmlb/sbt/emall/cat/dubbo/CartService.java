package com.zhiyou100.rmlb.sbt.emall.cat.dubbo;

import java.util.List;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItem;



public interface CartService {
	
	N3Result addCart(long userId,long itemId,int num);
	N3Result mergeCart(long userId,List<TbItem> list);
	List<TbItem> getCartList(long userId);
	N3Result updateCartNum(long userId,long itemId,int num);
	N3Result deleteCart(long userId,long itemId);
	N3Result clearCartItem(Long userId);
	
}
