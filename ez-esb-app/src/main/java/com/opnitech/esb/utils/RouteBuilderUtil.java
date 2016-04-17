package com.opnitech.esb.utils;

import java.text.MessageFormat;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.elasticsearch.common.lang3.StringUtils;

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

                from(fromRouteURI).log("Inbound: ${body}").unmarshal().json(JsonLibrary.Jackson, unmarshalClass).to(toRouteURI);
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

                String completeWithoutJSONMarshalRoute = fromDirect(
                        new StringBuffer().append(fromRouteURI).append("_completeWithoutJSONMarshal").toString());

                String completeWithJSONMarshalRoute = fromDirect(
                        new StringBuffer().append(fromRouteURI).append("_completeWithJSONMarshal").toString());

                String completeRoute = fromDirect(new StringBuffer().append(fromRouteURI).append("_complete").toString());

                from(fromRouteURI).choice().when(simple("${body} is 'java.lang.String'")).to(completeWithoutJSONMarshalRoute)
                        .otherwise().to(completeWithJSONMarshalRoute).end();

                from(completeWithoutJSONMarshalRoute).to(completeRoute);
                from(completeWithJSONMarshalRoute).marshal().json(JsonLibrary.Jackson).to(completeRoute);

                from(completeRoute).log("Outbound: ${body}").to(toRouteURI);
            }
        };

        return routeBuilder;
    }

    public static RouteBuilder createGroovyRouteBuilder(String fromURI, String toURI, final String transformationTemplate) {

        final String fromRouteURI = fromDirect(fromURI);
        final String toRouteURI = fromDirect(toURI);

        RouteBuilder routeBuilder = StringUtils.isBlank(transformationTemplate)
                ? createGroovyRouteBuilderWithoutTransformations(fromRouteURI, toRouteURI)
                : createGroovyRouteBuilderWithTransformations(transformationTemplate, fromRouteURI, toRouteURI);

        return routeBuilder;
    }

    private static RouteBuilder createGroovyRouteBuilderWithTransformations(final String transformationTemplate,
            final String fromRouteURI, final String toRouteURI) {

        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                String logRoute = fromDirect(new StringBuffer().append(toRouteURI).append("_log").toString());
                String notificationRoute = fromDirect(new StringBuffer().append(toRouteURI).append("_notification").toString());

                from(fromRouteURI).multicast().to(logRoute, notificationRoute);

                from(logRoute).marshal().json(JsonLibrary.Jackson).log("Notification: ${body}");
                from(notificationRoute).setBody().groovy(transformationTemplate).to(toRouteURI);
            }
        };

        return routeBuilder;
    }

    private static RouteBuilder createGroovyRouteBuilderWithoutTransformations(final String fromRouteURI,
            final String toRouteURI) {

        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                String logRoute = fromDirect(new StringBuffer().append(toRouteURI).append("_log").toString());
                String notificationRoute = fromDirect(new StringBuffer().append(toRouteURI).append("_notification").toString());

                from(fromRouteURI).multicast().to(logRoute, notificationRoute);

                from(logRoute).marshal().json(JsonLibrary.Jackson).log("Notification: ${body}");
                from(notificationRoute).to(toRouteURI);
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
