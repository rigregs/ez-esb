package com.opnitech.esb.processor.factories.routes;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.services.impl.routes.connection.RouteConnectionContainer;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
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
