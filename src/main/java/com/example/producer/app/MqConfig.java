package com.example.producer.app;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class MqConfig {

    private static final Logger logger = Logger.getLogger(MqConfig.class.getName());

    @Value("${MQ.HOST}")
    private String host;

    @Value("${MQ.PORT}")
    private int port;

    @Value("${MQ.CHANNEL}")
    private String channel;

    @Value("${MQ.QMGR}")
    private String queueManager;

    @Value("${MQ.USER}")
    private String user;

    @Value("${MQ.PASSWORD}")
    private String password;

    private JMSContext context = null;
    private Destination destination = null;
    private JMSProducer producer = null;

    private static boolean isConnected = false;

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        MQConnectionFactory connectionFactory = new MQConnectionFactory();
        connectionFactory.setHostName("127.0.0.1");
        connectionFactory.setPort(1414);
        connectionFactory.setQueueManager("MQ1");
        connectionFactory.setChannel("DEV.APP.SVRCONN");
        // connectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

        connectionFactory.createConnection("MUSR_MQADMIN", "1213");
        // Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // Queue queue = session.createQueue("QUEUE1");

        // MessageProducer producer = session.createProducer(queue);
        // TextMessage message = session.createTextMessage("Hello, IBM MQ!");

        return connectionFactory;
    }

    // @Bean
    // public MQConnectionFactory mqConnectionFactory() throws Exception {
    // MQConnectionFactory connectionFactory = new MQConnectionFactory();
    // connectionFactory.setHostName(host);
    // connectionFactory.setPort(port);
    // connectionFactory.setQueueManager(queueManager);
    // connectionFactory.setChannel(channel);
    // return connectionFactory;
    // }

    // @Bean
    // public SingleConnectionFactory singleConnectionFactory() throws Exception {
    // SingleConnectionFactory singleConnectionFactory = new
    // SingleConnectionFactory(mqConnectionFactory());
    // return singleConnectionFactory;
    // }

    @Bean
    public JmsTemplate jmsTemplate() throws Exception {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setDeliveryMode(javax.jms.DeliveryMode.PERSISTENT);
        return jmsTemplate;
    }

    public boolean connect() {
        try {

            // Create a connection factory
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            // Set the properties
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, "127.0.0.1");
            cf.setIntProperty(WMQConstants.WMQ_PORT, 1414);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, "DEV.APP.SVRCONN");
            // cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE,
            // WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, "MQ1");
            // cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "IDC Adapter");
            // cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);

            // connectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

            cf.setStringProperty(WMQConstants.USERID, "MUSR_MQADMIN");
            cf.setStringProperty(WMQConstants.PASSWORD, "1213");

            // Create JMS objects
            context = cf.createContext();
            // destination = context.createQueue("queue:///" + QUEUE_NAME);
            destination = context.createQueue("QUEUE1");
            // Create producer
            producer = context.createProducer();

            // recordSuccess();

        } catch (JMSException jmsex) {
            logger.severe("JMSException occurred: " + jmsex.getMessage());
            jmsex.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean sendXml(String xml) {

        isConnected = this.connect();
        System.out.println("connection is astablished " + isConnected);

        try

        {
            TextMessage message = context.createTextMessage(xml);
            producer.send(destination, message);
            System.out.println("destination : " + destination);
            System.out.println("message : " + message);
            // log.info("MQ Message sent:");

        } catch (Exception e) {
            logger.severe("JMSException occurred: " + e.getMessage());
            e.printStackTrace();

            isConnected = false;
            return false;
        }
        return true;
    }
}
