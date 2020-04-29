package com.wondergroup.cloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wondergroup.cloud.config.FeignConfiguration;
import com.wondergroup.cloud.entity.User;
import com.wondergroup.cloud.service.impl.HystrixUserServiceFactory;

/**
 * 指定服务名
 * @author 徐昶
 *
 */
@FeignClient(name="xxx",url="http://127.0.0.1:8081/", configuration=FeignConfiguration.class,
		fallbackFactory=HystrixUserServiceFactory.class)
public interface UserService {

	@RequestMapping(method = RequestMethod.GET, value = "/simple/{id}")
	public User getUserById(@PathVariable("id") Long id);
	
	//@RequestMapping(method = RequestMethod.GET, value = "/simple/{id}")
	/*@RequestLine("GET /simple/{id}")
	public User getUserById(@Param("id") Long id);*/

	/*@RequestMapping(method = RequestMethod.POST, value = "/update/{id}",
			consumes = "application/json")
	void update(@PathVariable("id") Long id, User user);*/

}
