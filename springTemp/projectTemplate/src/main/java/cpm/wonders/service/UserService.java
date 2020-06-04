package cpm.wonders.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cpm.wonders.dao.CustomerRepository;
import cpm.wonders.dao.LinkManRepository;
import cpm.wonders.entity.Customer;
import cpm.wonders.entity.LinkMan;
import cpm.wonders.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private LinkManRepository linkManRepository;

    public Customer getCustomerById(Long id){
        // return customerRepository.getOne(id);
        return customerMapper.getCustomerById(id);
    }

    public PageInfo list(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Customer> personByAddress = customerMapper.list();
        return new PageInfo<>(personByAddress);
    }

    public List<Customer> get(String custName, String address){
        return customerMapper.get(custName,address);
    }

    public List<Customer> listSample(){
        return customerMapper.listSample();
    }


    public void save(Long custId){
        Customer customer = customerMapper.getCustomerById(custId);
        if(customer!=null){
            LinkMan linkMan = new LinkMan();
            linkMan.setLkmPhone("19862720593");
            linkMan.setLkmName("我是一");
            linkMan.setCustomer(customer);

            LinkMan linkMan2 = new LinkMan();
            linkMan2.setLkmPhone("19862720593");
            linkMan2.setLkmName("我是二");
            linkMan2.setCustomer(customer);
            linkManRepository.save(linkMan);
            linkManRepository.save(linkMan2);
            throw new RuntimeException("保存失败");
        }
    }
}
