package com.opnitech.esb.processor.factories;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.processor.services.elastic.ElasticService;
import com.opnitech.esb.processor.services.elastic.impl.ElasticServiceImpl;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class ElasticServiceFactory {

    public ElasticServiceFactory() {
        // Default constructor
    }

    @Bean
    public ElasticService getElasticService(ElasticsearchTemplate elasticsearchTemplate) {

        return new ElasticServiceImpl(elasticsearchTemplate);
    }
}
