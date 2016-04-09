package com.opnitech.esb.processor.services.document;

import com.opnitech.esb.processor.model.ElasticIndexMetadata;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface DocumentIndexerService {

    void updateDocument(ElasticIndexMetadata elasticIndexMetadata, String id, String documentAsJSON);
}
