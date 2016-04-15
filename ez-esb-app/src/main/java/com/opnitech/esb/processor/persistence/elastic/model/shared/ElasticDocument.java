package com.opnitech.esb.processor.persistence.elastic.model.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ElasticDocument {

    private String id;

    public ElasticDocument() {
        // Default constructor
    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

        this.id = id;
    }
}
