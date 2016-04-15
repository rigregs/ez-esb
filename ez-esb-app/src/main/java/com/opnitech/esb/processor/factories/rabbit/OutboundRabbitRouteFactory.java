package com.opnitech.esb.processor.factories.rabbit;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.configuration.PropertyPlaceholder;
import com.opnitech.esb.processor.persistence.rabbit.DocumentCRUDCommand;
import com.opnitech.esb.processor.utils.RouteBuilderUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */

@Configuration
public class OutboundRabbitRouteFactory {

    @Autowired
    CamelContext camelContext;

    public OutboundRabbitRouteFactory() {
        // Default constructor
    }

    @Bean
    @Singleton
    public RouteBuilder getOutboundSendRouteBuilder(PropertyPlaceholder propertyPlaceholder) {

        RouteBuilder routeBuilder = RouteBuilderUtil.createSendRabbitRouteBuilder("outboundSend",
                propertyPlaceholder.getOutboundRoute());

        return routeBuilder;
    }

    @Bean
    @Singleton
    public RouteBuilder getOutboundReceiveRouteBuilder(PropertyPlaceholder propertyPlaceholder) {

        RouteBuilder routeBuilder = RouteBuilderUtil.createReceiveRabbitRouteBuilder("outboundMessageConsumer",
                propertyPlaceholder.getOutboundRoute(), DocumentCRUDCommand.class);

        return routeBuilder;
    }

}
