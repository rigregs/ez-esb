package com.opnitech.esb.factories.services;

import org.apache.camel.ProducerTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.persistence.elastic.repository.document.DocumentMetadataRepository;
import com.opnitech.esb.persistence.elastic.repository.document.DocumentRepository;
import com.opnitech.esb.persistence.elastic.repository.document.PercolatorRepository;
import com.opnitech.esb.services.DocumentIndexerService;
import com.opnitech.esb.services.cache.IndexMetadataCache;
import com.opnitech.esb.services.impl.DocumentIndexerServiceImpl;

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
            DocumentMetadataRepository elasticIndexMetadataRepository, ProducerTemplate producerTemplate,
            IndexMetadataCache indexMetadataCache, PercolatorRepository percolatorRepository) {

        DocumentIndexerService documentIndexerService = new DocumentIndexerServiceImpl(documentRepository,
                elasticIndexMetadataRepository, producerTemplate, indexMetadataCache, percolatorRepository);

        return documentIndexerService;
    }
}
