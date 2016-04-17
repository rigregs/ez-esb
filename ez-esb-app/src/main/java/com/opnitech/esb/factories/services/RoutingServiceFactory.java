package com.opnitech.esb.factories.services;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.persistence.elastic.repository.document.DocumentRepository;
import com.opnitech.esb.persistence.jpa.repository.subscriber.SubscriptionRepository;
import com.opnitech.esb.services.RoutingService;
import com.opnitech.esb.services.impl.RoutingServiceImpl;
import com.opnitech.esb.services.impl.routes.connection.RouteConnectionContainer;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class RoutingServiceFactory {

    public RoutingServiceFactory() {
        // Default constructor
    }

    @Singleton
    @Bean
    public RoutingService getRoutingService(SubscriptionRepository subscriptionRepository, DocumentRepository documentRepository,
            RouteConnectionContainer routeConnectionContainer) {

        RoutingService routingService = new RoutingServiceImpl(subscriptionRepository, documentRepository,
                routeConnectionContainer);

        return routingService;
    }
}
