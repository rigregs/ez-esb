package com.opnitech.esb.processor.factories.services;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.configuration.route.RouteConfiguration;
import com.opnitech.esb.processor.persistence.jpa.repository.subscriber.SubscriptionRepository;
import com.opnitech.esb.processor.services.RoutingService;
import com.opnitech.esb.processor.services.impl.RoutingServiceImpl;
import com.opnitech.esb.processor.services.impl.routes.RouteExecuter;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class RoutingServiceFactory {

    public RoutingServiceFactory() {
        // Default constructor
    }

    @Bean
    public RoutingService getRoutingService(List<RouteExecuter<? extends RouteConfiguration>> routeExecuters,
            SubscriptionRepository subscriptionRepository) {

        RoutingService routingService = new RoutingServiceImpl(routeExecuters, subscriptionRepository);

        return routingService;
    }
}
