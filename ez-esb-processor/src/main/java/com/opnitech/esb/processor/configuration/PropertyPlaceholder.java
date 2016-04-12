package com.opnitech.esb.processor.configuration;

import com.opnitech.esb.processor.routes.configuration.RabbitRouteConfiguration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class PropertyPlaceholder {

    private RabbitRouteConfiguration inboundRoute;
    private RabbitRouteConfiguration outboundRoute;

    public PropertyPlaceholder() {
        // Default constructor
    }

    public RabbitRouteConfiguration getInboundRoute() {

        return this.inboundRoute;
    }

    public void setInboundRoute(RabbitRouteConfiguration inboundRoute) {

        this.inboundRoute = inboundRoute;
    }

    public RabbitRouteConfiguration getOutboundRoute() {

        return this.outboundRoute;
    }

    public void setOutboundRoute(RabbitRouteConfiguration outboundRoute) {

        this.outboundRoute = outboundRoute;
    }
}
