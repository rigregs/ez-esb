package com.opnitech.esb.processor.persistence.elastic.repository.document;

import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import com.opnitech.esb.processor.common.data.ElasticIndexMetadata;
import com.opnitech.esb.processor.persistence.elastic.model.client.DocumentMetadata;
import com.opnitech.esb.processor.persistence.elastic.repository.shared.ElasticQueryBuilderFactory;
import com.opnitech.esb.processor.persistence.elastic.repository.shared.ElasticRepository;
import com.opnitech.esb.processor.persistence.elastic.repository.shared.queries.ElasticQueryBuilder;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentMetadataRepository extends ElasticRepository {

    public DocumentMetadataRepository(ElasticsearchTemplate elasticsearchTemplate) {

        super(elasticsearchTemplate);
    }

    public DocumentMetadata retrieveElasticDocumentMetadata(ElasticIndexMetadata elasticIndexMetadata, String id) {

        ElasticQueryBuilder<DocumentMetadata> builder = ElasticQueryBuilderFactory
                .booleanBuilder(DocumentMetadata.class);

        builder.andNotNull("documentId", id);

        builder.withAllElements();

        DocumentMetadata elasticDocumentMetadata = executeQuery(elasticIndexMetadata.getIndexName(),
                elasticIndexMetadata.getMetadataTypeName(), builder, new ResultsExtractor<DocumentMetadata>() {

                    @Override
                    public DocumentMetadata extract(SearchResponse response) {

                        SearchHit[] hits = response.getHits().getHits();

                        SearchHit searchHit = ArrayUtils.isNotEmpty(hits)
                                ? hits[0]
                                : null;

                        if (searchHit != null) {
                            String objectAsJSON = searchHit.getSourceAsString();

                            DocumentMetadata unmarshall = JSONUtil.unmarshall(DocumentMetadata.class, objectAsJSON);
                            unmarshall.setId(searchHit.getId());

                            return unmarshall;
                        }

                        return null;
                    }
                });

        return elasticDocumentMetadata;
    }
}
