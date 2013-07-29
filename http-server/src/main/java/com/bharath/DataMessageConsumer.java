package com.bharath;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Abstract class to act as template for all JMS Consumers
 * User: sumanthn
 * Date: 8/4/13
 * one might wonder why not use Spring JMS, Spring JMS message has many hidden anti-patterns so not using it anywhere
 */

public abstract class DataMessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DataMessageConsumer.class.getName());

    protected static String brokerURL = null;// MessageBroker.getInstance().brokerURL;
    protected static transient ConnectionFactory factory;
    protected transient Connection connection;
    protected transient Session session;
    protected Destination destination;

    protected final void init(final String brokerURL, final String topicName, final MessageListener messageHandler){
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
