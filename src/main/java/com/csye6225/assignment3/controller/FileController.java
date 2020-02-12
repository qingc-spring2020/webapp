package com.csye6225.assignment3.controller;

import cn.hutool.json.JSONObject;
import com.csye6225.assignment3.mbg.model.Account;
import com.csye6225.assignment3.mbg.model.AttachedFile;
import com.csye6225.assignment3.mbg.model.Bill;
import com.csye6225.assignment3.service.AccountService;
import com.csye6225.assignment3.service.AttachedFileService;
import com.csye6225.assignment3.service.BillService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class FileController {

    private final static String initDir = "/Users/ricardo/Desktop/csye6225_file_disk/";

    //private final static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    AccountService accountService;
    @Autowired
    BillService billService;
    @Autowired
    AttachedFileService fileService;
    @Autowired
    private Environment environment;

    @GetMapping("/v1/bill/{billId}/file/{fileId}")
    public Object getFileInfo(@PathVariable("billId") String billId, @PathVariable("fileId") String fileId, HttpServletRequest request, HttpServletResponse response) {

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

                    Bill bill = billService.getBillInfo(billId);
                    if (bill == null) {
                        response.setStatus(404);
                        jsonObject.put("message", "bill is not existed");
                    } else {
                        if(!bill.getOwnerId().equals(account.getUserId())) {
                            response.setStatus(400);
                            jsonObject.put("message","The bill does not belong to this account");
                        }else {

                            AttachedFile file = fileService.getFileInfoById(fileId);
                            if(null == file) {
                                response.setStatus(404);
                                jsonObject.put("message","fail to get the file info");
                            }else {
                                jsonObject.put("file_name", file.getFileName());
                                jsonObject.put("id", file.getFileId());
                                jsonObject.put("url", file.getUrl());

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = dateFormat.format(file.getUploadDate());
                                //System.out.println("date1:"+date);

                                jsonObject.put("upload_date", date);
                            }
                        }

                    }
                }else{
                    jsonObject.put("message", "Password is incorrect");
                }

            }

        }else {
            jsonObject.put("message","401 Unauthorized status");
        }
        return jsonObject;

    }

    @PostMapping("/v1/bill/{id}/file")
    public Object updateFile(@PathVariable String id , @RequestPart("file") MultipartFile frontFile,HttpServletRequest request, HttpServletResponse response) throws IOException {
        String auth = request.getHeader("Authorization");
        JSONObject jsonObject = new JSONObject(true);
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
                        response.setStatus(404);
                        jsonObject.put("message","Bill Id not found");
                        //throw new ResourceNotFoundException("Bill Id not found");
                    } else {
                        if(!bill.getOwnerId().equals(account.getUserId())) {
                            response.setStatus(400);
                            jsonObject.put("message","Bill conflict");
                            //throw new ResourceNotFoundException("Bill conflict");
                        }else {

                            if(null != bill.getFileId()) {
                                response.setStatus(404);
                                jsonObject.put("message","File conflict");
                                //throw new Exception("File conflict");
                            }else {

                                String originalFileName = frontFile.getOriginalFilename();
                                String uploadFileName = frontFile.getOriginalFilename();
                                String dir = "/Users/ricardo/Desktop/csye6225_file_disk/";
                                saveFileToDisk(dir, frontFile);

                                AttachedFile file = fileService.saveFileToDataBase(bill.getBillId(), originalFileName, uploadFileName);
                                billService.updateFileInfo(bill, file.getFileId());

                                jsonObject.put("file_name", file.getFileName());
                                jsonObject.put("id", file.getFileId());
                                jsonObject.put("url", file.getUrl());
                                System.out.println(file.getFileId());

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String date = dateFormat.format(file.getUploadDate());
                                jsonObject.put("upload_date", date);
                            }

                        }

                    }

                }else {
                    jsonObject.put("message","Password is incorrect");
                    //throw new Exception("Password is incorrect");
                }
            }
        }else {
            response.setStatus(401);
            jsonObject.put("message","401 Unauthorized status");
        }
        System.out.println("file.getFileId()");
        return jsonObject;


    }

    @DeleteMapping("/v1/bill/{billId}/file/{fileId}")
    public Object deleteBillInfo(@PathVariable("billId") String billId, @PathVariable("fileId") String fileId,HttpServletRequest request, HttpServletResponse response) {
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

                    Bill bill = billService.getBillInfo(billId);
                    if (bill == null) {
                        response.setStatus(404);
                        jsonObject.put("message", "bill is not existed");
                    } else {
                        System.out.println("billId"+bill.getBillId());
                        if(!bill.getOwnerId().equals(account.getUserId())) {
                            response.setStatus(400);
                            jsonObject.put("message","The bill does not belong to this account");
                        }else {
                            AttachedFile file = fileService.getFileInfoById(fileId);
                            if(null == file || !bill.getFileId().equals(fileId)) {
                                response.setStatus(404);
                                jsonObject.put("message", "file is not existed");
                            }else {
                                deleteFileOnDisk(file.getFileName());
                                billService.deleteFileInfoByBill(bill);
                                fileService.deleteFileInfoById(fileId);

                                jsonObject.put("message", "successfully");
                            }
                        }

                    }
                }else {
                    jsonObject.put("message","Password is incorrect");
                }
            }
        }else {
            jsonObject.put("message","401 Unauthorized status");
        }
        return jsonObject;
    }

    private static void deleteFileOnDisk(String fileName) {
        try {
            File file = new File(initDir + fileName);
            file.delete();
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    private static void saveFileToDisk(String dir,MultipartFile frontFile) throws IOException {
        //File dirFile = new File(dir);
        String filename = frontFile.getOriginalFilename();
        File file = new File(dir+filename);
        //file.createNewFile();
        System.out.println("-----------");
        System.out.println(file.getAbsolutePath());
        frontFile.transferTo(file);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        bufferedReader.readLine();
        bufferedReader.close();

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
