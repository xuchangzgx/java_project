package com.wonders.auto.config;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PersonConfiguration.class)
public @interface EnablePerson {
}
