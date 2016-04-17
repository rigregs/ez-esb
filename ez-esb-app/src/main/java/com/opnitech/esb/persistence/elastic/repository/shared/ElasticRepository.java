package com.opnitech.esb.persistence.elastic.repository.shared;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.client.util.FileUtils;
import com.opnitech.esb.client.util.JSONUtil;
import com.opnitech.esb.persistence.elastic.model.shared.ElasticDocument;
import com.opnitech.esb.persistence.elastic.model.shared.ElasticSourceDocument;
import com.opnitech.esb.persistence.elastic.repository.shared.queries.ElasticExecutionQueryBuilder;
import com.opnitech.esb.persistence.elastic.repository.shared.queries.ElasticQueryBuilder;

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

    public void createIndex(String indexName) throws ServiceException {

        if (!this.elasticsearchTemplate.indexExists(indexName)) {
            // this.elasticsearchTemplate.createIndex(indexName);

            CreateIndexRequestBuilder createIndexRequestBuilder = this.client.admin().indices().prepareCreate(indexName);

            updateIndexMapping(createIndexRequestBuilder, "mappings/document-metadata.json", "metadata");
            updateIndexMapping(createIndexRequestBuilder, "mappings/percolator-metadata.json", "percolator-metadata");

            createIndexRequestBuilder.execute().actionGet();
        }
    }

    private void updateIndexMapping(CreateIndexRequestBuilder createIndexRequestBuilder, String mappingFile, String type)
            throws ServiceException {

        String mapping = FileUtils.readFile(mappingFile);
        createIndexRequestBuilder.addMapping(type, mapping);
    }

    public List<Long> evaluatePercolator(String indexName, String type, String objectAsJSON) {

        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("{\"doc\": ");
        queryBuffer.append(objectAsJSON);
        queryBuffer.append("}");

        PercolateResponse response = this.client.preparePercolate().setIndices(indexName).setDocumentType(type)
                .setSource(queryBuffer.toString()).execute().actionGet();

        List<Long> matchIndexes = new ArrayList<>();

        for (PercolateResponse.Match match : response) {
            matchIndexes.add(Long.parseLong(match.getId().string()));
        }

        return matchIndexes;
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

    protected ElasticSourceDocument executeGetById(String indexName, String type, String id) {

        GetResponse response = this.client.prepareGet(indexName, type, id).execute().actionGet();

        String sourceAsString = response.getSourceAsString();
        long version = response.getVersion();

        ElasticSourceDocument elasticSourceDocument = new ElasticSourceDocument(id, sourceAsString, version);

        return elasticSourceDocument;
    }

    protected boolean executeDeleteById(String indexName, String type, String id) {

        DeleteResponse response = this.client.prepareDelete(indexName, type, id).execute().actionGet();

        return response.isFound();
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
