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
    private BillService billService;

    @Test
    void deleteBillInfo() {

        Assert.assertSame("delete successfully", 1, billService.deleteBill("004029e7-a1f8-4e68-b613-c14e99131bb3"));
    }

}
