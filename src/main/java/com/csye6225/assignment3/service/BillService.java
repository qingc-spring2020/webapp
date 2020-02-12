package com.csye6225.assignment3.service;

import com.csye6225.assignment3.mbg.model.Account;
import com.csye6225.assignment3.mbg.model.Bill;

import java.util.List;

public interface BillService {

    Bill createBill(Bill bill);

    int deleteBill(String billId);

    List<Bill> getAllBillInfo(String billId);

    Bill getBillInfo(String billId);

    Bill updateBillInfo(Bill bill);

    void updateFileInfo(Bill bill, String fileId);

    void deleteFileInfoByBill(Bill bill);
}
