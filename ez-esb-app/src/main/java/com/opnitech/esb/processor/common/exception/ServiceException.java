package com.opnitech.esb.processor.common.exception;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = -5169414609902532971L;

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
