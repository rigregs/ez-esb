package com.opnitech.esb.processor.configuration;

import com.opnitech.esb.processor.configuration.elastic.ElasticConfiguration;
import com.opnitech.esb.processor.configuration.route.RabbitRouteConfiguration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class PropertyPlaceholder {

    private ElasticConfiguration elastic;

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

    public ElasticConfiguration getElastic() {

        return this.elastic;
    }

    public void setElastic(ElasticConfiguration elastic) {

        this.elastic = elastic;
    }
}
