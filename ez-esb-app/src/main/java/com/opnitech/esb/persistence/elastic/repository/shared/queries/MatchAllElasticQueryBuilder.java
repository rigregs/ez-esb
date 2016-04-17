package com.opnitech.esb.persistence.elastic.repository.shared.queries;

import org.elasticsearch.index.query.MatchAllQueryBuilder;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class MatchAllElasticQueryBuilder<E> extends AbstractElasticQueryBuilder<E, MatchAllQueryBuilder> {

    public MatchAllElasticQueryBuilder(Class<E> entityClass) {

        super(entityClass);
    }

    @Override
    public MatchAllElasticQueryBuilder<E> andTerm(String fieldName, Object value) {

        throw new IllegalStateException();
    }

    @Override
    protected MatchAllQueryBuilder createQueryBuilder() {

        return new MatchAllQueryBuilder();
    }
}
