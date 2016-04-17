package com.opnitech.esb.processor.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import com.opnitech.esb.processor.common.data.ElasticIndexMetadata;
import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.configuration.route.RouteConfiguration;
import com.opnitech.esb.processor.persistence.elastic.repository.document.DocumentRepository;
import com.opnitech.esb.processor.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.processor.persistence.jpa.repository.subscriber.SubscriptionRepository;
import com.opnitech.esb.processor.persistence.rabbit.DocumentOutboundCommand;
import com.opnitech.esb.processor.services.RoutingService;
import com.opnitech.esb.processor.services.impl.routes.RouteExecuter;
import com.opnitech.esb.processor.services.impl.routes.connection.RouteConnection;
import com.opnitech.esb.processor.services.impl.routes.connection.RouteConnectionContainer;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RoutingServiceImpl implements RoutingService {

    private final List<RouteExecuter<? extends RouteConfiguration>> routeExecuters;
    private final SubscriptionRepository subscriptionRepository;
    private final DocumentRepository documentRepository;
    private final RouteConnectionContainer routeConnectionContainer;

    public RoutingServiceImpl(List<RouteExecuter<? extends RouteConfiguration>> routeExecuters,
            SubscriptionRepository subscriptionRepository, DocumentRepository documentRepository,
            RouteConnectionContainer routeConnectionContainer) {

        this.subscriptionRepository = subscriptionRepository;
        this.documentRepository = documentRepository;
        this.routeConnectionContainer = routeConnectionContainer;
        Validate.isTrue(CollectionUtils.isNotEmpty(routeExecuters));

        this.routeExecuters = routeExecuters;
    }

    @Override
    public void route(DocumentOutboundCommand documentOutboundCommand) throws ServiceException {

        Subscription subscription = this.subscriptionRepository
                .findSubscriptionOwnMatchQuery(documentOutboundCommand.getMatchQueryId());

        if (subscription != null) {

            String documentAsJSON = this.documentRepository.retrieveDocument(
                    new ElasticIndexMetadata(documentOutboundCommand.getVersion(), documentOutboundCommand.getDocumentType()),
                    documentOutboundCommand.getDocumentMetadata().getElasticDocumentId());

            RouteConnection<?> routeConnection = this.routeConnectionContainer.resolveRouteConnection(subscription);

            routeConnection.send(documentAsJSON);
        }
    }

    public void route(RouteConfiguration routeConfiguration, Object payload) {

        Validate.notNull(payload);

        String payloadAsJSON = payload instanceof String
                ? payload.toString()
                : JSONUtil.marshall(payload);

        RouteExecuter<? extends RouteConfiguration> routeExecuter = findRouteExecuter(routeConfiguration);
        Validate.notNull(routeExecuter);

        routeExecuter.doExecuteRoute(routeConfiguration, payloadAsJSON);
    }

    private RouteExecuter<? extends RouteConfiguration> findRouteExecuter(RouteConfiguration routeConfiguration) {

        for (RouteExecuter<? extends RouteConfiguration> routeExecuter : this.routeExecuters) {
            if (routeExecuter.acceptRouteConfiguration(routeConfiguration)) {
                return routeExecuter;
            }
        }

        return null;
    }
}
