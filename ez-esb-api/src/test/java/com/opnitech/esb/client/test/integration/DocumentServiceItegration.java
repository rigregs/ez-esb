package com.opnitech.esb.client.test.integration;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.client.v1.ServiceLocatorFactory;

/**
 * This is an integration test,you need the server running in the localhost PC
 * in order to execute this
 * 
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentServiceItegration {

    private ServiceLocatorFactory serviceLocatorFactory = new ServiceLocatorFactory("http://localhost:8080/ez-esb-app");

    public DocumentServiceItegration() {
        // Default constructor
    }

    @Test
    public void testDocumentServiceNotNull() {

        assertNotNull(this.serviceLocatorFactory.resolveDocumentService());
    }

    @Test
    public void testLocalDocumentServiceUpdateDocument() throws ServiceException {

        this.serviceLocatorFactory.resolveDocumentService().updateDocument("v1", "test", "test1", "{\"name\":\"test\"}");
    }
}
