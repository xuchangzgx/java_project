package com.wondergroup.cloud.service.impl;

import org.springframework.stereotype.Component;

import com.wondergroup.cloud.entity.User;
import com.wondergroup.cloud.service.UserService;

import feign.hystrix.FallbackFactory;

@Component
public class HystrixUserServiceFactory implements FallbackFactory<UserService>{

	@Override
	public UserService create(Throwable cause) {
		System.err.println("打印错误。。。。。。。。。。"+cause.getMessage());
		cause.printStackTrace();
		return new UserService() {
			
			@Override
			public User getUserById(Long id) {
				User user = new User();
				user.setId(12L);
				user.setName("feign使用hystrixFactory");
				System.out.println("-----------------------------");
				return user;
			}
		};
	}

}
