package com.opnitech.esb.processor.factories.repository;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Singleton;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class ElasticsearchTemplateFactory {

    public ElasticsearchTemplateFactory() {
        // Default constructor
    }

    @SuppressWarnings("resource")
    @Bean
    @Singleton
    public ElasticsearchTemplate getElasticsearchTemplate() {

        Node node = NodeBuilder.nodeBuilder().clusterName("incentives-services").client(true).node();

        Client client = node.client();

        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(client);

        return elasticsearchTemplate;
    }
}
