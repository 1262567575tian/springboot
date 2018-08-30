package com.zhiyou100.rmlb;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
@SpringBootTest

public class TestRedis {
	
	@Resource
	private RedisTemplate<String, String> redisTemplate;
	
	
	@Test
	public void redisTest() {
		String key="redisTestKey";
		String value="springBoot-Redis";
		
		/*ValueOperations<String, String> operations=redisTemplate.opsForValue();
		operations.set(key, value);
		System.out.println(operations.get(key));*/
		redisTemplate.opsForValue().set("author", "Damein_xym");
	
	}
	
	
	
}
