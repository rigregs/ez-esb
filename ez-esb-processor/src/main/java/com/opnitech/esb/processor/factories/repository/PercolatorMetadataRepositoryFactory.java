package com.opnitech.esb.processor.factories.repository;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.processor.persistence.elastic.repository.document.PercolatorMetadataRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class PercolatorMetadataRepositoryFactory {

    public PercolatorMetadataRepositoryFactory() {
        // Default constructor
    }

    @Singleton
    @Bean
    public PercolatorMetadataRepository getPercolatorMetadataRepository(ElasticsearchTemplate elasticsearchTemplate) {

        PercolatorMetadataRepository percolatorMetadataRepository = new PercolatorMetadataRepository(elasticsearchTemplate);

        return percolatorMetadataRepository;
    }
}
