package com.csye6225.assignment3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.csye6225.assignment3.exception.ResourceNotFoundException;
import com.csye6225.assignment3.mbg.model.AttachedFile;
import com.csye6225.assignment3.mbg.model.Bill;
import com.timgroup.statsd.StatsDClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;

@Service
public class AmazonS3ImageService {

    private AmazonS3 amazonS3;

    @Autowired
    private StatsDClient statsDClient;

    private static String bucketPath="/etc/environment";

    //@Value("${amazonProperties.bucketName}")
    private String s3BucketName;
    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;


    @Autowired
    public AmazonS3ImageService() throws IOException {
        //this.amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new InstanceProfileCredentialsProvider(false)).build();
        AWSCredentials credentials = new BasicAWSCredentials("AKIAVEFNXYRYWIO4NI5C", "AwKOfREjgxq1K/YE5p4qOvA4aNZfGzFe//AWf9L7");
        this.amazonS3 = new AmazonS3Client(credentials);
        this.s3BucketName = getBucketName(bucketPath);
        System.out.println("this.s3BucketName:"+this.s3BucketName);
/*
        try {
            amazonS3.createBucket(bucket_name);
        }catch(AmazonS3Exception e) {
            System.err.println(e.getErrorMessage());
        }

 */
    }

    private String getBucketName(String s) throws IOException {

        File file = new File(s);
        FileReader fileReader = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(fileReader);
        int number = 2;
        String content = "";
        int lines = 0;
        while(content != null ) {
            ++lines;
            content = reader.readLine();
            if(number == lines) {
                break;
            }
        }
        String[] temp = content.split("=");
        return temp[1];
    }


    public String uploadImageToS3(Bill bill, MultipartFile multipartFile) throws IOException {
        // File fileToUpload = convertMultiPartToFile(multipartFile);

        long start=System.currentTimeMillis();
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType("image");
        String key = bill.getBillId()+ "_" + multipartFile.getOriginalFilename();
        InputStream file = multipartFile.getInputStream();
        amazonS3.putObject(new PutObjectRequest(s3BucketName, key, file, objMeta));
        String signedUrl = getPreSignedUrl(key);
        long end=System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.uploadImageToS3.time",end-start);


        return signedUrl;
    }

    public void deleteImageFromS3(AttachedFile file){
        long start=System.currentTimeMillis();
        amazonS3.deleteObject(new DeleteObjectRequest(s3BucketName, file.getFileName()));
        long end=System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.login.http.deleteImageFromS3.time",end-start);
    }

    public String getPreSignedUrl(String key){
        if(!imageExistsInS3(key)){

            throw new ResourceNotFoundException("Image not found in S3 bucket");
        }
        /* get signed URL (valid for 2 minutes) */
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(s3BucketName, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(DateTime.now().plusMinutes(2).toDate());
        URL signedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return signedUrl.toString();
    }

    private boolean imageExistsInS3(String key){
        ObjectListing objectListing = amazonS3.listObjects(s3BucketName);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            if(os.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }

    public void deleteAll(){
        ObjectListing objectListing = amazonS3.listObjects(s3BucketName);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            amazonS3.deleteObject(new DeleteObjectRequest(s3BucketName, os.getKey()));
        }
    }

}