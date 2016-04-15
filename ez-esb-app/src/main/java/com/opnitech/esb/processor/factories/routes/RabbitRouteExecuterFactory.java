package com.opnitech.esb.processor.factories.routes;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.routes.executer.RabbitRouteExecuter;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class RabbitRouteExecuterFactory {

    public RabbitRouteExecuterFactory() {
        // Default constructor
    }

    @Bean
    @Singleton
    public RabbitRouteExecuter getRabbitRouteExecuter() {

        RabbitRouteExecuter rabbitRouteExecuter = new RabbitRouteExecuter();

        return rabbitRouteExecuter;
    }
}
