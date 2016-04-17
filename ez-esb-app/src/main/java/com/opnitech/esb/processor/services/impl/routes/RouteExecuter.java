package com.opnitech.esb.processor.services.impl.routes;

import com.opnitech.esb.processor.configuration.route.RouteConfiguration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public abstract class RouteExecuter<R extends RouteConfiguration> {

    public RouteExecuter() {
        // Default constructor
    }

    public abstract boolean acceptRouteConfiguration(RouteConfiguration routeConfiguration);

    protected abstract void executeRoute(R routeConfiguration, String payloadAsJSON);

    public void doExecuteRoute(RouteConfiguration routeConfiguration, String payloadAsJSON) {

        @SuppressWarnings("unchecked")
        R r = (R) routeConfiguration;

        executeRoute(r, payloadAsJSON);
    }
}