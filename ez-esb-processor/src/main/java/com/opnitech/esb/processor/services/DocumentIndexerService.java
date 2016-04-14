package com.opnitech.esb.processor.services;

import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.persistence.elastic.model.command.DocumentCRUDCommand;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface DocumentIndexerService {

    void queueUpdateDocument(String version, String documentType, String documentId, String documentAsJSON)
            throws ServiceException;

    void updateDocument(DocumentCRUDCommand documentCRUDCommand) throws ServiceException;
}
