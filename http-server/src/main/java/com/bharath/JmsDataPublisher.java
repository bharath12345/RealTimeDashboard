package com.bharath;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.Serializable;

/**
 * User: bharadwaj
 * Date: 30/07/13
 * Time: 11:06 AM
 */

public class JmsDataPublisher {

    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private transient MessageProducer producer;
    private Destination destination;

    public JmsDataPublisher(final String brokerURL, final String topicName) {
        try {
            factory = new ActiveMQConnectionFactory(brokerURL);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic(topicName);
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Serializable object) {
        try {
            Message msg = session.createObjectMessage(object);
            producer.send(msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public void sendText(String text) {
        try {
            Message msg = session.createTextMessage(text);
            producer.send(msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public void shutdown(){
        try {
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
