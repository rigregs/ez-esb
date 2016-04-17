package com.opnitech.esb.utils;

import java.text.MessageFormat;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import com.opnitech.esb.configuration.route.RabbitRouteConfiguration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public final class RouteBuilderUtil {

    private RouteBuilderUtil() {
        // Default constructor
    }

    public static RouteBuilder createReceiveRabbitRouteBuilder(final String beanName,
            RabbitRouteConfiguration rabbitRouteConfiguration, final Class<?> unmarshalClass) {

        final String fromRouteURI = buildRabbitRoute(rabbitRouteConfiguration);

        final String toRouteURI = MessageFormat.format("bean:{0}", beanName);

        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from(fromRouteURI).log("Raw message from queue:\n${body}").unmarshal().json(JsonLibrary.Jackson, unmarshalClass)
                        .to(toRouteURI);
            }
        };

        return routeBuilder;
    }

    public static RouteBuilder createSendRabbitRouteBuilder(final String fromURI,
            RabbitRouteConfiguration rabbitRouteConfiguration) {

        final String fromRouteURI = fromDirect(fromURI);

        final String toRouteURI = buildRabbitRoute(rabbitRouteConfiguration);

        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from(fromRouteURI).marshal().json(JsonLibrary.Jackson).log("CRUD:\n${body}").to(toRouteURI);
            }
        };

        return routeBuilder;
    }

    public static String fromDirect(final String directId) {

        return MessageFormat.format("direct:{0}", directId);
    }

    private static String buildRabbitRoute(RabbitRouteConfiguration rabbitRouteConfiguration) {

        final String routeURI = MessageFormat.format(
                "rabbitmq://{0}:{1}/{2}?username={3}&password={4}&threadPoolSize={5}&routingKey={6}&queue={7}&vhost={8}&autoDelete={9}",
                rabbitRouteConfiguration.getHost(), Integer.toString(rabbitRouteConfiguration.getPort()),
                rabbitRouteConfiguration.getExchangeName(), rabbitRouteConfiguration.getUsername(),
                rabbitRouteConfiguration.getPassword(), Integer.toString(rabbitRouteConfiguration.getThreadPoolSize()),
                rabbitRouteConfiguration.getRoutingKey(), rabbitRouteConfiguration.getQueue(),
                rabbitRouteConfiguration.getVhost(), rabbitRouteConfiguration.isAutoDelete());

        return routeURI;
    }
}
