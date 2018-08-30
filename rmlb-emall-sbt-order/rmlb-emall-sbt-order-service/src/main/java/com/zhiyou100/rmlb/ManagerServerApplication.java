package com.zhiyou100.rmlb;




import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhiyou100.rmlb.sbt.emall.dao")
public class ManagerServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ManagerServerApplication.class, args);
	}
}
