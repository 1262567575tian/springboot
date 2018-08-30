package com.zhiyou100.rmlb.sbt.emall.sso.dubbo;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;
import com.zhiyou100.rmlb.sbt.emall.pojo.TbUser;

public interface RegisterService {
	
	public N3Result userchick(String param,int type); 
	public N3Result UserRegis(TbUser tbUser);
}