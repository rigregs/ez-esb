package com.opnitech.esb.services.impl.routes.connection;

import java.text.MessageFormat;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.lang3.StringUtils;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.configuration.route.RouteConfiguration;
import com.opnitech.esb.utils.RouteBuilderUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public abstract class RouteConnection<R extends RouteConfiguration> {

    private final R routeConfiguration;
    private final String fromURI;

    private CamelContext context;
    private ProducerTemplate endpointProducerTemplate;
    private String transformationTemplate;

    public RouteConnection(String fromURI, R routeConfiguration, String transformationTemplate) throws ServiceException {

        this.fromURI = fromURI;
        this.routeConfiguration = routeConfiguration;
        this.transformationTemplate = transformationTemplate;

        initializeCamelContext();
    }

    protected abstract RoutesBuilder createEndpointCamelRoute(String fromRoute);

    private void initializeCamelContext() throws ServiceException {

        try {
            this.context = new DefaultCamelContext();

            registerTransformationRoute();
            registerEndpointCamelRoute();

            this.endpointProducerTemplate = this.context.createProducerTemplate();

            this.context.start();
        }
        catch (Exception exception) {
            throw new ServiceException(exception);
        }
    }

    private void registerTransformationRoute() throws Exception {

        RoutesBuilder groovyTransformCamelRoute = RouteBuilderUtil.createGroovyRouteBuilder(createTrasnformationFromRoute(),
                createEndpointFromRoute(), this.transformationTemplate);

        this.context.addRoutes(groovyTransformCamelRoute);
    }

    private void registerEndpointCamelRoute() throws Exception {

        RoutesBuilder endpointCamelRoute = createEndpointCamelRoute(createEndpointFromRoute());

        this.context.addRoutes(endpointCamelRoute);
    }

    private String createEndpointFromRoute() {

        String endpointFromRoute = MessageFormat.format("{0}_endpoint", getFromURI());

        return endpointFromRoute;
    }

    private String createTrasnformationFromRoute() {

        String endpointFromRoute = MessageFormat.format("{0}_transform", getFromURI());

        return endpointFromRoute;
    }

    public void close() throws ServiceException {

        try {
            this.endpointProducerTemplate.stop();
            this.endpointProducerTemplate = null;

            this.context.stop();
            this.context = null;
        }
        catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public void send(String objectAsJSON) {

        if (StringUtils.isNotBlank(objectAsJSON)) {
            this.endpointProducerTemplate.sendBody(RouteBuilderUtil.fromDirect(createEndpointFromRoute()), objectAsJSON);
        }
    }

    public R getRouteConfiguration() {

        return this.routeConfiguration;
    }

    public String getFromURI() {

        return this.fromURI;
    }
}
