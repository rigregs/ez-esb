package com.opnitech.esb.processor.services.impl;

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.opnitech.esb.processor.common.ServiceException;
import com.opnitech.esb.processor.persistence.jpa.model.consumer.Consumer;
import com.opnitech.esb.processor.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.processor.persistence.jpa.repository.subscriber.SubscriberRepository;
import com.opnitech.esb.processor.services.ConsumerService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ConsumerServiceImpl implements ConsumerService {

    private final SubscriberRepository subscriberRepository;

    public ConsumerServiceImpl(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public void synchConsumerConfiguration(long customerId) throws ServiceException {

        Consumer consumer = this.subscriberRepository.findOne(customerId);
        Validate.notNull(consumer);

        List<Subscription> subscriptions = consumer.getSubscriptions();
    }
}
