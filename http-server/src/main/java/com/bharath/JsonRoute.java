package com.bharath;

/**
 * User: bharadwaj
 * Date: 29/07/13
 * Time: 3:07 PM
 */

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class JsonRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("activemq:topic:"+JmsConstants.TOPIC_HTTP_JSON_MSG).routeId("fromJMStoWebSocketQuotes")
                .log(LoggingLevel.DEBUG,">> Http header received : ${body}")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("We just downloaded: " + exchange.getIn().getHeader("CamelFileName"));
                    }
                })
                .to("websocket://0.0.0.0:9090/stockQuoteTopic?sendToAll=true")
                .to("file:/tmp/httpHeaders.json");
    }
}
