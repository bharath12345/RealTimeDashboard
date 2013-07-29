package com.bharath;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.invoke.MethodHandles;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: bharadwaj
 * Date: 26/07/13
 * Time: 4:23 PM
 */

@Startup
@Singleton
public class AsyncServerServiceImpl implements AsyncServerService {

    private CamelContext camelContext;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String NAME = "com.bharath.http.server:service=AsyncServerServiceImpl";
    private MBeanServer server;
    private ObjectName objectName;

    private int port = 8123;
    private String url = "realtime";

    //private ConcurrentLinkedQueue queue = Queue.getQueue();

    private ActiveMQConnectionFactory connectionFactory;
    private ProducerTemplate producerTemplate;

    private static final String JMS_OBJECT_TOPIC = JmsConstants.TOPIC_HTTP_OBJECT_MSG + ":topic:" + JmsConstants.TOPIC_HTTP_OBJECT_MSG;
    private static final String JMS_JSON_TOPIC = JmsConstants.TOPIC_HTTP_JSON_MSG + ":topic:" + JmsConstants.TOPIC_HTTP_JSON_MSG;

    @PostConstruct
    public void start() {
        try {
            objectName = new ObjectName(NAME);
            server = ManagementFactory.getPlatformMBeanServer();
            server.registerMBean(this, objectName);
            System.out.println("Registered " + NAME + " Mbean");
        } catch (Exception e) {
            e.printStackTrace();
        }

        camelContext = new DefaultCamelContext();
        camelContext.setTracing(false);

        //add the routes
        HttpAsyncWebRouteBuilder httpAsyncWebRouteBuilder = new HttpAsyncWebRouteBuilder(camelContext, port, url);
        HttpJmsObjectToJsonRouteBuilder httpJmsObjectToJsonRouteBuilder = new HttpJmsObjectToJsonRouteBuilder(camelContext);

        try {
            camelContext.addRoutes(httpAsyncWebRouteBuilder);
            camelContext.addRoutes(httpJmsObjectToJsonRouteBuilder);
            camelContext.addRoutes(new JsonRoute());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            camelContext.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        connectionFactory = new ActiveMQConnectionFactory(JmsConstants.BROKER_URL);

        camelContext.addComponent(JmsConstants.TOPIC_HTTP_OBJECT_MSG, JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        camelContext.addComponent(JmsConstants.TOPIC_HTTP_JSON_MSG, JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        producerTemplate = camelContext.createProducerTemplate();

    }

    @PreDestroy
    public void stop() {
        try {
            if (server != null && objectName != null) {
                server.unregisterMBean(objectName);
            }

            try {
                camelContext.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class HttpAsyncWebRouteBuilder extends RouteBuilder {
        private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
        private final int port;
        private final String serverName;
        private static final String JETTY_HTTP_COMPONENT = "jetty:http://0.0.0.0:";

        public HttpAsyncWebRouteBuilder(CamelContext context, int port, String serverName) {
            super(context);
            this.port = port;
            this.serverName = serverName;
        }

        @Override
        public void configure() throws Exception {
            final String httpEndpoint = JETTY_HTTP_COMPONENT + port + "/" + serverName;
            from(httpEndpoint).process(new HttpHeaderProcessor());
            logger.info("Added HTTP Route " + httpEndpoint);
        }
    }

    class HttpHeaderProcessor implements AsyncProcessor {

        @Override
        public boolean process(Exchange httpExchange, AsyncCallback callback) {
            //System.out.println("process in callback handler");

            Map<String, Object> headers = httpExchange.getIn().getHeaders();
            process(headers);

            callback.done(true);
            return true;
        }

        @Override
        public void process(Exchange httpExchange) throws Exception {
            System.out.println("process in NON callback handler");
            Map<String, Object> headers = httpExchange.getIn().getHeaders();
            process(headers);
        }

        private void process(Map<String, Object> headers) {
            Request request = new Request();
            String httpUri = (String) headers.get(Exchange.HTTP_URI);
            request.setHttpUri(httpUri);

            String httpMethod = (String) headers.get(Exchange.HTTP_METHOD);
            request.setHttpMethod(httpMethod);

            String httpPath = (String) headers.get(Exchange.HTTP_PATH);
            request.setHttpPath(httpPath);

            String httpQuery = (String) headers.get(Exchange.HTTP_QUERY);
            request.setHttpQuery(httpQuery);

            //int responseCode = (Integer)headers.get(Exchange.HTTP_RESPONSE_CODE);
            //request.setResponseCode(responseCode);

            String httpCharacterEncoding = (String) headers.get(Exchange.HTTP_CHARACTER_ENCODING);
            request.setHttpCharacterEncoding(httpCharacterEncoding);

            String httpContentType = (String) headers.get(Exchange.CONTENT_TYPE);
            request.setHttpContentType(httpContentType);

            String httpContentEncoding = (String) headers.get(Exchange.CONTENT_ENCODING);
            request.setHttpContentEncoding(httpContentEncoding);

            String httpProtocolVersion = (String) headers.get(Exchange.HTTP_PROTOCOL_VERSION);
            request.setHttpProtocolVersion(httpProtocolVersion);

            System.out.println("Request = " + request.toString());
            //queue.add(request);
            producerTemplate.sendBody(JMS_OBJECT_TOPIC, request);

        }
    }

    class HttpJmsObjectToJsonRouteBuilder extends RouteBuilder {
        private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

        HttpJmsObjectToJsonRouteBuilder(CamelContext context) {
            super(context);
        }

        @Override
        public void configure() throws Exception {
            from(JMS_OBJECT_TOPIC).process(new JmsObjectToJsonProcessor());
            logger.info("Added JMS TO JMS Route " + JMS_OBJECT_TOPIC);
        }

    }

    class JmsObjectToJsonProcessor implements Processor {

        @Override
        public void process(Exchange httpExchange) throws Exception {
            System.out.println("process in NON callback handler");
            Request request = (Request) httpExchange.getIn().getBody();
            String textMsg = request.toString();
            producerTemplate.sendBody(JMS_JSON_TOPIC, textMsg);
        }
    }

    class JsonRoute extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            from(JMS_JSON_TOPIC).routeId("fromJMStoWebSocketQuotes")
                    .log(LoggingLevel.DEBUG, ">> Http header received : ${body}")
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            System.out.println("We just downloaded: " + exchange.getIn().getHeader("CamelFileName"));
                        }
                    })
                    .to("websocket://0.0.0.0:9090/httpJsonHeaderTopic?sendToAll=true")
                    .to("file:/tmp/httpHeaders.json");
        }
    }

}
