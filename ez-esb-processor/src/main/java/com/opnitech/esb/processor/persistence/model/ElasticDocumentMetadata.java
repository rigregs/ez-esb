package com.opnitech.esb.processor.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticDocumentMetadata extends ElasticDocument {

    private String elasticDocumentId;
    private String documentId;
    private String documentCheckSum;

    public ElasticDocumentMetadata() {
        // Default constructor
    }

    public String getDocumentId() {

        return this.documentId;
    }

    public void setDocumentId(String documentId) {

        this.documentId = documentId;
    }

    public String getElasticDocumentId() {

        return this.elasticDocumentId;
    }

    public void setElasticDocumentId(String elasticDocumentId) {

        this.elasticDocumentId = elasticDocumentId;
    }

    public String getDocumentCheckSum() {

        return this.documentCheckSum;
    }

    public void setDocumentCheckSum(String documentCheckSum) {

        this.documentCheckSum = documentCheckSum;
    }
}
