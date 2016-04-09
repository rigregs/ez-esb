package com.opnitech.esb.processor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.opnitech.esb.processor.controllers.shared.AbstractServerRestController;
import com.opnitech.esb.processor.controllers.shared.ControllerResult;
import com.opnitech.esb.processor.model.ElasticIndexMetadata;
import com.opnitech.esb.processor.services.document.DocumentIndexerService;

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

    @RequestMapping(value = "/{version}/{document}/{id}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<ControllerResult> updateDocument(@PathVariable String version,
            @PathVariable String document, @PathVariable String id, @RequestBody String documentAsJSON) {

        this.documentIndexerService.updateDocument(new ElasticIndexMetadata(version, document), id, documentAsJSON);

        return buildSuccessControllerResultResponse();
    }
}
