package com.xuecheng.ucenter;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UcenterApplication.class)
public class TestJpa {

    @Autowired
    private XcCompanyUserRepository xcCompanyUserRepository;

    @Test
    public void test(){
        List<XcCompanyUser> all = xcCompanyUserRepository.findAll();
        System.out.println( all );
    }

}
