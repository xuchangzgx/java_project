package com.wonders.jpa;

import com.wonders.jpa.dao.CustomerDao;
import com.wonders.jpa.dao.LinkManDao;
import com.wonders.jpa.entity.Customer;
import com.wonders.jpa.entity.LinkMan;
import com.wonders.jpa.service.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-hibernate.xml")
public class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private LinkManDao linkManDao;

    @Autowired
    private CustomerService customerService;

    @Test
    public void save(){
        customerService.save();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testDeleteLinkMan(){
        linkManDao.delete(2L);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testSave(){
        Customer customer = new Customer();
        customer.setCustName("测试jpa保存6");
        LinkMan linkMan = new LinkMan();
        linkMan.setLkmName("测试6的联系人1");
        linkMan.setCustomer(customer);
        linkManDao.save(linkMan);
        /*
        linkMan.setCustomer(customer);
        linkManDao.save(linkMan);
        customer.getLinkmans().add(linkMan);
        customerDao.save(customer);
        */
    }

    @Test
    public void testUpdate(){
        Customer customer = new Customer();
        customer.setCustId(1L);
        customer.setCustName("测试jpa更新");
        // 发送两条sql,第一条查询，第二条update
        customerDao.save(customer);
    }

    @Test
    public void testDelete(){
        Customer customer = new Customer();
        customer.setCustId(2L);
        customerDao.delete(customer);
    }

    @Test
    public void testQuerySimple(){
        // Customer customer = customerDao.findOne(2L);
        // customerDao.find
        // System.out.println( customer );
    }

    @Test
    public void testJPQLQuery(){
        /*List<Customer> allCustomer = customerDao.findAllCustomer();
        for (Customer customer : allCustomer) {
            System.out.println( customer );
        }*/
        Customer customer = customerDao.findCustomer("测试jpa保存2");
        System.out.println( customer );
    }

    @Test
    @Transactional   // 事物注解，JPAL执行更新/删除时，必须使用事物
    @Rollback(false) // true 回滚，false 不会滚
    public void testJPQLUpdate(){
        Integer integer = customerDao.updateCustomer("测试JPQL更新", 3L);
        System.out.println( String.format("更新记录 %d 条",integer) );
    }

    @Test
    public void testLocalSqlQuery(){
       /* List<Customer> allSql = customerDao.findAllSql();
        for (Customer o : allSql) {
            System.out.println( o );
        }*/
        Customer customer = customerDao.findCustomerSqlByCustId(3L);

        System.out.println( customer );
    }

    @Test
    public void testJpaRulesQuery(){
       /* Customer customerByCustId = customerDao.findCustomerByCustId(3L);
        System.out.println( customerByCustId );*/
        // List<Customer> customers = customerDao.findCustomerByCustNameLike("测试%");
        List<Customer> customers = customerDao.findCustomerByCustNameLikeAndCustId("测试%",3L);
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    @Test
    public void testSpecifications() {

        Specification<Customer> specification = new Specification<Customer>() {
            /**
             * 参数说明
             * @param root Root接口，代表查询的根对象，可以通过root获取实体中的属性
             * @param criteriaQuery 代表一个顶层查询对象，用来自定义查询
             * @param criteriaBuilder 用来构建查询，此对象里有很多条件方法
             * @return
             */
            public Predicate toPredicate(
                    Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
               /* 单表单条件查询
                // 1，获得比较的属性
                Path<Object> custId = root.get("custId");
                // 构建查询条件
                Predicate predicate = criteriaBuilder.equal(custId, 3L);
                */
               // 单表多条件
                Predicate custId = criteriaBuilder.equal(root.get("custId"), 3L);
                Predicate custName = criteriaBuilder.like(root.get("custName").as(String.class), "测试%");
                // 将多个条件拼接在一起
                Predicate predicate = criteriaBuilder.and(custId, custName);
                return predicate;
            }
        };
        List<Customer> customers = customerDao.findAll(specification);
        for (Customer customer : customers) {
            System.out.println( customer );
        }

    }

    @Test
    public void testSpecificationsSort() {

        Specification<Customer> specification = new Specification<Customer>() {
            /**
             * 参数说明
             * @param root Root接口，代表查询的根对象，可以通过root获取实体中的属性
             * @param criteriaQuery 代表一个顶层查询对象，用来自定义查询
             * @param criteriaBuilder 用来构建查询，此对象里有很多条件方法
             * @return
             */
            public Predicate toPredicate(
                    Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // 单表多条件
                Predicate custName = criteriaBuilder.like(root.get("custName").as(String.class), "测试%");
                return custName;
            }
        };
        // 根据custId进行降序
        Sort orders = new Sort(Sort.Direction.DESC,"custId");
        List<Customer> customers = customerDao.findAll(specification,orders);
        for (Customer customer : customers) {
            System.out.println( customer );
        }
    }

    @Test
    public void testSpecificationsPage() {

        Specification<Customer> specification = new Specification<Customer>() {
            /**
             * 参数说明
             * @param root Root接口，代表查询的根对象，可以通过root获取实体中的属性
             * @param criteriaQuery 代表一个顶层查询对象，用来自定义查询
             * @param criteriaBuilder 用来构建查询，此对象里有很多条件方法
             * @return
             */
            public Predicate toPredicate(
                    Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // 单表多条件
                Predicate custName = criteriaBuilder.like(root.get("custName").as(String.class), "测试%");
                return custName;
            }
        };
        //PageRequest对象是Pageable接口的实现类
        /**
         * 构造分页参数
         * 		Pageable : 接口
         * 			PageRequest实现了Pageable接口，调用构造方法的形式构造
         * 				第一个参数：页码（从0开始）
         * 				第二个参数：每页查询条数
         */
        PageRequest pageRequest = new PageRequest(1, 1);
        Page<Customer> page = customerDao.findAll(specification,pageRequest);
        List<Customer> customers = page.getContent();
        System.out.println( "总页数："+page.getTotalPages() );
        System.out.println( "总记录数："+page.getTotalElements() );
        for (Customer customer : customers) {
            System.out.println( customer );
        }
    }

    /**
     * 多表查询
     */
    @Test
    public void testSpecificationsJoin() {

        Specification<Customer> specification = new Specification<Customer>() {
            /**
             * 参数说明
             * @param root Root接口，代表查询的根对象，可以通过root获取实体中的属性
             * @param criteriaQuery 代表一个顶层查询对象，用来自定义查询
             * @param criteriaBuilder 用来构建查询，此对象里有很多条件方法
             * @return
             */
            public Predicate toPredicate(
                    Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //Join代表链接查询，通过root对象获取
                //创建的过程中，第一个参数为关联对象的属性名称，第二个参数为连接查询的方式（left，inner，right）
                //JoinType.LEFT : 左外连接,JoinType.INNER：内连接,JoinType.RIGHT：右外连接
                // root的泛型是主表
                Join<LinkMan, Customer> join = root.join("customer", JoinType.INNER);

                return criteriaBuilder.like(join.get("custName").as(String.class), "测试%");
            }
        };
        //PageRequest对象是Pageable接口的实现类
        /**
         * 构造分页参数
         * 		Pageable : 接口
         * 			PageRequest实现了Pageable接口，调用构造方法的形式构造
         * 				第一个参数：页码（从0开始）
         * 				第二个参数：每页查询条数
         */
        PageRequest pageRequest = new PageRequest(1, 1);
        Page<Customer> page = customerDao.findAll(specification,pageRequest);
        List<Customer> customers = page.getContent();
        System.out.println( "总页数："+page.getTotalPages() );
        System.out.println( "总记录数："+page.getTotalElements() );
        for (Customer customer : customers) {
            System.out.println( customer );
        }
    }


}
