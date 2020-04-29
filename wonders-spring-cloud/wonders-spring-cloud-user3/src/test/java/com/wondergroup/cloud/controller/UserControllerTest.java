package com.wondergroup.cloud.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.wondergroup.cloud.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

	@Autowired
	private RestTemplate restTemplate;
	
	@Test
	public void testFindById() {
		/*System.err.println( "-------------------------------------------------" );
		try{
			User forObject = restTemplate.getForObject("http://127.0.0.1:8081/simple/3", User.class);
			System.out.println( forObject.getName() );
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println( "-------------------------------------------------" );*/
	}

}
