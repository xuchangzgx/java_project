package com.wonders.jpa.service.impl;

import com.wonders.jpa.dao.CustomerDao;
import com.wonders.jpa.dao.LinkManDao;
import com.wonders.jpa.entity.Customer;
import com.wonders.jpa.entity.LinkMan;
import com.wonders.jpa.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private LinkManDao linkManDao;

    public Integer save() {
        /**
         * 通过对象关联关系保存关联对象时，需要将有控制权(维护关联关系的一方)的外键属性设置成级联
         * @ManyToOne(targetEntity=Customer.class,cascade = CascadeType.PERSIST)
         * 不然会由于关联的对象是瞬时或托管状态，导致保存失败
         * 放弃维护权的一方，不要加，@JoinColumn
         */
        Customer customer = new Customer();
        customer.setCustName("测试jpa保存6");
        LinkMan linkMan = new LinkMan();
        linkMan.setLkmName("测试6的联系人1");
        linkMan.setCustomer(customer);
        linkManDao.save(linkMan);

        return 0;
    }
}
