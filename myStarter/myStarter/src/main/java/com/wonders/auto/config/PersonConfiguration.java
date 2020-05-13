package com.wonders.auto.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass(Person.class)
@ConditionalOnMissingBean(Person.class)
@EnableConfigurationProperties(Person.class)
public class PersonConfiguration {

    @Bean
    public PersonActivity personActivity(Person person){
        return new PersonActivity(person);
    }

}
