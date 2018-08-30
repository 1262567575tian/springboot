package com.zhiyou100.rmlb.sbt.emall.sso.dubbo;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;

public interface UserLoginService {
		
	N3Result UserLogin(String username,String password);
}
