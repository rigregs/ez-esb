package com.opnitech.esb.persistence.elastic.repository.shared.queries;

import org.elasticsearch.index.query.BaseQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class BooleanElasticQueryBuilder<E> extends AbstractElasticQueryBuilder<E, BoolQueryBuilder> {

    public BooleanElasticQueryBuilder(Class<E> entityClass) {

        super(entityClass);
    }

    @Override
    public BooleanElasticQueryBuilder<E> and(String fieldName, Object value) {

        BaseQueryBuilder matchQuery = QueryBuilders.termQuery(fieldName, value);

        builder().must(matchQuery);

        return this;
    }

    @Override
    protected BoolQueryBuilder createQueryBuilder() {

        return new BoolQueryBuilder();
    }
}
