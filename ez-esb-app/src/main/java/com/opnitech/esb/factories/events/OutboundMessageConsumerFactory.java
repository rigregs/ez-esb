package com.opnitech.esb.factories.events;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.events.OutboundMessageConsumer;
import com.opnitech.esb.services.RoutingService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class OutboundMessageConsumerFactory {

    public OutboundMessageConsumerFactory() {
        // Default constructor
    }

    @Singleton
    @Bean(name = "outboundMessageConsumer")
    public OutboundMessageConsumer getOutboundMessageConsumer(RoutingService routingService) {

        OutboundMessageConsumer outboundMessageConsumer = new OutboundMessageConsumer(routingService);

        return outboundMessageConsumer;
    }
}
