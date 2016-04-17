package com.opnitech.esb.persistence.jpa.model.consumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.opnitech.esb.persistence.jpa.model.shared.Persistent;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Entity
@Table(name = "matchQuery")
public class MatchQuery extends Persistent {

    private static final long serialVersionUID = 4248655711039496482L;

    private Subscription subscription;

    private String query;

    public MatchQuery() {
        // Default constructor
    }

    @ManyToOne(targetEntity = Subscription.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriptionId", nullable = false)
    public Subscription getSubscription() {

        return this.subscription;
    }

    public void setSubscription(Subscription subscription) {

        this.subscription = subscription;
    }

    @Column(name = "query", nullable = true)
    @Type(type = "text")
    public String getQuery() {

        return this.query;
    }

    public void setQuery(String query) {

        this.query = query;
    }
}
