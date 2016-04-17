package com.opnitech.esb.services.impl;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.client.v1.model.inbound.DocumentCRUDCommand;
import com.opnitech.esb.client.v1.model.shared.ActionEnum;
import com.opnitech.esb.common.data.ElasticIndexMetadata;
import com.opnitech.esb.persistence.elastic.model.client.DocumentMetadata;
import com.opnitech.esb.persistence.elastic.repository.document.DocumentMetadataRepository;
import com.opnitech.esb.persistence.elastic.repository.document.DocumentRepository;
import com.opnitech.esb.persistence.elastic.repository.document.PercolatorRepository;
import com.opnitech.esb.persistence.jpa.model.consumer.MatchQuery;
import com.opnitech.esb.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.persistence.jpa.repository.consumer.SubscriptionRepository;
import com.opnitech.esb.persistence.rabbit.DocumentOutboundCommand;
import com.opnitech.esb.services.DocumentIndexerService;
import com.opnitech.esb.services.cache.IndexMetadataCache;
import com.opnitech.esb.utils.CheckSumUtil;
import com.opnitech.esb.utils.RouteBuilderUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@Transactional
public class DocumentIndexerServiceImpl implements DocumentIndexerService {

    private static final int MIN_SEQUECE_LENGHT = 20;

    private final DocumentRepository documentRepository;
    private final DocumentMetadataRepository documentMetadataRepository;
    private final ProducerTemplate producerTemplate;
    private final IndexMetadataCache indexMetadataCache;
    private final PercolatorRepository percolatorRepository;
    private final SubscriptionRepository subscriptionRepository;

    public DocumentIndexerServiceImpl(DocumentRepository documentRepository,
            DocumentMetadataRepository documentMetadataRepository, ProducerTemplate producerTemplate,
            IndexMetadataCache indexMetadataCache, PercolatorRepository percolatorRepository,
            SubscriptionRepository subscriptionRepository) {

        this.documentRepository = documentRepository;
        this.documentMetadataRepository = documentMetadataRepository;
        this.producerTemplate = producerTemplate;
        this.indexMetadataCache = indexMetadataCache;
        this.percolatorRepository = percolatorRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void queueUpdateDocument(String version, String documentType, String documentId, String documentAsJSON) {

        ElasticIndexMetadata elasticIndexMetadata = new ElasticIndexMetadata(version, documentType);

        DocumentMetadata documentMetadata = resolveElasticDocumentMetadata(false, elasticIndexMetadata, documentId);

        DocumentCRUDCommand documentCRUDCommand = new DocumentCRUDCommand();
        documentCRUDCommand.setAction(documentMetadata != null
                ? ActionEnum.UPDATE
                : ActionEnum.INSERT);
        documentCRUDCommand.setDocumentId(documentId);
        documentCRUDCommand.setVersion(version);
        documentCRUDCommand.setDocumentType(documentType);
        documentCRUDCommand.setDocumentAsJSON(documentAsJSON);

        updateSequenceIfNeeded(documentCRUDCommand);

        this.producerTemplate.sendBody(RouteBuilderUtil.fromDirect(INBOUND_SEND_CAMEL_ROUTE), documentCRUDCommand);
    }

    @Override
    public void queueDeleteDocument(String version, String documentType, String documentId) throws ServiceException {

        DocumentCRUDCommand documentCRUDCommand = new DocumentCRUDCommand();
        documentCRUDCommand.setAction(ActionEnum.DELETE);
        documentCRUDCommand.setDocumentId(documentId);
        documentCRUDCommand.setVersion(version);
        documentCRUDCommand.setDocumentType(documentType);

        updateSequenceIfNeeded(documentCRUDCommand);

        this.producerTemplate.sendBody(RouteBuilderUtil.fromDirect(INBOUND_SEND_CAMEL_ROUTE), documentCRUDCommand);
    }

    private void updateSequenceIfNeeded(DocumentCRUDCommand documentCRUDCommand) {

        if (StringUtils.isBlank(documentCRUDCommand.getSequence())) {

            String sequenceMillis = StringUtils.leftPad(Long.toString(System.currentTimeMillis()), MIN_SEQUECE_LENGHT, "0");
            String sequenceNano = StringUtils.leftPad(Long.toString(System.nanoTime()), MIN_SEQUECE_LENGHT, "0");

            String sequence = MessageFormat.format("{0}-{1}", sequenceMillis, sequenceNano);

            documentCRUDCommand.setSequence(sequence);
        }
    }

    @Override
    public void processDocumentCommand(DocumentCRUDCommand documentCRUDCommand) throws ServiceException {

        updateSequenceIfNeeded(documentCRUDCommand);

        ElasticIndexMetadata elasticIndexMetadata = new ElasticIndexMetadata(documentCRUDCommand.getVersion(),
                documentCRUDCommand.getDocumentType());

        this.indexMetadataCache.guaranteeIndexExists(elasticIndexMetadata);

        DocumentMetadata documentMetadata = resolveElasticDocumentMetadata(true, elasticIndexMetadata,
                documentCRUDCommand.getDocumentId());

        String newDocumentSequence = StringUtils.trimToEmpty(documentCRUDCommand.getSequence());
        String oldDocumentSequence = StringUtils.trimToEmpty(documentMetadata.getSequence());

        if (newDocumentSequence.compareTo(oldDocumentSequence) >= 0) {

            processDocument(documentCRUDCommand, elasticIndexMetadata, documentMetadata);
        }
    }

    private void notifyConsumers(DocumentCRUDCommand documentCRUDCommand, ElasticIndexMetadata elasticIndexMetadata,
            DocumentMetadata documentMetadata) throws ServiceException {

        List<Long> matchIds = this.percolatorRepository.evaluatePercolator(elasticIndexMetadata.getIndexName(),
                elasticIndexMetadata.getDocumentType(), documentCRUDCommand.getDocumentAsJSON());

        Set<Long> processedMatchIdSubcription = new HashSet<>();

        for (Long matchId : matchIds) {
            if (!processedMatchIdSubcription.contains(matchId)) {

                Subscription subscription = this.subscriptionRepository.findSubscriptionOwnMatchQuery(matchId);

                if (subscription != null) {
                    registerProcessedMatchIdSubcription(processedMatchIdSubcription, subscription);
                    sendNotification(documentCRUDCommand, elasticIndexMetadata, documentMetadata, subscription.getId());
                }
            }
        }
    }

    private void registerProcessedMatchIdSubcription(Set<Long> processedMatchIdSubcription, Subscription subscription) {

        List<MatchQuery> matchQueries = subscription.getMatchQueries();
        for (MatchQuery matchQuery : matchQueries) {
            processedMatchIdSubcription.add(matchQuery.getId());
        }
    }

    private void sendNotification(DocumentCRUDCommand documentCRUDCommand, ElasticIndexMetadata elasticIndexMetadata,
            DocumentMetadata documentMetadata, long subscriptionId) {

        DocumentOutboundCommand documentOutboundCommand = new DocumentOutboundCommand();
        documentOutboundCommand.setAction(documentCRUDCommand.getAction());
        documentOutboundCommand.setDocumentType(elasticIndexMetadata.getDocumentType());
        documentOutboundCommand.setVersion(elasticIndexMetadata.getVersion());

        documentOutboundCommand.setDocumentMetadata(documentMetadata);

        documentOutboundCommand.setSubscriptionId(subscriptionId);

        this.producerTemplate.sendBody(RouteBuilderUtil.fromDirect(OUTBOUND_SEND_CAMEL_ROUTE), documentOutboundCommand);
    }

    private void processDocument(DocumentCRUDCommand documentCRUDCommand, ElasticIndexMetadata elasticIndexMetadata,
            DocumentMetadata documentMetadata) throws ServiceException {

        if (Objects.equals(ActionEnum.INSERT, documentCRUDCommand.getAction())
                || Objects.equals(ActionEnum.UPDATE, documentCRUDCommand.getAction())) {

            updateOrInsertDocument(documentCRUDCommand, elasticIndexMetadata, documentMetadata);
        }
        else if (Objects.equals(ActionEnum.DELETE, documentCRUDCommand.getAction())) {
            deleteDocument(documentCRUDCommand, elasticIndexMetadata, documentMetadata);
        }
        else {
            throw new ServiceException("Invalid CRUD action");
        }
    }

    private void deleteDocument(DocumentCRUDCommand documentCRUDCommand, ElasticIndexMetadata elasticIndexMetadata,
            DocumentMetadata documentMetadata) throws ServiceException {

        if (documentMetadata != null) {
            this.documentRepository.deleteElasticDocumentMetadata(elasticIndexMetadata, documentMetadata);
            this.documentMetadataRepository.deleteElasticDocumentMetadata(elasticIndexMetadata, documentMetadata);

            notifyConsumers(documentCRUDCommand, elasticIndexMetadata, documentMetadata);
        }

    }

    private void updateOrInsertDocument(DocumentCRUDCommand documentCRUDCommand, ElasticIndexMetadata elasticIndexMetadata,
            DocumentMetadata documentMetadata) throws ServiceException {

        documentMetadata.setSequence(documentCRUDCommand.getSequence());

        String documentAsJSON = documentCRUDCommand.getDocumentAsJSON();

        String documentCheckSum = CheckSumUtil.checkSum(documentAsJSON);
        if (!Objects.equals(documentCheckSum, documentMetadata.getDocumentCheckSum())) {

            String elasticDocumentId = this.documentRepository.save(elasticIndexMetadata.getIndexName(),
                    elasticIndexMetadata.getDocumentTypeName(), documentMetadata.getElasticDocumentId(), documentAsJSON);

            updateElasticDocumentMetadata(elasticIndexMetadata, documentMetadata, documentCheckSum, elasticDocumentId);
            notifyConsumers(documentCRUDCommand, elasticIndexMetadata, documentMetadata);
        }
    }

    private void updateElasticDocumentMetadata(ElasticIndexMetadata elasticIndexMetadata, DocumentMetadata documentMetadata,
            String documentCheckSum, String elasticDocumentId) {

        documentMetadata.setElasticDocumentId(elasticDocumentId);
        documentMetadata.setDocumentCheckSum(documentCheckSum);

        this.documentRepository.save(elasticIndexMetadata.getIndexName(), elasticIndexMetadata.getMetadataTypeName(),
                documentMetadata);
    }

    private DocumentMetadata resolveElasticDocumentMetadata(boolean autoCreate, ElasticIndexMetadata elasticIndexMetadata,
            String id) {

        DocumentMetadata elasticDocumentMetadata = this.documentMetadataRepository
                .retrieveElasticDocumentMetadata(elasticIndexMetadata, id);

        if (autoCreate && elasticDocumentMetadata == null) {
            elasticDocumentMetadata = new DocumentMetadata();
            elasticDocumentMetadata.setDocumentId(id);

            elasticDocumentMetadata = this.documentRepository.save(elasticIndexMetadata.getIndexName(),
                    elasticIndexMetadata.getMetadataTypeName(), elasticDocumentMetadata);
        }

        return elasticDocumentMetadata;
    }
}
