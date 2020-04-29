package com.wondergroup.cloud.config;

import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
public class ZuulPatternRouterBean {
/*
	@Bean
	public PatternServiceRouteMapper serviceRouteMapper() {
		// 参数1：serviceId(服务的application.name) wonders-provider-user2
		// 参数2：zuul匹配到的地址：例如http://192.168.25.1:8763/user2/wonders-provider/simple/2，匹配到的
		// 			就是user2/wonders-provider，user匹配的是user2,name为wonders-provider
		return new PatternServiceRouteMapper("(?<name>^.+)-(?<user>u.+$)", "${user}/${name}");
	}
	*/
}
