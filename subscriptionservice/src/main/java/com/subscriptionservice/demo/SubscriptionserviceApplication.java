package com.subscriptionservice.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.subscriptionservice.client")
@EnableScheduling
@ComponentScan(basePackages = "com.subscriptionservice")
@EntityScan(basePackages = "com.subscriptionservice.model")
@EnableJpaRepositories(basePackages = "com.subscriptionservice.repository")
public class SubscriptionserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionserviceApplication.class, args);
	}

}
