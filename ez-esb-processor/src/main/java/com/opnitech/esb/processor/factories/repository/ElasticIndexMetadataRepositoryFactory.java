package com.opnitech.esb.processor.factories.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.processor.persistence.repository.document.ElasticIndexMetadataRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class ElasticIndexMetadataRepositoryFactory {

    public ElasticIndexMetadataRepositoryFactory() {
        // Default constructor
    }

    @Bean
    public ElasticIndexMetadataRepository getElasticIndexMetadataRepository(ElasticsearchTemplate elasticsearchTemplate) {

        ElasticIndexMetadataRepository elasticIndexMetadataRepository = new ElasticIndexMetadataRepository(elasticsearchTemplate);

        return elasticIndexMetadataRepository;
    }
}
