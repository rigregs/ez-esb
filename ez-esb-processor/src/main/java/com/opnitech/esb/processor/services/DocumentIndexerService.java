package com.opnitech.esb.processor.services;

import com.opnitech.esb.processor.persistence.index.ElasticIndexMetadata;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface DocumentIndexerService {

    void updateDocument(ElasticIndexMetadata elasticIndexMetadata, String id, String documentAsJSON);
}
