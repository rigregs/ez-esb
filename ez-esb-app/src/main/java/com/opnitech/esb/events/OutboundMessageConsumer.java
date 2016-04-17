package com.opnitech.esb.events;

import org.apache.camel.Handler;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.persistence.rabbit.DocumentOutboundCommand;
import com.opnitech.esb.services.RoutingService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class OutboundMessageConsumer {

    private final RoutingService routingService;

    public OutboundMessageConsumer(RoutingService routingService) {
        this.routingService = routingService;
    }

    @Handler
    public void consume(DocumentOutboundCommand documentOutboundCommand) throws ServiceException {

        this.routingService.route(documentOutboundCommand);
    }
}
