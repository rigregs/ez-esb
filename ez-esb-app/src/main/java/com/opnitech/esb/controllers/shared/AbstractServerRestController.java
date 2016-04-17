package com.opnitech.esb.controllers.shared;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public abstract class AbstractServerRestController {

    protected ResponseEntity<ControllerResult> buildSuccessControllerResultResponse() {

        return buildResponse(new ControllerResult(ControllerResultEnum.SUCCESS, "SUCCESS"), HttpStatus.FOUND);
    }

    protected ResponseEntity<ControllerResult> buildCancelledControllerResultResponse(String description) {

        return buildResponse(new ControllerResult(ControllerResultEnum.CANCELLED, description), HttpStatus.FOUND);
    }

    protected ResponseEntity<ControllerResult> buildErrorControllerResultResponse(String description) {

        return buildResponse(new ControllerResult(ControllerResultEnum.ERROR, description), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected <DtoType> ResponseEntity<DtoType> buildErrorResponse(DtoType dto) {

        return buildResponse(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public <DtoType> ResponseEntity<DtoType> buildResponse(DtoType dto) {

        return buildResponse(dto, HttpStatus.FOUND);
    }

    private <DtoType> ResponseEntity<DtoType> buildResponse(DtoType dto, HttpStatus status) {

        return dto != null
                ? new ResponseEntity<>(dto, status)
                : new ResponseEntity<DtoType>(status);
    }
}
