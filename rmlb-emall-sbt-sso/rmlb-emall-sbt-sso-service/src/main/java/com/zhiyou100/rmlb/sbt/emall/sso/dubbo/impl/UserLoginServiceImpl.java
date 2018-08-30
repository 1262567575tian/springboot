package com.zhiyou100.rmlb.sbt.emall.sso.dubbo.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.common.utils.JsonUtils;
import com.zhiyou100.rmlb.sbt.emall.dao.TbUserMapper;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUser;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUserExample;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUserExample.Criteria;
import com.zhiyou100.rmlb.sbt.emall.sso.dubbo.UserLoginService;

import redis.clients.jedis.JedisCluster;
@Service(version="1.0.0")
public class UserLoginServiceImpl implements UserLoginService{

	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	@Override
	public N3Result UserLogin(String username, String password) {
		//1.判断用户和密码是否正确
		//根据用户名查询用户信息
		TbUserExample example=new TbUserExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andUsernameEqualTo(username);
		List<TbUser> list=userMapper.selectByExample(example);
		if(list == null || list.size() == 0) {
			return N3Result.build(400, "用户名或密码错误");
		}
		//取用户信息
		TbUser user=list.get(0);
		//判断密码是否正确
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return N3Result.build(400, "用户名或密码错误");
		}
		//如果正确生成token
		String token=UUID.randomUUID().toString();
		//把用户信息写入redis，key：token,value:用户信息
		user.setPassword(null);
		jedisCluster.set("SESSION:"+token, JsonUtils.objectToJson(user));
		//设置过期时间
		jedisCluster.expire("SESSION:"+token, 1800);
		//返回token
		return N3Result.ok(token);
	}

}
