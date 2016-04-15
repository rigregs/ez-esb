package com.opnitech.esb.processor.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import com.opnitech.esb.processor.configuration.route.RouteConfiguration;
import com.opnitech.esb.processor.routes.executer.RouteExecuter;
import com.opnitech.esb.processor.services.RoutingService;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RoutingServiceImpl implements RoutingService {

    private List<RouteExecuter<? extends RouteConfiguration>> routeExecuters;

    public RoutingServiceImpl(List<RouteExecuter<? extends RouteConfiguration>> routeExecuters) {

        Validate.isTrue(CollectionUtils.isNotEmpty(routeExecuters));

        this.routeExecuters = routeExecuters;
    }

    public void route(RouteConfiguration routeConfiguration, Object payload) {

        Validate.notNull(payload);

        String payloadAsJSON = payload instanceof String
                ? payload.toString()
                : JSONUtil.marshall(payload);

        RouteExecuter<? extends RouteConfiguration> routeExecuter = findRouteExecuter(routeConfiguration);
        Validate.notNull(routeExecuter);

        routeExecuter.doExecuteRoute(routeConfiguration, payloadAsJSON);
    }

    private RouteExecuter<? extends RouteConfiguration> findRouteExecuter(RouteConfiguration routeConfiguration) {

        for (RouteExecuter<? extends RouteConfiguration> routeExecuter : this.routeExecuters) {
            if (routeExecuter.acceptRouteConfiguration(routeConfiguration)) {
                return routeExecuter;
            }
        }

        return null;
    }
}
