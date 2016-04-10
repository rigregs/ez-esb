package com.opnitech.esb.processor.persistence.repository;

import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import com.opnitech.esb.processor.persistence.index.ElasticIndexMetadata;
import com.opnitech.esb.processor.persistence.model.ElasticDocumentMetadata;
import com.opnitech.esb.processor.persistence.repository.queries.ElasticQueryBuilder;
import com.opnitech.esb.processor.persistence.repository.queries.ElasticQueryBuilderFactory;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ElasticIndexMetadataRepository extends ElasticRepository {

    public ElasticIndexMetadataRepository(ElasticsearchTemplate elasticsearchTemplate) {

        super(elasticsearchTemplate);
    }

    public ElasticDocumentMetadata retrieveElasticDocumentMetadata(ElasticIndexMetadata elasticIndexMetadata, String id) {

        ElasticQueryBuilder<ElasticDocumentMetadata> builder = ElasticQueryBuilderFactory
                .booleanBuilder(ElasticDocumentMetadata.class);

        builder.andNotNull("documentId", id);

        builder.withAllElements();

        ElasticDocumentMetadata elasticDocumentMetadata = executeQuery(elasticIndexMetadata.getIndexName(),
                elasticIndexMetadata.getMetadataTypeName(), builder, new ResultsExtractor<ElasticDocumentMetadata>() {

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
}