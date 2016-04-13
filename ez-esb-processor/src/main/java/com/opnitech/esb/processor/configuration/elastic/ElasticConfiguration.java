package com.opnitech.esb.processor.configuration.elastic;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ElasticConfiguration {

    private boolean sniff;
    private String hosts;

    public ElasticConfiguration() {
        // Default constructor
    }

    public String getHosts() {

        return this.hosts;
    }

    public void setHosts(String hosts) {

        this.hosts = hosts;
    }

    public boolean isSniff() {

        return this.sniff;
    }

    public void setSniff(boolean sniff) {

        this.sniff = sniff;
    }
}
