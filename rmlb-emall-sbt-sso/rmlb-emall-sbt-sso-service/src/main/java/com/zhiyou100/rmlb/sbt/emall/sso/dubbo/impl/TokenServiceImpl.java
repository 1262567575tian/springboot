package com.zhiyou100.rmlb.sbt.emall.sso.dubbo.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.common.utils.JsonUtils;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUser;
import com.zhiyou100.rmlb.sbt.emall.sso.dubbo.TokenService;

import redis.clients.jedis.JedisCluster;
@Service(version="1.0.0")
public class TokenServiceImpl implements TokenService{
	
	@Autowired
	private JedisCluster jedis;
	
	@Override
	public N3Result getUserByToken(String token) {
		//根据token到redis中取用户信息
		String json=jedis.get("SESSION:"+token);
		//取不到用户信息，登录已经过期，返回登录过期
		if(StringUtils.isBlank(json)) {
			return N3Result.build(201, "用户登录已过期");
		}
		//取到用户信息更新token过期时间
		jedis.expire("SESSION:"+token, 1800);
		//返回结果，包含用户信息
		TbUser tbUser=JsonUtils.jsonToPojo(json, TbUser.class);
		
		return N3Result.ok(tbUser);
	}

}
