package com.opnitech.esb.processor.services.elastic.impl.queries;

import org.springframework.data.domain.Pageable;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface ElasticQueryBuilder<E> {

    ElasticQueryBuilder<E> andNotNull(String fieldName, Object value);

    ElasticQueryBuilder<E> and(String fieldName, Object value);

    ElasticQueryBuilder<E> withAllElements();

    ElasticQueryBuilder<E> withPageable(int size);

    ElasticQueryBuilder<E> withPageable(int page, int size);

    Pageable getPageable();
}
