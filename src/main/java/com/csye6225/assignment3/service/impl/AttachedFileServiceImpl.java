package com.csye6225.assignment3.service.impl;

import com.csye6225.assignment3.mbg.mapper.AttachedFileMapper;
import com.csye6225.assignment3.mbg.model.AttachedFile;
import com.csye6225.assignment3.service.AttachedFileService;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AttachedFileServiceImpl implements AttachedFileService {

    private static final String dir = "/home/ubuntu/csye6225_file_disk/";

    @Autowired
    AttachedFileMapper fileMapper;
    @Autowired
    private StatsDClient statsDClient;


    @Override
    public AttachedFile saveFileToDataBase(String billId, String fileName, String md5, Long fileSize, String url) {

        long start=System.currentTimeMillis();
        AttachedFile file = new AttachedFile();
        String uuid = UUID.randomUUID().toString();
        file.setFileId(uuid);

        file.setFileName(fileName);

        //String initUrl = "/Users/ricardo/Desktop/csye6225_database_disk";
        file.setUrl(url);
        file.setFileMd5(md5);
        file.setFileSize(fileSize);

        fileMapper.insertSelective(file);
        long end=System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.saveFileToDataBase.time",end-start);
        return fileMapper.selectByPrimaryKey(uuid);
    }

    @Override
    public AttachedFile getFileInfoById(String fileId) {
        long start=System.currentTimeMillis();
        AttachedFile file = fileMapper.selectByPrimaryKey(fileId);
        long end=System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.getFileInfoById.time",end-start);
        return file;
    }

    @Override
    public void deleteFileInfoById(String fileId) {
        long start=System.currentTimeMillis();
        fileMapper.deleteByPrimaryKey(fileId);
        long end=System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.deleteFileInfoById.time",end-start);


    }
}
