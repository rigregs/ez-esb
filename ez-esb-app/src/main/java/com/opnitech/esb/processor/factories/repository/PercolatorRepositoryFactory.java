package com.opnitech.esb.processor.factories.repository;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.processor.persistence.elastic.repository.document.PercolatorRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class PercolatorRepositoryFactory {

    public PercolatorRepositoryFactory() {
        // Default constructor
    }

    @Singleton
    @Bean
    public PercolatorRepository getPercolatorRepository(ElasticsearchTemplate elasticsearchTemplate) {

        PercolatorRepository percolatorMetadataRepository = new PercolatorRepository(elasticsearchTemplate);

        return percolatorMetadataRepository;
    }
}
