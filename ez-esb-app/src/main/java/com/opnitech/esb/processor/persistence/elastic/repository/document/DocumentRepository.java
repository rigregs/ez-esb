package com.opnitech.esb.processor.persistence.elastic.repository.document;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.processor.common.data.ElasticIndexMetadata;
import com.opnitech.esb.processor.persistence.elastic.repository.shared.ElasticRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentRepository extends ElasticRepository {

    public DocumentRepository(ElasticsearchTemplate elasticsearchTemplate) {
        super(elasticsearchTemplate);
    }

    public String retrieveDocument(ElasticIndexMetadata elasticIndexMetadata, String id) {

        String documentAsJSON = executeGetById(elasticIndexMetadata.getIndexName(), elasticIndexMetadata.getDocumentTypeName(),
                id);

        return documentAsJSON;
    }
}
