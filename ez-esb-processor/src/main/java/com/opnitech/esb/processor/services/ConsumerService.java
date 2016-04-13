package com.opnitech.esb.processor.services;

import org.springframework.web.bind.annotation.PathVariable;

import com.opnitech.esb.processor.common.ServiceException;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface ConsumerService {

    void synchConsumerConfiguration(@PathVariable long customerId) throws ServiceException;
}
