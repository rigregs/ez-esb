package com.opnitech.esb.processor.factories.asynch;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.asynch.InboundMessageConsumer;
import com.opnitech.esb.processor.services.DocumentIndexerService;

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
