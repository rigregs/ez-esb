package com.opnitech.esb.processor.persistence.elastic.repository.shared.queries;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public final class ElasticQueryBuilderFactory {

    private ElasticQueryBuilderFactory() {

        // Default constructor
    }

    public static <E, R extends ElasticQueryBuilder<E>> R booleanBuilder(Class<E> entityClass) {

        @SuppressWarnings("unchecked")
        R r = (R) new BooleanElasticQueryBuilder<>(entityClass);

        return r;
    }
}
