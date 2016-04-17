package com.opnitech.esb.factories.events;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.events.InboundMessageConsumer;
import com.opnitech.esb.services.DocumentIndexerService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class InboundMessageConsumerFactory {

    public InboundMessageConsumerFactory() {
        // Default constructor
    }

    @Singleton
    @Bean(name = "inboundMessageConsumer")
    public InboundMessageConsumer getInboundMessageConsumer(DocumentIndexerService documentIndexerService) {

        InboundMessageConsumer inboundMessageConsumer = new InboundMessageConsumer(documentIndexerService);

        return inboundMessageConsumer;
    }
}
