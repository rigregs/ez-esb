package com.opnitech.esb.processor.services.impl;

import java.text.MessageFormat;
import java.util.Objects;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;

import com.opnitech.esb.processor.common.data.ElasticIndexMetadata;
import com.opnitech.esb.processor.persistence.elastic.model.command.DocumentCRUDCommand;
import com.opnitech.esb.processor.persistence.elastic.model.document.DocumentMetadata;
import com.opnitech.esb.processor.persistence.elastic.repository.document.DocumentMetadataRepository;
import com.opnitech.esb.processor.persistence.elastic.repository.document.DocumentRepository;
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

    public DocumentIndexerServiceImpl(DocumentRepository documentRepository,
            DocumentMetadataRepository elasticIndexMetadataRepository, ProducerTemplate producerTemplate,
            IndexMetadataCache indexMetadataCache) {

        this.documentRepository = documentRepository;
        this.elasticIndexMetadataRepository = elasticIndexMetadataRepository;
        this.producerTemplate = producerTemplate;
        this.indexMetadataCache = indexMetadataCache;
    }

    @Override
    public void queueUpdateDocument(String version, String documentType, String documentId, String documentAsJSON) {

        DocumentCRUDCommand documentCRUDCommand = new DocumentCRUDCommand();
        documentCRUDCommand.setDocumentId(documentId);
        documentCRUDCommand.setVersion(version);
        documentCRUDCommand.setDocumentType(documentType);
        documentCRUDCommand.setDocumentAsJSON(documentAsJSON);

        updateSequence(documentCRUDCommand);

        this.producerTemplate.sendBody(RouteBuilderUtil.fromDirect("inboundSend"), documentCRUDCommand);
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
    public void updateDocument(DocumentCRUDCommand documentCRUDCommand) {

        updateSequence(documentCRUDCommand);

        ElasticIndexMetadata elasticIndexMetadata = new ElasticIndexMetadata(documentCRUDCommand.getVersion(),
                documentCRUDCommand.getDocumentType());

        this.indexMetadataCache.guaranteeIndexExists(elasticIndexMetadata);

        DocumentMetadata elasticDocumentMetadata = resolveElasticDocumentMetadata(elasticIndexMetadata,
                documentCRUDCommand.getDocumentId());

        String newDocumentSequence = StringUtils.trimToEmpty(documentCRUDCommand.getSequence());
        String oldDocumentSequence = StringUtils.trimToEmpty(elasticDocumentMetadata.getSequnce());

        if (newDocumentSequence.compareTo(oldDocumentSequence) >= 0) {

            processDocument(documentCRUDCommand, elasticIndexMetadata, elasticDocumentMetadata);
        }
    }

    private void processDocument(DocumentCRUDCommand documentCRUDCommand, ElasticIndexMetadata elasticIndexMetadata,
            DocumentMetadata elasticDocumentMetadata) {

        elasticDocumentMetadata.setSequnce(documentCRUDCommand.getSequence());

        String documentAsJSON = documentCRUDCommand.getDocumentAsJSON();

        String documentCheckSum = CheckSumUtil.checkSum(documentAsJSON);
        if (!Objects.equals(documentCheckSum, elasticDocumentMetadata.getDocumentCheckSum())) {

            String elasticDocumentId = this.documentRepository.save(elasticIndexMetadata.getIndexName(),
                    elasticIndexMetadata.getDocumentTypeName(), elasticDocumentMetadata.getElasticDocumentId(), documentAsJSON);

            updateElasticDocumentMetadata(elasticIndexMetadata, elasticDocumentMetadata, documentCheckSum, elasticDocumentId);
        }
    }

    private void updateElasticDocumentMetadata(ElasticIndexMetadata elasticIndexMetadata,
            DocumentMetadata elasticDocumentMetadata, String documentCheckSum, String elasticDocumentId) {

        elasticDocumentMetadata.setElasticDocumentId(elasticDocumentId);
        elasticDocumentMetadata.setDocumentCheckSum(documentCheckSum);

        this.documentRepository.save(elasticIndexMetadata.getIndexName(), elasticIndexMetadata.getMetadataTypeName(),
                elasticDocumentMetadata);
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
