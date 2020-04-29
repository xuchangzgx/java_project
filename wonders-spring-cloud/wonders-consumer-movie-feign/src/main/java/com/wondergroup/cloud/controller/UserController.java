package com.wondergroup.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.wondergroup.cloud.entity.User;
import com.wondergroup.cloud.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/simple/{id}")
	public User findById(@PathVariable Long id) {
		System.out.println( "-----------------"+id );
		return userService.getUserById(id);
	}
	
	/*@GetMapping("/update/{id}")
	public void updateById(@PathVariable Long id) {
		User user = new User();
		user.setUsername("xuchang");
		user.setAge((short) 12);
		userService.update(id,user);
	}*/
}
