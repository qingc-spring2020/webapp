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

        Assert.assertSame("delete successfully", 1, billService.deleteBill("02105ec4-97c1-4b16-8e1a-b181a0bfe4c4"));
    }

}
