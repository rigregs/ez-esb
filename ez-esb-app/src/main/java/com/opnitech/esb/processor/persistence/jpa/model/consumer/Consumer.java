package com.opnitech.esb.processor.persistence.jpa.model.consumer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.opnitech.esb.processor.persistence.jpa.model.shared.Persistent;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Entity
@Table(name = "consumer")
public class Consumer extends Persistent {

    private static final long serialVersionUID = 7439222999982792495L;

    private String description;

    private List<Subscription> subscriptions;

    public Consumer() {
        // Default constructor
    }

    @OneToMany(targetEntity = Subscription.class, cascade = CascadeType.ALL, mappedBy = "consumer", fetch = FetchType.LAZY)
    public List<Subscription> getSubscriptions() {

        if (this.subscriptions == null) {
            this.subscriptions = new ArrayList<>();
        }

        return this.subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {

        this.subscriptions = subscriptions;
    }

    @Column(name = "description")
    public String getDescription() {

        return this.description;
    }

    public void setDescription(String description) {

        this.description = description;
    }
}
