package com.zhiyou100.rmlb.sbt.emall.dubbo;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.EasyUIDataGridResult;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItem;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItemDesc;

public interface ItemDubboService {

	/** 根据商品id去商品信息,显示商品详情中 */
	TbItem getItemById(long id) ;

	/** 分页查询 */
	EasyUIDataGridResult getItemList(Integer page, Integer rows);
	
	/** 添加商品 */
	N3Result addItem(TbItem item, String desc);
	
	/** 使用缓存查询：根据商品id查询商品描述,显示商品详情中 */
	TbItemDesc getItemDescById_1_1(long itemId);
	N3Result getItemDescById_1_2(long itemId);
	
	/** 删除商品 */
	N3Result deleteItem(long[] ids);
	
	/** 商品上架 */
	N3Result shelvesItem(long[] ids);
	
	/** 商品下架 */
	N3Result offShelfItem(long[] ids);
	
	/** 通过id查询描述 */
	N3Result getItemDescById_1_0(long id);
	
	/** 更改商品 */
	N3Result updateItem(TbItem tbItem, String desc);

	/** 緩存：根据商品id去商品信息,显示商品详情中 */
	TbItem getItemById_1_1(long itemId);
}
