package com.opnitech.esb.persistence.jpa.repository.consumer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.opnitech.esb.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.persistence.jpa.repository.shared.PersistentRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface SubscriptionRepository extends PersistentRepository<Subscription> {

    @Query("select subscription from Subscription subscription join subscription.matchQueries matchQuery where matchQuery.id=:matchQueryId")
    Subscription findSubscriptionOwnMatchQuery(@Param("matchQueryId") Long matchQueryId);
}
