package com.opnitech.esb.processor.persistence.repository.shared;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.opnitech.esb.processor.persistence.model.elastic.ElasticDocument;
import com.opnitech.esb.processor.persistence.repository.shared.queries.ElasticExecutionQueryBuilder;
import com.opnitech.esb.processor.persistence.repository.shared.queries.ElasticQueryBuilder;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ElasticRepository {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticRepository(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public void createIndex(String indexName) {

        if (!this.elasticsearchTemplate.indexExists(indexName)) {
            this.elasticsearchTemplate.createIndex(indexName);
        }
    }

    public <T extends ElasticDocument> T save(String indexName, String type, T document) {

        if (document == null) {
            return null;
        }

        if (document.getId() == null) {
            String objectAsJSON = JSONUtil.marshall(document);

            String id = insertDocument(indexName, type, objectAsJSON);
            document.setId(id);
        }

        String objectAsJSON = JSONUtil.marshall(document);
        updateDocument(indexName, type, document.getId(), objectAsJSON);

        return document;
    }

    public String insertDocument(String indexName, String type, String objectAsJSON) {

        IndexRequestBuilder indexRequestBuilder = this.elasticsearchTemplate.getClient().prepareIndex(indexName, type)
                .setSource(objectAsJSON);

        IndexResponse response = indexRequestBuilder.get();

        return response.getId();
    }

    public void updateDocument(String indexName, String type, String id, String objectAsJSON) {

        this.elasticsearchTemplate.getClient().prepareIndex(indexName, type, id).setSource(objectAsJSON).get();
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
