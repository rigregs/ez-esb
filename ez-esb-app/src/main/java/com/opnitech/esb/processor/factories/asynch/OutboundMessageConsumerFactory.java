package com.opnitech.esb.processor.factories.asynch;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.asynch.OutboundMessageConsumer;

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
    public OutboundMessageConsumer getOutboundMessageConsumer() {

        OutboundMessageConsumer outboundMessageConsumer = new OutboundMessageConsumer();

        return outboundMessageConsumer;
    }
}
