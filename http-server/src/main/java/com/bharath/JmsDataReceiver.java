package com.bharath;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class JmsDataReceiver {

    private static final Logger logger = LoggerFactory.getLogger(JmsDataReceiver.class.getName());

    private static String brokerURL = null;// MessageBroker.getInstance().brokerURL;
    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private Destination destination;

    JmsDataReceiver(final String brokerURL, final String topicName, final MessageListener messageHandler){
        try{
            this.brokerURL = brokerURL;
            factory = new ActiveMQConnectionFactory(brokerURL);
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = session.createTopic(topicName);
            MessageConsumer messageConsumer  = session.createConsumer(destination);

            messageConsumer.setMessageListener(messageHandler);

        } catch (Exception e){
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