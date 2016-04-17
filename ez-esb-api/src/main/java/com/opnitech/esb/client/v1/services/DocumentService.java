package com.opnitech.esb.client.v1.services;

import com.opnitech.esb.client.controller.ControllerResultEnum;
import com.opnitech.esb.client.exception.ServiceException;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface DocumentService {

    ControllerResultEnum updateDocument(String version, String documentType, String documentId, String documentAsJSON)
            throws ServiceException;
}
