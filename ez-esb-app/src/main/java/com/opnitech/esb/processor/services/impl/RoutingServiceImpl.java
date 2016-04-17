package com.opnitech.esb.processor.services.impl;

import com.opnitech.esb.processor.common.data.ElasticIndexMetadata;
import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.persistence.elastic.model.client.DocumentChangeNotification;
import com.opnitech.esb.processor.persistence.elastic.model.client.DocumentMetadata;
import com.opnitech.esb.processor.persistence.elastic.model.shared.ElasticSourceDocument;
import com.opnitech.esb.processor.persistence.elastic.repository.document.DocumentRepository;
import com.opnitech.esb.processor.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.processor.persistence.jpa.repository.subscriber.SubscriptionRepository;
import com.opnitech.esb.processor.persistence.rabbit.DocumentOutboundCommand;
import com.opnitech.esb.processor.services.RoutingService;
import com.opnitech.esb.processor.services.impl.routes.connection.RouteConnection;
import com.opnitech.esb.processor.services.impl.routes.connection.RouteConnectionContainer;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class RoutingServiceImpl implements RoutingService {

    private final SubscriptionRepository subscriptionRepository;
    private final DocumentRepository documentRepository;
    private final RouteConnectionContainer routeConnectionContainer;

    public RoutingServiceImpl(SubscriptionRepository subscriptionRepository, DocumentRepository documentRepository,
            RouteConnectionContainer routeConnectionContainer) {

        this.subscriptionRepository = subscriptionRepository;
        this.documentRepository = documentRepository;
        this.routeConnectionContainer = routeConnectionContainer;
    }

    @Override
    public void route(DocumentOutboundCommand documentOutboundCommand) throws ServiceException {

        Subscription subscription = this.subscriptionRepository
                .findSubscriptionOwnMatchQuery(documentOutboundCommand.getMatchQueryId());

        if (subscription != null) {
            DocumentMetadata documentMetadata = documentOutboundCommand.getDocumentMetadata();

            ElasticSourceDocument elasticSourceDocument = this.documentRepository.retrieveDocument(
                    new ElasticIndexMetadata(documentOutboundCommand.getVersion(), documentOutboundCommand.getDocumentType()),
                    documentMetadata.getElasticDocumentId());

            RouteConnection<?> routeConnection = this.routeConnectionContainer.resolveRouteConnection(subscription);

            DocumentChangeNotification documentChangeNotification = new DocumentChangeNotification(
                    documentOutboundCommand.getAction(), documentOutboundCommand.getDocumentType(),
                    documentMetadata.getDocumentId(), documentOutboundCommand.getVersion(),
                    documentMetadata.getElasticDocumentId(), documentMetadata.getSequence(),
                    documentMetadata.getDocumentCheckSum(), elasticSourceDocument.getObjectAsJSON(),
                    elasticSourceDocument.getVersion());

            String documentChangeNotificationAsJSON = JSONUtil.marshall(documentChangeNotification);

            routeConnection.send(documentChangeNotificationAsJSON);
        }
    }
}
