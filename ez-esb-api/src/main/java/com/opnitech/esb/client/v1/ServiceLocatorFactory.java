package com.opnitech.esb.client.v1;

import com.opnitech.esb.client.v1.services.DocumentService;
import com.opnitech.esb.client.v1.services.locators.DocumentServiceLocator;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ServiceLocatorFactory {

    private String serviceBaseUrl;

    private DocumentService documentService;

    public ServiceLocatorFactory(String serviceBaseUrl) {
        this.serviceBaseUrl = serviceBaseUrl;
    }

    public DocumentService resolveDocumentService() {

        if (this.documentService == null) {
            synchronized (this) {
                if (this.documentService == null) {
                    this.documentService = new DocumentServiceLocator(
                            new StringBuffer().append(this.serviceBaseUrl).append("/rest/api/v1/collector").toString());
                }
            }
        }

        return this.documentService;
    }
}
