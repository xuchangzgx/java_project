package cpm.wonders.mapper;

import cpm.wonders.entity.Customer;
import cpm.wonders.entity.LinkMan;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomerMapper {
    /**
     * 方式1：使用注解编写sql
     *
     * @return
     */
    @Select("SELECT * FROM cst_customer")
    List<Customer> list();

    /**
     * 方式2：使用注解指定某个工具类的方法来动态编写SQL.
     *
     * @param name
     * @return
     */
    @SelectProvider(type = CustomerSqlProvider.class, method = "listByName")
    List<Customer> listByName(String name);

    /**
     * 延伸：上述两种方式都可以附加@Results注解来指定结果集的映射关系.
     * <p>
     * PS：如果符合下划线转驼峰的匹配项可以直接省略不写。
     */
    @Results(id = "customerRes",value = {
            @Result(property = "custId", column = "cust_id"),
            @Result(property = "custAddress", column = "cust_address"),
            @Result(property = "custIndustry", column = "cust_industry"),
            @Result(property = "custLevel", column = "cust_level"),
            @Result(property = "custName", column = "cust_name"),
            @Result(property = "custPhone", column = "cust_phone"),
            @Result(property = "custSource", column = "cust_source"),
    })
    @Select("select * from cst_customer")
    List<Customer> listSample();

    /**
     * 延伸：无论什么方式,如果涉及多个参数,则必须加上@Param注解,否则无法使用EL表达式获取参数。
     */
    @Select("select * from cst_customer" +
            " where cust_name like CONCAT('%',#{name},'%')" +
            " and cust_address like CONCAT('%',#{address},'%')")
    List<Customer> get(@Param("name") String custName, @Param("address") String address);

    @Results(value = {
            @Result(property = "custId", column = "cust_id"),
            @Result(property = "custAddress", column = "cust_address"),
            @Result(property = "custIndustry", column = "cust_industry"),
            @Result(property = "custLevel", column = "cust_level"),
            @Result(property = "custName", column = "cust_name"),
            @Result(property = "custPhone", column = "cust_phone"),
            @Result(property = "custSource", column = "cust_source"),
            @Result(property = "linkmans",column="cust_id",javaType = Set.class,
                    many=@Many(select="cpm.wonders.mapper.LinkManMapper.getLinkMansByCustId",fetchType= FetchType.LAZY)
            )
    })
    @Select("SELECT * FROM cst_customer" +
            " cs WHERE cs.cust_id = #{custId}")
    public Customer getCustomerById(Long custId);

}
