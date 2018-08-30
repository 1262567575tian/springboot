package com.zhiyou100.rmlb;

import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQTopic;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.zhiyou100.rmlb.sbt.emall.dao")
public class ManagerServerApplication {
	
	@Bean
	public Topic topic() {
	       return new ActiveMQTopic("topicDestination");
	    }
	
	public static void main(String[] args) {
		SpringApplication.run(ManagerServerApplication.class, args);
	}
}
