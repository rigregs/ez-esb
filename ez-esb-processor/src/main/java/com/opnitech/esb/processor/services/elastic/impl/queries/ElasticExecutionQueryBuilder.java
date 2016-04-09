package com.opnitech.esb.processor.services.elastic.impl.queries;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface ElasticExecutionQueryBuilder<E, Q extends QueryBuilder> extends ElasticQueryBuilder<E> {

    Q builder();

    Class<E> getEntityClass();
}
