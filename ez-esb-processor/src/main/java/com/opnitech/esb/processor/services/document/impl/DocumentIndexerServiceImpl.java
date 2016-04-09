package com.opnitech.esb.processor.services.document.impl;

import com.opnitech.esb.processor.services.document.DocumentIndexerService;
import com.opnitech.esb.processor.services.elastic.ElasticService;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class DocumentIndexerServiceImpl implements DocumentIndexerService {

    private final ElasticService elasticService;

    public DocumentIndexerServiceImpl(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    @Override
    public void updateDocument(String version, String document, String id, String documentAsJSON) {

        // TODO Auto-generated method stub
        
    }
}
