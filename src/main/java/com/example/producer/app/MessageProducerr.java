package com.example.producer.app;

import javax.jms.JMSException;
import javax.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.ibm.mq.jms.MQQueue;

@Component
public class MessageProducerr {

    @Autowired
    private JmsTemplate jmsTemplate;

    // MqConfig myConfig = new MqConfig();

    @Value("${MQ.QUEUE_NAME}")
    private String queueName;

    public void sendMessage(String message) throws JMSException {
        System.out.println("in service class message : " + message);
        Queue queue = new MQQueue(queueName);
        // Queue queue = () -> queueName;
        jmsTemplate.convertAndSend(queue, message);
        System.out.println("Messege sent");
        // myConfig.sendXml(message);
    }

    public void sendMessageUsingConn(String message) {
        MqConfig mqConfig = new MqConfig();
        if (mqConfig.connect()) { // Make sure you call connect() before using context
            mqConfig.sendXml(message);
        } else {
            System.out.println("Failed to connect.");
        }
    }

}
