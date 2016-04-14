package com.opnitech.esb.processor.persistence.elastic.repository.shared;

import com.opnitech.esb.processor.persistence.elastic.repository.shared.queries.BooleanElasticQueryBuilder;
import com.opnitech.esb.processor.persistence.elastic.repository.shared.queries.ElasticQueryBuilder;
import com.opnitech.esb.processor.persistence.elastic.repository.shared.queries.MatchAllElasticQueryBuilder;

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

    public static <E, R extends ElasticQueryBuilder<E>> R matchAllBuilder(Class<E> entityClass) {

        @SuppressWarnings("unchecked")
        R r = (R) new MatchAllElasticQueryBuilder<>(entityClass);

        return r;
    }
}
