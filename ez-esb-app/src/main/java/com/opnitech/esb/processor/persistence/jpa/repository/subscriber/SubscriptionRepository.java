package com.opnitech.esb.processor.persistence.jpa.repository.subscriber;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.opnitech.esb.processor.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.processor.persistence.jpa.repository.shared.PersistentRepository;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface SubscriptionRepository extends PersistentRepository<Subscription> {

    @Query("select subscription from Subscription subscription join subscription.matchQueries matchQuery where matchQuery.id=:matchQueryId")
    Subscription findSubscriptionOwnMatchQuery(@Param("matchQueryId") Long matchQueryId);

}
