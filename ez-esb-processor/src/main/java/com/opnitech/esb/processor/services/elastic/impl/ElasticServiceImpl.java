package com.opnitech.esb.processor.services.elastic.impl;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.processor.services.elastic.ElasticService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ElasticServiceImpl implements ElasticService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticServiceImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }
}
