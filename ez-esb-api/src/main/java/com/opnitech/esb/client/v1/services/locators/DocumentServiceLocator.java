package com.opnitech.esb.client.v1.services.locators;

import org.apache.commons.lang3.Validate;

import com.opnitech.esb.client.controller.ControllerResult;
import com.opnitech.esb.client.controller.ControllerResultEnum;
import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.client.v1.services.DocumentService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentServiceLocator extends AbstractServiceLocator implements DocumentService {

    public DocumentServiceLocator(String serviceBaseUrl) {

        super(serviceBaseUrl);
    }

    @Override
    public ControllerResultEnum updateDocument(String version, String documentType, String documentId, String documentAsJSON)
            throws ServiceException {

        try {
            String methodUrl = buildMethodUrl("updateDocument", "/{version}", "/{documentType}", "/{documentId}");
            Validate.notNull(methodUrl);

            ControllerResult controllerResult = invokePost(methodUrl, ControllerResult.class, documentAsJSON, version,
                    documentType, documentId);

            return controllerResult.getControllerResultEnum();
        }
        catch (Exception e) {
            throw wrapException(e);
        }
    }
}
