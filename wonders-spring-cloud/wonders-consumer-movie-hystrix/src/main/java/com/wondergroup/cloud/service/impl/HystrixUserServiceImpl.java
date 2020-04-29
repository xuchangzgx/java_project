package com.wondergroup.cloud.service.impl;

import org.springframework.stereotype.Component;

import com.wondergroup.cloud.entity.User;
import com.wondergroup.cloud.service.UserService;

@Component
public class HystrixUserServiceImpl implements UserService{

	@Override
	public User getUserById(Long id) {
		User user = new User();
		user.setId(12L);
		user.setName("feign使用hystrix");
		System.out.println("-----------------------------");
		return user;
	}

}
