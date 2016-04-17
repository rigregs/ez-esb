package com.opnitech.esb.processor.persistence.elastic.repository.shared;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.persistence.elastic.model.shared.ElasticDocument;
import com.opnitech.esb.processor.persistence.elastic.repository.shared.queries.ElasticExecutionQueryBuilder;
import com.opnitech.esb.processor.persistence.elastic.repository.shared.queries.ElasticQueryBuilder;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public abstract class ElasticRepository {

    private static final String PERCOLATOR_TYPE = ".percolator";

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final Client client;

    public ElasticRepository(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.client = elasticsearchTemplate.getClient();
    }

    public void createIndex(String indexName) {

        if (!this.elasticsearchTemplate.indexExists(indexName)) {
            this.elasticsearchTemplate.createIndex(indexName);
        }
    }

    public List<Long> evaluatePercolator(String indexName, String type, String objectAsJSON) throws ServiceException {

        try (XContentBuilder docBuilder = XContentFactory.jsonBuilder().startObject()) {
            docBuilder.field("doc").startObject();
            docBuilder.field("content", objectAsJSON);
            docBuilder.endObject();
            docBuilder.endObject();

            PercolateResponse response = this.client.preparePercolate().setIndices(indexName).setDocumentType(type)
                    .setSource(docBuilder).execute().actionGet();

            List<Long> matchIndexes = new ArrayList<>();

            for (PercolateResponse.Match match : response) {
                matchIndexes.add(Long.parseLong(match.getId().string()));
            }

            return matchIndexes;
        }
        catch (IOException exception) {
            throw new ServiceException(exception);
        }
    }

    public void savePercolator(String indexName, String elasticId, Object owner, String queryAsJSON) {

        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("{\"query\": ");
        queryBuffer.append(queryAsJSON);
        queryBuffer.append(",");

        queryBuffer.append("\"owner\": ");
        String metadataASJSON = JSONUtil.marshall(owner);
        queryBuffer.append(metadataASJSON);
        queryBuffer.append("}");

        this.client.prepareIndex(indexName, PERCOLATOR_TYPE, elasticId).setSource(queryBuffer.toString()).setRefresh(true)
                .execute().actionGet();
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

    public String save(String indexName, String type, String elasticId, String objectAsJSON) {

        String elasticIdToUse = elasticId;

        if (StringUtils.isBlank(elasticIdToUse)) {
            elasticIdToUse = insertDocument(indexName, type, objectAsJSON);
        }
        else {
            updateDocument(indexName, type, elasticIdToUse, objectAsJSON);
        }

        return elasticIdToUse;
    }

    private String insertDocument(String indexName, String type, String objectAsJSON) {

        IndexRequestBuilder indexRequestBuilder = this.client.prepareIndex(indexName, type).setSource(objectAsJSON);

        IndexResponse response = indexRequestBuilder.get();

        return response.getId();
    }

    private void updateDocument(String indexName, String type, String id, String objectAsJSON) {

        this.client.prepareIndex(indexName, type, id).setSource(objectAsJSON).setRefresh(true).get();
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
