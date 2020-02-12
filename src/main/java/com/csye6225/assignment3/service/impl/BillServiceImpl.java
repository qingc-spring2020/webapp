package com.csye6225.assignment3.service.impl;

import com.csye6225.assignment3.mbg.mapper.BillMapper;
import com.csye6225.assignment3.mbg.model.Account;
import com.csye6225.assignment3.mbg.model.AccountExample;
import com.csye6225.assignment3.mbg.model.Bill;
import com.csye6225.assignment3.mbg.model.BillExample;
import com.csye6225.assignment3.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    BillMapper billMapper;
    @Override
    public Bill createBill(Bill bill) {

        String uuid = UUID.randomUUID().toString();
        bill.setBillId(uuid);
        Date date = new Date();
        bill.setUpdatedTs(date);
        bill.setCreatedTs(date);
        billMapper.insertSelective(bill);
        return billMapper.selectByPrimaryKey(uuid);
    }

    @Override
    public int deleteBill(String billId) {

        return billMapper.deleteByPrimaryKey(billId);
    }

    @Override
    public List<Bill> getAllBillInfo(String ownerId) {
        BillExample example = new BillExample();
        example.createCriteria().andOwnerIdEqualTo(ownerId);
        List<Bill> billList = billMapper.selectByExample(example);
        return billList;
    }

    @Override
    public Bill getBillInfo(String billId) {
        return billMapper.selectByPrimaryKey(billId);
    }

    @Override
    public Bill updateBillInfo(Bill bill) {
        Date date = new Date();
        bill.setUpdatedTs(date);
        billMapper.updateByPrimaryKey(bill);
        return billMapper.selectByPrimaryKey(bill.getBillId());
    }

    @Override
    public void updateFileInfo(Bill bill, String fileId) {
        bill.setFileId(fileId);
        billMapper.updateByPrimaryKey(bill);
    }

    @Override
    public void deleteFileInfoByBill(Bill bill) {
        bill.setFileId("");
        billMapper.updateByPrimaryKey(bill);
    }
}
