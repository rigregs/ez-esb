package com.opnitech.esb.services;

import com.opnitech.esb.common.exception.ServiceException;
import com.opnitech.esb.persistence.rabbit.DocumentOutboundCommand;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface RoutingService {

    void route(DocumentOutboundCommand documentOutboundCommand) throws ServiceException;
}
