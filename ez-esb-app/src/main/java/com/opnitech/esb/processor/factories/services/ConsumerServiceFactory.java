package com.opnitech.esb.processor.factories.services;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.persistence.elastic.repository.document.PercolatorRepository;
import com.opnitech.esb.processor.persistence.jpa.repository.subscriber.ConsumerRepository;
import com.opnitech.esb.processor.services.ConsumerService;
import com.opnitech.esb.processor.services.cache.IndexMetadataCache;
import com.opnitech.esb.processor.services.impl.ConsumerServiceImpl;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class ConsumerServiceFactory {

    public ConsumerServiceFactory() {
        // Default constructor
    }

    @Singleton
    @Bean
    public ConsumerService getConsumerService(ConsumerRepository consumerRepository,
            PercolatorRepository percolatorMetadataRepository, IndexMetadataCache indexMetadataCache) {

        ConsumerService consumerService = new ConsumerServiceImpl(consumerRepository, percolatorMetadataRepository,
                indexMetadataCache);

        return consumerService;
    }
}
