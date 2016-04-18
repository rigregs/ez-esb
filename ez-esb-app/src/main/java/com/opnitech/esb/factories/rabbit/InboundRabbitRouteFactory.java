package com.opnitech.esb.factories.rabbit;

import org.apache.camel.builder.RouteBuilder;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.client.v1.model.inbound.DocumentCRUDCommand;
import com.opnitech.esb.configuration.PropertyPlaceholder;
import com.opnitech.esb.services.DocumentIndexerService;
import com.opnitech.esb.utils.RouteBuilderUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */

@Configuration
public class InboundRabbitRouteFactory {

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
