package com.opnitech.esb.processor.events;

import org.apache.camel.Handler;

import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.persistence.rabbit.DocumentOutboundCommand;
import com.opnitech.esb.processor.services.RoutingService;

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
