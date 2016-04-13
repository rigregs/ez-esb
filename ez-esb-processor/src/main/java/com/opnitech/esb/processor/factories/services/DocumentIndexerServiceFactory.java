package com.opnitech.esb.processor.factories.services;

import org.apache.camel.ProducerTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.persistence.elastic.repository.document.DocumentRepository;
import com.opnitech.esb.processor.persistence.elastic.repository.document.ElasticIndexMetadataRepository;
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

    @Bean(name = "documentIndexerService")
    public DocumentIndexerService getDocumentIndexerService(DocumentRepository documentRepository,
            ElasticIndexMetadataRepository elasticIndexMetadataRepository, ProducerTemplate producerTemplate) {

        return new DocumentIndexerServiceImpl(documentRepository, elasticIndexMetadataRepository, producerTemplate);
    }
}
