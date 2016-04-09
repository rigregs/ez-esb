package com.opnitech.esb.processor.services.document.impl;

import java.util.HashSet;
import java.util.Set;

import com.opnitech.esb.processor.model.ElasticDocumentMetadata;
import com.opnitech.esb.processor.model.ElasticIndexMetadata;
import com.opnitech.esb.processor.services.document.DocumentIndexerService;
import com.opnitech.esb.processor.services.elastic.ElasticService;
import com.opnitech.esb.processor.utils.CheckSumUtil;
import com.opnitech.esb.processor.utils.JSONUtil;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentIndexerServiceImpl implements DocumentIndexerService {

    private final ElasticService elasticService;

    private final Set<ElasticIndexMetadata> elasticIndexMetadatas = new HashSet<>();

    public DocumentIndexerServiceImpl(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    @Override
    public void updateDocument(ElasticIndexMetadata elasticIndexMetadata, String id, String documentAsJSON) {

        guaranteeIndexExists(elasticIndexMetadata);

        ElasticDocumentMetadata elasticDocumentMetadata = resolveElasticDocumentMetadata(elasticIndexMetadata, id);

        String documentCheckSum = CheckSumUtil.checkSum(documentAsJSON);

        System.out.println(elasticDocumentMetadata);
    }

    private ElasticDocumentMetadata resolveElasticDocumentMetadata(ElasticIndexMetadata elasticIndexMetadata, String id) {

        ElasticDocumentMetadata elasticDocumentMetadata = this.elasticService
                .retrieveElasticDocumentMetadata(elasticIndexMetadata, id);

        if (elasticDocumentMetadata == null) {
            elasticDocumentMetadata = new ElasticDocumentMetadata();
            elasticDocumentMetadata.setDocumentId(id);

            String marshall = JSONUtil.marshall(elasticDocumentMetadata);

            String elasticId = this.elasticService.insertDocument(elasticIndexMetadata, "test", marshall);
            elasticDocumentMetadata.setId(elasticId);
        }
        return elasticDocumentMetadata;
    }

    private void guaranteeIndexExists(ElasticIndexMetadata elasticIndexMetadata) {

        if (!this.elasticIndexMetadatas.contains(elasticIndexMetadata)) {
            this.elasticService.createDocument(elasticIndexMetadata);
            this.elasticIndexMetadatas.add(elasticIndexMetadata);
        }
    }
}
