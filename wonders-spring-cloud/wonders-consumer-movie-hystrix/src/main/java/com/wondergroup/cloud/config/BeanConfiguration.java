package com.wondergroup.cloud.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
public class BeanConfiguration {
	
	@Bean
	public RestTemplate restTemple(){
		return new RestTemplate();
	}
	
}
