package com.opnitech.esb.persistence.elastic.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.opnitech.esb.persistence.elastic.model.shared.ElasticDocument;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentMetadata extends ElasticDocument {

    public static final String DOCUMENT_ID_PROPERTY = "documentId";

    private String elasticDocumentId;
    private String documentId;
    private String documentCheckSum;

    private String sequence;

    public DocumentMetadata() {
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

    public String getSequence() {

        return this.sequence;
    }

    public void setSequence(String sequence) {

        this.sequence = sequence;
    }
}
