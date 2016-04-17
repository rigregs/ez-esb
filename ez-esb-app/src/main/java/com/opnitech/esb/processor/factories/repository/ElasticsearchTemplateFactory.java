package com.opnitech.esb.processor.factories.repository;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.inject.Singleton;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import com.opnitech.esb.processor.configuration.PropertyPlaceholder;
import com.opnitech.esb.processor.configuration.elastic.ElasticConfiguration;

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
    public ElasticsearchTemplate getElasticsearchTemplate(PropertyPlaceholder propertyPlaceholder) {

        ElasticConfiguration elasticConfiguration = propertyPlaceholder.getElastic();
        Validate.notNull(elasticConfiguration);

        Settings settings = ImmutableSettings.settingsBuilder().put(CLUSTER_NAME, "incentives-services")
                .put(CLIENT_TRANSPORT_SNIFF, elasticConfiguration.isSniff()).build();

        TransportClient client = new TransportClient(settings);

        populateTransportAddress(client, elasticConfiguration);

        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(client);

        return elasticsearchTemplate;
    }

    private void populateTransportAddress(TransportClient client, ElasticConfiguration elasticConfiguration) {

        String hosts = elasticConfiguration.getHosts();

        if (StringUtils.isNotBlank(hosts)) {
            String[] hostArray = StringUtils.split(StringUtils.trim(hosts), ",");

            for (String hostData : hostArray) {
                String[] hostPort = StringUtils.split(StringUtils.trim(hostData), ":");

                String hostname = StringUtils.trim(hostPort[0]);
                int port = Integer.parseInt(StringUtils.trim(hostPort[1]));

                client.addTransportAddress(new InetSocketTransportAddress(hostname, port));
            }
        }
    }
}
