package com.opnitech.esb.processor.factories.asynch;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.asynch.InboundMessageConsumer;

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
    public InboundMessageConsumer getInboundMessageConsumer() {

        InboundMessageConsumer inboundMessageConsumer = new InboundMessageConsumer();

        return inboundMessageConsumer;
    }
}
