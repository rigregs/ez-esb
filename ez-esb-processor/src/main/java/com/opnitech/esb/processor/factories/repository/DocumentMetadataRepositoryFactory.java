package com.opnitech.esb.processor.factories.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.processor.persistence.elastic.repository.document.DocumentMetadataRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class DocumentMetadataRepositoryFactory {

    public DocumentMetadataRepositoryFactory() {
        // Default constructor
    }

    @Bean
    public DocumentMetadataRepository getDocumentMetadataRepository(ElasticsearchTemplate elasticsearchTemplate) {

        DocumentMetadataRepository documentMetadataRepository = new DocumentMetadataRepository(elasticsearchTemplate);

        return documentMetadataRepository;
    }
}
