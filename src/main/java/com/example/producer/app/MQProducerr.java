package com.example.producer.app;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.*;

public class MQProducerr {
    public static void main(String[] args) {
        try {
            MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
            factory.setHostName("127.0.0.1");
            factory.setPort(1415);
            factory.setQueueManager("MQ2");
            factory.setChannel("channel1");
            factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

            Connection connection = factory.createConnection("MUSR_MQADMIN", "1213");
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("QUEUE2");

            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage("Hello, IBM MQ!");

            producer.send(message);

            System.out.println("Message sent!");

            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
