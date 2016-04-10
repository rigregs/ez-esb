package com.opnitech.esb.processor.persistence.repository.queries;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public abstract class AbstractElasticQueryBuilder<E, Q extends QueryBuilder> implements ElasticExecutionQueryBuilder<E, Q> {

    private final Q queryBuilder;

    private final Class<E> entityClass;

    private Pageable pageable;

    public AbstractElasticQueryBuilder(Class<E> entityClass) {

        this.entityClass = entityClass;
        this.queryBuilder = createQueryBuilder();
    }

    protected abstract Q createQueryBuilder();

    @Override
    public Q builder() {

        return this.queryBuilder;
    }

    @Override
    public ElasticQueryBuilder<E> andNotNull(String fieldName, Object value) {

        if (value != null) {
            and(fieldName, value);
        }

        return this;
    }

    @Override
    public ElasticQueryBuilder<E> withAllElements() {

        buildPageRequest(0, Integer.MAX_VALUE);

        return this;
    }

    @Override
    public ElasticQueryBuilder<E> withPageable(int size) {

        buildPageRequest(0, size);

        return this;
    }

    @Override
    public ElasticQueryBuilder<E> withPageable(int page, int size) {

        buildPageRequest(page, size);

        return this;
    }

    private void buildPageRequest(int page, int size) {

        this.pageable = new PageRequest(page, size);
    }

    @Override
    public Class<E> getEntityClass() {

        return this.entityClass;
    }

    @Override
    public Pageable getPageable() {

        return this.pageable;
    }
}
