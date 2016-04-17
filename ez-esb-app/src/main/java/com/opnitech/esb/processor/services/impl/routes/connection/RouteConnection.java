package com.opnitech.esb.processor.services.impl.routes.connection;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.lang3.StringUtils;

import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.configuration.route.RouteConfiguration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public abstract class RouteConnection<R extends RouteConfiguration> {

    private final R routeConfiguration;
    private final String fromURI;

    private CamelContext context;
    private ProducerTemplate producerTemplate;
    private final String fromFullRoute;

    public RouteConnection(String fromURI, String fromFullRoute, R routeConfiguration) throws ServiceException {
        this.fromURI = fromURI;
        this.fromFullRoute = fromFullRoute;
        this.routeConfiguration = routeConfiguration;

        initializeCamelContext();
    }

    protected abstract RoutesBuilder createCamelRoute();

    private void initializeCamelContext() throws ServiceException {

        try {
            this.context = new DefaultCamelContext();

            RoutesBuilder createCamelRoute = createCamelRoute();

            this.context.addRoutes(createCamelRoute);

            this.producerTemplate = this.getContext().createProducerTemplate();

            this.context.start();
        }
        catch (Exception exception) {
            throw new ServiceException(exception);
        }
    }

    public void close() throws ServiceException {

        try {
            this.producerTemplate.stop();
            this.producerTemplate = null;

            this.context.stop();
            this.context = null;
        }
        catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public void send(String objectAsJSON) {

        if (StringUtils.isNotBlank(objectAsJSON)) {
            this.producerTemplate.sendBody(this.fromFullRoute, objectAsJSON);
        }
    }

    public R getRouteConfiguration() {

        return this.routeConfiguration;
    }

    public String getFromURI() {

        return this.fromURI;
    }

    public CamelContext getContext() {

        return this.context;
    }

    public void setContext(CamelContext context) {

        this.context = context;
    }

    public ProducerTemplate getProducerTemplate() {

        return this.producerTemplate;
    }

    public void setProducerTemplate(ProducerTemplate producerTemplate) {

        this.producerTemplate = producerTemplate;
    }
}
