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
public class DocumentChangeNotification implements Serializable {

    private static final long serialVersionUID = 6761241065475982761L;

    private CRUDActionEnum action;

    private String documentType;
    private String documentId;
    private String documentVersion;
    private String documentInternalId;
    private String documentInternalSequence;
    private String documentCheckSum;

    private String documentAsJSON;
    private Long version;

    public DocumentChangeNotification() {
        // Default constructor
    }

    public DocumentChangeNotification(CRUDActionEnum action, String documentType, String documentId, String documentVersion,
            String documentInternalId, String documentInternalSequence, String documentCheckSum, String documentAsJSON,
            Long version) {
        this.action = action;
        this.setDocumentType(documentType);
        this.documentId = documentId;
        this.documentVersion = documentVersion;
        this.documentInternalId = documentInternalId;
        this.documentInternalSequence = documentInternalSequence;
        this.setDocumentCheckSum(documentCheckSum);
        this.documentAsJSON = documentAsJSON;
        this.version = version;
    }

    public String getDocumentAsJSON() {

        return this.documentAsJSON;
    }

    public void setDocumentAsJSON(String documentAsJSON) {

        this.documentAsJSON = documentAsJSON;
    }

    public Long getVersion() {

        return this.version;
    }

    public void setVersion(Long version) {

        this.version = version;
    }

    public CRUDActionEnum getAction() {

        return this.action;
    }

    public void setAction(CRUDActionEnum action) {

        this.action = action;
    }

    public String getDocumentId() {

        return this.documentId;
    }

    public void setDocumentId(String documentId) {

        this.documentId = documentId;
    }

    public String getDocumentVersion() {

        return this.documentVersion;
    }

    public void setDocumentVersion(String documentVersion) {

        this.documentVersion = documentVersion;
    }

    public String getDocumentInternalId() {

        return this.documentInternalId;
    }

    public void setDocumentInternalId(String documentInternalId) {

        this.documentInternalId = documentInternalId;
    }

    public String getDocumentInternalSequence() {

        return this.documentInternalSequence;
    }

    public void setDocumentInternalSequence(String documentInternalSequence) {

        this.documentInternalSequence = documentInternalSequence;
    }

    public String getDocumentCheckSum() {

        return this.documentCheckSum;
    }

    public void setDocumentCheckSum(String documentCheckSum) {

        this.documentCheckSum = documentCheckSum;
    }

    public String getDocumentType() {

        return this.documentType;
    }

    public void setDocumentType(String documentType) {

        this.documentType = documentType;
    }
}
