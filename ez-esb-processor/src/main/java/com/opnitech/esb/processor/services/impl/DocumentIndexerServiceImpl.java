package com.opnitech.esb.processor.services.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.opnitech.esb.processor.persistence.index.ElasticIndexMetadata;
import com.opnitech.esb.processor.persistence.model.ElasticDocumentMetadata;
import com.opnitech.esb.processor.persistence.repository.ElasticIndexMetadataRepository;
import com.opnitech.esb.processor.persistence.repository.queries.DocumentRepository;
import com.opnitech.esb.processor.services.DocumentIndexerService;
import com.opnitech.esb.processor.utils.CheckSumUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentIndexerServiceImpl implements DocumentIndexerService {

    private final Set<ElasticIndexMetadata> elasticIndexMetadatas = new HashSet<>();

    private final DocumentRepository documentRepository;
    private final ElasticIndexMetadataRepository elasticIndexMetadataRepository;

    public DocumentIndexerServiceImpl(DocumentRepository documentRepository,
            ElasticIndexMetadataRepository elasticIndexMetadataRepository) {

        this.documentRepository = documentRepository;
        this.elasticIndexMetadataRepository = elasticIndexMetadataRepository;
    }

    @Override
    public void updateDocument(ElasticIndexMetadata elasticIndexMetadata, String id, String documentAsJSON) {

        guaranteeIndexExists(elasticIndexMetadata);

        ElasticDocumentMetadata elasticDocumentMetadata = resolveElasticDocumentMetadata(elasticIndexMetadata, id);

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
