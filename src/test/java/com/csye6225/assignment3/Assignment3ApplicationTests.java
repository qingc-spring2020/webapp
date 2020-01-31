package com.csye6225.assignment3;

import com.csye6225.assignment3.mbg.model.Account;
import com.csye6225.assignment3.service.AccountService;
import com.csye6225.assignment3.service.BillService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class Assignment3ApplicationTests {

    @Autowired
    private AccountService accountService;

    @Test
    void createAccount() {

        Account account = new Account();
        account.setPassword( "qcw123456789");
        account.setEmailAddress("123@qq.com");
        Assert.assertSame("create successfully", 1, accountService.createAccount(account));
    }

}
