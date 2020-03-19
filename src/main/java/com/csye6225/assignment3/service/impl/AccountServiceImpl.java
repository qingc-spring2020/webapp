package com.csye6225.assignment3.service.impl;

import com.csye6225.assignment3.jwt.UpdatableBCrypt;
import com.csye6225.assignment3.mbg.mapper.AccountMapper;
import com.csye6225.assignment3.mbg.model.Account;
import com.csye6225.assignment3.mbg.model.AccountExample;
import com.csye6225.assignment3.service.AccountService;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountServiceImpl implements AccountService {

    static final Integer salt = 5;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    private StatsDClient statsDClient;

    @Override
    public int createAccount(Account account) {

        long start=System.currentTimeMillis();

        if(!verify(account.getPassword())) {
            return 0;
        }

        if(!checkEmailFormat(account.getEmailAddress())) {
            return -2;
        }
        Account examAccount = getAccountByEmail(account.getEmailAddress());
        if(examAccount != null) {
            return -1;
        }

        String uuid = UUID.randomUUID().toString();
        System.out.println("uuid:"+uuid);
        String passwordBeforeHash = account.getPassword();
        String passwordHash = new UpdatableBCrypt(salt).hash(passwordBeforeHash);
        Date date = new Date();

        account.setUserId(uuid);
        account.setPassword(passwordHash);
        account.setAccountCreated(date);
        account.setAccountUpdated(date);
        long end=System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.createAccount.time",end-start);
        return accountMapper.insertSelective(account);
    }

    @Override
    public Account getAccountByEmail(String email) {
        long start=System.currentTimeMillis();
        AccountExample example = new AccountExample();
        example.createCriteria().andEmailAddressEqualTo(email);
        List<Account> accountList = accountMapper.selectByExample(example);
        long end=System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.getAccountByEmail.time",end-start);


        if(accountList != null && accountList.size() > 0) {
            return accountList.get(0);
        }
        return null;
    }

    @Override
    public int updateAccount(String email, Map<String, String> info) {

        long start=System.currentTimeMillis();

        Account account = getAccountByEmail(email);

        for(Map.Entry<String, String> entry : info.entrySet()) {
            String str = entry.getKey();
            if(str.equals("firstName")) {
                account.setFirstName(entry.getValue());
            }else if(str.equals("lastName")) {
                account.setLastName(entry.getValue());
            }else if(str.equals("email")) {
                if(!checkEmailFormat(account.getEmailAddress())) {
                    return -3;
                }
                Account temp = getAccountByEmail(entry.getValue());
                if(temp != null && (account.getEmailAddress() != temp.getEmailAddress())) {
                    return -1;
                }
                account.setEmailAddress(entry.getValue());
            }else if(str.equals("password")) {
                if(!verify(entry.getValue())) {
                    return -2;
                }
                String passwordHash = new UpdatableBCrypt(salt).hash(entry.getValue());
                account.setPassword(passwordHash);
            }
        }
        account.setAccountUpdated(new Date());
        long end=System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.updateAccount.time",end-start);
        return accountMapper.updateByPrimaryKeySelective(account);
    }

    @Override
    public boolean login(String username, String password) {
        AccountExample example = new AccountExample();
        example.createCriteria().andEmailAddressEqualTo(username);
        List<Account> accountList = accountMapper.selectByExample(example);
        Account account = accountList.get(0);
        String passwordHash = account.getPassword();
        System.out.println("password:"+password);
        if(new UpdatableBCrypt(salt).verifyHash(password, passwordHash)) {
            return true;
        }else {

            return false;
        }
    }

    private boolean verify(String password) {
        if(password.length() <= 8 || isPhonticName(password) || isNumericZidai(password)) {
            return false;
        }
        return true;
    }

    private boolean checkEmailFormat(String content){

        String REGEX="^\\w+((-\\w+)|(\\.\\w+))*@\\w+(\\.\\w{2,3}){1,3}$";
        Pattern p = Pattern.compile(REGEX);
        Matcher matcher=p.matcher(content);

        return matcher.matches();
    }

    public static boolean isPhonticName(String str) {
        char[] chars=str.toCharArray();
        boolean isPhontic = false;
        for(int i = 0; i < chars.length; i++) {
            isPhontic = (chars[i] >= 'a' && chars[i] <= 'z') || (chars[i] >= 'A' && chars[i] <= 'Z');
            if (!isPhontic) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumericZidai(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
