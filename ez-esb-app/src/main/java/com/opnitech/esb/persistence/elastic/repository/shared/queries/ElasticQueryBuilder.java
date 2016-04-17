package com.opnitech.esb.persistence.elastic.repository.shared.queries;

import org.springframework.data.domain.Pageable;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface ElasticQueryBuilder<E> {

    ElasticQueryBuilder<E> andTermNotNull(String fieldName, Object value);

    ElasticQueryBuilder<E> andTerm(String fieldName, Object value);

    ElasticQueryBuilder<E> withAllElements();

    ElasticQueryBuilder<E> withPageable(int size);

    ElasticQueryBuilder<E> withPageable(int page, int size);

    Pageable getPageable();
}
