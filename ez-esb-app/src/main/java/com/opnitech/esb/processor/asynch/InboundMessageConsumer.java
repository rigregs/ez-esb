package com.opnitech.esb.processor.asynch;

import org.apache.camel.Handler;

import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.persistence.elastic.model.command.DocumentCRUDCommand;
import com.opnitech.esb.processor.services.DocumentIndexerService;

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

        this.documentIndexerService.updateDocument(documentCRUDCommand);
    }
}
