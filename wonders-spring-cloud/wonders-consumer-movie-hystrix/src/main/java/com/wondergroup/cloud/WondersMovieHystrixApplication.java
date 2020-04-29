package com.wondergroup.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableHystrix // 开启熔断器
// @EnableCircuitBreaker
public class WondersMovieHystrixApplication {

	public static void main(String[] args) {
		SpringApplication.run(WondersMovieHystrixApplication.class, args);
	}

}
