package com.opnitech.esb.processor.services.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.opnitech.esb.processor.common.data.ElasticIndexMetadata;
import com.opnitech.esb.processor.persistence.elastic.repository.document.DocumentRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class IndexMetadataCache {

    private final Map<Object, Object> elasticIndexMetadatas = new ConcurrentHashMap<>();

    private DocumentRepository documentRepository;

    public IndexMetadataCache(DocumentRepository documentRepository) {

        this.documentRepository = documentRepository;
    }

    public void guaranteeIndexExists(ElasticIndexMetadata elasticIndexMetadata) {

        if (!this.elasticIndexMetadatas.containsKey(elasticIndexMetadata)) {
            this.documentRepository.createIndex(elasticIndexMetadata.getIndexName());
            this.elasticIndexMetadatas.put(elasticIndexMetadata, elasticIndexMetadata);
        }
    }
}
