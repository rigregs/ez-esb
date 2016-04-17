package com.opnitech.esb.persistence.elastic.repository.document;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.common.data.ElasticIndexMetadata;
import com.opnitech.esb.persistence.elastic.model.shared.ElasticSourceDocument;
import com.opnitech.esb.persistence.elastic.repository.shared.ElasticRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentRepository extends ElasticRepository {

    public DocumentRepository(ElasticsearchTemplate elasticsearchTemplate) {
        super(elasticsearchTemplate);
    }

    public ElasticSourceDocument retrieveDocument(ElasticIndexMetadata elasticIndexMetadata, String id) {

        ElasticSourceDocument elasticSourceDocument = executeGetById(elasticIndexMetadata.getIndexName(),
                elasticIndexMetadata.getDocumentTypeName(), id);

        return elasticSourceDocument;
    }
}
