package com.opnitech.esb.processor.services.elastic.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.opnitech.esb.processor.model.ElasticDocumentMetadata;
import com.opnitech.esb.processor.model.ElasticIndexMetadata;
import com.opnitech.esb.processor.services.elastic.ElasticService;
import com.opnitech.esb.processor.services.elastic.impl.queries.ElasticExecutionQueryBuilder;
import com.opnitech.esb.processor.services.elastic.impl.queries.ElasticQueryBuilder;
import com.opnitech.esb.processor.services.elastic.impl.queries.ElasticQueryBuilderFactory;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ElasticServiceImpl implements ElasticService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticServiceImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public void createDocument(ElasticIndexMetadata elasticIndexMetadata) {

        String indexName = elasticIndexMetadata.compileIndexName();

        if (!this.elasticsearchTemplate.indexExists(indexName)) {
            this.elasticsearchTemplate.createIndex(indexName);
        }
    }

    @Override
    public String insertDocument(ElasticIndexMetadata elasticIndexMetadata, String type, String objectAsJSON) {

        IndexResponse response = this.elasticsearchTemplate.getClient()
                .prepareIndex(elasticIndexMetadata.compileIndexName(), "test").setSource(objectAsJSON).get();

        return response.getId();
    }

    @Override
    public void updateDocument(ElasticIndexMetadata elasticIndexMetadata, String type, String id, String objectAsJSON) {

        this.elasticsearchTemplate.getClient().prepareIndex(elasticIndexMetadata.compileIndexName(), "test", id)
                .setSource(objectAsJSON).get();
    }

    @Override
    public ElasticDocumentMetadata retrieveElasticDocumentMetadata(ElasticIndexMetadata elasticIndexMetadata, String id) {

        ElasticQueryBuilder<ElasticDocumentMetadata> builder = ElasticQueryBuilderFactory
                .booleanBuilder(ElasticDocumentMetadata.class);

        builder.andNotNull("documentId", id);

        builder.withAllElements();

        ElasticDocumentMetadata elasticDocumentMetadata = executeQuery(elasticIndexMetadata.compileIndexName(), "test", builder,
                new ResultsExtractor<ElasticDocumentMetadata>() {

                    @Override
                    public ElasticDocumentMetadata extract(SearchResponse response) {

                        SearchHit[] hits = response.getHits().getHits();

                        SearchHit searchHit = ArrayUtils.isNotEmpty(hits)
                                ? hits[0]
                                : null;

                        if (searchHit != null) {
                            String objectAsJSON = searchHit.getSourceAsString();

                            ElasticDocumentMetadata unmarshall = JSONUtil.unmarshall(ElasticDocumentMetadata.class, objectAsJSON);
                            unmarshall.setId(searchHit.getId());

                            return unmarshall;
                        }

                        return null;
                    }
                });

        return elasticDocumentMetadata;
    }

    protected <E, R> E executeQuery(String indexName, String type, ElasticQueryBuilder<R> queryBuilder,
            ResultsExtractor<E> resultsExtractor) {

        @SuppressWarnings("unchecked")
        ElasticExecutionQueryBuilder<E, ?> elasticExecutionQueryBuilder = (ElasticExecutionQueryBuilder<E, ?>) queryBuilder;

        QueryBuilder builder = elasticExecutionQueryBuilder.builder();

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(builder)
                .withIndices(indexName).withTypes(type);

        Pageable pageable = elasticExecutionQueryBuilder.getPageable();
        if (pageable != null) {
            nativeSearchQueryBuilder.withPageable(pageable);
        }

        SearchQuery searchQuery = nativeSearchQueryBuilder.build();

        E result = this.elasticsearchTemplate.<E> query(searchQuery, resultsExtractor);

        return result;
    }

}
