package com.wondergroup.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.wondergroup.cloud.config.TestRibbonConfig;

@SpringBootApplication
@EnableEurekaClient
// 对指定的服务使用指定的Ribbon方式
@RibbonClient(name = "wonders-provider-user", configuration = TestRibbonConfig.class)
@ComponentScan(excludeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value = ExcludeFromComponentScan.class) })
public class WondersMovieRibbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(WondersMovieRibbonApplication.class, args);
	}

}
