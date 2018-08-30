package com.zhiyou100.rmlb.sbt.emall.sso.dubbo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.dao.TbUserMapper;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUser;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUserExample;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUserExample.Criteria;
import com.zhiyou100.rmlb.sbt.emall.sso.dubbo.RegisterService;


@Service(version="1.0.0")
public class RegisterServiceImpl implements RegisterService{

	@Autowired
	private TbUserMapper userMapper;
	
	
	
	@Override
	public N3Result userchick(String param, int type) {
		//根据输入的类型指定查询条件
		TbUserExample example=new TbUserExample();
		Criteria createCriteria = example.createCriteria();
		if(type == 1) {
			createCriteria.andUsernameEqualTo(param);
		}else if(type == 2) {
			createCriteria.andPhoneEqualTo(param);
		}else {
			createCriteria.andEmailEqualTo(param);
		}
		//根据查询条件查询
		List<TbUser> list= userMapper.selectByExample(example);
		//如果有数据返回false，否则返回true
		if(list !=null && list.size()>0) {
			return N3Result.ok(false);
		}
		//返回结果
		return N3Result.ok(true);
	}

	@Override
	public N3Result UserRegis(TbUser tbUser) {
		//数据有效性校验
		if(StringUtils.isBlank(tbUser.getUsername()) || StringUtils.isBlank(tbUser.getPhone())) {
			return N3Result.build(400, "表单信息输入不完整，注册失败");
		}
		N3Result result=userchick(tbUser.getUsername(), 1);
		if(!(boolean) result.getData()) {
			return N3Result.build(400, "此用户名已被占用");
		}
		result=userchick(tbUser.getPhone(), 2);
		if(!(boolean) result.getData()) {
			return N3Result.build(400, "此手机号已注册");
		}
		//处理密码md5加密
		String md5pass=DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		//补全对象中的值
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		tbUser.setPassword(md5pass);
		//调用添加方法
		userMapper.insert(tbUser);
		//返回结果
		return N3Result.ok();
	}

}
