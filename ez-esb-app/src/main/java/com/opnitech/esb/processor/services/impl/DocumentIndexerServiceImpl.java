package com.opnitech.esb.processor.services.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;

import com.opnitech.esb.processor.common.data.ElasticIndexMetadata;
import com.opnitech.esb.processor.common.exception.ServiceException;
import com.opnitech.esb.processor.persistence.elastic.model.client.DocumentMetadata;
import com.opnitech.esb.processor.persistence.elastic.repository.document.DocumentMetadataRepository;
import com.opnitech.esb.processor.persistence.elastic.repository.document.DocumentRepository;
import com.opnitech.esb.processor.persistence.elastic.repository.document.PercolatorRepository;
import com.opnitech.esb.processor.persistence.rabbit.DocumentCRUDCommand;
import com.opnitech.esb.processor.persistence.rabbit.DocumentOutboundCommand;
import com.opnitech.esb.processor.services.DocumentIndexerService;
import com.opnitech.esb.processor.services.cache.IndexMetadataCache;
import com.opnitech.esb.processor.utils.CheckSumUtil;
import com.opnitech.esb.processor.utils.RouteBuilderUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentIndexerServiceImpl implements DocumentIndexerService {

    private static final int MIN_SEQUECE_LENGHT = 20;

    private final DocumentRepository documentRepository;
    private final DocumentMetadataRepository elasticIndexMetadataRepository;
    private final ProducerTemplate producerTemplate;
    private final IndexMetadataCache indexMetadataCache;
    private final PercolatorRepository percolatorRepository;

    public DocumentIndexerServiceImpl(DocumentRepository documentRepository,
            DocumentMetadataRepository elasticIndexMetadataRepository, ProducerTemplate producerTemplate,
            IndexMetadataCache indexMetadataCache, PercolatorRepository percolatorRepository) {

        this.documentRepository = documentRepository;
        this.elasticIndexMetadataRepository = elasticIndexMetadataRepository;
        this.producerTemplate = producerTemplate;
        this.indexMetadataCache = indexMetadataCache;
        this.percolatorRepository = percolatorRepository;
    }

    @Override
    public void queueUpdateDocument(String version, String documentType, String documentId, String documentAsJSON) {

        DocumentCRUDCommand documentCRUDCommand = new DocumentCRUDCommand();
        documentCRUDCommand.setDocumentId(documentId);
        documentCRUDCommand.setVersion(version);
        documentCRUDCommand.setDocumentType(documentType);
        documentCRUDCommand.setDocumentAsJSON(documentAsJSON);

        updateSequence(documentCRUDCommand);

        this.producerTemplate.sendBody(RouteBuilderUtil.fromDirect(INBOUND_SEND_CAMEL_ROUTE), documentCRUDCommand);
    }

    private void updateSequence(DocumentCRUDCommand documentCRUDCommand) {

        if (StringUtils.isBlank(documentCRUDCommand.getSequence())) {

            String sequenceMillis = StringUtils.leftPad(Long.toString(System.currentTimeMillis()), MIN_SEQUECE_LENGHT, "0");
            String sequenceNano = StringUtils.leftPad(Long.toString(System.nanoTime()), MIN_SEQUECE_LENGHT, "0");

            String sequence = MessageFormat.format("{0}-{1}", sequenceMillis, sequenceNano);

            documentCRUDCommand.setSequence(sequence);
        }
    }

    @Override
    public void updateDocument(DocumentCRUDCommand documentCRUDCommand) throws ServiceException {

        updateSequence(documentCRUDCommand);

        ElasticIndexMetadata elasticIndexMetadata = new ElasticIndexMetadata(documentCRUDCommand.getVersion(),
                documentCRUDCommand.getDocumentType());

        this.indexMetadataCache.guaranteeIndexExists(elasticIndexMetadata);

        DocumentMetadata documentMetadata = resolveElasticDocumentMetadata(elasticIndexMetadata,
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

        for (Long matchId : matchIds) {
            DocumentOutboundCommand documentOutboundCommand = new DocumentOutboundCommand();
            documentOutboundCommand.setAction(documentCRUDCommand.getAction());
            documentOutboundCommand.setDocumentType(elasticIndexMetadata.getDocumentType());
            documentOutboundCommand.setVersion(elasticIndexMetadata.getVersion());

            documentOutboundCommand.setDocumentMetadata(documentMetadata);

            documentOutboundCommand.setMatchQueryId(matchId);

            this.producerTemplate.sendBody(RouteBuilderUtil.fromDirect(OUTBOUND_SEND_CAMEL_ROUTE), documentOutboundCommand);
        }
    }

    private void processDocument(DocumentCRUDCommand documentCRUDCommand, ElasticIndexMetadata elasticIndexMetadata,
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

    private DocumentMetadata resolveElasticDocumentMetadata(ElasticIndexMetadata elasticIndexMetadata, String id) {

        DocumentMetadata elasticDocumentMetadata = this.elasticIndexMetadataRepository
                .retrieveElasticDocumentMetadata(elasticIndexMetadata, id);

        if (elasticDocumentMetadata == null) {
            elasticDocumentMetadata = new DocumentMetadata();
            elasticDocumentMetadata.setDocumentId(id);

            elasticDocumentMetadata = this.documentRepository.save(elasticIndexMetadata.getIndexName(),
                    elasticIndexMetadata.getMetadataTypeName(), elasticDocumentMetadata);
        }

        return elasticDocumentMetadata;
    }
}
