
package com.opnitech.esb.processor.persistence.elastic.model.command;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Command implements Serializable {

    private static final long serialVersionUID = 5151247712036974106L;

    public Command() {
        // Default constructor
    }
}
