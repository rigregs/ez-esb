package com.opnitech.esb.processor.services.impl;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;

import com.opnitech.esb.processor.persistence.index.ElasticIndexMetadata;
import com.opnitech.esb.processor.persistence.model.command.DocumentCRUDCommand;
import com.opnitech.esb.processor.persistence.model.elastic.ElasticDocumentMetadata;
import com.opnitech.esb.processor.persistence.repository.ElasticIndexMetadataRepository;
import com.opnitech.esb.processor.persistence.repository.queries.DocumentRepository;
import com.opnitech.esb.processor.services.DocumentIndexerService;
import com.opnitech.esb.processor.utils.CheckSumUtil;
import com.opnitech.esb.processor.utils.RouteBuilderUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentIndexerServiceImpl implements DocumentIndexerService {

    private static final int MIN_SEQUECE_LENGHT = 20;

    private final Set<ElasticIndexMetadata> elasticIndexMetadatas = new HashSet<>();

    private final DocumentRepository documentRepository;
    private final ElasticIndexMetadataRepository elasticIndexMetadataRepository;
    private final ProducerTemplate producerTemplate;

    public DocumentIndexerServiceImpl(DocumentRepository documentRepository,
            ElasticIndexMetadataRepository elasticIndexMetadataRepository, ProducerTemplate producerTemplate) {

        this.documentRepository = documentRepository;
        this.elasticIndexMetadataRepository = elasticIndexMetadataRepository;
        this.producerTemplate = producerTemplate;
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

        guaranteeIndexExists(elasticIndexMetadata);

        ElasticDocumentMetadata elasticDocumentMetadata = resolveElasticDocumentMetadata(elasticIndexMetadata,
                documentCRUDCommand.getDocumentId());

        String newDocumentSequence = StringUtils.trimToEmpty(documentCRUDCommand.getSequence());
        String oldDocumentSequence = StringUtils.trimToEmpty(elasticDocumentMetadata.getSequnce());

        if (newDocumentSequence.compareTo(oldDocumentSequence) >= 0) {

            processDocument(documentCRUDCommand, elasticIndexMetadata, elasticDocumentMetadata);
        }
    }

    private void processDocument(DocumentCRUDCommand documentCRUDCommand, ElasticIndexMetadata elasticIndexMetadata,
            ElasticDocumentMetadata elasticDocumentMetadata) {

        elasticDocumentMetadata.setSequnce(documentCRUDCommand.getSequence());

        String documentAsJSON = documentCRUDCommand.getDocumentAsJSON();

        String documentCheckSum = CheckSumUtil.checkSum(documentAsJSON);
        if (!Objects.equals(documentCheckSum, elasticDocumentMetadata.getDocumentCheckSum())) {
            String elasticDocumentId = elasticDocumentMetadata.getElasticDocumentId();
            if (StringUtils.isBlank(elasticDocumentId)) {
                elasticDocumentId = this.documentRepository.insertDocument(elasticIndexMetadata.getIndexName(),
                        elasticIndexMetadata.getDocumentTypeName(), documentAsJSON);
            }
            else {
                this.documentRepository.updateDocument(elasticIndexMetadata.getIndexName(),
                        elasticIndexMetadata.getDocumentTypeName(), elasticDocumentId, documentAsJSON);
            }

            updateElasticDocumentMetadata(elasticIndexMetadata, elasticDocumentMetadata, documentCheckSum, elasticDocumentId);
        }
    }

    private void updateElasticDocumentMetadata(ElasticIndexMetadata elasticIndexMetadata,
            ElasticDocumentMetadata elasticDocumentMetadata, String documentCheckSum, String elasticDocumentId) {

        elasticDocumentMetadata.setElasticDocumentId(elasticDocumentId);
        elasticDocumentMetadata.setDocumentCheckSum(documentCheckSum);

        this.documentRepository.save(elasticIndexMetadata.getIndexName(), elasticIndexMetadata.getMetadataTypeName(),
                elasticDocumentMetadata);
    }

    private ElasticDocumentMetadata resolveElasticDocumentMetadata(ElasticIndexMetadata elasticIndexMetadata, String id) {

        ElasticDocumentMetadata elasticDocumentMetadata = this.elasticIndexMetadataRepository
                .retrieveElasticDocumentMetadata(elasticIndexMetadata, id);

        if (elasticDocumentMetadata == null) {
            elasticDocumentMetadata = new ElasticDocumentMetadata();
            elasticDocumentMetadata.setDocumentId(id);

            elasticDocumentMetadata = this.documentRepository.save(elasticIndexMetadata.getIndexName(),
                    elasticIndexMetadata.getMetadataTypeName(), elasticDocumentMetadata);
        }

        return elasticDocumentMetadata;
    }

    private void guaranteeIndexExists(ElasticIndexMetadata elasticIndexMetadata) {

        if (!this.elasticIndexMetadatas.contains(elasticIndexMetadata)) {
            this.documentRepository.createIndex(elasticIndexMetadata.getIndexName());
            this.elasticIndexMetadatas.add(elasticIndexMetadata);
        }
    }
}
