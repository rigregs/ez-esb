package com.opnitech.esb.services.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.common.data.ElasticIndexMetadata;
import com.opnitech.esb.persistence.elastic.repository.document.DocumentRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class IndexMetadataCache {

    private final Map<Object, Object> elasticIndexMetadatas = new ConcurrentHashMap<>();

    private DocumentRepository documentRepository;

    public IndexMetadataCache(DocumentRepository documentRepository) {

        this.documentRepository = documentRepository;
    }

    public void guaranteeIndexExists(ElasticIndexMetadata elasticIndexMetadata) throws ServiceException {

        if (!this.elasticIndexMetadatas.containsKey(elasticIndexMetadata)) {
            this.documentRepository.createIndex(elasticIndexMetadata.getIndexName());
            this.elasticIndexMetadatas.put(elasticIndexMetadata, elasticIndexMetadata);
        }
    }
}
