package com.csye6225.assignment3.controller;

import cn.hutool.json.JSONObject;

import com.csye6225.assignment3.mbg.model.Account;
import com.csye6225.assignment3.service.AccountService;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
public class AccountController {

    private final static Logger logger= LoggerFactory.getLogger(BillController.class);
    @Autowired
    private StatsDClient statsDClient;

    @Autowired
    AccountService accountService;

    @GetMapping("/v1/user/self")
    public Object getInfo(HttpServletRequest request,HttpServletResponse response) {

        long start = System.currentTimeMillis();


        statsDClient.incrementCounter("endpoint.login.http.get.count");

        logger.info("Getting User authenticated");

        String auth = request.getHeader("Authorization");
        System.out.println("/v1/user/self/get");
        JSONObject jsonObject = new JSONObject(true);
        System.out.println("auth"+auth);
        if(null != auth) {
            String[] userAccount = decode(auth);
            Account account = accountService.getAccountByEmail(userAccount[0]);
            if(account == null) {
                response.setStatus(401);
                jsonObject.put("message","401 Unauthorized status");
            }else {
                if(accountService.login(userAccount[0],userAccount[1])) {
                    jsonObject.put("id",account.getUserId());
                    jsonObject.put("first_name",account.getFirstName());
                    jsonObject.put("last_name",account.getLastName());
                    jsonObject.put("email_address",account.getEmailAddress());

                /*
                String dateCreated = accountTemp.getAccountCreated().toString();
                String dateUpdated = accountTemp.getAccountUpdated().toString();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
                Date dateCreatedParse = format.parse(dateCreated);
                Date dateUpdatedParse = format.parse(dateUpdated);

                 */

                    jsonObject.put("account_created",account.getAccountCreated());
                    jsonObject.put("account_updated",account.getAccountUpdated());

                    logger.info("User successfully authenticated");

                }else {
                    jsonObject.put("message","Password is incorrect");
                }
            }
        }else {
            jsonObject.put("message","401 Unauthorized status");
        }

        long end = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.get.time",end-start);

        return jsonObject;
    }


    @PostMapping("/v1/user")
    public Object register(@RequestBody Account account, HttpServletResponse response) throws IOException, ParseException {

        long start=System.currentTimeMillis();

        statsDClient.incrementCounter("endpoint.user.http.post.count");
        logger.info("Getting Account registered");
        // System.out.println("/v1/user");

        JSONObject jsonObject = new JSONObject(true);

        if(account.getFirstName() == null || account.getLastName() == null || account.getPassword() == null || account.getEmailAddress() == null) {
            jsonObject.put("message", "email json format is not right");
        }else {

            if (-1 == accountService.createAccount(account)) {
                response.setStatus(400);
                //response.getWriter().append("bad request");
                jsonObject.put("message", "email conflict");

                logger.error("Account already exist");
                logger.info("Account Registered Response code: " + HttpStatus.CONFLICT);

            } else if (0 == accountService.createAccount(account)) {
                logger.error("Password not accepted.Password must be greater than 8 characters with atleast one uppercase, one lowercase, one digit and one special character" );
                logger.info("User Registered Response code: " + HttpStatus.BAD_REQUEST);
                jsonObject.put("message", "please input a NIST password");
            } else if (-2 == accountService.createAccount(account)) {
                jsonObject.put("message", "please input a right email");
            } else {



                Account accountTemp = accountService.getAccountByEmail(account.getEmailAddress());
                jsonObject.put("id",accountTemp.getUserId());
                jsonObject.put("first_name",accountTemp.getFirstName());
                jsonObject.put("last_name",accountTemp.getLastName());
                jsonObject.put("email_address",accountTemp.getEmailAddress());

                /*
                String dateCreated = accountTemp.getAccountCreated().toString();
                String dateUpdated = accountTemp.getAccountUpdated().toString();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
                Date dateCreatedParse = format.parse(dateCreated);
                Date dateUpdatedParse = format.parse(dateUpdated);

                 */

                jsonObject.put("account_created",accountTemp.getAccountCreated());
                jsonObject.put("account_updated",accountTemp.getAccountUpdated());

                logger.info("Account successfully registered");
            }
        }
        logger.info("Account Registered Response code: " + HttpStatus.CREATED);

        long end=System.currentTimeMillis();

        statsDClient.recordExecutionTime("endpoint.login.http.post.time",end-start);
        return jsonObject;

    }

    private static String[] decode(String authentication) {
        final Base64.Decoder decoder = Base64.getDecoder();
        String[] auths = authentication.split("\\s+");
        String authInfo = auths[1];
        byte[] decodedBytes = decoder.decode(authInfo);
        String pair = new String(decodedBytes);
        String[] userAccount = pair.split(":",2);
        return userAccount;
    }

    @PutMapping("/v1/user/self")
    public Object update(@RequestBody Map<String, String> info, HttpServletRequest request,HttpServletResponse response) throws IOException {

        long start=System.currentTimeMillis();

        statsDClient.incrementCounter("endpoint.login.http.put.count");

        logger.info("Update user information");

        String auth = request.getHeader("Authorization");
        System.out.println("/v1/user/self/put");
        JSONObject jsonObject = new JSONObject();

        if(null != auth) {
            String[] userAccount = decode(auth);
            Account account = accountService.getAccountByEmail(userAccount[0]);
            if(account == null) {
                response.setStatus(401);
                jsonObject.put("message","401 Unauthorized status");
            }else {
                if(accountService.login(userAccount[0],userAccount[1])) {
                    info.remove("userId");
                    info.remove("accountCreated");
                    info.remove("accountUpdated");
                    Integer status = accountService.updateAccount(userAccount[0], info);
                    if(status == 0) {
                        logger.info("Account does not exist in the system");
                        jsonObject.put("message","The account has not been created");
                    }else if (status == -1) {
                        response.setStatus(400);
                        response.getWriter().append("bad request");
                        jsonObject.put("message","Email has existed");
                    }else if(status == -2) {
                        jsonObject.put("message","please input a NIST password");
                    }else if(status == -3){
                        jsonObject.put("message","please input a right email");
                    }
                    else {
                        jsonObject.put("message","Update successfully");
                    }
                }else {
                    jsonObject.put("message","Password is incorrect");
                }
            }
        }else {
            response.setStatus(401);
            jsonObject.put("message","401 Unauthorized status");
        }


        long end=System.currentTimeMillis();

        statsDClient.recordExecutionTime("endpoint.login.http.put.time",end-start);

        return jsonObject;
    }






}
