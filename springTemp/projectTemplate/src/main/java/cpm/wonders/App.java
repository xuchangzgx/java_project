package cpm.wonders;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
// 开启事务管理
@EnableTransactionManagement
@MapperScan("cpm.wonders.mapper")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }

}
