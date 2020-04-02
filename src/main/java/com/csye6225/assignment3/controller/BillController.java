package com.csye6225.assignment3.controller;


import cn.hutool.json.JSONObject;
import com.csye6225.assignment3.jwt.SQSFIFOJavaClientExample;
import com.csye6225.assignment3.mbg.model.Account;
import com.csye6225.assignment3.mbg.model.Bill;
import com.csye6225.assignment3.service.AccountService;
import com.csye6225.assignment3.service.BillService;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class BillController {

    private final static Logger logger = LoggerFactory.getLogger(BillController.class);

    @Autowired
    private StatsDClient statsDClient;

    @Autowired
    AccountService accountService;
    @Autowired
    BillService billService;
    @Autowired
    SQSFIFOJavaClientExample sqsfifoJavaClientExample;

    @GetMapping("/v1/bills")
    public Object getAllBillInfo(HttpServletRequest request,HttpServletResponse response) {


        long start=System.currentTimeMillis();

        statsDClient.incrementCounter("endpoint.allBills.http.get");
        logger.info("Get all bills");

        String auth = request.getHeader("Authorization");
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
                    List<Bill> billList = billService.getAllBillInfo(account.getUserId());
                    int i = 0;
                    for(Bill bill : billList) {

                        JSONObject jsonObject1 = new JSONObject(true);


                        jsonObject1.put("id",bill.getBillId());
                        jsonObject1.put("created_ts",bill.getCreatedTs());
                        jsonObject1.put("updated_ts",bill.getUpdatedTs());
                        jsonObject1.put("owner_id",bill.getOwnerId());
                        jsonObject1.put("vendor",bill.getVendor());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String date = dateFormat.format(bill.getBillDate());
                        //System.out.println("date1:"+date);

                        jsonObject1.put("bill_date",date);

                        date = dateFormat.format(bill.getDueDate());
                        //date = dateFormat.parse(sDate);
                        jsonObject1.put("due_date",date);

                        jsonObject1.put("amount_due",bill.getAmountDue());
                        jsonObject1.put("categories",bill.getCategories());
                        jsonObject1.put("paymentStatus",bill.getPaymentStatus());
                        ++i;
                        String str = "bill" + i;
                        jsonObject.put(str,jsonObject1);

                    }
                }else {
                    jsonObject.put("message","Password is incorrect");
                }
            }
        }else {
            jsonObject.put("message","401 Unauthorized status");
        }
        logger.info("Get all Bills Successful- Response code: " + HttpStatus.OK);


        long end=System.currentTimeMillis();

        statsDClient.recordExecutionTime("endpoint.login.http.getBills.time",end-start);
        return jsonObject;
    }

    @PutMapping("/v1/bill/{id}")
    public Object updateBillInfo(@PathVariable("id") String id, @RequestBody Map<String, Object> billInfo, HttpServletRequest request,HttpServletResponse response) throws ParseException {

        long start=System.currentTimeMillis();

        statsDClient.incrementCounter("endpoint.updateBook.http.put");
        logger.info("Update Bill request " + id);

        String auth = request.getHeader("Authorization");
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

                    Bill bill = billService.getBillInfo(id);

                    if(!billInfo.containsKey("amount_due") || ((Double)billInfo.get("amount_due") < 0.01)){
                        jsonObject.put("message","amount_due must more than 0.01");
                        return jsonObject;

                    }

                    if (bill == null) {
                        logger.error("Validation failed for request: Bill cannot be null. ID: "+ id );

                        response.setStatus(401);
                        jsonObject.put("message", "bill is not existed");
                    } else {
                        bill.setAmountDue((Double) billInfo.get("amount_due"));
                        //System.out.println("billId"+bill.getBillId());
                        if(!bill.getOwnerId().equals(account.getUserId())) {
                            response.setStatus(400);
                            jsonObject.put("message","The bill does not belong to this account");
                        }else {

                            billInfo.remove("billId");
                            billInfo.remove("createdTs");
                            billInfo.remove("updatedTs");
                            billInfo.remove("ownerId");

                            if(billInfo.containsKey("vendor")) {
                                bill.setVendor(billInfo.get("vendor").toString());
                            }
                            if(billInfo.containsKey("bill_date")) {
                                //System.out.println("bill_date1:");
                                String billDate = billInfo.get("bill_date").toString();
                                //System.out.println("bill_date2:");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = dateFormat.parse(billDate);
                                //System.out.println("bill_date3:"+date);
                                bill.setBillDate((date));
                            }
                            if(billInfo.containsKey("due_date")) {
                                String dueDate = billInfo.get("due_date").toString();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = dateFormat.parse(dueDate);
                                bill.setDueDate(date);
                            }
                            if(billInfo.containsKey("amount_due")) {
                                bill.setAmountDue((Double)billInfo.get("amount_due"));
                            }
                            if(billInfo.containsKey("categories")) {
                                bill.setCategories(billInfo.get("categories").toString());
                            }
                            if(billInfo.containsKey("paymentStatus")) {
                                if(!billInfo.get("paymentStatus").toString().equals("paid") &&
                                        !billInfo.get("paymentStatus").toString().equals("due") &&
                                        !billInfo.get("paymentStatus").toString().equals("past_due") &&
                                        !billInfo.get("paymentStatus").toString().equals("no_payment_required"))
                                {
                                    jsonObject.put("message","please input a right paymentStatus");
                                    return jsonObject;
                                }
                                bill.setPaymentStatus(billInfo.get("paymentStatus").toString());
                            }

                            bill.setOwnerId(account.getUserId());
                            Bill billTemp = billService.updateBillInfo(bill);
                            jsonObject.put("id",billTemp.getBillId());
                            jsonObject.put("created_ts",billTemp.getCreatedTs());
                            jsonObject.put("updated_ts",billTemp.getUpdatedTs());
                            jsonObject.put("owner_id",billTemp.getOwnerId());
                            jsonObject.put("vendor",billTemp.getVendor());

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String date = dateFormat.format(billTemp.getBillDate());
                            //System.out.println("date1:"+date);

                            jsonObject.put("bill_date",date);

                            date = dateFormat.format(billTemp.getDueDate());
                            //date = dateFormat.parse(sDate);
                            jsonObject.put("due_date",date);

                            jsonObject.put("amount_due",billTemp.getAmountDue());
                            jsonObject.put("categories",billTemp.getCategories());
                            jsonObject.put("paymentStatus",billTemp.getPaymentStatus());

                            logger.info("Update Bill Successful- Response code: " + HttpStatus.NO_CONTENT);




                        }

                    }
                }else {
                    jsonObject.put("message","Password is incorrect");
                }
            }
        }else {
            jsonObject.put("message","401 Unauthorized status");
        }

        long end=System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.updateBill.time",end-start);


        return jsonObject;

    }


    @DeleteMapping("/v1/bill/{id}")
    public Object deleteBillInfo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {

        long start=System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.deleteBill.http.delete");
        logger.info("Delete bill by id:" + id);

        String auth = request.getHeader("Authorization");
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

                    Bill bill = billService.getBillInfo(id);
                    if (bill == null) {
                        logger.warn("Bill ID " + id + " not found. " );

                        response.setStatus(404);
                        jsonObject.put("message", "bill is not existed");
                    } else {
                        System.out.println("billId"+bill.getBillId());
                        if(!bill.getOwnerId().equals(account.getUserId())) {
                            response.setStatus(400);
                            jsonObject.put("message","The bill does not belong to this account");
                        }else {
                            logger.info("Delete Bill Successful- Response code: " + HttpStatus.NO_CONTENT);

                            billService.deleteBill(id);
                            jsonObject.put("message","successfully");
                        }

                    }
                }else {
                    jsonObject.put("message","Password is incorrect");
                }
            }
        }else {
            jsonObject.put("message","401 Unauthorized status");
        }
        long end=System.currentTimeMillis();

        statsDClient.recordExecutionTime("endpoint.login.http.deleteBill.time",end-start);

        return jsonObject;
    }

    @GetMapping("/v1/bill/{id}")
    public Object getBillInfo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {

        long start=System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.billById.http.get");
        logger.info("Searching bill by id: " + id);



        String auth = request.getHeader("Authorization");
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
                    Bill bill = billService.getBillInfo(id);
                    if (bill == null) {

                        logger.warn("Bill ID " +id + " not found. " );
                        logger.info("Get bill by Id- Response code: " + HttpStatus.NOT_FOUND);

                        response.setStatus(404);
                        jsonObject.put("message", "bill is not existed");
                    } else {
                        if(!bill.getOwnerId().equals(account.getUserId())) {
                            response.setStatus(400);
                            jsonObject.put("message","The bill does not belong to this account");
                        }else {

                            logger.info("Bill returned: id" + id);
                            logger.info("Get bill by Id Successful- Response code: " + HttpStatus.OK);

                            jsonObject.put("id", bill.getBillId());
                            jsonObject.put("created_ts", bill.getCreatedTs());
                            jsonObject.put("updated_ts", bill.getUpdatedTs());
                            jsonObject.put("owner_id", bill.getOwnerId());
                            jsonObject.put("vendor", bill.getVendor());

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String date = dateFormat.format(bill.getBillDate());
                            //System.out.println("date1:"+date);

                            jsonObject.put("bill_date", date);

                            date = dateFormat.format(bill.getDueDate());
                            //date = dateFormat.parse(sDate);
                            jsonObject.put("due_date", date);

                            jsonObject.put("amount_due", bill.getAmountDue());
                            jsonObject.put("categories", bill.getCategories());
                            jsonObject.put("paymentStatus", bill.getPaymentStatus());
                        }

                    }
                }else{
                    jsonObject.put("message", "Password is incorrect");
                }

            }

        }else {
            jsonObject.put("message","401 Unauthorized status");
        }
        long end=System.currentTimeMillis();

        statsDClient.recordExecutionTime("endpoint.login.http.getBill.time",end-start);

        return jsonObject;
    }

    @PostMapping("/v1/bill/")
    public Object createBill(@RequestBody Map<String,Object> billInfo , HttpServletRequest request, HttpServletResponse response) throws ParseException {

        long start=System.currentTimeMillis();

        statsDClient.incrementCounter("endpoint.addBill.http.post");
        logger.info("Add bill request ");

        String auth = request.getHeader("Authorization");
        System.out.println("/v1/bill/");
        JSONObject jsonObject = new JSONObject(true);

        if(null != auth) {
            String[] userAccount = decode(auth);
            Account account = accountService.getAccountByEmail(userAccount[0]);
            if(account == null) {


                response.setStatus(401);
                jsonObject.put("message","401 Unauthorized status");
            }else {
                if(accountService.login(userAccount[0],userAccount[1])) {

                    Bill bill = new Bill();
                    if(!billInfo.containsKey("amount_due") || ((Double)billInfo.get("amount_due") < 0.01)){
                        jsonObject.put("message","amount_due must more than 0.01");
                        return jsonObject;

                    }else {
                            bill.setAmountDue((Double) billInfo.get("amount_due"));
                    }

                    System.out.println("accountService");

                    if(billInfo.containsKey("vendor")) {
                        bill.setVendor(billInfo.get("vendor").toString());
                    }
                    if(billInfo.containsKey("bill_date")) {
                        //System.out.println("bill_date1:");
                        String billDate = billInfo.get("bill_date").toString();
                        //System.out.println("bill_date2:");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = dateFormat.parse(billDate);
                        //System.out.println("bill_date3:"+date);
                        bill.setBillDate((date));
                    }
                    if(billInfo.containsKey("due_date")) {
                        String dueDate = billInfo.get("due_date").toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = dateFormat.parse(dueDate);
                        bill.setDueDate(date);
                    }

                    if(billInfo.containsKey("categories")) {
                        bill.setCategories(billInfo.get("categories").toString());
                    }
                    if(billInfo.containsKey("paymentStatus")) {
                        if(!billInfo.get("paymentStatus").toString().equals("paid") &&
                                !billInfo.get("paymentStatus").toString().equals("due") &&
                                !billInfo.get("paymentStatus").toString().equals("past_due") &&
                                !billInfo.get("paymentStatus").toString().equals("no_payment_required"))
                        {
                            jsonObject.put("message","please input a right paymentStatus");
                            return jsonObject;
                        }
                        bill.setPaymentStatus(billInfo.get("paymentStatus").toString());
                    }

                    bill.setOwnerId(account.getUserId());
                    Bill billTemp = billService.createBill(bill);

                    response.setStatus(201);
                    jsonObject.put("id",billTemp.getBillId());
                    jsonObject.put("created_ts",billTemp.getCreatedTs());
                    jsonObject.put("updated_ts",billTemp.getUpdatedTs());
                    jsonObject.put("owner_id",billTemp.getOwnerId());
                    jsonObject.put("vendor",billTemp.getVendor());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(billTemp.getBillDate());
                    //System.out.println("date1:"+date);

                    jsonObject.put("bill_date",date);

                    date = dateFormat.format(billTemp.getDueDate());
                    //date = dateFormat.parse(sDate);
                    jsonObject.put("due_date",date);

                    jsonObject.put("amount_due",billTemp.getAmountDue());
                    jsonObject.put("categories",billTemp.getCategories());
                    jsonObject.put("paymentStatus",billTemp.getPaymentStatus());

                    logger.info("Add Bill Successful- Response code: " + HttpStatus.CREATED);

                }else {
                    jsonObject.put("message","Password is incorrect");
                }
            }
        }else {
            response.setStatus(401);
            jsonObject.put("message","401 Unauthorized status");
        }

        long end=System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.createBill.time",end-start);

        return jsonObject;
    }

    @GetMapping("/v1/bills/due/{day}")
    public Object getBillList(@PathVariable("day") Integer day,HttpServletRequest request,HttpServletResponse response){
        long start=System.currentTimeMillis();

        statsDClient.incrementCounter("endpoint.getBillList.http.get");
        logger.info("send all bills");

        String auth = request.getHeader("Authorization");
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
                    List<Bill> billList = billService.getAllBillInfo(account.getUserId());
                    int i = 0;
                    Date currentTime = new Date();
                    String message = "";
                    for(Bill bill : billList) {

                        if(isValidDistanceTime(bill.getDueDate(), currentTime,day)) {
                            message = message + "url: http://prod.qingc.me//v1/bill/" + bill.getBillId()+"; ";
                        }

                    }
                    sqsfifoJavaClientExample.sendMessage(message);
                    logger.info("send to SQS successfully");
                    jsonObject.put("message","send to SQS successfully");
                }else {
                    jsonObject.put("message","Password is incorrect");
                }
            }
        }else {
            response.setStatus(401);
            jsonObject.put("message","401 Unauthorized status");
        }
        logger.info("Send all Bills Successful- Response code: " + HttpStatus.OK);


        long end=System.currentTimeMillis();

        statsDClient.recordExecutionTime("endpoint.login.http.sendBills.time",end-start);
        return jsonObject;

    }

    private boolean isValidDistanceTime(Date billDate, Date currentTime, Integer day) {
        int days = 0;
        long time1 = billDate.getTime();
        long time2 = currentTime.getTime();

        long diff;
        if(time1 < time2)
            return false;
        else {
            days = (int) (time1 - time2) /(24*60*60*1000);
            if (days <= day)
                return true;
            else return false;
        }

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

}
