package com.opnitech.esb.processor.persistence.elastic.model.client;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriberPayload implements Serializable {

    private static final long serialVersionUID = 6761241065475982761L;

    private String documentId;
    private String documentType;
    private String documentVersion;

    private String documentAsJSON;
    private String sequence;
    private String version;

    public SubscriberPayload() {
        // Default constructor
    }
}
