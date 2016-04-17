package com.opnitech.esb.processor.services.impl.routes.connection;

import com.opnitech.esb.processor.configuration.route.RabbitRouteConfiguration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RabbitRouteConnection extends RouteConnection<RabbitRouteConfiguration> {

    public RabbitRouteConnection(RabbitRouteConfiguration rabbitRouteConfiguration) {
        super(rabbitRouteConfiguration);
    }

    @Override
    public void close() {

        // TODO Auto-generated method stub
        
    }

    @Override
    protected void routeConsumerPayload(String objectAsJSON) {

        // TODO Auto-generated method stub
        
    }
}
