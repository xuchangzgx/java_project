package com.wondergroup.cloud.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
public class BeanConfiguration {

	@Bean
	@LoadBalanced
	@ConditionalOnMissingBean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
}
