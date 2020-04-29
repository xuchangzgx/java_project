package com.wondergroup.cloud.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.wondergroup.cloud.ExcludeFromComponentScan;

@ExcludeFromComponentScan
@SpringBootConfiguration
public class TestRibbonConfig {

	@Bean
	public IRule ribbonRule(){
		System.out.println( "-------------------" );
		return new RandomRule();
	}
	
}
