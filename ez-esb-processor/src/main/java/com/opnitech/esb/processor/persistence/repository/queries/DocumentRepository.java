package com.opnitech.esb.processor.persistence.repository.queries;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.processor.persistence.repository.ElasticRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentRepository extends ElasticRepository {

    public DocumentRepository(ElasticsearchTemplate elasticsearchTemplate) {
        super(elasticsearchTemplate);
    }
}