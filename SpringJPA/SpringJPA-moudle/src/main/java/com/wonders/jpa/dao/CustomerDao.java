package com.wonders.jpa.dao;

import com.wonders.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Entity;
import java.util.List;

/**
 *  JpaRepository<实体类,主键类型>基本CURD操作
 *  JpaSpecificationExecutor<实体类> 复杂操作，如分页
 */
public interface CustomerDao extends JpaRepository<Customer,Long>, JpaSpecificationExecutor<Customer> {

    //  nativeQuery 是否为原生sql查询 true:原生sql，false: JPQL
    @Query(value = "select * from cst_customer", nativeQuery = true)
    public List<Customer> findAllSql();

    // 原生sql的参数要使用，@Param 映射在原生sql条件参数中，sql中参数要用 :参数名(@Param参数的value值)
    @Query(value = "select * from cst_customer where cust_id=:custId", nativeQuery = true)
    public Customer findCustomerSqlByCustId(@Param("custId") Long custId);

    @Query(value = "update Customer set custName=?1 where custId=?2") // @Query用于更新/删除时，必须使用@Modifying
    @Modifying
    public Integer updateCustomer(String custName,Long custId);

    /**
     * jpql查询  表明使用属性名代替，字段名使用字段名代替
     * @return
     */
    @Query(value = "from Customer")
    public List<Customer> findAllCustomer();

    @Query(value = "from Customer where custName = ?")
    public Customer findCustomer(String name);

    public Customer findCustomerByCustId(Long custId);

    public List<Customer> findCustomerByCustNameLike(String custName);

    public List<Customer> findCustomerByCustNameLikeAndCustId(String custName,Long custId);
}
