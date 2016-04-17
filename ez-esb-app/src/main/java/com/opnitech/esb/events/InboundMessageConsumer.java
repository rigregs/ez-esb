package com.opnitech.esb.events;

import org.apache.camel.Handler;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.client.v1.model.inbound.DocumentCRUDCommand;
import com.opnitech.esb.services.DocumentIndexerService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class InboundMessageConsumer {

    private final DocumentIndexerService documentIndexerService;

    public InboundMessageConsumer(DocumentIndexerService documentIndexerService) {
        this.documentIndexerService = documentIndexerService;
    }

    @Handler
    public void consume(DocumentCRUDCommand documentCRUDCommand) throws ServiceException {

        this.documentIndexerService.processDocumentCommand(documentCRUDCommand);
    }
}
