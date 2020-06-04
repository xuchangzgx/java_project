package cpm.wonders.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;


/**
 * 读取druid连接池配置初始化datasource
 */
@SpringBootConfiguration
@ServletComponentScan
public class DruidConfiguration {

    @Bean(name="dataSource")
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource(){
        return new DruidDataSource();
    }

    /**
     * 分页插件
     */
    /*@Bean
    public PaginationInterceptor paginationInterceptor() {
        // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
        PaginationInterceptor paginationInterceptor =new PaginationInterceptor();
        return paginationInterceptor;

    }*/
}

