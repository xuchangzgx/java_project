package com.wondergroup.cloud;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class ConfigClientApplication {
  public static void main(String[] args) {
    ConfigurableApplicationContext run = SpringApplication.run(ConfigClientApplication.class, args);
    ConfigurableListableBeanFactory beanFactory = run.getBeanFactory();
    // beanFactory.registerSingleton(beanName, singletonObject);
    // new AnnotatedBeanDefinitionReader(registry)
    run.close();
  }
}
