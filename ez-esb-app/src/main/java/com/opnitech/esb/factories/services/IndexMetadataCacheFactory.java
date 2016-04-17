package com.opnitech.esb.factories.services;

import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opnitech.esb.persistence.elastic.repository.document.DocumentRepository;
import com.opnitech.esb.services.cache.IndexMetadataCache;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class IndexMetadataCacheFactory {

    public IndexMetadataCacheFactory() {
        // Default constructor
    }

    @Singleton
    @Bean
    public IndexMetadataCache getIndexMetadataCache(DocumentRepository documentRepository) {

        IndexMetadataCache indexMetadataCache = new IndexMetadataCache(documentRepository);

        return indexMetadataCache;
    }
}
