package com.opnitech.esb.processor.persistence.rabbit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.opnitech.esb.processor.persistence.elastic.model.document.DocumentMetadata;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentOutboundCommand extends Command {

    private static final long serialVersionUID = 416704714172458307L;

    private String documentType;
    private String version;

    private DocumentMetadata documentMetadata;

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

    public DocumentMetadata getDocumentMetadata() {

        return this.documentMetadata;
    }

    public void setDocumentMetadata(DocumentMetadata documentMetadata) {

        this.documentMetadata = documentMetadata;
    }
}
