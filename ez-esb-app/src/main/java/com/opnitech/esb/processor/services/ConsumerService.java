package com.opnitech.esb.processor.services;

import com.opnitech.esb.processor.common.exception.ServiceException;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface ConsumerService {

    void synchConsumerConfiguration(long consumerId) throws ServiceException;
}
