package com.opnitech.esb.processor.persistence.elastic.model.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticSourceDocument extends ElasticDocument {

    private Long version;
    private String objectAsJSON;

    public ElasticSourceDocument() {
        // Default constructor
    }

    public ElasticSourceDocument(String id, String objectAsJSON, Long version) {

        setId(id);

        this.objectAsJSON = objectAsJSON;
        this.version = version;
    }

    public Long getVersion() {

        return this.version;
    }

    public void setVersion(Long version) {

        this.version = version;
    }

    public String getObjectAsJSON() {

        return this.objectAsJSON;
    }

    public void setObjectAsJSON(String objectAsJSON) {

        this.objectAsJSON = objectAsJSON;
    }
}
