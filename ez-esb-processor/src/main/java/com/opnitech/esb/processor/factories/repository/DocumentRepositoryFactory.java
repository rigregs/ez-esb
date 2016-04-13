package com.opnitech.esb.processor.factories.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.processor.persistence.repository.document.DocumentRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class DocumentRepositoryFactory {

    public DocumentRepositoryFactory() {
        // Default constructor
    }

    @Bean
    public DocumentRepository getDocumentRepository(ElasticsearchTemplate elasticsearchTemplate) {

        return new DocumentRepository(elasticsearchTemplate);
    }
}
