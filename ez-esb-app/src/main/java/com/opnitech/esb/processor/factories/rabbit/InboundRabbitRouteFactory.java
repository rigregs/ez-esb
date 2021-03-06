package com.opnitech.esb.processor.factories.rabbit;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.configuration.PropertyPlaceholder;
import com.opnitech.esb.processor.persistence.rabbit.DocumentCRUDCommand;
import com.opnitech.esb.processor.services.DocumentIndexerService;
import com.opnitech.esb.processor.utils.RouteBuilderUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */

@Configuration
public class InboundRabbitRouteFactory {

    @Autowired
    CamelContext camelContext;

    public InboundRabbitRouteFactory() {
        // Default constructor
    }

    @Bean
    @Singleton
    public RouteBuilder getInboundSendRouteBuilder(PropertyPlaceholder propertyPlaceholder) {

        RouteBuilder routeBuilder = RouteBuilderUtil.createSendRabbitRouteBuilder(DocumentIndexerService.INBOUND_SEND_CAMEL_ROUTE,
                propertyPlaceholder.getInboundRoute());

        return routeBuilder;
    }

    @Bean
    @Singleton
    public RouteBuilder getInboundReceiveRouteBuilder(PropertyPlaceholder propertyPlaceholder) {

        RouteBuilder routeBuilder = RouteBuilderUtil.createReceiveRabbitRouteBuilder("inboundMessageConsumer",
                propertyPlaceholder.getInboundRoute(), DocumentCRUDCommand.class);

        return routeBuilder;
    }

}
