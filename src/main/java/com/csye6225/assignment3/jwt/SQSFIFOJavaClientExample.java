package com.csye6225.assignment3.jwt;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.iotdata.model.PublishRequest;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

import com.csye6225.assignment3.controller.BillController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map.Entry;

@Component
public class SQSFIFOJavaClientExample {

    private String myQueueUrl;
    static final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    private final static Logger logger = LoggerFactory.getLogger(BillController.class);
    //
    private AmazonSNS sns;


    public SQSFIFOJavaClientExample() {

        logger.info("create SQSFIFOJavaClientExample instance");
        try {
            CreateQueueResult create_result = sqs.createQueue("testQueue");
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                throw e;
            }
        }
        this.myQueueUrl = sqs.getQueueUrl("testQueue").getQueueUrl();

        //
        AWSCredentials credentials = new BasicAWSCredentials("AKIAVEFNXYRYWIO4NI5C", "AwKOfREjgxq1K/YE5p4qOvA4aNZfGzFe//AWf9L7");
        this.sns = AmazonSNSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).build();



    }

   
    public void sendMessage(String message) {

        // Send a message.
        logger.info("Sending a message to " + this.myQueueUrl);

        if(message.isEmpty()){
            logger.info("No message being sent to " + this.myQueueUrl);
            return;
        }

        SendMessageRequest sendMessageRequest =
                new SendMessageRequest().withQueueUrl(myQueueUrl).withMessageBody(message);

        /*
         * When you send messages to a FIFO queue, you must provide a
         * non-empty MessageGroupId.
         */
        //sendMessageRequest.setMessageGroupId("messageGroup1");

        // Uncomment the following to provide the MessageDeduplicationId
        //sendMessageRequest.setMessageDeduplicationId("1");
        SendMessageResult sendMessageResult = sqs
                .sendMessage(sendMessageRequest);
        String sequenceNumber = sendMessageResult.getSequenceNumber();
        String messageId = sendMessageResult.getMessageId();
        logger.info("SendMessage succeed with messageId "
                + messageId + ", sequence number " + sequenceNumber + "\n");



    }

}
