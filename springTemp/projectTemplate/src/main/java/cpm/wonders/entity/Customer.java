package cpm.wonders.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity //声明实体类
@Table(name="cst_customer") //建立实体类和表的映射关系
@Data
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"}) // 解决实体类转换json出现的异常
public class Customer {

    /* 使用uuid作为主键
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(length = 32)
    */
    @Id
    //声明当前私有属性为主键
    @GeneratedValue(strategy= GenerationType.IDENTITY) //配置主键的生成策略
    @Column(name="cust_id") //指定和表中cust_id字段的映射关系
    private Long custId;

    @Column(name="cust_name") //指定和表中cust_name字段的映射关系
    private String custName;

    @Column(name="cust_source")//指定和表中cust_source字段的映射关系
    private String custSource;

    @Column(name="cust_industry")//指定和表中cust_industry字段的映射关系
    private String custIndustry;

    @Column(name="cust_level")//指定和表中cust_level字段的映射关系
    private String custLevel;

    @Column(name="cust_address")//指定和表中cust_address字段的映射关系
    private String custAddress;

    @Column(name="cust_phone")//指定和表中cust_phone字段的映射关系
    private String custPhone;

    /**
     * 在客户对象的@OneToMany注解中添加fetch属性
     * 		FetchType.EAGER	：立即加载
     * 		FetchType.LAZY	：延迟加载
     */
    //配置客户和联系人的一对多关系
    @OneToMany(targetEntity= LinkMan.class,fetch = FetchType.EAGER)
    @JoinColumn(name="lkm_cust_id",referencedColumnName="cust_id")
    private Set<LinkMan> linkmans = new HashSet<LinkMan>(0);
}
