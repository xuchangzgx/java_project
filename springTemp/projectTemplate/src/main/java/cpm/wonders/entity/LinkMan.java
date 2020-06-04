package cpm.wonders.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="cst_linkman")
@Data
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"}) // 解决实体类转换json出现的异常
public class LinkMan implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="lkm_id")
    private Long lkmId;
    @Column(name="lkm_name")
    private String lkmName;
    @Column(name="lkm_gender")
    private String lkmGender;
    @Column(name="lkm_phone")
    private String lkmPhone;
    @Column(name="lkm_mobile")
    private String lkmMobile;
    @Column(name="lkm_email")
    private String lkmEmail;
    @Column(name="lkm_position")
    private String lkmPosition;
    @Column(name="lkm_memo")
    private String lkmMemo;

    //多对一关系映射：多个联系人对应客户
    // targetEntity配置的是一那一方的
    @ManyToOne(targetEntity=Customer.class,cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name="lkm_cust_id",referencedColumnName="cust_id")
    private Customer customer;//用它的主键，对应联系人表中的外键
}

