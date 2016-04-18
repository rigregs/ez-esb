package com.opnitech.esb.persistence.elastic.repository.document;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import com.opnitech.esb.client.util.JSONUtil;
import com.opnitech.esb.common.data.ElasticIndexMetadata;
import com.opnitech.esb.persistence.elastic.model.document.PercolatorMetadata;
import com.opnitech.esb.persistence.elastic.repository.shared.ElasticQueryBuilderFactory;
import com.opnitech.esb.persistence.elastic.repository.shared.ElasticRepository;
import com.opnitech.esb.persistence.elastic.repository.shared.queries.ElasticQueryBuilder;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class PercolatorRepository extends ElasticRepository {

    public PercolatorRepository(ElasticsearchTemplate elasticsearchTemplate) {
        super(elasticsearchTemplate);
    }

    public List<PercolatorMetadata> retrievePercolatorMetadatas(ElasticIndexMetadata elasticIndexMetadata,
            final long consumerId) {

        ElasticQueryBuilder<PercolatorMetadata> builder = ElasticQueryBuilderFactory.booleanBuilder(PercolatorMetadata.class);

        builder.andTermNotNull(PercolatorMetadata.CONSUMER_ID_PROPERTY, consumerId);

        builder.withAllElements();

        List<PercolatorMetadata> percolatorMetadatas = executeQuery(elasticIndexMetadata.getIndexName(),
                elasticIndexMetadata.getPercolatorMetadataTypeName(), builder, new ResultsExtractor<List<PercolatorMetadata>>() {

                    @Override
                    public List<PercolatorMetadata> extract(SearchResponse response) {

                        List<PercolatorMetadata> innerPercolatorMetadatas = new ArrayList<>();

                        SearchHit[] hits = response.getHits().getHits();
                        if (ArrayUtils.isNotEmpty(hits)) {
                            for (SearchHit hit : hits) {
                                String objectAsJSON = hit.getSourceAsString();

                                PercolatorMetadata unmarshall = JSONUtil.unmarshall(PercolatorMetadata.class, objectAsJSON);
                                unmarshall.setId(hit.getId());

                                innerPercolatorMetadatas.add(unmarshall);
                            }
                        }

                        return innerPercolatorMetadatas;
                    }
                });

        return percolatorMetadatas;
    }
}
