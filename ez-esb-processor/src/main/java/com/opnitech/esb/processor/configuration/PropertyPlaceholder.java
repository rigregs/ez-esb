package com.opnitech.esb.processor.configuration;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class PropertyPlaceholder {

    private RabbitConfiguration inboundRabbitConfiguration;
    private RabbitConfiguration outboundRabbitConfiguration;

    public PropertyPlaceholder() {
        // Default constructor
    }

    public RabbitConfiguration getInboundRabbitConfiguration() {

        return this.inboundRabbitConfiguration;
    }

    public void setInboundRabbitConfiguration(RabbitConfiguration inboundRabbitConfiguration) {

        this.inboundRabbitConfiguration = inboundRabbitConfiguration;
    }

    public RabbitConfiguration getOutboundRabbitConfiguration() {

        return this.outboundRabbitConfiguration;
    }

    public void setOutboundRabbitConfiguration(RabbitConfiguration outboundRabbitConfiguration) {

        this.outboundRabbitConfiguration = outboundRabbitConfiguration;
    }
}
