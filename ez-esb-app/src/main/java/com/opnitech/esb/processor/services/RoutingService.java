package com.opnitech.esb.processor.services;

import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.persistence.rabbit.DocumentOutboundCommand;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface RoutingService {

    void route(DocumentOutboundCommand documentOutboundCommand) throws ServiceException;
}
