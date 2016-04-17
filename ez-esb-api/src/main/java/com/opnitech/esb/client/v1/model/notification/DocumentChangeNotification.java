package com.opnitech.esb.client.v1.model.notification;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.opnitech.esb.client.v1.model.shared.ActionEnum;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentChangeNotification implements Serializable {

    private static final long serialVersionUID = 6761241065475982761L;

    private ActionEnum action;

    private String documentType;
    private String documentId;
    private String documentVersion;
    private String documentInternalId;

    private String documentAsJSON;
    private Long version;

    public DocumentChangeNotification() {
        // Default constructor
    }

    public DocumentChangeNotification(ActionEnum action, String documentType, String documentId, String documentVersion,
            String documentInternalId, String documentAsJSON, Long version) {

        this.action = action;
        this.setDocumentType(documentType);
        this.documentId = documentId;
        this.documentVersion = documentVersion;
        this.documentInternalId = documentInternalId;
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

    public ActionEnum getAction() {

        return this.action;
    }

    public void setAction(ActionEnum action) {

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

    public String getDocumentType() {

        return this.documentType;
    }

    public void setDocumentType(String documentType) {

        this.documentType = documentType;
    }
}
