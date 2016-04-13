package com.opnitech.esb.processor.persistence.elastic.model.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentOutboundCommand extends Command {

    private static final long serialVersionUID = 416704714172458307L;

    private String documentType;
    private String documentId;
    private String version;

    private long matchQueryId;

    public DocumentOutboundCommand() {
        // Default constructor
    }

    public String getDocumentType() {

        return this.documentType;
    }

    public void setDocumentType(String documentType) {

        this.documentType = documentType;
    }

    public String getDocumentId() {

        return this.documentId;
    }

    public void setDocumentId(String documentId) {

        this.documentId = documentId;
    }

    public String getVersion() {

        return this.version;
    }

    public void setVersion(String version) {

        this.version = version;
    }

    public long getMatchQueryId() {

        return this.matchQueryId;
    }

    public void setMatchQueryId(long matchQueryId) {

        this.matchQueryId = matchQueryId;
    }
}
