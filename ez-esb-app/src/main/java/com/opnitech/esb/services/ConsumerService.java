package com.opnitech.esb.services;

import com.opnitech.esb.client.exception.ServiceException;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public interface ConsumerService {

    void synchConsumerConfiguration(long consumerId) throws ServiceException;
}
