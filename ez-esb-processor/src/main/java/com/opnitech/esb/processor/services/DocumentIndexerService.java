package com.opnitech.esb.processor.services;

import com.opnitech.esb.processor.persistence.model.command.DocumentCRUDCommand;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface DocumentIndexerService {

    void queueUpdateDocument(String version, String documentType, String documentId, String documentAsJSON);

    void updateDocument(DocumentCRUDCommand documentCRUDCommand);
}
