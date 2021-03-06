package com.opnitech.esb.processor.services;

import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.persistence.rabbit.DocumentCRUDCommand;

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
