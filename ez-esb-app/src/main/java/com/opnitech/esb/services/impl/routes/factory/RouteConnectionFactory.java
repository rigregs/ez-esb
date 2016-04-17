package com.opnitech.esb.services.impl.routes.factory;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.client.util.JSONUtil;
import com.opnitech.esb.configuration.route.RabbitRouteConfiguration;
import com.opnitech.esb.configuration.route.RouteConfiguration;
import com.opnitech.esb.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.services.impl.routes.connection.RabbitRouteConnection;
import com.opnitech.esb.services.impl.routes.connection.RouteConnection;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RouteConnectionFactory {

    private static Map<Class<?>, RouteConnectionBuilder<?, ?>> BUILDERS = new HashMap<>();

    static {
        BUILDERS.put(RabbitRouteConfiguration.class, new RabbitRouteConnectionBuilder());
    }

    public RouteConnectionFactory() {
        // Default constructor
    }

    public RouteConnection<?> build(String fromURI, Subscription subscription) throws ServiceException {

        String subscriptionEnpointConfig = subscription.getSubscriptionEnpointConfig();

        RouteConfiguration routeConfiguration = JSONUtil.unmarshall(RouteConfiguration.class, subscriptionEnpointConfig);

        RouteConnectionBuilder<?, ?> routeConnectionBuilder = BUILDERS.get(routeConfiguration.getClass());
        Validate.notNull(routeConnectionBuilder);

        return routeConnectionBuilder.build(fromURI, routeConfiguration);
    }

    private static abstract class RouteConnectionBuilder<R extends RouteConfiguration, C extends RouteConnection<R>> {

        public RouteConnectionBuilder() {
            // Default constructor
        }

        protected abstract C doBuild(String fromURI, R routeConfiguration) throws ServiceException;

        public RouteConnection<?> build(String fromURI, RouteConfiguration routeConfiguration) throws ServiceException {

            @SuppressWarnings("unchecked")
            R r = (R) routeConfiguration;

            return doBuild(fromURI, r);
        }
    }

    private static class RabbitRouteConnectionBuilder
            extends RouteConnectionBuilder<RabbitRouteConfiguration, RabbitRouteConnection> {

        public RabbitRouteConnectionBuilder() {
            // Default constructor
        }

        @Override
        protected RabbitRouteConnection doBuild(String fromURI, RabbitRouteConfiguration routeConfiguration)
                throws ServiceException {

            RabbitRouteConnection rabbitRouteConnection = new RabbitRouteConnection(fromURI, routeConfiguration);

            return rabbitRouteConnection;
        }
    }
}
