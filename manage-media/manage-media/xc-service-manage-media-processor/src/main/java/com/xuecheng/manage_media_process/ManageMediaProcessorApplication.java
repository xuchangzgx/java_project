package com.xuecheng.manage_media_process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 8:57
 **/
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.media")//扫描实体类
// @ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
//扫描common与本项目下的所有类
@ComponentScan(basePackages={"com.xuecheng.framework","com.xuecheng.manage_media_process"})
public class ManageMediaProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageMediaProcessorApplication.class, args);
    }
}