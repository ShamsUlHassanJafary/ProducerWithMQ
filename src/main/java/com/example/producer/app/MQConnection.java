package com.example.producer.app;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQQueueManager;

public class MQConnection {
    public static void main(String[] args) {
        MQEnvironment.hostname = "127.0.0.1";
        MQEnvironment.port = 1414;
        MQEnvironment.channel = "DEV.APP.SVRCONN";
        MQEnvironment.userID = "MUSR_MQADMIN";
        MQEnvironment.password = "1213";

        try {
            MQQueueManager qMgr = new MQQueueManager("MQ1");
            System.out.println("Connection successful!");
            qMgr.disconnect();
        } catch (Exception e) {
            System.err.println("Error connecting to the queue manager: " + e.getMessage());
        }
    }
}
