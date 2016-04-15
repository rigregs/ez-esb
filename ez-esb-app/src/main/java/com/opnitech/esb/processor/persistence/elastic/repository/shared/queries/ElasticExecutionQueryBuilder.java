package com.opnitech.esb.processor.persistence.elastic.repository.shared.queries;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface ElasticExecutionQueryBuilder<E, Q extends QueryBuilder> extends ElasticQueryBuilder<E> {

    Q builder();

    Class<E> getEntityClass();
}
