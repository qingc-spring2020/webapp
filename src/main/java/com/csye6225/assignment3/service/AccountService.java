package com.csye6225.assignment3.service;

import com.csye6225.assignment3.mbg.model.Account;

import java.util.Map;

public interface AccountService {

    int createAccount(Account account);

    Account getAccountByEmail(String email);

    int updateAccount(String email, Map<String,String> info);

    boolean login(String username,String password);
}
