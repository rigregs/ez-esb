package com.opnitech.esb.processor.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ElasticIndexMetadata {

    private static final String SEPARATOR = "-";
    private static final String ESB_PREFIX = "esb-document-";

    private final String version;
    private final String document;

    public ElasticIndexMetadata(String version, String document) {

        this.version = version;
        this.document = document;
    }

    public String compileIndexName() {

        StringBuffer buffer = new StringBuffer();

        buffer.append(ESB_PREFIX).append(this.version).append(SEPARATOR).append(this.document);

        return buffer.toString();
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

    public String getDocument() {

        return this.document;
    }
}
