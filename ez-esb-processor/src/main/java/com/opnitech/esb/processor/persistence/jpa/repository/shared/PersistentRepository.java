package com.opnitech.esb.processor.persistence.jpa.repository.shared;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.opnitech.esb.processor.persistence.jpa.model.shared.Persistent;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@NoRepositoryBean
public interface PersistentRepository<T extends Persistent> extends PagingAndSortingRepository<T, Long> {
    // NOP
}
