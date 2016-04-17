package com.opnitech.esb.persistence.elastic.model.document;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PercolatorOwner implements Serializable {

    private static final long serialVersionUID = 8843881557125577789L;

    private long consumerId;
    private long subscriberId;
    private long matchQueryId;

    public PercolatorOwner() {
        // Default constructor
    }

    public PercolatorOwner(long consumerId, long subscriberId, long matchQueryId) {
        this.consumerId = consumerId;
        this.subscriberId = subscriberId;
        this.matchQueryId = matchQueryId;
    }

    public long getConsumerId() {

        return this.consumerId;
    }

    public void setConsumerId(long consumerId) {

        this.consumerId = consumerId;
    }

    public long getSubscriberId() {

        return this.subscriberId;
    }

    public void setSubscriberId(long subscriberId) {

        this.subscriberId = subscriberId;
    }

    public long getMatchQueryId() {

        return this.matchQueryId;
    }

    public void setMatchQueryId(long matchQueryId) {

        this.matchQueryId = matchQueryId;
    }
}
