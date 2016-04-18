package com.opnitech.esb.services.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.opnitech.esb.client.exception.ServiceException;
import com.opnitech.esb.common.data.ElasticIndexMetadata;
import com.opnitech.esb.persistence.elastic.model.document.PercolatorMetadata;
import com.opnitech.esb.persistence.elastic.model.document.PercolatorOwner;
import com.opnitech.esb.persistence.elastic.repository.document.PercolatorRepository;
import com.opnitech.esb.persistence.jpa.model.consumer.Consumer;
import com.opnitech.esb.persistence.jpa.model.consumer.MatchQuery;
import com.opnitech.esb.persistence.jpa.model.consumer.Subscription;
import com.opnitech.esb.persistence.jpa.repository.consumer.ConsumerRepository;
import com.opnitech.esb.persistence.jpa.repository.consumer.SubscriptionRepository;
import com.opnitech.esb.services.ConsumerService;
import com.opnitech.esb.services.cache.IndexMetadataCache;
import com.opnitech.esb.services.impl.routes.connection.RouteConnectionContainer;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
public class ConsumerServiceImpl implements ConsumerService {

    private static final String MATCH_ALL_PERCOLATOR = "{\"match_all\": {}}";

    private final ConsumerRepository consumerRepository;
    private final PercolatorRepository percolatorMetadataRepository;
    private final IndexMetadataCache indexMetadataCache;
    private final SubscriptionRepository subscriptionRepository;
    private final RouteConnectionContainer routeConnectionContainer;

    public ConsumerServiceImpl(ConsumerRepository consumerRepository, PercolatorRepository percolatorMetadataRepository,
            IndexMetadataCache indexMetadataCache, SubscriptionRepository subscriptionRepository,
            RouteConnectionContainer routeConnectionContainer) {

        this.consumerRepository = consumerRepository;
        this.percolatorMetadataRepository = percolatorMetadataRepository;
        this.indexMetadataCache = indexMetadataCache;
        this.subscriptionRepository = subscriptionRepository;
        this.routeConnectionContainer = routeConnectionContainer;
    }

    @Override
    public void synchConsumerConfiguration(long consumerId) throws ServiceException {

        Consumer consumer = this.consumerRepository.findOne(consumerId);
        Validate.notNull(consumer);

        synchConsumerConfiguration(consumer);
    }

    private void synchConsumerConfiguration(Consumer consumer) throws ServiceException {

        List<Subscription> subscriptions = consumer.getSubscriptions();
        if (CollectionUtils.isNotEmpty(subscriptions)) {
            for (Subscription subscription : subscriptions) {
                processSubscription(consumer, subscription);
            }
        }
    }

    private void processSubscription(Consumer consumer, Subscription subscription) throws ServiceException {

        ElasticIndexMetadata elasticIndexMetadata = new ElasticIndexMetadata(subscription.getDocumentVersion(),
                subscription.getDocumentType());

        this.indexMetadataCache.guaranteeIndexExists(elasticIndexMetadata);

        Map<Long, PercolatorInfo> percolatorInfoMap = createPercolatorInfoMap(consumer, subscription, elasticIndexMetadata);

        Collection<PercolatorInfo> percolatorInfos = percolatorInfoMap.values();
        for (PercolatorInfo percolatorInfo : percolatorInfos) {
            if (percolatorInfo.needPercolationDeletion()) {
                deletePercolator(percolatorInfo);
            }
            else {
                updatePercolator(elasticIndexMetadata, percolatorInfo);
            }
        }

        updateSubscriptionGenerationCount(subscription);
    }

    private void updateSubscriptionGenerationCount(Subscription subscription) throws ServiceException {

        // Force update the version to release and recreate the router
        subscription.increaseGenerationCount();
        this.subscriptionRepository.save(subscription);

        this.routeConnectionContainer.closeRouteConnection(subscription);
    }

    private void updatePercolator(ElasticIndexMetadata elasticIndexMetadata, PercolatorInfo percolatorInfo) {

        PercolatorMetadata percolatorMetadata = percolatorInfo.getPercolatorMetadata();
        if (percolatorMetadata == null) {
            percolatorMetadata = createPercolatorMetatada(percolatorInfo);
            percolatorInfo.setPercolatorMetadata(percolatorMetadata);
        }

        this.percolatorMetadataRepository.save(elasticIndexMetadata.getIndexName(),
                elasticIndexMetadata.getPercolatorMetadataTypeName(), percolatorMetadata);

        updatePercolatorQuery(elasticIndexMetadata, percolatorInfo);
    }

    private void updatePercolatorQuery(ElasticIndexMetadata elasticIndexMetadata, PercolatorInfo percolatorInfo) {

        String percolatorId = Long.toString(percolatorInfo.getMatchQuery().getId());

        String query = percolatorInfo.getMatchQuery().getQuery();
        if (StringUtils.isBlank(query)) {
            query = MATCH_ALL_PERCOLATOR;
        }

        PercolatorOwner percolatorOwner = new PercolatorOwner(percolatorInfo.getConsumer().getId(),
                percolatorInfo.getSubscription().getId(), percolatorInfo.getMatchQuery().getId());

        this.percolatorMetadataRepository.savePercolator(elasticIndexMetadata.getIndexName(), percolatorId, percolatorOwner,
                query);
    }

    private PercolatorMetadata createPercolatorMetatada(PercolatorInfo percolatorInfo) {

        MatchQuery matchQuery = percolatorInfo.getMatchQuery();

        PercolatorMetadata percolatorMetadata = new PercolatorMetadata();
        percolatorMetadata.setConsumerId(matchQuery.getSubscription().getConsumer().getId());
        percolatorMetadata.setPercolatorId(matchQuery.getId());

        return percolatorMetadata;
    }

    private void deletePercolator(PercolatorInfo percolatorInfo) {

        // TODO Auto-generated method stub

    }

    private Map<Long, PercolatorInfo> createPercolatorInfoMap(Consumer consumer, Subscription subscription,
            ElasticIndexMetadata elasticIndexMetadata) {

        Map<Long, PercolatorInfo> percolatorInfoMap = new HashMap<>();

        populatePercolatorInfoFromMatchQuery(subscription, percolatorInfoMap);
        populatePercolatorInfoFromPercolatorMetadata(consumer, elasticIndexMetadata, percolatorInfoMap);

        return percolatorInfoMap;
    }

    private void populatePercolatorInfoFromPercolatorMetadata(Consumer consumer, ElasticIndexMetadata elasticIndexMetadata,
            Map<Long, PercolatorInfo> percolatorInfoMap) {

        Map<Long, PercolatorMetadata> percolatorMetadataMap = createPercolatorMetadataMap(consumer, elasticIndexMetadata);

        Set<Entry<Long, PercolatorMetadata>> entrySet = percolatorMetadataMap.entrySet();
        for (Entry<Long, PercolatorMetadata> entry : entrySet) {
            Long matchQueryId = entry.getKey();

            PercolatorInfo percolatorInfo = resolvePercolatorInfo(percolatorInfoMap, matchQueryId);

            percolatorInfo.setPercolatorMetadata(entry.getValue());
        }
    }

    private Map<Long, PercolatorMetadata> createPercolatorMetadataMap(Consumer consumer,
            ElasticIndexMetadata elasticIndexMetadata) {

        Map<Long, PercolatorMetadata> percolatorMetadataMap = new HashMap<>();
        List<PercolatorMetadata> percolatorMetadatas = this.percolatorMetadataRepository
                .retrievePercolatorMetadatas(elasticIndexMetadata, consumer.getId());

        for (PercolatorMetadata percolatorMetadata : percolatorMetadatas) {
            percolatorMetadataMap.put(percolatorMetadata.getPercolatorId(), percolatorMetadata);
        }

        return percolatorMetadataMap;
    }

    private void populatePercolatorInfoFromMatchQuery(Subscription subscription, Map<Long, PercolatorInfo> percolatorInfoMap) {

        Map<Long, MatchQuery> matchQueryMap = createMatchQueryMap(subscription);
        Set<Entry<Long, MatchQuery>> entrySet = matchQueryMap.entrySet();
        for (Entry<Long, MatchQuery> entry : entrySet) {
            Long matchQueryId = entry.getKey();

            PercolatorInfo percolatorInfo = resolvePercolatorInfo(percolatorInfoMap, matchQueryId);

            MatchQuery matchQuery = entry.getValue();
            percolatorInfo.setMatchQuery(matchQuery);
            percolatorInfo.setSubscription(subscription);

            Consumer consumer = subscription.getConsumer();
            percolatorInfo.setConsumer(consumer);
        }
    }

    private PercolatorInfo resolvePercolatorInfo(Map<Long, PercolatorInfo> percolatorInfoMap, Long matchQueryId) {

        PercolatorInfo percolatorInfo = percolatorInfoMap.get(matchQueryId);
        if (percolatorInfo == null) {
            percolatorInfo = new PercolatorInfo();
            percolatorInfoMap.put(matchQueryId, percolatorInfo);
        }

        return percolatorInfo;
    }

    private Map<Long, MatchQuery> createMatchQueryMap(Subscription subscription) {

        Map<Long, MatchQuery> matchQueryMap = new HashMap<>();

        List<MatchQuery> matchQueries = subscription.getMatchQueries();
        if (CollectionUtils.isNotEmpty(matchQueries)) {
            for (MatchQuery matchQuery : matchQueries) {
                matchQueryMap.put(matchQuery.getId(), matchQuery);
            }
        }

        return matchQueryMap;
    }

    private class PercolatorInfo {

        private Consumer consumer;
        private Subscription subscription;
        private MatchQuery matchQuery;
        private PercolatorMetadata percolatorMetadata;

        public PercolatorInfo() {
            // Default constructor
        }

        public boolean needPercolationDeletion() {

            return this.matchQuery == null;
        }

        public MatchQuery getMatchQuery() {

            return this.matchQuery;
        }

        public void setMatchQuery(MatchQuery matchQuery) {

            this.matchQuery = matchQuery;
        }

        public PercolatorMetadata getPercolatorMetadata() {

            return this.percolatorMetadata;
        }

        public void setPercolatorMetadata(PercolatorMetadata percolatorMetadata) {

            this.percolatorMetadata = percolatorMetadata;
        }

        public Subscription getSubscription() {

            return this.subscription;
        }

        public void setSubscription(Subscription subscription) {

            this.subscription = subscription;
        }

        public Consumer getConsumer() {

            return this.consumer;
        }

        public void setConsumer(Consumer consumer) {

            this.consumer = consumer;
        }
    }
}
