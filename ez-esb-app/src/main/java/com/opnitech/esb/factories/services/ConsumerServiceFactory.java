package com.opnitech.esb.factories.services;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.opnitech.esb.persistence.elastic.repository.document.PercolatorRepository;
import com.opnitech.esb.persistence.jpa.repository.consumer.ConsumerRepository;
import com.opnitech.esb.persistence.jpa.repository.consumer.SubscriptionRepository;
import com.opnitech.esb.services.ConsumerService;
import com.opnitech.esb.services.cache.IndexMetadataCache;
import com.opnitech.esb.services.impl.ConsumerServiceImpl;
import com.opnitech.esb.services.impl.routes.connection.RouteConnectionContainer;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
@EnableTransactionManagement
public class ConsumerServiceFactory {

    public ConsumerServiceFactory() {
        // Default constructor
    }

    @Singleton
    @Bean
    public ConsumerService getConsumerService(ConsumerRepository consumerRepository,
            PercolatorRepository percolatorMetadataRepository, IndexMetadataCache indexMetadataCache,
            SubscriptionRepository subscriptionRepository, RouteConnectionContainer routeConnectionContainer) {

        ConsumerService consumerService = new ConsumerServiceImpl(consumerRepository, percolatorMetadataRepository,
                indexMetadataCache, subscriptionRepository, routeConnectionContainer);

        return consumerService;
    }
}
