package com.opnitech.esb.services.impl;

import java.util.Objects;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.client.v1.model.notification.DocumentChangeNotification;
import com.opnitech.esb.client.v1.model.shared.ActionEnum;
import com.opnitech.esb.common.data.ElasticIndexMetadata;
import com.opnitech.esb.persistence.elastic.model.client.DocumentMetadata;
import com.opnitech.esb.persistence.elastic.model.shared.ElasticSourceDocument;
import com.opnitech.esb.persistence.elastic.repository.document.DocumentRepository;
import com.opnitech.esb.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.persistence.jpa.repository.consumer.SubscriptionRepository;
import com.opnitech.esb.persistence.rabbit.DocumentOutboundCommand;
import com.opnitech.esb.services.RoutingService;
import com.opnitech.esb.services.impl.routes.connection.RouteConnection;
import com.opnitech.esb.services.impl.routes.connection.RouteConnectionContainer;

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

        Subscription subscription = this.subscriptionRepository.findOne(documentOutboundCommand.getSubscriptionId());

        if (subscription != null) {
            DocumentMetadata documentMetadata = documentOutboundCommand.getDocumentMetadata();

            ElasticSourceDocument elasticSourceDocument = resolveElasticSourceDocument(documentOutboundCommand, documentMetadata);

            DocumentChangeNotification documentChangeNotification = createDocumentChangeNotification(documentOutboundCommand,
                    documentMetadata, elasticSourceDocument);

            RouteConnection<?> routeConnection = this.routeConnectionContainer.resolveRouteConnection(subscription);
            routeConnection.send(documentChangeNotification);
        }
    }

    private DocumentChangeNotification createDocumentChangeNotification(DocumentOutboundCommand documentOutboundCommand,
            DocumentMetadata documentMetadata, ElasticSourceDocument elasticSourceDocument) {

        DocumentChangeNotification documentChangeNotification = new DocumentChangeNotification(
                documentOutboundCommand.getAction(), documentOutboundCommand.getDocumentType(), documentMetadata.getDocumentId(),
                documentOutboundCommand.getVersion(), documentMetadata.getElasticDocumentId(), elasticSourceDocument != null
                        ? elasticSourceDocument.getObjectAsJSON()
                        : null,
                elasticSourceDocument != null
                        ? elasticSourceDocument.getVersion()
                        : null);

        return documentChangeNotification;
    }

    private ElasticSourceDocument resolveElasticSourceDocument(DocumentOutboundCommand documentOutboundCommand,
            DocumentMetadata documentMetadata) {

        ElasticSourceDocument elasticSourceDocument = !Objects.equals(ActionEnum.DELETE, documentOutboundCommand.getAction())
                ? this.documentRepository.retrieveDocument(
                        new ElasticIndexMetadata(documentOutboundCommand.getVersion(), documentOutboundCommand.getDocumentType()),
                        documentMetadata.getElasticDocumentId())
                : null;
        return elasticSourceDocument;
    }
}
