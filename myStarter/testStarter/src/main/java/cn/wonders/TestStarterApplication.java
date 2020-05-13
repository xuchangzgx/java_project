package cn.wonders;

import com.wonders.auto.config.EnablePerson;
import com.wonders.auto.config.PersonActivity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnablePerson // 测试自定义的start
public class TestStarterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(TestStarterApplication.class, args);
        PersonActivity bean = context.getBean(PersonActivity.class);
        bean.eat();
    }

}
