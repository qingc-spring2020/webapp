package com.csye6225.assignment3.service.impl;

import com.csye6225.assignment3.mbg.mapper.AttachedFileMapper;
import com.csye6225.assignment3.mbg.model.AttachedFile;
import com.csye6225.assignment3.service.AttachedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AttachedFileServiceImpl implements AttachedFileService {

    private static final String dir = "/Users/ricardo/Desktop/csye6225_file_disk/";

    @Autowired
    AttachedFileMapper fileMapper;


    @Override
    public AttachedFile saveFileToDataBase(String billId, String fileName, String md5, Long fileSize) {

        AttachedFile file = new AttachedFile();
        String uuid = UUID.randomUUID().toString();
        file.setFileId(uuid);

        file.setFileName(fileName);

        //String initUrl = "/Users/ricardo/Desktop/csye6225_database_disk";
        file.setUrl(dir+fileName);
        file.setFileMd5(md5);
        file.setFileSize(fileSize);

        fileMapper.insertSelective(file);
        return fileMapper.selectByPrimaryKey(uuid);
    }

    @Override
    public AttachedFile getFileInfoById(String fileId) {
        AttachedFile file = fileMapper.selectByPrimaryKey(fileId);
        return file;
    }

    @Override
    public void deleteFileInfoById(String fileId) {
        fileMapper.deleteByPrimaryKey(fileId);
    }
}
