package com.opnitech.esb.services.impl.routes.connection;

import org.apache.camel.RoutesBuilder;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.configuration.route.RabbitRouteConfiguration;
import com.opnitech.esb.utils.RouteBuilderUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RabbitRouteConnection extends RouteConnection<RabbitRouteConfiguration> {

    public RabbitRouteConnection(String fromURI, RabbitRouteConfiguration rabbitRouteConfiguration, String transformationTemplate)
            throws ServiceException {

        super(fromURI, rabbitRouteConfiguration, transformationTemplate);
    }

    @Override
    protected RoutesBuilder createEndpointCamelRoute(String fromRoute) {

        return RouteBuilderUtil.createSendRabbitRouteBuilder(fromRoute, getRouteConfiguration());
    }
}
