package com.opnitech.esb.processor.services.impl.routes;

import java.util.HashMap;
import java.util.Map;

import com.opnitech.esb.processor.configuration.route.RabbitRouteConfiguration;
import com.opnitech.esb.processor.configuration.route.RouteConfiguration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RabbitRouteExecuter extends RouteExecuter<RabbitRouteConfiguration> {

    private Map<RabbitRouteConfiguration, CamelRabbitRouteContainer> routes = new HashMap<>();

    public RabbitRouteExecuter() {
        // Default constructor
    }

    @Override
    public boolean acceptRouteConfiguration(RouteConfiguration routeConfiguration) {

        boolean accept = routeConfiguration instanceof RabbitRouteConfiguration;

        return accept;
    }

    @Override
    protected void executeRoute(RabbitRouteConfiguration routeConfiguration, String payloadAsJSON) {

    }

    private CamelRabbitRouteContainer resolveCamelRabbitRouteContainer(RabbitRouteConfiguration routeConfiguration) {

        CamelRabbitRouteContainer camelRabbitRouteContainer = this.routes.get(routeConfiguration);
        if (camelRabbitRouteContainer == null) {
            camelRabbitRouteContainer = new CamelRabbitRouteContainer(routeConfiguration);
            this.routes.put(routeConfiguration, camelRabbitRouteContainer);
        }

        return camelRabbitRouteContainer;
    }

    private class CamelRabbitRouteContainer {

        private RabbitRouteConfiguration routeConfiguration;

        public CamelRabbitRouteContainer(RabbitRouteConfiguration routeConfiguration) {
            this.routeConfiguration = routeConfiguration;
        }
    }
}
