package com.opnitech.esb.persistence.jpa.model.consumer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.opnitech.esb.persistence.jpa.model.shared.Persistent;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Entity
@Table(name = "subscription")
public class Subscription extends Persistent {

    private static final long serialVersionUID = -7709994880259380464L;

    private String description;

    private String transformationTemplate;

    private Consumer consumer;

    private String documentType;
    private String documentVersion;

    private String subscriptionEnpointConfig;

    private List<MatchQuery> matchQueries;

    public Subscription() {
        // Default constructor
    }

    @Column(name = "transformationTemplate", nullable = true)
    @Type(type = "text")
    public String getTransformationTemplate() {

        return this.transformationTemplate;
    }

    public void setTransformationTemplate(String transformationTemplate) {

        this.transformationTemplate = transformationTemplate;
    }

    @ManyToOne(targetEntity = Consumer.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consumerId", nullable = false)
    public Consumer getConsumer() {

        return this.consumer;
    }

    public void setConsumer(Consumer consumer) {

        this.consumer = consumer;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {

        return this.description;
    }

    public void setDescription(String description) {

        this.description = description;
    }


    @Column(name = "documentVersion", nullable = false)
    public String getDocumentVersion() {

        return this.documentVersion;
    }

    public void setDocumentVersion(String documentVersion) {

        this.documentVersion = documentVersion;
    }

    @OneToMany(targetEntity = MatchQuery.class, cascade = CascadeType.ALL, mappedBy = "subscription", fetch = FetchType.LAZY)
    public List<MatchQuery> getMatchQueries() {

        if (this.matchQueries == null) {
            this.matchQueries = new ArrayList<>();
        }

        return this.matchQueries;
    }

    public void setMatchQueries(List<MatchQuery> matchQueries) {

        this.matchQueries = matchQueries;
    }

    @Column(name = "subscriptionEnpointConfig", nullable = false)
    @Type(type = "text")
    public String getSubscriptionEnpointConfig() {

        return this.subscriptionEnpointConfig;
    }

    public void setSubscriptionEnpointConfig(String subscriptionEnpointConfig) {

        this.subscriptionEnpointConfig = subscriptionEnpointConfig;
    }

    @Column(name = "documentType", nullable = false)
    public String getDocumentType() {

        return this.documentType;
    }

    public void setDocumentType(String documentType) {

        this.documentType = documentType;
    }
}
