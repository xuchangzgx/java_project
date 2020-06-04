package cpm.wonders.mapper;

import cpm.wonders.entity.Customer;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @Description: 主要用途：根据复杂的业务需求来动态生成SQL.
 * <p>
 * 目标：使用Java工具类来替代传统的XML文件.(例如：PersonSqlProvider.java <-- PersonMapper.xml)
 */
public class CustomerSqlProvider {

    /**
     * 方式1：在工具类的方法里,可以自己手工编写SQL。
     */
    public String listByName(String name) {
        return "select * from person where name = #{name}";
    }

    /**
     * 方式2：也可以根据官方提供的API来编写动态SQL。
     */
    public String getByAddress(String custName) {
        return new SQL() {{
            SELECT("*");
            FROM("cst_customer");
            if (custName != null) {
                WHERE("cust_name = #{custName}");
            } else {
                WHERE("1=2");
            }
        }}.toString();
    }


    public String save(Customer customer) {
        return new SQL() {{
            INSERT_INTO("cst_customer");
            if (customer.getCustName() != null) {
                VALUES("cust_name", "#{custName}");
            }
            if (customer.getCustAddress() != null) {
                VALUES("cust_address", "#{custAddress}");
            }
            if (customer.getCustSource() != null) {
                VALUES("cust_source", "#{custSource");
            }
        }}.toString();
    }

}
