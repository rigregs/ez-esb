package com.opnitech.esb.factories.services;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.persistence.elastic.repository.document.PercolatorRepository;
import com.opnitech.esb.persistence.jpa.repository.subscriber.ConsumerRepository;
import com.opnitech.esb.services.ConsumerService;
import com.opnitech.esb.services.cache.IndexMetadataCache;
import com.opnitech.esb.services.impl.ConsumerServiceImpl;

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
