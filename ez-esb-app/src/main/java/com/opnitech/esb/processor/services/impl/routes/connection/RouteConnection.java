package com.opnitech.esb.processor.services.impl.routes.connection;

import org.apache.commons.lang3.StringUtils;

import com.opnitech.esb.processor.configuration.route.RouteConfiguration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public abstract class RouteConnection<R extends RouteConfiguration> {

    private final R routeConfiguration;

    private long lastVisit;

    public RouteConnection(R routeConfiguration) {
        this.routeConfiguration = routeConfiguration;
        visit();
    }

    protected abstract void routeConsumerPayload(String objectAsJSON);

    public abstract void close();

    public void send(String objectAsJSON) {

        if (StringUtils.isNotBlank(objectAsJSON)) {
            routeConsumerPayload(objectAsJSON);
        }
    }

    public void visit() {

        this.lastVisit = System.currentTimeMillis();
    }

    public R getRouteConfiguration() {

        return this.routeConfiguration;
    }

    public long getLastVisit() {

        return this.lastVisit;
    }

    public void setLastVisit(long lastVisit) {

        this.lastVisit = lastVisit;
    }
}
