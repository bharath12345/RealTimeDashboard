package com.bharath;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * User: bharadwaj
 * Date: 29/07/13
 * Time: 2:21 PM
 */

public class HttpDataRcvr extends DataMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(HttpDataRcvr.class.getName());

    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private transient MessageProducer producer;
    private Destination destination;

    public void init(){
        init(JmsConstants.BROKER_URL, JmsConstants.TOPIC_HTTP_OBJECT_MSG, new HttpRcvr());

        try {
            factory = new ActiveMQConnectionFactory(JmsConstants.BROKER_URL);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination  = session.createTopic(JmsConstants.TOPIC_HTTP_JSON_MSG);
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class HttpRcvr implements MessageListener {
        @Override
        public void onMessage(Message message) {

            ObjectMessage objMsg = (ObjectMessage) message;
            try {
                Request request = (Request) objMsg.getObject();
                logger.info("Http Request received on JMS Topic " + request.toString());

                Message msg = session.createTextMessage(request.toString());
                producer.send(msg);

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
