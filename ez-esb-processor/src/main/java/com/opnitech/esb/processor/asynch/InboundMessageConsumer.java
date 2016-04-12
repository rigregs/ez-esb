package com.opnitech.esb.processor.asynch;

import org.apache.camel.Handler;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class InboundMessageConsumer {

    public InboundMessageConsumer() {
        // Default constructor
    }

    @Handler
    public void consume(String message) {

        System.out.println(message);
    }
}
