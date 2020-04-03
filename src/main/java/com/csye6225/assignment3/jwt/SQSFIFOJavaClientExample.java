package com.csye6225.assignment3.jwt;


import com.amazonaws.services.iotdata.model.PublishRequest;
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

    }

    @Scheduled(fixedRate = 6000 * 3)
    public void receiveMessages() {
        // Receive messages.
        logger.info("Receiving messages from MyFifoQueue.fifo.\n");
        final ReceiveMessageRequest receiveMessageRequest =
                new ReceiveMessageRequest(myQueueUrl);

        // Uncomment the following to provide the ReceiveRequestDeduplicationId
        //receiveMessageRequest.setReceiveRequestAttemptId("1");
        final List<Message> messages = sqs.receiveMessage(receiveMessageRequest)
                .getMessages();
        for (final Message message : messages) {
            for (final Entry<String, String> entry : message.getAttributes()
                    .entrySet()) {
                logger.info("  Message:  " + message.toString());
                logger.info("  Name:  " + entry.getKey() + "  Value: " + entry.getValue());
            }
        }

    }

    public void sendMessage(String message) {

        // Send a message.
        logger.info("Sending a message to csye6225_queue.fifo.");
        final SendMessageRequest sendMessageRequest =
                new SendMessageRequest(myQueueUrl,
                        message);

        /*
         * When you send messages to a FIFO queue, you must provide a
         * non-empty MessageGroupId.
         */
        sendMessageRequest.setMessageGroupId("messageGroup1");

        // Uncomment the following to provide the MessageDeduplicationId
        //sendMessageRequest.setMessageDeduplicationId("1");
        final SendMessageResult sendMessageResult = sqs
                .sendMessage(sendMessageRequest);
        final String sequenceNumber = sendMessageResult.getSequenceNumber();
        final String messageId = sendMessageResult.getMessageId();
        logger.info("SendMessage succeed with messageId "
                + messageId + ", sequence number " + sequenceNumber + "\n");



    }

}
