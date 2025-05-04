package com.jwt.implementation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
public class JwtWithRoleApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtWithRoleApplication.class, args);
	}

}
