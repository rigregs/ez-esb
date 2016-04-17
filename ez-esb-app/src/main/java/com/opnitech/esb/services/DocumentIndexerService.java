package com.opnitech.esb.services;

import com.opnitech.esb.client.model.inbound.DocumentCRUDCommand;
import com.opnitech.esb.common.exception.ServiceException;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface DocumentIndexerService {

    final String OUTBOUND_SEND_CAMEL_ROUTE = "outboundSend";
    final String INBOUND_SEND_CAMEL_ROUTE = "inboundSend";

    void queueUpdateDocument(String version, String documentType, String documentId, String documentAsJSON)
            throws ServiceException;

    void updateDocument(DocumentCRUDCommand documentCRUDCommand) throws ServiceException;
}
