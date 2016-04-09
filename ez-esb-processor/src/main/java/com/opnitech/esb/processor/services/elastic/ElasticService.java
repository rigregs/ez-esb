package com.opnitech.esb.processor.services.elastic;

import com.opnitech.esb.processor.model.ElasticDocumentMetadata;
import com.opnitech.esb.processor.model.ElasticIndexMetadata;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface ElasticService {

    void createDocument(ElasticIndexMetadata elasticIndexMetadata);

    ElasticDocumentMetadata retrieveElasticDocumentMetadata(ElasticIndexMetadata elasticIndexMetadata, String id);

    String insertDocument(ElasticIndexMetadata elasticIndexMetadata, String type, String objectAsJSON);

    void updateDocument(ElasticIndexMetadata elasticIndexMetadata, String type, String id, String objectAsJSON);
}
