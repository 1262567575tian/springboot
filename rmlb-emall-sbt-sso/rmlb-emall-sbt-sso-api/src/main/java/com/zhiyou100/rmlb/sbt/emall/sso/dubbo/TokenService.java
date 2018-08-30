package com.zhiyou100.rmlb.sbt.emall.sso.dubbo;

import com.zhiyou100.rmlb.sbt.emall.common.pojo.N3Result;

public interface TokenService {

	N3Result getUserByToken(String token);
}
