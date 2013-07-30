package com.bharath;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * User: bharadwaj
 * Date: 30/07/13
 * Time: 11:14 AM
 */
public class RequestObjectReceiverJsonPublisher implements MessageListener {

    private JmsDataPublisher jsonDataPublisher;

    public RequestObjectReceiverJsonPublisher() {
        jsonDataPublisher = new JmsDataPublisher(JmsConstants.BROKER_URL, JmsConstants.TOPIC_HTTP_JSON_MSG);
    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage objMsg = (ObjectMessage) message;
        try {
            Request request = (Request) objMsg.getObject();
            System.out.println("Http Request received on JMS Topic " + request.toString());

            jsonDataPublisher.sendText(request.toString());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        jsonDataPublisher.shutdown();
    }
}
