package com.wondergroup.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.wondergroup.cloud.entity.User;
import com.wondergroup.cloud.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/simple/{id}")
	public User findById(@PathVariable Long id) {
		System.out.println( "-----------------"+id );
		return userService.getUserById(id);
	}
	
	@GetMapping("/simple2/{id}")
	@HystrixCommand(fallbackMethod = "findByIdFallback",
		commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE"))
	public User findById2(@PathVariable Long id) {
		return restTemplate.getForObject("http://192.168.25.1:8081/simple/"+id,User.class);
	}
	
	public User findByIdFallback(Long id){
		User user = new User();
		user.setId(0L);
		user.setName("xxxxxxxx");
		return user;
	}
	
	/*@GetMapping("/update/{id}")
	public void updateById(@PathVariable Long id) {
		User user = new User();
		user.setUsername("xuchang");
		user.setAge((short) 12);
		userService.update(id,user);
	}*/
}
