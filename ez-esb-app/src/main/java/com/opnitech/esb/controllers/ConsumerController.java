package com.opnitech.esb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.opnitech.esb.client.controller.ControllerResult;
import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.controllers.shared.AbstractServerRestController;
import com.opnitech.esb.services.ConsumerService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@RestController
@RequestMapping(value = "/rest/api/v1/consumer/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConsumerController extends AbstractServerRestController {

    @Autowired
    private ConsumerService consumerService;

    public ConsumerController() {
        // Default constructor
    }

    @RequestMapping(value = "/synch-match-query/{consumerId}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<ControllerResult> synchConsumerConfiguration(@PathVariable long consumerId)
            throws ServiceException {

        this.consumerService.synchConsumerConfiguration(consumerId);

        return buildSuccessControllerResultResponse();
    }
}
