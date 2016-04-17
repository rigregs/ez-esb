package com.opnitech.esb.processor.services.impl.routes.connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Objects;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RouteConnectionContainer {

    private Map<Long, RouteConnection<?>> routeConnections = new ConcurrentHashMap<>();

    public RouteConnectionContainer() {
        // Default constructor
    }

    public <R extends RouteConnection<?>> R resolveRouteConnection(Long subscriptionId) {

        @SuppressWarnings("unchecked")
        R routeConnection = (R) this.routeConnections.get(subscriptionId);

        if (routeConnection != null) {
            routeConnection.visit();
        }

        return routeConnection;
    }

    public void registerConnection(Long subscriptionId, RouteConnection<?> routeConnection) {

        RouteConnection<?> oldRouteConnection = resolveRouteConnection(subscriptionId);
        if (oldRouteConnection != null && !Objects.equal(oldRouteConnection, routeConnection)) {
            this.routeConnections.remove(subscriptionId);
            oldRouteConnection.close();
        }

        this.routeConnections.put(subscriptionId, routeConnection);
    }
}
