package com.opnitech.esb.factories.routes;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.opnitech.esb.services.impl.routes.connection.RouteConnectionContainer;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
@EnableTransactionManagement
public class RouteConnectionContainerFactory {

    public RouteConnectionContainerFactory() {
        // Default constructor
    }

    @Singleton
    @Bean
    public RouteConnectionContainer getRouteConnectionContainer() {

        RouteConnectionContainer routeConnectionContainer = new RouteConnectionContainer();

        return routeConnectionContainer;
    }
}
