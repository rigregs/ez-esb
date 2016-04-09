package com.opnitech.esb.processor.factories;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.processor.services.document.DocumentIndexerService;
import com.opnitech.esb.processor.services.document.impl.DocumentIndexerServiceImpl;
import com.opnitech.esb.processor.services.elastic.ElasticService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class DocumentIndexerServiceFactory {

    public DocumentIndexerServiceFactory() {
        // Default constructor
    }

    @Bean
    public DocumentIndexerService getDocumentIndexerService(ElasticService elasticService) {

        return new DocumentIndexerServiceImpl(elasticService);
    }
}
