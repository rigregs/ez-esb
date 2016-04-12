package com.opnitech.esb.processor.factories.repository;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.inject.Singleton;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Configuration
public class ElasticsearchTemplateFactory {

    private static final String CLIENT_TRANSPORT_SNIFF = "client.transport.sniff";
    private static final String CLUSTER_NAME = "cluster.name";

    public ElasticsearchTemplateFactory() {
        // Default constructor
    }

    @SuppressWarnings("resource")
    @Bean
    @Singleton
    public ElasticsearchTemplate getElasticsearchTemplate() {

        final Settings settings = ImmutableSettings.settingsBuilder().put(CLUSTER_NAME, "incentives-services")
                .put(CLIENT_TRANSPORT_SNIFF, false).build();
        final TransportClient client = new TransportClient(settings);

        client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));

        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(client);

        return elasticsearchTemplate;
    }
}
