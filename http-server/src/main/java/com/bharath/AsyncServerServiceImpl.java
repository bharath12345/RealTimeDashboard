package com.bharath;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.management.ManagementFactory;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.stream.InputStreamCache;
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

    private CamelContext camelContext ;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String NAME = "com.bharath.http:service=AsyncServerServiceImpl";
    private MBeanServer server;
    private ObjectName objectName;

    private int port = 8123;
    private String url = "realtime";

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
        HttpRouteBuilder httpRouteBuilder =  new HttpRouteBuilder(camelContext,port,url);
        try {
            camelContext.addRoutes(httpRouteBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            camelContext.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stop() {
        try {
            if(server != null && objectName != null) {
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

    class HttpRouteBuilder extends RouteBuilder {
        private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
        private final int port;
        private final String serverName;
        private static final String JETTY_HTTP_COMPONENT="jetty:http://0.0.0.0:";

        public HttpRouteBuilder(CamelContext context, int port, String serverName) {
            super(context);
            this.port = port;
            this.serverName = serverName;
        }

        @Override
        public void configure() throws Exception {
            final String httpEndpoint = JETTY_HTTP_COMPONENT +port + "/"+serverName;
            from(httpEndpoint).process(new TxnMessageProcessor());
            logger.info("Added HTTP Route " +  httpEndpoint);
        }
    }

    class TxnMessageProcessor implements AsyncProcessor {

        @Override
        public boolean process(Exchange httpExchange, AsyncCallback callback){
            System.out.println("process in callback handler");
            httpExchange.getIn().getHeaders();
            callback.done(true);
            return true;
        }

        @Override
        public void process(Exchange httpExchange) throws Exception {
            System.out.println("process in NON callback handler");
            httpExchange.getIn().getHeaders();
        }
    }

}
