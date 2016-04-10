package com.opnitech.esb.processor.factories.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.persistence.repository.ElasticIndexMetadataRepository;
import com.opnitech.esb.processor.persistence.repository.queries.DocumentRepository;
import com.opnitech.esb.processor.services.DocumentIndexerService;
import com.opnitech.esb.processor.services.impl.DocumentIndexerServiceImpl;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class DocumentIndexerServiceFactory {

    public DocumentIndexerServiceFactory() {
        // Default constructor
    }

    @Bean
    public DocumentIndexerService getDocumentIndexerService(DocumentRepository documentRepository,
            ElasticIndexMetadataRepository elasticIndexMetadataRepository) {

        return new DocumentIndexerServiceImpl(documentRepository, elasticIndexMetadataRepository);
    }
}