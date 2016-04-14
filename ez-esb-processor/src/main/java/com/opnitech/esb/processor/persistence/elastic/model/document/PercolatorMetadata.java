package com.opnitech.esb.processor.persistence.elastic.model.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PercolatorMetadata extends ElasticDocument {

    private long consumerId;

    private long percolatorId;

    private String queryAsJSON;

    public PercolatorMetadata() {
        // Default constructor
    }

    public long getConsumerId() {

        return this.consumerId;
    }

    public void setConsumerId(long consumerId) {

        this.consumerId = consumerId;
    }

    public long getPercolatorId() {

        return this.percolatorId;
    }

    public void setPercolatorId(long percolatorId) {

        this.percolatorId = percolatorId;
    }

    public String getQueryAsJSON() {

        return this.queryAsJSON;
    }

    public void setQueryAsJSON(String queryAsJSON) {

        this.queryAsJSON = queryAsJSON;
    }
}
