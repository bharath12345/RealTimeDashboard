package com.bharath;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.management.ManagementFactory;

/**
 * User: bharadwaj
 * Date: 26/07/13
 * Time: 5:56 PM
 */
public class AsyncClientServiceImpl implements AsyncClientService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String NAME = "com.bharath.http:service=AsyncClientServiceImpl";
    private MBeanServer server;
    private ObjectName objectName;

    AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder().setKeepAlive(true).build();
    AsyncHttpClient asyncHttpClient = new AsyncHttpClient(cf);

    private int port = 8123;
    private String url = "realtime";
    final String baseUrl = "http://localhost:" + port + "/" + url;

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
    }

    @PreDestroy
    public void stop() {
        try {
            if(server != null && objectName != null) {
                server.unregisterMBean(objectName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void firePostRequests(long clientCount, long requestCounter) {
        for(int i=0;i<clientCount;i++) {
            fireParallelPostRequests(requestCounter);
        }
    }

    public void fireParallelPostRequests(long requestCounter) {
        for(int i=0;i<requestCounter;i++) {
            try {
                asyncHttpClient.preparePost(baseUrl).setBody("Hello World").execute(new ResponseHandler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ResponseHandler extends AsyncCompletionHandler<String> {

        @Override
        public String onCompleted(Response response) throws Exception {
            System.out.println("got response "+ response.getStatusCode());
            return null;
        }
    }

}
