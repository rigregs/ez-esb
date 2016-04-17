package com.opnitech.esb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.opnitech.esb.client.controller.ControllerResult;
import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.controllers.shared.AbstractServerRestController;
import com.opnitech.esb.services.DocumentIndexerService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@RestController
@RequestMapping(value = "/rest/api/v1/collector/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CollectorController extends AbstractServerRestController {

    @Autowired
    private DocumentIndexerService documentIndexerService;

    public CollectorController() {
        // Default constructor
    }

    @RequestMapping(value = "/{version}/{documentType}/{documentId}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<ControllerResult> updateDocument(@PathVariable String version,
            @PathVariable String documentType, @PathVariable String documentId, @RequestBody String documentAsJSON)
                    throws ServiceException {

        this.documentIndexerService.queueUpdateDocument(version, documentType, documentId, documentAsJSON);

        return buildSuccessControllerResultResponse();
    }

    @RequestMapping(value = "/{version}/{documentType}/{documentId}", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<ControllerResult> deleteDocument(@PathVariable String version,
            @PathVariable String documentType, @PathVariable String documentId) throws ServiceException {

        this.documentIndexerService.queueDeleteDocument(version, documentType, documentId);

        return buildSuccessControllerResultResponse();
    }
}
