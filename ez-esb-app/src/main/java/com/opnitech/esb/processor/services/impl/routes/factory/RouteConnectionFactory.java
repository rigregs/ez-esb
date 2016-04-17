package com.opnitech.esb.processor.services.impl.routes.factory;

import com.opnitech.esb.processor.configuration.route.RouteConfiguration;
import com.opnitech.esb.processor.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.processor.services.impl.routes.connection.RouteConnection;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RouteConnectionFactory {

    public RouteConnectionFactory() {
        // Default constructor
    }

    public RouteConnection<?> build(Subscription subscription) {

        String subscriptionEnpointConfig = subscription.getSubscriptionEnpointConfig();

        RouteConfiguration unmarshall = JSONUtil.unmarshall(RouteConfiguration.class, subscriptionEnpointConfig);

        return null;
    }
}
