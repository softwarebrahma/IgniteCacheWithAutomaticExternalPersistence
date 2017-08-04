package com.brocade.dcm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class ObjectCacheMgrApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ObjectCacheMgrApplication.class);
	}
	
}
