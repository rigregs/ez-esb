package com.opnitech.esb.processor.persistence.index;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ElasticIndexMetadata {

    private static final String ESB_INDEX_FORMAT = "esb-{0}-v{1}-document";
    private static final String ESB_METADATA_TYPE_FORMAT = "metadata";
    private static final String ESB_DOCUMENT_TYPE_FORMAT = "document";

    private String version;
    private String document;

    private String indexName;
    private String metadataTypeName;
    private String documentTypeName;

    private String messageSequence;

    public ElasticIndexMetadata() {
        // Default constructor
    }

    public ElasticIndexMetadata(String version, String document) {

        this.version = version;
        this.document = document;

        this.indexName = StringUtils.lowerCase(MessageFormat.format(ESB_INDEX_FORMAT, this.document, this.version));
        this.metadataTypeName = ESB_METADATA_TYPE_FORMAT;
        this.documentTypeName = ESB_DOCUMENT_TYPE_FORMAT;
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(this.version).append(this.document).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof ElasticIndexMetadata)) {
            return false;
        }

        ElasticIndexMetadata other = (ElasticIndexMetadata) obj;

        return new EqualsBuilder().append(this.version, other.getVersion()).append(this.document, other.getDocument()).isEquals();
    }

    public String getVersion() {

        return this.version;
    }

    public void setVersion(String version) {

        this.version = version;
    }

    public String getDocument() {

        return this.document;
    }

    public void setDocument(String document) {

        this.document = document;
    }

    public String getIndexName() {

        return this.indexName;
    }

    public void setIndexName(String indexName) {

        this.indexName = indexName;
    }

    public String getMetadataTypeName() {

        return this.metadataTypeName;
    }

    public void setMetadataTypeName(String metadataTypeName) {

        this.metadataTypeName = metadataTypeName;
    }

    public String getDocumentTypeName() {

        return this.documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {

        this.documentTypeName = documentTypeName;
    }

    public String getMessageSequence() {

        return this.messageSequence;
    }

    public void setMessageSequence(String messageSequence) {

        this.messageSequence = messageSequence;
    }
}
