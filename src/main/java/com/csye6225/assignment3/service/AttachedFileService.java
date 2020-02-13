package com.csye6225.assignment3.service;

import com.csye6225.assignment3.mbg.model.AttachedFile;


public interface AttachedFileService {

    AttachedFile saveFileToDataBase(String billId, String fileName, String md5, Long fileSize);

    AttachedFile getFileInfoById(String fileId);

    void deleteFileInfoById(String fileId);
}
