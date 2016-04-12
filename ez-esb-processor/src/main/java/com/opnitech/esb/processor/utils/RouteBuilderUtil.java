package com.opnitech.esb.processor.utils;

import java.text.MessageFormat;

import org.apache.camel.builder.RouteBuilder;

import com.opnitech.esb.processor.routes.configuration.RabbitRouteConfiguration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public final class RouteBuilderUtil {

    private RouteBuilderUtil() {
        // Default constructor
    }

    public static RouteBuilder createReceiveRabbitRouteBuilder(final String beanName,
            RabbitRouteConfiguration rabbitRouteConfiguration) {

        final String fromRouteURI = MessageFormat.format(
                "rabbitmq://{0}:{1}/{2}?username={3}&password={4}&threadPoolSize={5}&routingKey={6}&queue={7}&vhost={8}&autoDelete={9}",
                rabbitRouteConfiguration.getHost(), Integer.toString(rabbitRouteConfiguration.getPort()),
                rabbitRouteConfiguration.getExchangeName(), rabbitRouteConfiguration.getUsername(),
                rabbitRouteConfiguration.getPassword(), Integer.toString(rabbitRouteConfiguration.getThreadPoolSize()),
                rabbitRouteConfiguration.getRoutingKey(), rabbitRouteConfiguration.getQueue(),
                rabbitRouteConfiguration.getVhost(), rabbitRouteConfiguration.isAutoDelete());

        final String toRouteURI = MessageFormat.format("bean:{0}", beanName);

        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from(fromRouteURI).autoStartup(true).log("Raw message from queue:\n${body}").to(toRouteURI);
            }
        };

        return routeBuilder;
    }

    public static RouteBuilder createSendRabbitRouteBuilder(final String fromURI,
            RabbitRouteConfiguration rabbitRouteConfiguration) {

        final String fromRouteURI = MessageFormat.format("direct:{0}", fromURI);

        final String toRouteURI = MessageFormat.format("rabbitmq://{0}:{1}/{2}?username={3}&password={4}&threadPoolSize={5}",
                rabbitRouteConfiguration.getHost(), Integer.toString(rabbitRouteConfiguration.getPort()),
                rabbitRouteConfiguration.getExchangeName(), rabbitRouteConfiguration.getUsername(),
                rabbitRouteConfiguration.getPassword(), Integer.toString(rabbitRouteConfiguration.getThreadPoolSize()));

        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from(fromRouteURI).to(toRouteURI);
            }
        };

        return routeBuilder;
    }
}
