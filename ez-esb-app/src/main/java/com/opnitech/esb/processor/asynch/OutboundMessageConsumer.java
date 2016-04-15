package com.opnitech.esb.processor.asynch;

import org.apache.camel.Handler;

import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.persistence.rabbit.DocumentOutboundCommand;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class OutboundMessageConsumer {

    public OutboundMessageConsumer() {
    }

    @Handler
    public void consume(DocumentOutboundCommand documentOutboundCommand) throws ServiceException {

        System.out.println(JSONUtil.marshall(documentOutboundCommand));
    }
}
