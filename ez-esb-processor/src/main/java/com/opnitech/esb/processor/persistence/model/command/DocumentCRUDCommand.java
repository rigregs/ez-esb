package com.opnitech.esb.processor.persistence.model.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentCRUDCommand {

    private String documentType;
    private String documentId;
    private String version;

    private String documentAsJSON;
    private CRUDActionEnum action;

    public DocumentCRUDCommand() {
        // Default constructor
    }

    public String getDocumentAsJSON() {

        return this.documentAsJSON;
    }

    public void setDocumentAsJSON(String documentAsJSON) {

        this.documentAsJSON = documentAsJSON;
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

    public String getDocumentType() {

        return this.documentType;
    }

    public void setDocumentType(String documentType) {

        this.documentType = documentType;
    }

    public String getVersion() {

        return this.version;
    }

    public void setVersion(String version) {

        this.version = version;
    }
}
