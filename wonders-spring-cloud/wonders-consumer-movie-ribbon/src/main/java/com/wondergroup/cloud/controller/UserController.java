package com.wondergroup.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.wondergroup.cloud.entity.User;

@RestController
public class UserController {

	/*
	 * @Autowired private UserRepository userRepository;
	 */
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@GetMapping("/simple/{id}")
	public User findById(@PathVariable Long id) {
		return restTemplate.getForObject("http://WONDERS-PROVIDER-USER:8081/simple/" + id, User.class);
	}

	@GetMapping("/test")
	public String test() {
		ServiceInstance serviceInstance = this.loadBalancerClient.choose("wonders-provider-user");
		System.out.println("111" + ":" + serviceInstance.getServiceId() + ":" + serviceInstance.getHost() + ":"
				+ serviceInstance.getPort());

		ServiceInstance serviceInstance2 = this.loadBalancerClient.choose("wonders-provider-user2");
		System.out.println("222" + ":" + serviceInstance2.getServiceId() + ":" + serviceInstance2.getHost() + ":"
				+ serviceInstance2.getPort());

		return "1";
	}

	/*
	 * @GetMapping("/eureka-instance") public String serviceUrl() { InstanceInfo
	 * instance =
	 * this.eurekaClient.getNextServerFromEureka("MICROSERVICE-PROVIDER-USER",
	 * false); return instance.getHomePageUrl(); }
	 * 
	 * @GetMapping("/instance-info") public ServiceInstance showInfo() {
	 * ServiceInstance localServiceInstance =
	 * this.discoveryClient.getLocalServiceInstance(); return
	 * localServiceInstance; }
	 * 
	 * @PostMapping("/user") public User postUser(@RequestBody User user) {
	 * return user; }
	 * 
	 * // 该请求不会成功
	 * 
	 * @GetMapping("/get-user") public User getUser(User user) { return user; }
	 * 
	 * @GetMapping("list-all") public List<User> listAll() { ArrayList<User>
	 * list = Lists.newArrayList(); User user = new User(1L, "zhangsan"); User
	 * user2 = new User(2L, "zhangsan"); User user3 = new User(3L, "zhangsan");
	 * list.add(user); list.add(user2); list.add(user3); return list;
	 * 
	 * }
	 */
}
