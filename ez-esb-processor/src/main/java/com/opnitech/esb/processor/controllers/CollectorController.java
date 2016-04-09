package com.opnitech.esb.processor.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@RestController
@RequestMapping(value = "/rest/api/v1/collector/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CollectorController extends AbstractServerRestController {

    public CollectorController() {
        // Default constructor
    }

    @RequestMapping(value = "/{version}/{document}/{id}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<ControllerResult> updateDocument(@PathVariable String version,
            @PathVariable String document, @PathVariable String id, @RequestBody String documentAsJSON) {

        System.out.println(version);
        System.out.println(document);
        System.out.println(id);
        System.out.println(documentAsJSON);

        return buildSuccessControllerResultResponse();
    }
}
