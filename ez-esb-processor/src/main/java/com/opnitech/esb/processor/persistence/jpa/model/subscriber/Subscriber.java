package com.opnitech.esb.processor.persistence.jpa.model.subscriber;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.opnitech.esb.processor.persistence.jpa.model.shared.Persistent;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Entity
@Table(name = "subscriber")
public class Subscriber extends Persistent {

    private static final long serialVersionUID = 7439222999982792495L;

    public Subscriber() {
        // Default constructor
    }
}
