package com.zhiyou100.rmlb.sbt.emall.cat.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhiyou100.rmlb.sbt.emall.cat.dubbo.CartService;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.common.utils.CookieUtils;
import com.zhiyou100.rmlb.sbt.emall.common.utils.JsonUtils;
import com.zhiyou100.rmlb.sbt.emall.dubbo.ItemDubboService;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbItem;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUser;



/**
 * 购物车处理Controller
 */
@Controller
public class CartController {

	@Reference(version="1.0.0")
	private ItemDubboService itemService;
	@Reference(version="1.0.0")
	private CartService cartService;

	/**
	 * 从cookie中取购物车列表处理
	 */
	private List<TbItem> getCartByCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		// 判断json是否为空
		if (StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}
		// 把json转换为商品列表
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}

	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果是登录状态把购物车写入redis
		if (user != null) {
			// 保存到服务端
			cartService.addCart(user.getId(), itemId, num);
			// 返回
			return "cartSuccess";
		}
		// 如果未登录写入cookie中
		// 从cookie中取购物车列表
		List<TbItem> cartList = getCartByCookie(request);
		// 判断商品在商品列表中是否存在
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			// 如果存在数量相加
			if (tbItem.getId() == itemId.longValue()) {
				flag = true;
				// 找到商品数量相加
				tbItem.setNum(tbItem.getNum() + num);

				// 跳出循环
				break;
			}
		}
		if (!flag) {
			// 根据商品id查询商品信息，得到一个Tbitem对象
			TbItem tbItem = itemService.getItemById(itemId);
			// 设置商品数量
			tbItem.setNum(num);
			// 取商品图片
			if (StringUtils.isNoneBlank((tbItem.getImage()))) {
				tbItem.setImage(tbItem.getImage().split(",")[0]);
			}
			// 把商品添加到商品列表
			cartList.add(tbItem);
		}
		// 写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), 3600, true);
		// 返回成功页面
		return "cartSuccess";
	}

	/**
	 * 展示购物车列表
	 */
	@RequestMapping("/cart/cart")
	public String getCartList(HttpServletRequest request, HttpServletResponse response) {
		// 从cookie中取商品列表
		List<TbItem> list = getCartByCookie(request);
		// 判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果是登录状态
		if (user != null) {
			// 把cokkie中的购物车列表和redis中合并
			cartService.mergeCart(user.getId(), list);
			// 把cokkie中的购物车删除
			CookieUtils.deleteCookie(request, response, "cart");
			// 把购物车从服务端取出
			list = cartService.getCartList(user.getId());
		}
		// 把商品列表传递给前台
		request.setAttribute("cartList", list);
		// 返回页面
		return "cart";
	}

	/**
	 * 更新购物车数量
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public N3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果是登录状态
		if (user != null) {
			cartService.updateCartNum(user.getId(), itemId, num);
			return N3Result.ok();
		}
		// 从cookie中取购物车列表
		List<TbItem> list = getCartByCookie(request);
		// 遍历商品列表找到对应的商品
		for (TbItem tbItem : list) {
			if (tbItem.getId() == itemId.longValue()) {
				// 更新数量
				tbItem.setNum(num);
				break;
			}
		}
		// 把购物车列表写会cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), 3600, true);
		// 返回成功
		return N3Result.ok();
	}

	/**
	 * 删除购物车
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果是登录状态
		if (user != null) {
			cartService.deleteCart(user.getId(), itemId);
			return "redirect:/cart/cart";
		}
		// 从cookie中取购物车列表
		List<TbItem> list = getCartByCookie(request);
		// 遍历商品列表找到对应的商品
		for (TbItem tbItem : list) {
			if (tbItem.getId() == itemId.longValue()) {
				// 更新数量
				list.remove(tbItem);
				break;
			}
		}
		// 把购物车写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), 3600, true);
		// 返回逻辑视图
		return "redirect:/cart/cart";
	}
}
