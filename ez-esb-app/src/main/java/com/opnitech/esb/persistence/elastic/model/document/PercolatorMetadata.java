package com.opnitech.esb.persistence.elastic.model.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.opnitech.esb.persistence.elastic.model.shared.ElasticDocument;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PercolatorMetadata extends ElasticDocument {

    private long consumerId;
    private long percolatorId;

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
}
