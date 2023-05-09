package com.example.producer.app;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    private MessageProducerr messageProducer;

    @GetMapping("/send/{message}")
    public void sendMessage(@PathVariable String message) throws JMSException {
        System.out.println("in controller message : " + message);
        messageProducer.sendMessage(message);
    }

    @GetMapping("/sendmessage/{message}")
    public void sendMessageUsingConn(@PathVariable String message) {
        System.out.println("in controller message : " + message);
        messageProducer.sendMessageUsingConn(message);
    }
}
