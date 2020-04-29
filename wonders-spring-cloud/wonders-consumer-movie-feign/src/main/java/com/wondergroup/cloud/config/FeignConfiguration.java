package com.wondergroup.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Contract;
import feign.Logger;

@Configuration
public class FeignConfiguration {
	
	// 为FeignConfiguration添加链接eureka的权限
	/*@Bean
	BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
		return new BasicAuthRequestInterceptor("user", "password123");
	}*/
	
	@Bean
	public Contract feignContract() {
		return new Contract.Default();
	}
	
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
	
}
