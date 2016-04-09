package com.opnitech.esb.processor.services.document;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface DocumentIndexerService {

    void updateDocument(String version, String document, String id, String documentAsJSON);
}
